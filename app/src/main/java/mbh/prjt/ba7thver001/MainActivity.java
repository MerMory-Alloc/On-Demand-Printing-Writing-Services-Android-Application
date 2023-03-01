package mbh.prjt.ba7thver001;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ClientProfileFragment.OnFragmentInteractionListener {

    private static final int SELECT_PICTURE=1;

    Client client=new Client();
    private FirebaseAuth mAuth;
    ArrayList<Notification> notificationArrayList=new ArrayList<Notification>();
    BottomNavigationView bottomNavigation;
    private FirebaseDatabase database;
    DatabaseReference usersRef;
    DatabaseReference clientRef;
    DatabaseReference notificationRef;

    private StorageReference mStorageRef;
    private StorageReference clientStorageRef;
    private StorageReference profilePhotoRef;

    BadgeDrawable badge;

    int countNewNotifications;
    ArrayList<String> keys=new ArrayList<String>();

    HashMap map=new HashMap();


    ValueEventListener mListner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            notificationArrayList.clear();
            keys.clear();
            countNewNotifications=0;
            if (snapshot!=null) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("toId").getValue(String.class).equals(mAuth.getCurrentUser().getUid())){
                        notificationArrayList.add(snap.getValue(Notification.class));
                        if (!snap.child("seen").getValue(Boolean.class)) {
                            countNewNotifications++;
                            keys.add(snap.getKey());
                        }
                    }
                }
                addBadge(countNewNotifications);
                sort(notificationArrayList);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        map.put("seen",true);

        badge = bottomNavigation.getOrCreateBadge(R.id.notification);

        database=FirebaseDatabase.getInstance();
        usersRef=database.getReference("Users");
        clientRef=usersRef.child("Clients");
        notificationRef=database.getReference("Notifications");

        mAuth=FirebaseAuth.getInstance();




        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                client=snapshot.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).getValue(Client.class);
                if (client != null) {
                    openFirstFragment(ClientDashboardFragment.newInstance(client.getUserId()
                            ,client.getServiceRequesterCountry(),client.getServiceRequesterProvince()));
                   notificationRef.addValueEventListener(mListner);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();



        bottomNavigation.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.profile:
                    openFragment(ClientProfileFragment.newInstance(client));
                    return true;
                case R.id.dashboard:
                    openFirstFragment(ClientDashboardFragment.newInstance(client.getUserId()
                            ,client.getServiceRequesterCountry(),client.getServiceRequesterProvince()));
                    return true;
                case R.id.notification:
                    openNote(keys.size());
                    return true;
            }

            return false;
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Gather the values from the entities here.

        notificationRef.removeEventListener(mListner);
    }

    public void sort(ArrayList<Notification> arrayList) {
        Notification notification = new Notification();
        boolean sorted = false;

        while(!sorted) {
            sorted = true;
            for (int i = 0; i < arrayList.size()-1; i++) {
                Calendar cal1=Calendar.getInstance();
                cal1.setTimeInMillis((arrayList.get(i).getNotificationcreatTime()));
                Calendar cal2=Calendar.getInstance();
                cal2.setTimeInMillis(arrayList.get(i+1).getNotificationcreatTime());
                if (cal1.before(cal2)){
                    notification = arrayList.get(i);
                    arrayList.set(i,arrayList.get(i+1));
                    arrayList.set(i+1,notification);
                    sorted = false;
                }
            }
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFirstFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    public void openNote(int size){

        if (size==0){
            addBadge(0);
            openFragment(ClientNotificationFragment.newInstance(notificationArrayList));
        }
        else {
            notificationRef.child(keys.get(size-1)).updateChildren(map);
            size=size-1;
            openNote(size);
        }
    }

    @Override
    public void onFragmentInteraction(Client client) {
        HashMap map=new HashMap();
        map.put("serviceRequesterPhotoProfile",client.getServiceRequesterPhotoProfile());
        clientRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);
        this.client=client;
    }

    @Override
    public void onBackPressed() {
        FrameLayout fl = (FrameLayout) findViewById(R.id.frame);
        if (fl.getChildCount() == 1) {
            super.onBackPressed();
            if (fl.getChildCount() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Close App?")
                        .setMessage("Do you really want to close this beautiful app?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
                // load your first Fragment here
            }
        } else if (fl.getChildCount() == 0) {
            // load your first Fragment here
            openFirstFragment(DashboardFragment.newInstance(client.getUserId()));
        } else {
            super.onBackPressed();
        }
    }
    private void addBadge(int count) {

        badge.setNumber(count);
        if (count>0)
            badge.setVisible(true);
        else
            badge.setVisible(false);
    }
}
