package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    final int GLOBAL=(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=!:?./§<>*;,(_)~{|`])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
    RadioGroup typeUserRadio;
    RadioButton srvcproRbtn,srvcreqRbtn;
    EditText emailView,passView,confpassView,confemailView;
    TextInputLayout emailLayout,passLayout,confipassLayout,confiemailLayout;
    String emailStr,passStr,confpassStr,confemailStr,userTypeStr;
    Button rgstrBtn;
    ProgressBar progressBar;

    ArrayList<String> papers= new ArrayList<String>();
    Admin admin=new Admin(0,"","","","","","","","","","","","","","","",papers,0.00,0.5f,0,false,false);
    Client client=new Client(0,"","","","","","","","","","","","","","",false);
    Integer userIdall;
    Integer adminIdall;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference usersRef;
    DatabaseReference clientRef;
    DatabaseReference idUserRef;
    DatabaseReference adminRef;
    DatabaseReference idAdminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hideSystemUI();


        typeUserRadio=(RadioGroup) findViewById(R.id.typeuser);
        srvcproRbtn=(RadioButton) findViewById(R.id.srvcpro);
        srvcreqRbtn=(RadioButton) findViewById(R.id.srvcreq);
        emailView=(EditText) findViewById(R.id.entrmail);
        confemailView=(EditText) findViewById(R.id.confirmemail);
        passView=(EditText) findViewById(R.id.entrpass);
        confpassView=(EditText) findViewById(R.id.confrmpass);
        emailLayout=(TextInputLayout) findViewById(R.id.email_text_input_layout);
        confiemailLayout=(TextInputLayout) findViewById(R.id.confirm_email_text_input_layout);
        confipassLayout=(TextInputLayout) findViewById(R.id.confirm_password_text_input_layout);
        passLayout=(TextInputLayout) findViewById(R.id.password_text_input_layout);
        rgstrBtn=(Button) findViewById(R.id.rgstr);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular1);

        progressBar.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        idUserRef=myRef.child("UserNumber");
        usersRef=myRef.child("Users");
        clientRef=usersRef.child("Clients");
        idAdminRef=myRef.child("AdminsNumber");
        adminRef=usersRef.child("Admins");

        idUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    userIdall=snapshot.getValue(Integer.class);
                else
                    userIdall=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        idAdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    adminIdall = snapshot.getValue(Integer.class);
                else
                    adminIdall=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmailValid(emailView.getText().toString()) && (!emailView.getText().toString().isEmpty())) {
                    emailLayout.setError(getResources().getString(R.string.pleasenteremail));
                    emailLayout.setErrorEnabled(true);
                }
                else
                    emailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        confiemailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!isEmailMatch(emailView.getText().toString(),confemailView.getText().toString())) && (!confemailView.getText().toString().isEmpty())) {
                    confiemailLayout.setError(getResources().getString(R.string.emailnotmatch));
                    confiemailLayout.setErrorEnabled(true);
                }
                else
                    confiemailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length()>0)&&(s.length()<8)) {
                    passLayout.setError(getResources().getString(R.string.passwordmorethaneight));
                    passLayout.setErrorEnabled(true);
                }
                else if ((!isPasswordValid(passView.getText().toString())) && (!passView.getText().toString().isEmpty())) {
                    passLayout.setError(getResources().getString(R.string.passwordneedsonenumberandcaracter));
                    passLayout.setErrorEnabled(true);
                }
                else
                    passLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        confipassLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!isPasswordMatch(passView.getText().toString(),confpassView.getText().toString())) && (!confpassView.getText().toString().isEmpty())) {
                    confipassLayout.setError(getResources().getString(R.string.passdeosnotmatch));
                    confipassLayout.setErrorEnabled(true);
                }
                else
                    confipassLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        rgstrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                convertAll();

                if (isRegistrationInfoIsNotEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.requiredemty, Toast.LENGTH_LONG).show();
                    if (emailStr.isEmpty()) {
                        emailLayout.setError(getResources().getString(R.string.thisempty));
                        emailLayout.setErrorEnabled(true);
                    }
                    if (passStr.isEmpty()) {
                        passLayout.setError(getResources().getString(R.string.thisempty));
                        passLayout.setErrorEnabled(true);
                    }
                }
                else if (!isEmailValid(emailStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.emailnotvalid, Toast.LENGTH_LONG).show();
                }
                else if(!isEmailMatch(emailStr,confemailStr)){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),R.string.confemailnotmatch,Toast.LENGTH_LONG).show();
                }
                else if(!isPasswordValid(passStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.passnotvalid, Toast.LENGTH_LONG).show();
                }
                else if (!isPasswordMatch(passStr,confpassStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),R.string.confpassnotmatch,Toast.LENGTH_LONG).show();
                }
                else {
                    signIn();
                }
            }
        });
    }

    public void convertAll(){
        int checkedId=typeUserRadio.getCheckedRadioButtonId();
        if(checkedId == R.id.srvcreq) {
            userTypeStr= srvcreqRbtn.getText().toString();///////////////////////////////////
        }
        else if(checkedId == R.id.srvcpro) {
            userTypeStr=srvcproRbtn.getText().toString();/////////////////////////////////////
        }
        emailStr=emailView.getText().toString();
        passStr=passView.getText().toString();
        confpassStr=confpassView.getText().toString();
        confemailStr=confemailView.getText().toString();

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    boolean isPasswordValid(CharSequence password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    boolean isPasswordMatch(String password,String conffirmPasword){
        return password.equals(conffirmPasword);
    }

    boolean isEmailMatch(String email,String confirmemail){
        return email.equals(confirmemail);
    }

    boolean isRegistrationInfoIsNotEmpty(){
        return ((TextUtils.isEmpty(passStr)) || (TextUtils.isEmpty(emailStr)) || (TextUtils.isEmpty(confpassStr))|| (TextUtils.isEmpty(confemailStr)));
    }

    private  void signIn(){
        progressBar.setVisibility(View.INVISIBLE);
        final AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.proceed);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailStr,passStr)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = task.getResult().getUser();
                                    if (userTypeStr.equals("Service provider")) {///////////////////////////////////////////////////////////////
                                        admin.setSrvcProviderEmail(user.getEmail());
                                        admin.setSrvcProviderId(adminIdall);
                                        adminRef.child(user.getUid()).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    idAdminRef.setValue(admin.getSrvcProviderId() + 1);
                                                    Intent intent = new Intent(getApplicationContext(), RegisterSP1Activity.class);
                                                    intent.putExtra("admin", admin);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),R.string.failed+" "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }
                                    else {
                                        client.setServiceRequesterEmail(user.getEmail());
                                        client.setUserId(userIdall);
                                        clientRef.child(user.getUid()).setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    idUserRef.setValue(client.getUserId() + 1);
                                                    Intent intent=new Intent(getApplicationContext(),RegisterSRActivity.class);
                                                    intent.putExtra("client",client);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(intent);
                                                }
                                                else {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),R.string.failed+" "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.w("AuthError", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(),R.string.failed+" "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(GLOBAL);
    }
}
