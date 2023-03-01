package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterSP1Activity extends AppCompatActivity {

    final int GLOBAL=(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z]{1}\\w{4,30}$");

    Admin admin=new Admin();
    DatePicker birthday;
    RadioGroup typeSPRadio;
    RadioButton freeRbtn,buisRbtn;
    TextInputLayout usernameLayout,firstLayout,lastLayout;
    EditText nameView,firstView,lastView;
    String nameStr,firstStr="",lastStr="",birthdateStr,typeSpStr;
    ImageButton rgstrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sp1);
        hideSystemUI();

        usernameLayout=(TextInputLayout) findViewById(R.id.username_text_input_layout);
        nameView=(EditText) findViewById(R.id.username);
        firstView=(EditText) findViewById(R.id.firstname);
        firstLayout=(TextInputLayout) findViewById(R.id.fistname_text_input_layout);
        lastView=(EditText) findViewById(R.id.lastname);
        lastLayout=(TextInputLayout) findViewById(R.id.lastname_text_input_layout);
        typeSPRadio=(RadioGroup) findViewById(R.id.typesp);
        freeRbtn=(RadioButton) findViewById(R.id.freelance);
        buisRbtn=(RadioButton) findViewById(R.id.buisness);
        birthday=(DatePicker) findViewById(R.id.datePicker1);
        rgstrBtn=(ImageButton) findViewById(R.id.rgstr);

        Intent ourIntent=getIntent();
        admin= (Admin) ourIntent.getSerializableExtra("admin");
    }

    @Override
    protected void onStart() {
        super.onStart();


        usernameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!isUsernameValid(nameView.getText().toString()))&&(!nameView.getText().toString().isEmpty())) {
                    if (s.length()<5)
                        usernameLayout.setError(getResources().getString(R.string.usernamemustbemore));
                    else
                        usernameLayout.setError(getResources().getString(R.string.usernamedontstartwithnumber));

                    usernameLayout.setErrorEnabled(true);
                }
                else
                    usernameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        rgstrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertAll();

                if (isRegistrationInfoIsNotEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.requiredemty, Toast.LENGTH_LONG).show();
                    if (nameStr.isEmpty()) {
                        usernameLayout.setError(getResources().getString(R.string.thisempty));
                        usernameLayout.setErrorEnabled(true);
                    }

                }
                else if (!isUsernameValid(nameStr)) {
                    Toast.makeText(getApplicationContext(), R.string.invalidusername, Toast.LENGTH_LONG).show();
                }
                else {
                    admin.setSrvcProviderName(nameStr);
                    admin.setSrvcProviderFirstName(firstStr);
                    admin.setSrvcProviderLastName(lastStr);
                    admin.setSrvcProviderType(typeSpStr);
                    admin.setSrvcProviderBirthDay(birthdateStr);

                    Intent intent=new Intent(getApplicationContext(),SPLocationRegisterActivity.class);
                    intent.putExtra("admin",admin);
                    startActivity(intent);
                }
            }
        });

    }

    public void convertAll(){
        int checkedId=typeSPRadio.getCheckedRadioButtonId();
        if(checkedId == R.id.freelance) {
            typeSpStr= "Freelance";//////////////////////////////////////////////////////
        }
        else if(checkedId == R.id.buisness) {
            typeSpStr="Business Owner";//////////////////////////////////////////////////
        }
        nameStr=nameView.getText().toString();
        firstStr=firstView.getText().toString();
        lastStr=lastView.getText().toString();

        String day=String.valueOf(birthday.getDayOfMonth());
        String month=String.valueOf(birthday.getMonth());
        String year=String.valueOf(birthday.getYear());

        birthdateStr=day+"/"+month+"/"+year;

    }

    boolean isUsernameValid(CharSequence username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    boolean isRegistrationInfoIsNotEmpty(){
        return ((TextUtils.isEmpty(nameStr)));
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
