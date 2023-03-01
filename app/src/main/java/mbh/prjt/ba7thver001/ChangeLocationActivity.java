package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ChangeLocationActivity extends AppCompatActivity {

    private static final Pattern ONLY_LETTERS =
            Pattern.compile("^[a-zA-Z\\s]{0,40}$");

    String countryStr,provinceStr,cityStr,zipStr="",sAddressStr="";
    EditText provinceView,cityView,zipView,sAddressView;
    TextInputLayout countryLayout,provinceLayout,cityLayout,sAddressLayout,zipLayout;
    CountryCodePicker countryPicker;
    Button rgstrBtn;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference usersRef;
    DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        rgstrBtn=(Button) findViewById(R.id.rgstr);
        countryPicker=(CountryCodePicker) findViewById(R.id.country);
        provinceView=(EditText) findViewById(R.id.province);
        cityView=(EditText) findViewById(R.id.city);
        zipView=(EditText) findViewById(R.id.zipcode);
        sAddressView=(EditText) findViewById(R.id.address);
        provinceLayout=(TextInputLayout) findViewById(R.id.province_text_input_layout);
        cityLayout=(TextInputLayout) findViewById(R.id.city_text_input_layout);
        zipLayout=(TextInputLayout) findViewById(R.id.zip_code_text_input_layout);
        sAddressLayout=(TextInputLayout) findViewById(R.id.street_address_text_input_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular1);

        progressBar.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        usersRef=myRef.child("Users");
        adminRef=usersRef.child("Admins/"+mAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();

        provinceLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!onlyLetters(provinceView.getText().toString())) && (!provinceView.getText().toString().isEmpty())) {
                    provinceLayout.setError("Province Name is not valid");
                    provinceLayout.setErrorEnabled(true);
                }
                else
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
                }
                else
                    cityLayout.setErrorEnabled(false);
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
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    final AlertDialog.Builder builder=new AlertDialog.Builder(ChangeLocationActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to proceed?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            progressBar.setVisibility(View.VISIBLE);
                            HashMap map=new HashMap<>();
                            map.put("",countryStr);
                            map.put("",provinceStr);
                            map.put("",cityStr);
                            map.put("",zipStr);
                            map.put("",sAddressStr);

                            adminRef.updateChildren(map);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
    }

    boolean isRegistrationInfoIsNotEmpty(){

        return ((TextUtils.isEmpty(countryStr)) || (TextUtils.isEmpty(provinceStr)) || (TextUtils.isEmpty(cityStr)) || (TextUtils.isEmpty(zipStr)) || (TextUtils.isEmpty(sAddressStr)) );
    }

    boolean onlyLetters(String s) {
        return ONLY_LETTERS.matcher(s).matches();
    }

    void emptyError(String s,TextInputLayout l) {
        if (s.isEmpty()) {
            l.setError("This field is required");
            l.setErrorEnabled(true);
        }
    }

}
