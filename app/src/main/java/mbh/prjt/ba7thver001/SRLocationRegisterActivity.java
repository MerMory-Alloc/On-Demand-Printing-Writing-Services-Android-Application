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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SRLocationRegisterActivity extends AppCompatActivity {

    final int GLOBAL=(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    private static final Pattern ONLY_LETTERS =
            Pattern.compile("^[a-zA-Z\\s]{0,40}$");

    String phonStr,countryStr,provinceStr,cityStr,zipStr="",sAddressStr="",passStr;
    EditText phonView,provinceView,cityView,zipView,sAddressView;
    TextInputLayout phoneLayout,countryLayout,provinceLayout,cityLayout,sAddressLayout,zipLayout;
    CountryCodePicker ccp,countryPicker;
    Button rgstrBtn;
    ProgressBar progressBar;
    Client client=new Client();

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srlocation_register);
        hideSystemUI();

        rgstrBtn=(Button) findViewById(R.id.rgstr);
        countryPicker=(CountryCodePicker) findViewById(R.id.country);
        provinceView=(EditText) findViewById(R.id.province);
        cityView=(EditText) findViewById(R.id.city);
        zipView=(EditText) findViewById(R.id.zipcode);
        sAddressView=(EditText) findViewById(R.id.address);
        phonView=(EditText) findViewById(R.id.entrphon);
        phoneLayout=(TextInputLayout) findViewById(R.id.phone_text_input_layout);
        provinceLayout=(TextInputLayout) findViewById(R.id.province_text_input_layout);
        cityLayout=(TextInputLayout) findViewById(R.id.city_text_input_layout);
        zipLayout=(TextInputLayout) findViewById(R.id.zip_code_text_input_layout);
        sAddressLayout=(TextInputLayout) findViewById(R.id.street_address_text_input_layout);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular1);

        progressBar.setVisibility(View.INVISIBLE);

        ccp.registerCarrierNumberEditText(phonView);

        Intent ourIntent=getIntent();
        client=(Client) ourIntent.getSerializableExtra("client");

        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Users/Clients");
    }

    @Override
    protected void onStart() {
        super.onStart();

        countryPicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                ccp.setCountryForNameCode(countryPicker.getSelectedCountryNameCode());
            }
        });

        provinceLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!onlyLetters(provinceView.getText().toString())) && (!provinceView.getText().toString().isEmpty())) {
                    provinceLayout.setError("Province Name is not valid");
                    provinceLayout.setErrorEnabled(true);
                } else
                    provinceLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cityLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!onlyLetters(cityView.getText().toString())) && (!cityView.getText().toString().isEmpty())) {
                    cityLayout.setError("City Name is not valid");
                    cityLayout.setErrorEnabled(true);
                } else
                    cityLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!ccp.isValidFullNumber()) && (!phonView.getText().toString().isEmpty())) {
                    phoneLayout.setError(getResources().getString(R.string.fui_invalid_phone_number));
                    phoneLayout.setErrorEnabled(true);
                } else
                    phoneLayout.setErrorEnabled(false);
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
                    Toast.makeText(getApplicationContext(), "one of the required fields is empty", Toast.LENGTH_LONG).show();
                    emptyError(countryStr,countryLayout);
                    emptyError(provinceStr,provinceLayout);
                    emptyError(cityStr,cityLayout);
                    emptyError(zipStr,zipLayout);
                    emptyError(phonStr,phoneLayout);
                    emptyError(sAddressStr,sAddressLayout);

                }
                else if (!onlyLetters(countryStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "the Country name is not valid", Toast.LENGTH_LONG).show();
                }
                else if (!onlyLetters(provinceStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "the Province name is not valid", Toast.LENGTH_LONG).show();
                }
                else if (!onlyLetters(cityStr)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "the City name is not valid", Toast.LENGTH_LONG).show();
                }
                else if (!ccp.isValidFullNumber()) {
                    Toast.makeText(getApplicationContext(), R.string.fui_invalid_phone_number, Toast.LENGTH_LONG).show();
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    final AlertDialog.Builder builder=new AlertDialog.Builder(SRLocationRegisterActivity.this);
                    builder.setTitle(R.string.confirmation);
                    builder.setMessage(R.string.proceed);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            progressBar.setVisibility(View.VISIBLE);
                            client.setServiceRequesterCountry(countryStr);
                            client.setServiceRequesterProvince(provinceStr);
                            client.setServiceRequesterCity(cityStr);
                            client.setServiceRequesterZipCode(zipStr);
                            client.setServiceRequesterStreetAddress(sAddressStr);
                            client.setServiceRequesterPhone(phonStr);

                            HashMap map=new HashMap();
                            map.put(mAuth.getCurrentUser().getUid(),client);

                            myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                        new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                                            @Override
                                            public void timecallback(Long value, String key) {
                                                Notification notification=new Notification(-1,"welcome","MBH:",mAuth.getCurrentUser().getUid()," Welcome to our beautiful App",value,false);
                                                database.getReference("Notifications").push().setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        database.getReference("timestamp").child(key).removeValue();
                                                        gotoLogin();
                                                    }
                                                });
                                            }
                                        });
                                        progressBar.setVisibility(View.INVISIBLE);
                                        gotoLogin();
                                    }
                                    else {
                                        progressBar.setVisibility(View.INVISIBLE);
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

            }
        });
    }

    public void convertAll(){
        countryStr=countryPicker.getSelectedCountryName();
        provinceStr=provinceView.getText().toString();
        cityStr=cityView.getText().toString();
        zipStr=zipView.getText().toString();
        sAddressStr=sAddressView.getText().toString();
        phonStr=phonView.getText().toString();
    }

    boolean isRegistrationInfoIsNotEmpty(){

        return ((TextUtils.isEmpty(phonStr)) || (TextUtils.isEmpty(countryStr)) || (TextUtils.isEmpty(provinceStr)) || (TextUtils.isEmpty(cityStr)) || (TextUtils.isEmpty(zipStr)) || (TextUtils.isEmpty(sAddressStr)) );
    }

    boolean onlyLetters(String s) {
        return ONLY_LETTERS.matcher(s).matches();
    }

    void emptyError(String s,TextInputLayout l) {
        if (s.isEmpty()) {
            l.setError(getResources().getString(R.string.thisempty));
            l.setErrorEnabled(true);
        }
    }

    public void gotoLogin(){
        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
        finish();
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
