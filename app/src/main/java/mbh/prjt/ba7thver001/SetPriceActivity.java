package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetPriceActivity extends AppCompatActivity implements RefundPolicyDialog.RefundDialogListner{

    TextInputEditText priceText;
    Button confirm;
    Double finalprice;
    Payment payment=new Payment("","","","",0L,0L,"debit_card","init",0.00,false,false,0,"","");

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference paymentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_price);

        priceText=(TextInputEditText) findViewById(R.id.final_price);
        confirm=(Button) findViewById(R.id.confirm);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        paymentRef=myRef.child("Payments").push();
        payment.setPaymentId(paymentRef.getKey());

    }

    @Override
    protected  void onStart(){
        super.onStart();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!priceText.getText().toString().isEmpty()){
                    AlertDialog.Builder pbuilder=new AlertDialog.Builder(SetPriceActivity.this);
                    pbuilder.setTitle("Refund?");
                    pbuilder.setMessage("Do you want to offer a refund policy to your client");
                    pbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAlertDialog();
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(SetPriceActivity.this);
                            builder.setTitle(getResources().getString(R.string.confirmation));
                            builder.setMessage(getResources().getString(R.string.proceed));
                            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finalprice=Double.parseDouble(priceText.getText().toString());

                                    payment.setAmount(finalprice);

                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {

                                            payment.setCreatDay(value);
                                            new GetTime().deleteTimestamp(key,myRef);

                                            paymentRef.setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent resultIntent = new Intent();
                                                        resultIntent.putExtra("finalprice", finalprice);
                                                        resultIntent.putExtra("payment", payment);
                                                        setResult(RESULT_OK, resultIntent);
                                                        finish();
                                                    }
                                                    else
                                                        Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    }).show();
                }
            }
        });
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RefundPolicyDialog alertDialog = RefundPolicyDialog.newInstance("Set a Refund Policy");
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onConfirm(Integer percent, String guid) {
        finalprice=Double.parseDouble(priceText.getText().toString());

        payment.setAmount(finalprice);
        payment.setAboutRefund(true);
        payment.setRefundPercent(percent);
        payment.setRefundGuidlines(guid);

        new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
            @Override
            public void timecallback(Long value, String key) {

                payment.setCreatDay(value);
                new GetTime().deleteTimestamp(key,myRef);

                paymentRef.setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("finalprice", finalprice);
                            resultIntent.putExtra("payment", payment);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
