package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.VirtualLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;

public class ClientServiceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    Button changeProviderBtn,cancelBtn1,changeOrderBtn,deleteBtn1,cancelBtn2,sendFilesBtn,reorderBtn,deleteBtn2,seeProveBtn,confirmDeliverBtn,deleteBtn3,cancelBtn3,cancelBtn4;
    ImageButton phoneContactBtn,emailContactBtn;
    TextView titleView,providerView,typeView,pagesView,startTimeView,endTimeView,detailsView,priceView,coloredView,paperSizeView,offeredpriceTextView,offeredrefundTextView,refundConditionsTextView;
    Service selectedItem;
    String activity;
    String keyprevius,keynow;
    Double balance;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference adminRef;
    DatabaseReference notifyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        selectedItem=(Service) intent.getSerializableExtra("service");
        activity= intent.getStringExtra("activity");

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Services/"+selectedItem.getServiceId().toString());
        adminRef=database.getReference("Users/Admins");
        notifyRef=database.getReference("Notifications").push();

        if (activity.equals("pending")) {
            setContentView(R.layout.activity_client_pending_service_detail);

            initUi();

            changeProviderBtn=(Button) findViewById(R.id.change_provider);
            cancelBtn1=(Button) findViewById(R.id.canceldd);
            changeOrderBtn=(Button) findViewById(R.id.change_order);

            changeProviderBtn.setOnClickListener(this);
            cancelBtn1.setOnClickListener(this);
            changeOrderBtn.setOnClickListener(this);

        }
        else
            if (activity.equals("refused")){
                setContentView(R.layout.activity_client_refused_service_detail);
                initUi();

                deleteBtn1=(Button) findViewById(R.id.deleterefused);

                deleteBtn1.setOnClickListener(this);
        }
            else
                if (activity.equals("onWait")) {
                    setContentView(R.layout.activity_client_on_wait_for_paiment_service_detail);
                    initUi();

                    offeredpriceTextView=(TextView) findViewById(R.id.priceoffered);
                    offeredrefundTextView=(TextView) findViewById(R.id.refundoffered);
                    refundConditionsTextView=(TextView) findViewById(R.id.refundcondition);
                    cancelBtn2=(Button) findViewById(R.id.canceldd);
                    sendFilesBtn=(Button) findViewById(R.id.send_files);

                    cancelBtn2.setOnClickListener(this);
                    sendFilesBtn.setOnClickListener(this);

                    offeredpriceTextView.setText(String.format(Locale.ENGLISH,"%.2f "+getResources().getString(R.string.da),selectedItem.getFinalPrice()));

                    database.getReference("Payments/"+selectedItem.getPaymentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.child("aboutRefund").getValue(Boolean.class)){
                                offeredrefundTextView.setText(R.string.none);
                                refundConditionsTextView.setText(R.string.none);
                            }
                            else {
                                offeredrefundTextView.setText(String.format(Locale.ENGLISH,"%d%%",snapshot.child("refundPercent").getValue(Integer.class)));
                                refundConditionsTextView.setText(snapshot.child("refundGuidlines").getValue(String.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                else
                    if (activity.equals("canceled")) {
                        setContentView(R.layout.activity_client_canceled_service_detail);
                        initUi();

                        reorderBtn=(Button) findViewById(R.id.reorder);
                        deleteBtn2=(Button) findViewById(R.id.delete);

                        reorderBtn.setOnClickListener(this);
                        deleteBtn2.setOnClickListener(this);
                    }
                    else
                        if (activity.equals("workingOn")) {
                            setContentView(R.layout.activity_client_working_on_service_detail);
                            initUi();

                            cancelBtn3=(Button) findViewById(R.id.cancelrefund);

                            cancelBtn3.setOnClickListener(this);

                        }
                        else
                            if (activity.equals("finished")) {
                                setContentView(R.layout.activity_client_finished_service_detail);
                                initUi();

                                seeProveBtn=(Button) findViewById(R.id.see_prove);
                                confirmDeliverBtn=(Button) findViewById(R.id.confirm_deliver);
                                cancelBtn4=(Button) findViewById(R.id.cancelrefund);

                                seeProveBtn.setOnClickListener(this);///////////////////
                                confirmDeliverBtn.setOnClickListener(this);
                                cancelBtn4.setOnClickListener(this);

                            }
                            else
                                if (activity.equals("delivered")) {
                                    setContentView(R.layout.activity_client_delivered_service_detail);
                                    initUi();

                                    deleteBtn3 = (Button) findViewById(R.id.deletedeliver);

                                    deleteBtn3.setOnClickListener(this);
                                }


    }

    private void initUi() {
        titleView=(TextView) findViewById(R.id.service_titledd);
        providerView=(TextView) findViewById(R.id.service_providerdd);///////////////////////////////////////////////////////////////
        providerView.setOnClickListener(this);
        typeView=(TextView) findViewById(R.id.service_typedd);
        pagesView=(TextView) findViewById(R.id.service_pagesdd);
        startTimeView=(TextView) findViewById(R.id.starttimedd);
        endTimeView=(TextView) findViewById(R.id.endtimedd);
        paperSizeView=(TextView) findViewById(R.id.service_papers);
        coloredView=(TextView) findViewById(R.id.service_papers_colored);
        priceView=(TextView) findViewById(R.id.service_price);
        detailsView=(TextView) findViewById(R.id.detailsdd);
        phoneContactBtn=(ImageButton) findViewById(R.id.contact_by_phone);
        emailContactBtn=(ImageButton) findViewById(R.id.contact_by_email);

        phoneContactBtn.setOnClickListener(this);
        emailContactBtn.setOnClickListener(this);

        titleView.setText(selectedItem.getServiceSubject());
        providerView.setText(selectedItem.getServiceProviderUsername());
        typeView.setText(selectedItem.getServiceType());
        pagesView.setText(selectedItem.getPageNumber().toString());
        startTimeView.setText(selectedItem.getStartServiceTime());
        endTimeView.setText(selectedItem.getEndServiceTime());
        paperSizeView.setText(selectedItem.getPapersSize());
        String colored;
        if (selectedItem.getPapersColored())
            colored=getResources().getString(R.string.yes);
        else
            colored=getResources().getString(R.string.no);
        coloredView.setText(colored);
        priceView.setText(String.format(Locale.ENGLISH,"%.2f DA",selectedItem.getFinalPrice()));
        detailsView.setText(selectedItem.getServiceDetails());
    }

    @Override
    public void onClick(View v) {

        if (v==changeProviderBtn) {
            database.getReference("Users/Clients/"+mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Intent intent=new Intent(ClientServiceDetailActivity.this,AdminListActivity.class);
                    intent.putExtra("activity", 1);
                    intent.putExtra("country",snapshot.child("serviceRequesterCountry").getValue(String.class));
                    intent.putExtra("province",snapshot.child("serviceRequesterProvince").getValue(String.class));
                    startActivityForResult(intent, 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if ((v==cancelBtn1) || (v==cancelBtn2)) {
            cancelOrder();
        }
        else if ((v==deleteBtn1) || (v==deleteBtn2) || (v==deleteBtn3)) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ClientServiceDetailActivity.this);
            builder.setTitle(getResources().getString(R.string.confirmation));
            builder.setMessage(getResources().getString(R.string.proceed));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (selectedItem.getServiceState().equals("refused_deleted_bySP")
                            || selectedItem.getServiceState().equals("delivered_deleted_bySP")
                                || selectedItem.getServiceState().equals("canceled")) {
                        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), R.string.sucseed, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                    else if (selectedItem.getServiceState().equals("refused")){
                        HashMap map = new HashMap();
                        map.put("serviceState","refused_deleted_bySR");
                        myRef.updateChildren(map);
                        finish();
                    }
                    else if (selectedItem.getServiceState().equals("delivered")){
                        HashMap map = new HashMap();
                        map.put("serviceState","delivered_deleted_bySR");
                        myRef.updateChildren(map);
                        finish();
                    }
                }
            }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
        else if (v==reorderBtn) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ClientServiceDetailActivity.this);
            builder.setTitle(getResources().getString(R.string.confirmation));
            builder.setMessage(getResources().getString(R.string.proceed));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap:snapshot.getChildren()){
                                if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                    keyprevius = snap.getKey();
                                    break;
                                }
                            }

                            HashMap map = new HashMap();
                            map.put("serviceState","pending");
                            myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                            @Override
                                            public void timecallback(Long value, String key) {
                                                Notification notification = new Notification(selectedItem.getServiceId(),"newOrder", selectedItem.getServiceRequesterUsername(), keyprevius, " asks you for an order ("+selectedItem.getServiceSubject()+")", value, false);
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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
        else if (v==phoneContactBtn){
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                makePhoneCall();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
            }


        }
        else if (v==emailContactBtn) {
            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String email="";
                    for (DataSnapshot snap:snapshot.getChildren()){
                        if (snap.child("srvcProviderId").getValue()==selectedItem.getServiceProviderId()){
                            email=snap.child("srvcProviderEmail").getValue(String.class);
                            break;
                        }
                    }

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    //need this to prompts email client only
                    emailIntent.setType("message/rfc822");

                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (v==changeOrderBtn) {
            Intent intent=new Intent(ClientServiceDetailActivity.this,requestActivity.class);
            intent.putExtra("activity", 1);
            startActivityForResult(intent, 2);
        }
        else if (v==confirmDeliverBtn) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ClientServiceDetailActivity.this);
            builder.setTitle(getResources().getString(R.string.confirmation));
            builder.setMessage(getResources().getString(R.string.proceed));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(ClientServiceDetailActivity.this,RateActivity.class);
                    intent.putExtra("serviceId",selectedItem.getServiceId());
                    intent.putExtra("requesterId",selectedItem.getServiceRequesterId());
                    intent.putExtra("providerId",selectedItem.getServiceProviderId());
                    startActivityForResult(intent,5);
                }
            }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
        else if (v==sendFilesBtn){
            Intent intent=new Intent(getApplicationContext(),SendFileActivity.class);
            intent.putExtra("serviceType",selectedItem.getServiceType());
            intent.putExtra("paymentId",selectedItem.getPaymentId());
            intent.putExtra("serviceId",selectedItem.getServiceId());
            intent.putExtra("username",selectedItem.getServiceRequesterUsername());
            startActivityForResult(intent,3);
        }
        else if (v==cancelBtn3 || v==cancelBtn4){
            database.getReference("Payments/"+selectedItem.getPaymentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child("aboutRefund").getValue(Boolean.class)){
                        cancelOrder();
                    }
                    else {
                        if (!snapshot.child("refundDemanded").getValue(Boolean.class)) {
                            Intent intent = new Intent(getApplicationContext(), RequestRefundActivity.class);
                            intent.putExtra("amount", snapshot.child("amount").getValue(Double.class));
                            intent.putExtra("refundPercent", snapshot.child("refundPercent").getValue(Integer.class));
                            intent.putExtra("refundConditions", snapshot.child("refundGuidlines").getValue(String.class));
                            startActivityForResult(intent, 4);
                        } else
                            Toast.makeText(getApplicationContext(), R.string.refunddemanded, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (v==seeProveBtn){
            Intent intent=new Intent(getApplicationContext(),SeeProveActivity.class);
            intent.putExtra("serviceId",selectedItem.getServiceId());
            intent.putExtra("username",selectedItem.getServiceProviderUsername());
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                int adminId = data.getIntExtra("idAdmin", 0);
                String adminName= data.getStringExtra("usernameAdmin");

                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class).equals(adminId)) {
                                keynow = snap.getKey();
                            }
                            else if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                keyprevius = snap.getKey();
                            }
                        }
                        selectedItem.setServiceProviderId(adminId);
                        selectedItem.setServiceProviderUsername(adminName);
                        providerView.setText(selectedItem.getServiceProviderUsername());

                        HashMap map = new HashMap();
                        map.put("serviceProviderId", adminId);
                        map.put("serviceProviderUsername",adminName);
                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification = new Notification(selectedItem.getServiceId(),"cancel", selectedItem.getServiceRequesterUsername(), keyprevius, " canceled his order("+selectedItem.getServiceSubject()+")", value, false);
                                            notifyRef.setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Notification notification = new Notification(selectedItem.getServiceId(),"newOrder", selectedItem.getServiceRequesterUsername(), keynow, " asks you for an order("+selectedItem.getServiceSubject()+")", value, false);
                                                        notifyRef.setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                database.getReference("timestamp").child(key).removeValue();
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });


                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }
        else if (requestCode==2) {
            if (resultCode == RESULT_OK) {
                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                keyprevius = snap.getKey();
                                break;
                            }
                        }

                        HashMap map = new HashMap();
                        map.put("papersSize",data.getStringExtra("paperSize"));
                        map.put("pageNumber",data.getIntExtra("pages",selectedItem.getPageNumber()));
                        map.put("serviceSubject",data.getStringExtra("subject"));
                        map.put("serviceType",data.getStringExtra("type"));
                        map.put("papersColored",data.getBooleanExtra("colored",selectedItem.getPapersColored()));
                        map.put("startServiceTime",data.getStringExtra("startDay"));
                        map.put("endServiceTime",data.getStringExtra("endDay"));
                        map.put("serviceDetails",data.getStringExtra("details"));
                        map.put("estimatedPrice",data.getDoubleExtra("estimatedPrice",selectedItem.getEstimatedPrice()));

                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification=new Notification(selectedItem.getServiceId(),"updateOrder",selectedItem.getServiceRequesterUsername(),keyprevius," updates his order("+selectedItem.getServiceSubject()+")",value,false);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        }
        else if (requestCode==3){
            if (resultCode==RESULT_OK) {
                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                keyprevius = snap.getKey();
                                balance =snap.child("walletBalance").getValue(Double.class);
                                break;
                            }
                        }
                                HashMap map = new HashMap();
                                map.put("serviceState", "workingOn");
                                myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            HashMap map2 = new HashMap();
                                            Double some=selectedItem.getFinalPrice() + balance;
                                            map2.put("balance", some);
                                            database.getReference("Wallets/" + keyprevius).updateChildren(map2).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        HashMap map3 = new HashMap();
                                                        map3.put("walletBalance", selectedItem.getFinalPrice() + balance);
                                                        adminRef.child(keyprevius).updateChildren(map3).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                                                    @Override
                                                                    public void timecallback(Long value, String key) {

                                                                        Notification notification = new Notification(selectedItem.getServiceId(),"payment", selectedItem.getServiceRequesterUsername(), keyprevius, " confirms the payment for his order("+selectedItem.getServiceSubject()+"),now you can start the work on it ", value, false);
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
                                                        });
                                                    }
                                                }
                                            });
                                        }

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        else if (requestCode==4){
            if (resultCode==RESULT_OK){
                String reasonRefund=data.getStringExtra("refundReason");

                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                keyprevius = snap.getKey();
                                break;
                            }
                        }

                        HashMap map=new HashMap();
                        map.put("paymentState","init_refund");
                        map.put("refundDemanded",true);
                        map.put("reasonRefundDemanded",reasonRefund);

                        database.getReference("Payments/"+selectedItem.getPaymentId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification=new Notification(selectedItem.getServiceId(),"refund",selectedItem.getServiceRequesterUsername(),keyprevius," asks for a refund("+selectedItem.getServiceSubject()+")",value,false);
                                            notifyRef.setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        database.getReference("timestamp").child(key).removeValue();
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(ClientServiceDetailActivity.this);
                                                        builder.setTitle(getResources().getString(R.string.refundrequested));
                                                        builder.setView(R.layout.requestrefundsucceded);
                                                        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        }).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        else if (requestCode==5) {
            if (resultCode == RESULT_OK) {

                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class).equals(selectedItem.getServiceProviderId())) {
                                keyprevius = snap.getKey();
                                break;
                            }
                        }
                        HashMap map = new HashMap();
                        map.put("serviceState","delivered");
                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    confirmDeliverBtn.setText(R.string.delivered);
                                    confirmDeliverBtn.setGravity(Gravity.START);
                                    confirmDeliverBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle,0);

                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification=new Notification(selectedItem.getServiceId(),"deliver",selectedItem.getServiceRequesterUsername(),keyprevius," confirms the delivering of his order("+selectedItem.getServiceSubject()+")",value,false);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void makePhoneCall(){
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phonNumber="";
                for (DataSnapshot snap:snapshot.getChildren()){
                    if (snap.child("srvcProviderId").getValue().equals(selectedItem.getServiceProviderId())){
                        phonNumber=snap.child("srvcProviderPhone").getValue(String.class);
                        break;
                    }
                }
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    String dial = "tel:" + PhoneNumberUtils.formatNumber(phonNumber,"DZ");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    Toast.makeText(ClientServiceDetailActivity.this, R.string.calldenied, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makePhoneCall();
                }
                return;
        }
    }

    public void cancelOrder(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ClientServiceDetailActivity.this);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.proceed));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("srvcProviderId").getValue(Integer.class)==selectedItem.getServiceProviderId()) {
                                keyprevius = snap.getKey();
                                break;
                            }
                        }

                        HashMap map = new HashMap();
                        map.put("serviceState","canceled");
                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification=new Notification(selectedItem.getServiceId(),"cancel",selectedItem.getServiceRequesterUsername(),keyprevius," canceled his order("+selectedItem.getServiceSubject()+")",value,false);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
}
