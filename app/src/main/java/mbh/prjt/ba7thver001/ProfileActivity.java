/*package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE=1;
    final int GLOBAL=(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    ImageView photoProfil;
    CardView gotoServices,signOutBtn,stingBtn;
    TextView usernamProfile;
    Admin admin=new Admin();
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    StorageReference adminStorageRef;
    StorageReference profilePhotoRef;
    private FirebaseDatabase database;
    DatabaseReference usersRef;
    DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        hideSystemUI();

        photoProfil=(ImageView) findViewById(R.id.profilphoto);
        gotoServices=(CardView) findViewById(R.id.seerequests);
        usernamProfile=(TextView) findViewById(R.id.usernameprofile);
        stingBtn=(CardView) findViewById(R.id.setting);
        signOutBtn=(CardView) findViewById(R.id.out);

        database=FirebaseDatabase.getInstance();
        usersRef=database.getReference("Users");
        adminRef=usersRef.child("Admins");

        mAuth=FirebaseAuth.getInstance();

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admin=snapshot.child(mAuth.getCurrentUser().getUid()).getValue(Admin.class);
                usernamProfile.setText(admin.getSrvcProviderName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();
        adminStorageRef=mStorageRef.child("AdminsFiles").child(mAuth.getCurrentUser().getUid());
        profilePhotoRef=adminStorageRef.child("profileimg.jpg");

        profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(photoProfil);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        photoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        gotoServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,listOfRequestsActivity.class);
                intent.putExtra("adminId",admin.getSrvcProviderId());
                startActivity(intent);
            }
        });

        stingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfilSettingsActivity.class);
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        mAuth.signOut();
                        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                        startActivity(intent);
                        finish();
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
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && data != null) {
            Uri selectedimage = data.getData();

            uploadImageToFirebase(selectedimage);
        }
    }

    public void uploadImageToFirebase(Uri image){

        profilePhotoRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(photoProfil);
                        admin.setSrvcProviderProfilImage(uri.toString());
                        adminRef.child(mAuth.getCurrentUser().getUid()).setValue(admin);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"couldnt upload the picture",Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(GLOBAL);
    }

}*/
