package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ProgressBar progressBar;
    Button acceptBtn, refuseBtn,refuseBtn2, deleteBtn1, deleteBtn2, submitWorkBtn;
    ImageButton phoneContactBtn, emailContactBtn;
    TextView titleView, requesterView, typeView, pagesView, startTimeView, endTimeView, detailsView, coloredView, paperSizeView;
    NonScrollListView provFilesList;
    Service selectedItem;
    String activity;
    String keyprevius;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference workSrvcFilesRef;

    FileAdapterForAdmins filesAdapter;
    ArrayList<Uri> uris =new ArrayList<Uri>();

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference clientRef;
    DatabaseReference notifyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        selectedItem = (Service) intent.getSerializableExtra("service");
        activity = intent.getStringExtra("activity");

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        workSrvcFilesRef= storageRef.child("ServicesFiles/"+selectedItem.getServiceId().toString()+"/"+selectedItem.getServiceRequesterUsername());

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Services/" + selectedItem.getServiceId().toString());
        clientRef = database.getReference("Users/Clients");
        notifyRef=database.getReference("Notifications").push();


        if (activity.equals("pending")) {
            setContentView(R.layout.activity_pending_service_detail);

            initUi();

            acceptBtn = (Button) findViewById(R.id.accept);
            refuseBtn = (Button) findViewById(R.id.refuse);

            acceptBtn.setOnClickListener(this);
            refuseBtn.setOnClickListener(this);

        } else if (activity.equals("refused")) {
            setContentView(R.layout.activity_refused_service_detail);
            initUi();

            deleteBtn1 = (Button) findViewById(R.id.deletedd);

            deleteBtn1.setOnClickListener(this);

        } else if (activity.equals("onWait")) {
            setContentView(R.layout.activity_on_wait_for_paiment_service_detail);
            initUi();

            refuseBtn2 = (Button) findViewById(R.id.cancelrefuse);
            refuseBtn2.setOnClickListener(this);

        } else if (activity.equals("delivered")) {
            setContentView(R.layout.activity_delivered_service_detail);
            initUi();

            deleteBtn2 = (Button) findViewById(R.id.deletedddel);

            deleteBtn2.setOnClickListener(this);

        } else if (activity.equals("workingOn")) {
            setContentView(R.layout.activity_working_on_service_detail);
            initUi();

            progressBar= (ProgressBar) findViewById(R.id.prgs);
            provFilesList= (NonScrollListView) findViewById(R.id.imageslist);
            submitWorkBtn= (Button) findViewById(R.id.sbmit_work);

            filesAdapter = new FileAdapterForAdmins(getApplicationContext(), R.layout.fileslooksforadmin, uris);
            provFilesList.setAdapter(filesAdapter);

            submitWorkBtn.setOnClickListener(this);

            workSrvcFilesRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {

                            for (StorageReference item : listResult.getItems()) {
                                // All the items under listRef.
                                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        uris.add(uri);
                                        filesAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Uh-oh, an error occurred!
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),R.string.couldntdownload,Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (activity.equals("finished")) {
            setContentView(R.layout.activity_finished_service_detail);
            initUi();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(activity.equals("workingOn")) {

            provFilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long viewId = view.getId();

                    if (viewId == R.id.dlt) {
                        Toast.makeText(getApplicationContext(),R.string.startdownloading,Toast.LENGTH_LONG).show();
                        Uri curFileUri = uris.get(position);

                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(curFileUri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                        request.setTitle(curFileUri.getLastPathSegment());
                        request.setDescription("Downloading the file.");

                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloadfileName");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);

                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {


        if (v==acceptBtn){
            Intent intent=new Intent(getApplicationContext(),SetPriceActivity.class);
            startActivityForResult(intent,0);
        }
        else if (v==refuseBtn || v== refuseBtn2) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ServiceDetailActivity.this);
            builder.setTitle(getResources().getString(R.string.confirmation));
            builder.setMessage(getResources().getString(R.string.proceed));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snap.child("userId").getValue(Integer.class).equals(selectedItem.getServiceRequesterId())) {
                                    keyprevius = snap.getKey();
                                    break;
                                }
                            }
                            HashMap map = new HashMap();
                            map.put("serviceState","refused");
                            myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                        new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                            @Override
                                            public void timecallback(Long value, String key) {
                                                Notification notification=new Notification(selectedItem.getServiceId(),"refuse",selectedItem.getServiceProviderUsername(),keyprevius," refuses your order("+selectedItem.getServiceSubject()+")",value,false);
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
        else if ((v==deleteBtn1) || (v==deleteBtn2)) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ServiceDetailActivity.this);
            builder.setTitle(getResources().getString(R.string.confirmation));
            builder.setMessage(getResources().getString(R.string.proceed));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (selectedItem.getServiceState().equals("refused_deleted_bySR")
                            || selectedItem.getServiceState().equals("delivered_deleted_bySR")) {
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
                        map.put("serviceState","refused_deleted_bySP");
                        myRef.updateChildren(map);
                        finish();
                    }
                    else if (selectedItem.getServiceState().equals("delivered")){
                        HashMap map = new HashMap();
                        map.put("serviceState","delivered_deleted_bySP");
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
        else if (v==phoneContactBtn){
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                makePhoneCall();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
            }
        }
        else if (v==emailContactBtn) {
            clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String email="";
                    for (DataSnapshot snap:snapshot.getChildren()){
                        if (snap.child("userId").getValue()==selectedItem.getServiceRequesterId()){
                            email=snap.child("serviceRequesterEmail").getValue(String.class);
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
        else if (v==submitWorkBtn){
            Intent intent=new Intent(getApplicationContext(),SubmitWorkActivity.class);
            intent.putExtra("serviceId",selectedItem.getServiceId());
            intent.putExtra("username",selectedItem.getServiceProviderUsername());
            startActivityForResult(intent,1);

        }
    }

    private void initUi() {
        titleView = (TextView) findViewById(R.id.service_titledd);
        requesterView = (TextView) findViewById(R.id.service_requesterdd);///////////////////////////////////////////////////////////////
        requesterView.setOnClickListener(this);
        typeView = (TextView) findViewById(R.id.service_typedd);
        pagesView = (TextView) findViewById(R.id.service_pagesdd);
        startTimeView = (TextView) findViewById(R.id.starttimedd);
        endTimeView = (TextView) findViewById(R.id.endtimedd);
        paperSizeView = (TextView) findViewById(R.id.service_papers);
        coloredView = (TextView) findViewById(R.id.service_papers_colored);
        detailsView = (TextView) findViewById(R.id.detailsdd);
        phoneContactBtn = (ImageButton) findViewById(R.id.contact_by_phone);
        emailContactBtn = (ImageButton) findViewById(R.id.contact_by_email);

        phoneContactBtn.setOnClickListener(this);
        emailContactBtn.setOnClickListener(this);

        titleView.setText(selectedItem.getServiceSubject());
        requesterView.setText(selectedItem.getServiceRequesterUsername());
        typeView.setText(selectedItem.getServiceType());
        pagesView.setText(selectedItem.getPageNumber().toString());
        startTimeView.setText(selectedItem.getStartServiceTime());
        endTimeView.setText(selectedItem.getEndServiceTime());
        paperSizeView.setText(selectedItem.getPapersSize());
        String colored;
        if (selectedItem.getPapersColored())
            colored = getResources().getString(R.string.yes);
        else
            colored = getResources().getString(R.string.no);
        coloredView.setText(colored);
        detailsView.setText(selectedItem.getServiceDetails());
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void makePhoneCall(){
        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phonNumber="";
                for (DataSnapshot snap:snapshot.getChildren()){
                    if (snap.child("userId").getValue()==selectedItem.getServiceRequesterId()){
                        phonNumber=snap.child("serviceRequesterPhone").getValue(String.class);
                        break;
                    }
                }
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    String dial = "tel:" + PhoneNumberUtils.formatNumber(phonNumber,"DZ");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    Toast.makeText(ServiceDetailActivity.this, R.string.calldenied, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==0) {
            if (resultCode==RESULT_OK) {
                clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("userId").getValue(Integer.class).equals(selectedItem.getServiceRequesterId())) {
                                keyprevius = snap.getKey();
                                break;
                            }

                        }

                        Double finalPrice=data.getDoubleExtra("finalprice",selectedItem.getFinalPrice());
                        Payment payment=(Payment) data.getSerializableExtra("payment");

                        DatabaseReference paymentRef=database.getReference().child("Payments").child(payment.getPaymentId());

                        HashMap paymap = new HashMap();
                        paymap.put("providerId",selectedItem.getServiceProviderId().toString());
                        paymap.put("requesterId",selectedItem.getServiceRequesterId().toString());
                        paymap.put("serviceId",selectedItem.getServiceId().toString());
                        paymentRef.updateChildren(paymap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    HashMap map = new HashMap();
                                    map.put("serviceState","onWait");
                                    map.put("finalPrice",finalPrice);
                                    map.put("paymentId",payment.getPaymentId());
                                    myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()){
                                                new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                                    @Override
                                                    public void timecallback(Long value, String key) {
                                                        Notification notification=new Notification(selectedItem.getServiceId(),"accept",selectedItem.getServiceProviderUsername(),keyprevius," accepts your order("+selectedItem.getServiceSubject()+")",value,false);
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
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        else if (requestCode==1){
            if (resultCode==RESULT_OK) {
                clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("userId").getValue(Integer.class).equals(selectedItem.getServiceRequesterId())) {
                                keyprevius = snap.getKey();
                                break;
                            }
                        }

                        HashMap map = new HashMap();
                        map.put("serviceState","finished");
                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                        @Override
                                        public void timecallback(Long value, String key) {
                                            Notification notification = new Notification(selectedItem.getServiceId(),"finish", selectedItem.getServiceProviderUsername(), keyprevius, " finishes your order("+selectedItem.getServiceSubject()+")", value, false);
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
}

 /*    filepathview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.dlt) {
                    Uri curFileUri = uris.get(position);

                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(curFileUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                            DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                    request.setTitle(curFileUri.getLastPathSegment());
                    request.setDescription("Downloading the file.");

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloadfileName");
                    request.setMimeType("");
            downloadManager.enqueue(request);*/

  /*
        if (uris.isEmpty())
            none.setVisibility(View.VISIBLE);

        filesAdapter = new FileAdapterForAdmins(getApplicationContext(), R.layout.fileslooksforadmin, uris);
        filepathview.setAdapter(filesAdapter);

        srvcFilesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uris.add(uri);
                                    filesAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });*/