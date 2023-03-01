package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.checkout.android_sdk.CheckoutAPIClient;
import com.checkout.android_sdk.Request.CardTokenisationRequest;
import com.checkout.android_sdk.Response.CardTokenisationFail;
import com.checkout.android_sdk.Response.CardTokenisationResponse;
import com.checkout.android_sdk.Utils.Environment;
import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class RefundAcceptedActivity extends AppCompatActivity {

    final int GET_NEW_CARD = 1;
    TextView priceView,refundPercentView,refundConditionsView,refundAmountView,afterTexView;
    Button requestBtn;

    Integer serviceId;
    Double balance;
    String keyprevius;

    private CheckoutAPIClient mCheckoutAPIClient;

    Transaction transaction=new Transaction();

    DatabaseReference transactionRef;

    String transactionid;

    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference servicesRef;
    DatabaseReference paymentRef;
    DatabaseReference walletRef;
    DatabaseReference notifyRef;

    Service service;
    Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_accepted);

        priceView=(TextView) findViewById(R.id.priceoffered);
        refundPercentView=(TextView) findViewById(R.id.refundoffered);
        refundConditionsView=(TextView) findViewById(R.id.refundcondition);
        refundAmountView=(TextView) findViewById(R.id.refundamount);
        afterTexView=(TextView) findViewById(R.id.priceaftertax);
        requestBtn=(Button) findViewById(R.id.refundrequest);

        Intent intent=getIntent();
        serviceId=intent.getIntExtra("service",0);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        servicesRef=myRef.child("Services").child(serviceId.toString());
        paymentRef=myRef.child("Payments");
        walletRef=myRef.child("Wallets");
        notifyRef=database.getReference("Notifications").push();

        mAuth= FirebaseAuth.getInstance();

        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                service=snapshot.getValue(Service.class);
                paymentRef.child(service.getPaymentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        payment=snap.getValue(Payment.class);
                        if (payment.getPaymentState().equals("init_refund")){

                        database.getReference("Users/Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot s) {
                                for (DataSnapshot snap:s.getChildren()){
                                    if (snap.child("srvcProviderId").getValue(Integer.class).equals(service.getServiceProviderId())) {
                                        keyprevius = snap.getKey();
                                        break;
                                    }
                                }

                                walletRef.child(keyprevius).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot ss) {
                                balance=ss.getValue(Double.class);
                                refundAmountView.setText(String.format(Locale.getDefault(),"%.2f DA",calculatedRefundAmount(payment.getAmount(),payment.getRefundPercent())));
                                refundConditionsView.setText(payment.getRefundGuidlines());
                                priceView.setText(String.format(Locale.ENGLISH,"%.2f DA",payment.getAmount()));
                                refundPercentView.setText(String.format(Locale.ENGLISH,"%d%%",payment.getRefundPercent()));
                                //progressdialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                        else
                        {
                            //progressdialog.dismiss();
                            finish();
                            Toast.makeText(getApplicationContext(),"You all ready taken your refund",Toast.LENGTH_LONG).show();
                        }

                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefundAcceptedActivity.this, CardEditActivity.class);
                startActivityForResult(intent, GET_NEW_CARD);
            }
        });
    }

    private Double calculatedRefundAmount(Double amount,Integer refundPercent){
        return  (amount*refundPercent)/100;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
        String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
        String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
        String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
        // Your processing goes here.

        String[] string=expiry.split("/");

        if (!CardValidator.validateCardNumber(cardNumber)) {
            Toast.makeText(getApplicationContext(),R.string.invalidcardnumber, Toast.LENGTH_LONG).show();

        }
        else if (!CardValidator.validateExpiryDate(string[0],string[1])) {
            Toast.makeText(getApplicationContext(),R.string.error_card_expired, Toast.LENGTH_LONG).show();

        }
        else {
            CheckoutAPIClient.OnTokenGenerated mTokenListener = new CheckoutAPIClient.OnTokenGenerated() {
                @Override
                public void onTokenGenerated(CardTokenisationResponse token) {
                    // your token

                    transaction.setCardToken(token.getToken());
                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                        @Override
                        public void timecallback(Long value, String key) {
                            transaction.setCreatedDate(value);

                            transactionRef.setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HashMap map = new HashMap();
                                        map.put("paymentState", "done_refund");
                                        map.put("finishedDay",value);
                                        paymentRef.child(service.getPaymentId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    HashMap map2 = new HashMap();
                                                    Double some=balance-calculatedRefundAmount(payment.getAmount(),payment.getRefundPercent());
                                                    map2.put("balance", some);
                                                    walletRef.child(keyprevius).updateChildren(map2).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()){
                                                        HashMap map3 = new HashMap();
                                                        map3.put("walletBalance",some);
                                                        myRef.child("Users").child("Admins").child(keyprevius).updateChildren(map3).addOnCompleteListener(new OnCompleteListener() {
                                                      @Override
                                                    public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()){
                                                        database.getReference("timestamp").child(key).removeValue();
                                                        finish();
                                                    }
                                                      }
                                                        });

                                                    }
                                                    }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(CardTokenisationFail error) {
                    // your error
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNetworkError(VolleyError error) {
                    // your network error
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                    finish();
                }
            };

            transactionRef=myRef.child("Transactions").push();
            transactionid=transactionRef.getKey();
            transaction.setTransactionId(transactionid);
            transaction.setAmount(((payment.getAmount()*6)/100)+payment.getAmount());
            transaction.setStatus("authorized");
            transaction.setUserId(mAuth.getCurrentUser().getUid());

            mCheckoutAPIClient = new CheckoutAPIClient(
                    this,                // context
                    "pk_test_aec487a9-a39f-4df9-abcc-30bd68d96cec",          // your public key
                    Environment.SANDBOX  // the environment
            );
            mCheckoutAPIClient.setTokenListener(mTokenListener);

            mCheckoutAPIClient.generateToken(new CardTokenisationRequest(cardNumber, cardHolderName, string[0], string[1], cvv));
        }
    }
}

