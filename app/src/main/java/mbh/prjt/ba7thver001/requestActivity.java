package mbh.prjt.ba7thver001;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class requestActivity extends Activity {


    boolean proceed=true;
    ProgressBar progressBar;
    Button registrBtn;
    RadioGroup typeRadio;
    RadioButton writRbtn,printRbtn;
    //TextView estimatedPriceView;
    EditText edtitle,edpages,eddetails;
    TextInputLayout titleLayout,pagesLayout,detailsLayout;
    Spinner endDay,colored,paperSize;
    String key,typeStr="",titleStr="",detailStr="",endDayStr="",payId="",pageStr="",startDayStr,paperSizeStr,keyStr,userNameStr,adminNameStr;
    Integer pages=0,adminId=0,userId,orderIdall,activity;
    Double estimatedPrice=0.00;
    Long timestamp;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    Boolean coloredBool;
    ArrayAdapter<CharSequence> spinerAdapter,spinerAdapter3,spinerAdapter4;
    int value=0;

    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference servicesRef;
    DatabaseReference orderRef;
    DatabaseReference clientRef;
    DatabaseReference notificationRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_activity);

        typeRadio=(RadioGroup) findViewById(R.id.type);
        writRbtn=(RadioButton) findViewById(R.id.writing);
        printRbtn=(RadioButton) findViewById(R.id.printing);
        titleLayout=(TextInputLayout) findViewById(R.id.subject_text_input_layout);
        edtitle=(EditText) findViewById(R.id.title);
        endDay=(Spinner) findViewById(R.id.endServiceTime);
        pagesLayout=(TextInputLayout) findViewById(R.id.pages_text_input_layout);
        edpages=(EditText) findViewById(R.id.pages);
        colored=(Spinner) findViewById(R.id.colored);
        paperSize=(Spinner) findViewById(R.id.papersize);
        detailsLayout=(TextInputLayout) findViewById(R.id.details_text_input_layout);
        eddetails=(EditText) findViewById(R.id.details);
        //estimatedPriceView=(TextView) findViewById(R.id.estimatedprice);
        registrBtn=(Button) findViewById(R.id.saverqst);
        progressBar=(ProgressBar) findViewById(R.id.prgs);

        progressBar.setVisibility(View.INVISIBLE);

        spinerAdapter = ArrayAdapter.createFromResource(this, R.array.date, R.layout.spinner_layout);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endDay.setAdapter(spinerAdapter);

        spinerAdapter3 = ArrayAdapter.createFromResource(this, R.array.papersize , R.layout.spinner_layout);
        spinerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paperSize.setAdapter(spinerAdapter3);
        paperSize.setSelection(3);

        spinerAdapter4 = ArrayAdapter.createFromResource(this, R.array.yesno, R.layout.spinner_layout);
        spinerAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colored.setAdapter(spinerAdapter4);

        mAuth=FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();


        myRef = database.getReference();
        servicesRef=myRef.child("Services");
        orderRef=myRef.child("OrderNumber");
        clientRef=myRef.child("Users").child("Clients");

        Intent ourintent=getIntent();
        adminId=(Integer) ourintent.getIntExtra("idAdmin",0);
        adminNameStr=(String) ourintent.getStringExtra("usernameAdmin");
        activity=ourintent.getIntExtra("activity",0);


        if (activity!=1) {
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        orderIdall = snapshot.getValue(Integer.class);
                    else
                        orderIdall = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userId = snapshot.child(mAuth.getCurrentUser().getUid()).child("userId").getValue(Integer.class);
                    userNameStr = snapshot.child(mAuth.getCurrentUser().getUid()).child("serviceRequesterName").getValue(String.class);
                    database.getReference("Users/Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap:snapshot.getChildren()){
                                if (snap.child("srvcProviderId").getValue(Integer.class)==adminId){
                                    key=snap.getKey();
                                }
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

    }

    @Override
    protected void onStart(){
        super.onStart();

        //String message="Your data has been registred";
        //final Snackbar snackbar=Snackbar.make((findViewById(R.id.linearLayout)),message,Snackbar.LENGTH_SHORT);

        paperSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paperSizeStr=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        colored.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String coloredStr=parent.getItemAtPosition(position).toString();
                if (coloredStr.equals("YES")) {
                    coloredBool = true;
                    value = value * 2;
                    //estimatedPrice=pages*value*1.00;
                    //estimatedPriceView.setText(String.format(Locale.getDefault(),"%.2f DA",estimatedPrice));
                }
                else
                    coloredBool=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        typeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.writing) {
                    typeStr= "Writing/Printing";
                    //value=10;
                    //estimatedPrice=pages*value*1.00;
                   // estimatedPriceView.setText(String.format(Locale.getDefault(),"%.2f DA",estimatedPrice));
                }
                else {
                    typeStr="Printing";
                    //value=5;
                }
            }
        });

        titleLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtitle.getText().toString().isEmpty()) {
                    titleLayout.setErrorEnabled(false);
                    titleStr=edtitle.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pagesLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edpages.getText().toString().isEmpty()) {
                    pagesLayout.setErrorEnabled(false);
                    pageStr=edpages.getText().toString();
                    pages=Integer.parseInt(pageStr);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //estimatedPrice=pages*value*1.00;
               // estimatedPriceView.setText(String.format(Locale.getDefault(),"%.2f DA",estimatedPrice));
            }
        });

        registrBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                convertall();
                if (proceed) {
                    getTime(new MyCallbackforThisActivity() {
                        @Override
                        public void onCallback(boolean value) {
                            if (value) {
                                final AlertDialog.Builder builder=new AlertDialog.Builder(requestActivity.this);
                                builder.setTitle(getResources().getString(R.string.confirmation));
                                builder.setMessage(getResources().getString(R.string.proceed));
                                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        prgresVisible();
                                        if (activity!=1)
                                            addNewOrder(orderIdall, userId,userNameStr, adminId,adminNameStr,payId, pages,paperSizeStr
                                                    ,coloredBool,endDayStr,startDayStr,typeStr,titleStr,detailStr,"pending"
                                                    ,estimatedPrice,0.00);
                                        else
                                        {
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("paperSize", paperSizeStr);
                                            resultIntent.putExtra("pages", pages);
                                            resultIntent.putExtra("subject",titleStr);
                                            resultIntent.putExtra("type",typeStr);
                                            resultIntent.putExtra("colored",coloredBool);
                                            resultIntent.putExtra("startDay",startDayStr);
                                            resultIntent.putExtra("endDay",endDayStr);
                                            resultIntent.putExtra("details",detailStr);
                                            resultIntent.putExtra("estimatedPrice",estimatedPrice);
                                            myRef.child("timestamp").child(keyStr).removeValue();
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }
                            /*if (!uris.isEmpty())
                                uploadFiles(uris,orderIdall);*/
                                    }
                                });
                                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                    });

                }
            }

        });
    }

        public void convertall(){

            int checkedId=typeRadio.getCheckedRadioButtonId();
            if(checkedId == R.id.writing) {
                typeStr= "Writing/Printing";
            }
            else {
                typeStr="Printing";
            }

            detailStr=eddetails.getText().toString();

            if (TextUtils.isEmpty(titleStr) || TextUtils.isEmpty(pageStr)) {
                proceed = false;
                Toast.makeText(getApplicationContext(),"One of th required fields is empty",Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(titleStr)){
                    titleLayout.setError("This field is required");
                    titleLayout.setErrorEnabled(true);
                }
                if (TextUtils.isEmpty(pageStr)) {
                    pagesLayout.setError("This field is required");
                    pagesLayout.setErrorEnabled(true);
                }

            }
            else {
                proceed=true;
                titleLayout.setErrorEnabled(false);
                pagesLayout.setErrorEnabled(false);
            }
        }

        public void sendall(){
            Intent secintent=new Intent(requestActivity.this,RequestFinishActivity.class);
            startActivity(secintent);
            finish();
        }

    public void addNewOrder(Integer orderId, Integer userId,String userName, Integer adminId,String adminName,String payId, Integer pages, String paperSize, Boolean colored, String endDay,String startDay, String type, String title, String details, String stat, Double estimatedPrice,Double priceFinal){
        Service service=new Service(orderId,userId,userName,adminId,adminName,payId,pages,paperSize,colored,endDay,startDay,type,title,details,stat,estimatedPrice,priceFinal);
        String strhelp=Integer.toString(orderId);

        servicesRef.child(strhelp).setValue(service).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Notification notification=new Notification(service.getServiceId(),"newOrder",userName,key," asks you for an order("+service.getServiceSubject()+")",timestamp,false);
                database.getReference("Notifications").push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        orderRef.setValue(orderIdall + 1);
                        myRef.child("timestamp").child(keyStr).removeValue();

                        sendall();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                prgresInvisible();
                Toast.makeText(getApplicationContext(),"The order has not been sent",Toast.LENGTH_LONG).show();
            }
        });
    }

    public int numberOfAddMinutes(String s)  {
        switch (s) {
            case "10 minutes": return 10;
            case "15 minutes": return 15;
            case "20 minutes": return 20;
            case "30 minutes": return 30;
            case "45 minutes": return 45;
            case "1 hour": return 60;
            case "2 hours": return 2*60;
            case "3 hours": return 3*60;
            case "4 hours": return 4*60;
            case "6 hours": return 6*60;
            case "8 hours": return 8*60;
            case "10 hours": return 10*60;
            case "12 hours": return 12*60;
            case "14 hours": return 14*60;
            case "16 hours": return 16*60;
            case "18 hours": return 18*60;
            case "20 hours": return 20*60;
            case "1 day": return 24*60;
            case "2 days": return 24*60*2;
            case "3 days": return 24*60*3;
            case "4 days": return 24*60*4;
            case "5 days": return 24*60*5;
            case "6 days": return 24*60*6;
            case "1 week": return 24*60*7;
            case "2 weeks": return 24*60*7*2;
            case "3 weeks": return 24*60*7*3;
            case "1 month": return 24*60*7*4;
            default: return 0;
        }
    }

    public String addDate(String s,Calendar c) {
        SimpleDateFormat sdf;

        if (numberOfAddMinutes(s)==0)
            return "None";
        else {
            c.add(Calendar.MINUTE, numberOfAddMinutes(s));
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);
            return sdf.format(c.getTime());
        }
    }

    public interface MyCallbackforThisActivity{
        void onCallback(boolean value);
    }

    public void getTime(final MyCallbackforThisActivity myCallbackforThisActivity) {
        DatabaseReference timeRef= myRef.child("timestamp").push();
        timeRef.setValue(ServerValue.TIMESTAMP);
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keyStr=snapshot.getKey();

                    timestamp = (Long) snapshot.getValue();

                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(timestamp);

                    simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);
                    startDayStr = simpledateformat.format(calendar.getTime());

                    endDayStr = addDate(endDay.getSelectedItem().toString(), calendar);

                    myCallbackforThisActivity.onCallback(true);
                }
                else
                    myCallbackforThisActivity.onCallback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void prgresVisible() {
        progressBar.setVisibility(View.VISIBLE);

        typeRadio.setEnabled(false);
        edtitle.setEnabled(false);
        edpages.setEnabled(false);
        eddetails.setEnabled(false);
        paperSize.setEnabled(false);
        colored.setEnabled(false);
        endDay.setEnabled(false);
        //changBtn.setEnabled(false);
        registrBtn.setEnabled(false);
        //cancelBtn.setEnabled(false);
    }

    public void prgresInvisible() {
        progressBar.setVisibility(View.INVISIBLE);

        typeRadio.setEnabled(true);
        edtitle.setEnabled(true);
        edpages.setEnabled(true);
        eddetails.setEnabled(true);
        paperSize.setEnabled(true);
        colored.setEnabled(true);
        endDay.setEnabled(true);
        //changBtn.setEnabled(true);
        registrBtn.setEnabled(true);
        //cancelBtn.setEnabled(true);
    }
}
