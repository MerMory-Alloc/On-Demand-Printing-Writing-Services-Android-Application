package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


public class PersonalInfoActivity extends AppCompatActivity {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z]{1}\\w{4,30}$");

    EditText nameView,emailView,phoneView,firstView,lastView;
    DatePicker birthdaypick;
    Button editBtn,saveBtn,cancelBtn;
    String name,email,phone,firstname,lastname,birthdayStr;
    Admin admin=new Admin();
    FirebaseDatabase database;
    DatabaseReference usersRef;
    DatabaseReference adminRef;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        nameView=(EditText) findViewById(R.id.name);
        emailView=(EditText) findViewById(R.id.email);
        phoneView=(EditText) findViewById(R.id.phone);
        firstView=(EditText) findViewById(R.id.firstname);
        lastView=(EditText) findViewById(R.id.lastname);
        birthdaypick=(DatePicker) findViewById(R.id.datePicker1);
        editBtn=(Button) findViewById(R.id.edit);
        saveBtn=(Button) findViewById(R.id.save);
        cancelBtn=(Button) findViewById(R.id.cancel);

        mAuth=FirebaseAuth.getInstance();

        user=mAuth.getCurrentUser();

        database=FirebaseDatabase.getInstance();
        usersRef=database.getReference("Users");
        adminRef=usersRef.child("Admins");

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admin=snapshot.child(user.getUid()).getValue(Admin.class);
                name=admin.getSrvcProviderName();
                firstname=admin.getSrvcProviderFirstName();
                lastname=admin.getSrvcProviderLastName();
                email=admin.getSrvcProviderEmail();
                phone=admin.getSrvcProviderPhone();
                birthdayStr=admin.getSrvcProviderBirthDay();

                convertToDay(birthdayStr);
                nameView.setText(name);
                emailView.setText(email);
                phoneView.setText(phone);
                firstView.setText(firstname);
                lastView.setText(lastname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nameView.setEnabled(false);
        emailView.setEnabled(false);
        phoneView.setEnabled(false);
        firstView.setEnabled(false);
        lastView.setEnabled(false);
        birthdaypick.setEnabled(false);

        saveBtn.setVisibility(View.INVISIBLE);
        cancelBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart(){
        super.onStart();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameView.setEnabled(true);
                emailView.setEnabled(true);
                phoneView.setEnabled(true);
                firstView.setEnabled(true);
                lastView.setEnabled(true);
                birthdaypick.setEnabled(true);

                editBtn.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToDay(birthdayStr);
                nameView.setText(name);
                emailView.setText(email);
                phoneView.setText(phone);
                firstView.setText(firstname);
                lastView.setText(lastname);


                nameView.setEnabled(false);
                emailView.setEnabled(false);
                phoneView.setEnabled(false);
                firstView.setEnabled(false);
                lastView.setEnabled(false);
                birthdaypick.setEnabled(false);

                saveBtn.setVisibility(View.INVISIBLE);
                cancelBtn.setVisibility(View.INVISIBLE);
                editBtn.setVisibility(View.VISIBLE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr=nameView.getText().toString();
                String emailStr=emailView.getText().toString();
                String phonStr=phoneView.getText().toString();
                String firstStr=firstView.getText().toString();
                String lastStr=lastView.getText().toString();
                String day = null;

                if(TextUtils.isEmpty(nameStr)|| TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(phonStr) || TextUtils.isEmpty(firstStr) || TextUtils.isEmpty(lastStr)) {
                    Toast.makeText(getApplicationContext(),R.string.requiredemty,Toast.LENGTH_LONG).show();
                }
                else if (!isUsernameValid(nameStr)) {
                    Toast.makeText(getApplicationContext(),R.string.invalidusername,Toast.LENGTH_LONG).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    Toast.makeText(getApplicationContext(),R.string.fui_invalid_email_address,Toast.LENGTH_LONG).show();
                }
                else if(!android.util.Patterns.PHONE.matcher(phonStr).matches()) {
                    Toast.makeText(getApplicationContext(),R.string.fui_invalid_phone_number,Toast.LENGTH_LONG).show();
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            convertToString(birthdaypick, day);

                            admin.setSrvcProviderName(nameStr);
                            admin.setSrvcProviderEmail(emailStr);
                            admin.setSrvcProviderPhone(phonStr);
                            admin.setSrvcProviderFirstName(firstStr);
                            admin.setSrvcProviderLastName(lastStr);
                            admin.setSrvcProviderBirthDay(day);

                            adminRef.child(user.getUid()).setValue(admin);

                            nameView.setEnabled(false);
                            emailView.setEnabled(false);
                            phoneView.setEnabled(false);
                            firstView.setEnabled(false);
                            lastView.setEnabled(false);
                            birthdaypick.setEnabled(false);

                            saveBtn.setVisibility(View.INVISIBLE);
                            cancelBtn.setVisibility(View.INVISIBLE);
                            editBtn.setVisibility(View.VISIBLE);
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

    boolean isUsernameValid(CharSequence username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public void convertToDay(String str){
        int day,month,year;
        String[] strings=str.split("/");
        day =Integer.parseInt(strings[0]);
        month=Integer.parseInt(strings[1]);
        year=Integer.parseInt(strings[2]);
        birthdaypick.updateDate(year,month,day);
    }

    public void convertToString(DatePicker dtp,String str){
        String day=String.valueOf(dtp.getDayOfMonth());
        String month=String.valueOf(dtp.getMonth());
        String year=String.valueOf(dtp.getYear());

        str=day+"/"+month+"/"+year;
    }
}
