package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SendFileActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 0;
    final long MAX_SIZE=100;
    final int CONVERT_NUM =1048576;

    private CheckoutAPIClient mCheckoutAPIClient;
    final int GET_NEW_CARD = 1;

    FileAdapter filesAdapter;
    ArrayList<Uri> uris =new ArrayList<Uri>();
    Long size=0L;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference srvcFilesRef;

    ArrayList<Uri> finalFiles;
    int i;

    ProgressBar progressBar;
    TextView finalPriceView,priceAfterTaxView;
    Button addFileBtn,payBtn;
    NonScrollListView filesList;

    String transactionid;
    String paymentId;
    Integer serviceId;
    String serviceType;
    String userName;
    Double amount;

    Transaction transaction=new Transaction();

    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference paymentRef;
    DatabaseReference transactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        progressBar=(ProgressBar) findViewById(R.id.prgs);
        progressBar.setVisibility(View.INVISIBLE);
        finalPriceView=(TextView) findViewById(R.id.finalprice);
        priceAfterTaxView=(TextView) findViewById(R.id.priceaftertax);
        addFileBtn=(Button) findViewById(R.id.addfile);
        payBtn=(Button) findViewById(R.id.pay);
        filesList=(NonScrollListView) findViewById(R.id.filesList);

        Intent intent=getIntent();
        paymentId=intent.getStringExtra("paymentId");
        serviceId=intent.getIntExtra("serviceId",0);
        userName=intent.getStringExtra("username");
        serviceType=intent.getStringExtra("serviceType");

        filesAdapter =new FileAdapter(getApplicationContext(),R.layout.gridfileloks,uris);
        filesList.setAdapter(filesAdapter);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        srvcFilesRef= storageRef.child("ServicesFiles/"+serviceId.toString()+"/"+userName);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        paymentRef=myRef.child("Payments/"+paymentId).getRef();


        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                amount=snapshot.child("amount").getValue(Double.class);
                finalPriceView.setText(String.format(Locale.ENGLISH,"%.2f "+getResources().getString(R.string.da),amount));
                priceAfterTaxView.setText(String.format(Locale.ENGLISH,"%.2f "+getResources().getString(R.string.da),calucatePriceAfterTax(amount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size<=MAX_SIZE) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("file/*");
                    String[] extraMimeTypes = {"application/*", "text/*", "image/*"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                    startActivityForResult(intent, PICK_FILE_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.cantaddfiles,Toast.LENGTH_SHORT).show();
                }
            }
        });

        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.dlt) {
                    Uri file=uris.get(position);
                    uris.remove(position);
                    filesAdapter.notifyDataSetChanged();
                    File f=new File(file.getPath());
                    size= size -(f.length() / CONVERT_NUM);
                }
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceType.equals("Printing")) {
                    if (!uris.isEmpty()) {
                        Intent intent = new Intent(SendFileActivity.this, CardEditActivity.class);
                        startActivityForResult(intent, GET_NEW_CARD);
                    } else
                        Toast.makeText(getApplicationContext(), R.string.pleaseaddfilesfirst, Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SendFileActivity.this, CardEditActivity.class);
                    startActivityForResult(intent, GET_NEW_CARD);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && data!=null) {
            if (resultCode == RESULT_OK) {
                Uri file = data.getData();
                File f = new File(file.getPath());
                size =size + (f.length() / CONVERT_NUM);
                if (size<=MAX_SIZE) {
                    if(!uris.contains(file)) {
                        uris.add(file);
                        filesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.fileexiste,Toast.LENGTH_LONG).show();
                        size= size -(f.length() / CONVERT_NUM);
                        return;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),R.string.selectedmore,Toast.LENGTH_LONG).show();
                    size= size -(f.length() / CONVERT_NUM);
                    return;
                }
            }
        }
        else if (requestCode==GET_NEW_CARD) {
            if (resultCode == RESULT_OK){
                progressBar.setVisibility(View.VISIBLE);
                String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
                String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
                String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
                String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
                // Your processing goes here.

                String[] string=expiry.split("/");

                if (!CardValidator.validateCardNumber(cardNumber)) {
                    Toast.makeText(getApplicationContext(),R.string.invalidcardnumber, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if (!CardValidator.validateExpiryDate(string[0],string[1])) {
                    Toast.makeText(getApplicationContext(),R.string.error_card_expired, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
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

                                                uploadFiles(uris, new Callback() {
                                                    @Override
                                                    public void onCallback(boolean sent) {
                                                        if (sent){
                                                            HashMap map = new HashMap();
                                                            map.put("paymentState", "done");
                                                            map.put("finishedDay",value);
                                                            paymentRef.updateChildren(map);

                                                            myRef.child("timestamp").child(key).removeValue();
                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            Intent intent=new Intent();
                                                            setResult(RESULT_OK,intent);
                                                            finish();
                                                        }
                                                        else
                                                        {
                                                            progressBar.setVisibility(View.INVISIBLE);
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
                    transaction.setAmount(((amount*6)/100)+amount);
                    transaction.setStatus("authorized");
                    transaction.setUserId(user.getUid());

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
    }

    public void uploadFiles(final ArrayList<Uri> files, final Callback callback){

        finalFiles=files;
        //final int[] i = {0};
        if (files.size()==0){
            callback.onCallback(true);
        }
        else if (finalFiles.size()>0){
            i=finalFiles.size()-1;
            File f=new File(Objects.requireNonNull(finalFiles.get(i).getPath()));
            StorageReference curentOrderRef= srvcFilesRef.child(f.getName());
            curentOrderRef.putFile(finalFiles.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    finalFiles.remove(i);
                    uploadFiles(finalFiles,callback);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),R.string.failed+" "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    callback.onCallback(false);
                }
            });
        }
    }

    private interface Callback{
        void onCallback(boolean sent);
    }

    /*public void uploadFiles(ArrayList<Uri> files){
        final int[] i = {0};
        for (Uri file : files){
            StorageReference curentOrderRef= srvcFilesRef.child(file.getLastPathSegment());
            curentOrderRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (i[0] ==files.size()-1){
                        Toast.makeText(getApplicationContext(),R.string.firstfileuploaded,Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/

    private Double calucatePriceAfterTax(Double price) {
        return ((price*6)/100)+price;
    }
}
