package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientOnWaitForPaimentOrderActivity extends AppCompatActivity {

    ListView onWaitServiceListe;
    ArrayList<Service> listofonwaitservices=new ArrayList<Service>();
    RelativeLayout relativeLayout;

    ProgressBar progressBar;

    Integer currentClientId;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference servicesRef=myRef.child("Services");

    ValueEventListener mValueListner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            listofonwaitservices.clear();
            for (DataSnapshot snap:snapshot.getChildren()) {
                Service value= snap.getValue(Service.class);
                if (value.getServiceRequesterId()==currentClientId) {
                    if (value.getServiceState().equals("onWait")) {
                        listofonwaitservices.add(value);
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            final ServiceListAdapter onWaitServiceAdapter=new ServiceListAdapter(getApplicationContext(),R.layout.list_element,listofonwaitservices);
            onWaitServiceListe.setAdapter(onWaitServiceAdapter);

            if (onWaitServiceAdapter.isEmpty())
                relativeLayout.setVisibility(View.VISIBLE);
            else
                onWaitServiceListe.setVisibility(View.VISIBLE);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_on_wait_for_paiment_order);

        onWaitServiceListe= (ListView) findViewById(R.id.listonwaitservices);
        relativeLayout = (RelativeLayout) findViewById(R.id.laynone);
        relativeLayout.setVisibility(View.INVISIBLE);
        progressBar=(ProgressBar) findViewById(R.id.prgs);
        onWaitServiceListe.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        currentClientId=(Integer) intent.getIntExtra("clientId",0);

        servicesRef.addValueEventListener(mValueListner);


    }

    @Override
    protected void onStart() {
        super.onStart();

        onWaitServiceListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.seedetail) {
                    Service selectedItem = (Service) parent.getItemAtPosition(position);
                    Intent gointent=new Intent(getApplicationContext(),ClientServiceDetailActivity.class);
                    gointent.putExtra("service",selectedItem);
                    gointent.putExtra("activity","onWait");
                    startActivity(gointent);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            servicesRef.removeEventListener(mValueListner);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
