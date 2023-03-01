package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

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

    ProgressBar progressBar;
    EditText oldpassView,newpassView,confpassView;
    TextInputLayout oldpassLayout,newpassLayout,confipassLayout;
    Button chngBtn,vrfBtn;
    String oldpassStr,newpassStr,confpassStr;

    FirebaseDatabase database;
    DatabaseReference usersRef;
    DatabaseReference cleintRef;
    DatabaseReference emailRef;
    DatabaseReference passwordRef;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressBar=(ProgressBar) findViewById(R.id.prgs);
        chngBtn=(Button) findViewById(R.id.chng);
        vrfBtn=(Button) findViewById(R.id.vrf);
        oldpassView=(EditText) findViewById(R.id.oldpass);
        newpassView=(EditText) findViewById(R.id.newpass);
        confpassView=(EditText) findViewById(R.id.confrmpass);
        oldpassLayout=(TextInputLayout) findViewById(R.id.old_password_text_input_layout);
        newpassLayout=(TextInputLayout) findViewById(R.id.new_password_text_input_layout);
        confipassLayout=(TextInputLayout) findViewById(R.id.confirm_password_text_input_layout);

        progressBar.setVisibility(View.INVISIBLE);
        newpassView.setEnabled(false);
        newpassView.setVisibility(View.INVISIBLE);
        confpassView.setEnabled(false);
        confpassView.setVisibility(View.INVISIBLE);
        chngBtn.setEnabled(false);
        chngBtn.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();


    }

    @Override
    protected void onStart() {
        super.onStart();

        oldpassLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldpassStr = oldpassView.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        vrfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeybaord();

                if (TextUtils.isEmpty(oldpassStr)) {
                    Toast.makeText(getApplicationContext(), R.string.passwordemty, Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    String emailStr = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(emailStr, oldpassStr);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        newpassView.setEnabled(true);
                                        newpassView.setVisibility(View.VISIBLE);
                                        confpassView.setEnabled(true);
                                        confpassView.setVisibility(View.VISIBLE);
                                        chngBtn.setEnabled(true);
                                        chngBtn.setVisibility(View.VISIBLE);
                                        oldpassView.setEnabled(false);
                                        oldpassView.setVisibility(View.INVISIBLE);
                                        vrfBtn.setEnabled(false);
                                        vrfBtn.setVisibility(View.INVISIBLE);

                                        Toast.makeText(getApplicationContext(), R.string.rightoldpass, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getApplicationContext(), R.string.wrongpassword, Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        newpassLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0) && (s.length() < 8)) {
                    newpassLayout.setError("the password must be at least 8 characters ");
                    newpassLayout.setErrorEnabled(true);
                } else if ((!isPasswordValid(newpassView.getText().toString())) && (!newpassView.getText().toString().isEmpty())) {
                    newpassLayout.setError("the password must contain at least one number and one special character ");
                    newpassLayout.setErrorEnabled(true);
                } else {
                    newpassLayout.setErrorEnabled(false);
                    newpassStr = newpassView.getText().toString();
                }
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
                if ((!isPasswordMatch(newpassView.getText().toString(), confpassView.getText().toString())) && (!confpassView.getText().toString().isEmpty())) {
                    confipassLayout.setError("Password does not match!");
                    confipassLayout.setErrorEnabled(true);
                } else {
                    confipassLayout.setErrorEnabled(false);
                    confpassStr = confpassView.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        chngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeybaord();

                if (isFormInfoIsNotEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.requiredemty, Toast.LENGTH_SHORT).show();
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            user.updatePassword(newpassStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), R.string.passchanged, Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                                        startActivity(intent);
                                        finish();

                                    } else
                                        Toast.makeText(getApplicationContext(), R.string.passwordnotchanged, Toast.LENGTH_LONG).show();
                                }
                            });

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

    boolean isFormInfoIsNotEmpty(){
        return ((TextUtils.isEmpty(newpassStr)) || (TextUtils.isEmpty(confpassStr)));
    }

    boolean isPasswordValid(CharSequence password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    boolean isPasswordMatch(String password,String conffirmPasword){
        return password.equals(conffirmPasword);
    }

    private void hideKeybaord() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
