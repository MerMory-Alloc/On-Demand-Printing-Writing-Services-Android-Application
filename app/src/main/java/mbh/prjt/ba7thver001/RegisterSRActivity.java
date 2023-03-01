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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterSRActivity extends AppCompatActivity {

    final int GLOBAL=(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z]{1}\\w{4,30}$");

    Client client=new Client();
    DatePicker birthday;
    TextInputLayout usernameLayout,firstLayout,lastLayout;
    EditText nameView,firstView,lastView;
    String nameStr,firstStr="",lastStr="",birthdateStr;
    ImageButton rgstrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sr);
        hideSystemUI();

        usernameLayout=(TextInputLayout) findViewById(R.id.username_text_input_layout);
        nameView=(EditText) findViewById(R.id.username);
        firstView=(EditText) findViewById(R.id.firstname);
        firstLayout=(TextInputLayout) findViewById(R.id.fistname_text_input_layout);
        lastView=(EditText) findViewById(R.id.lastname);
        lastLayout=(TextInputLayout) findViewById(R.id.lastname_text_input_layout);
        birthday=(DatePicker) findViewById(R.id.datePicker1);
        rgstrBtn=(ImageButton) findViewById(R.id.rgstr);

        Intent ourIntent=getIntent();
        client= (Client) ourIntent.getSerializableExtra("client");

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
                    client.setServiceRequesterName(nameStr);
                    client.setServiceRequesterFirstName(firstStr);
                    client.setServiceRequesterLastName(lastStr);
                    client.setServiceRequesterBirthDay(birthdateStr);

                    Intent intent=new Intent(getApplicationContext(),SRLocationRegisterActivity.class);
                    intent.putExtra("client",client);
                    startActivity(intent);
                }
            }
        });
    }

    public void convertAll(){
        nameStr=nameView.getText().toString();
        firstStr=firstView.getText().toString();
        lastStr=lastView.getText().toString();

        String day=String.valueOf(birthday.getDayOfMonth());
        String month=String.valueOf(birthday.getMonth());
        String year=String.valueOf(birthday.getYear());

        birthdateStr=day+"/"+month+"/"+year;

    }

    boolean isRegistrationInfoIsNotEmpty(){
        return ((TextUtils.isEmpty(nameStr)));
    }

    boolean isUsernameValid(CharSequence username) {
        return USERNAME_PATTERN.matcher(username).matches();
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
