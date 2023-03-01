package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class RefundDemandedAvtivity extends AppCompatActivity {

    TextView priceView,refundPercentView,refundConditionsView,refundAmountView,refundReason;
    Button acceptBtn,refusBtn;

    Integer serviceId;
    Double balance;
    String keyprevius;

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

        setContentView(R.layout.activity_refund_demanded_avtivity);

        refundReason=(TextView) findViewById(R.id.refundreason);
        priceView=(TextView) findViewById(R.id.priceoffered);
        refundPercentView=(TextView) findViewById(R.id.refundoffered);
        refundConditionsView=(TextView) findViewById(R.id.refundcondition);
        refundAmountView=(TextView) findViewById(R.id.refundamount);
        acceptBtn=(Button) findViewById(R.id.accept);
        refusBtn=(Button) findViewById(R.id.refuse);



        Intent intent=getIntent();
        serviceId=intent.getIntExtra("service",0);

        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        servicesRef=myRef.child("Services").child(serviceId.toString()).getRef();
        paymentRef=myRef.child("Payments").getRef();
        walletRef=myRef.child("Wallets").child(mAuth.getCurrentUser().getUid()).getRef();
        notifyRef=database.getReference("Notifications").push();



        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                service=snapshot.getValue(Service.class);
                paymentRef.child(service.getPaymentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        payment = snap.getValue(Payment.class);
                        if (payment.getPaymentState().equals("init_refund")){

                            database.getReference("Users/Clients").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        if (snap.child("userId").getValue(Integer.class).equals(service.getServiceRequesterId())) {
                                            keyprevius = snap.getKey();
                                            break;
                                        }
                                    }
                                    refundAmountView.setText(String.format(Locale.getDefault(), "%.2f DA", calculatedRefundAmount(payment.getAmount(), payment.getRefundPercent())));
                                    refundConditionsView.setText(payment.getRefundGuidlines());
                                    refundReason.setText(payment.getReasonRefundDemanded());
                                    priceView.setText(String.format(Locale.ENGLISH, "%.2f DA", payment.getAmount()));
                                    refundPercentView.setText(String.format(Locale.ENGLISH, "%d%%", payment.getRefundPercent()));


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    }
                        else
                        {
                            finish();
                            Toast.makeText(getApplicationContext(),"you made your desicion about the refund",Toast.LENGTH_LONG).show();
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

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap map=new HashMap();
                map.put("paymentState","accept_refund");
                paymentRef.child(service.getPaymentId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            HashMap map1 = new HashMap();
                            map1.put("serviceState","canceled");
                            servicesRef.updateChildren(map1).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                       // HashMap map2 = new HashMap();
                                       // Double some=balance-calculatedRefundAmount(payment.getAmount(),payment.getRefundPercent());
                                       // map2.put("balance", some);
                                       // walletRef.updateChildren(map2).addOnCompleteListener(new OnCompleteListener() {
                                           // @Override
                                            //public void onComplete(@NonNull Task task) {
                                                //if (task.isSuccessful()){
                                                  //  HashMap map3 = new HashMap();
                                                    //map3.put("walletBalance",some);
                                                   // myRef.child("Users").child("Admins").child(mAuth.getCurrentUser().getUid()).updateChildren(map3).addOnCompleteListener(new OnCompleteListener() {
                                                      //  @Override
                                                        //public void onComplete(@NonNull Task task) {
                                                           // if (task.isSuccessful()){
                                                                new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                                                    @Override
                                                                    public void timecallback(Long value, String key) {

                                                                        Notification notification = new Notification(service.getServiceId(),"refundAccepted", service.getServiceProviderUsername(), keyprevius, " accepted your refund request for your order("+service.getServiceSubject()+"),now you can start the refund withdraw process ", value, false);
                                                                        notifyRef.setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                database.getReference("timestamp").child(key).removeValue();
                                                                                finish();
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            //}
                                                        //}
                                                    //});

                                               // }
                                            //}
                                        //});
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

        refusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap map=new HashMap();
                map.put("paymentState","refuse_refund");
                paymentRef.child(service.getPaymentId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                @Override
                                public void timecallback(Long value, String key) {

                                    Notification notification = new Notification(service.getServiceId(),"refundRefused", service.getServiceProviderUsername(), keyprevius, " refused your refund request for your order("+service.getServiceSubject()+"),you can contact us if there is any problem ", value, false);
                                    notifyRef.setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            database.getReference("timestamp").child(key).removeValue();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    private Double calculatedRefundAmount(Double amount,Integer refundPercent){
        return  (amount*refundPercent)/100;
    }
}
