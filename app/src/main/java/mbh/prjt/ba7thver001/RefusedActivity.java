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
import java.util.HashMap;

public class RefusedActivity extends AppCompatActivity {

    ListView refusedServiceList;
    ArrayList<Service> listofrefusedservices=new ArrayList<Service>();
    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    Integer currentAdminId;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference servicesRef=myRef.child("Services");

    ValueEventListener mValueListner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            listofrefusedservices.clear();
            for (DataSnapshot snap:snapshot.getChildren()) {
                Service value= snap.getValue(Service.class);
                if (value.getServiceProviderId()==currentAdminId) {
                    if ((value.getServiceState().equals("refused"))||(value.getServiceState().equals("refused_deleted_bySR"))) {
                        listofrefusedservices.add(value);
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            final AdminServiceListAdapter refusedServiceAdapter=new AdminServiceListAdapter(getApplicationContext(),R.layout.list_services,listofrefusedservices);
            refusedServiceList.setAdapter(refusedServiceAdapter);

            if (refusedServiceAdapter.isEmpty())
                relativeLayout.setVisibility(View.VISIBLE);
            else
                refusedServiceList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refused);

        refusedServiceList= (ListView) findViewById(R.id.listrefusedservices);
        relativeLayout = (RelativeLayout) findViewById(R.id.laynone);
        progressBar=(ProgressBar) findViewById(R.id.prgs);
        relativeLayout.setVisibility(View.INVISIBLE);
        refusedServiceList.setVisibility(View.INVISIBLE);


        Intent intent=getIntent();
        currentAdminId=(Integer) intent.getIntExtra("adminId",0);

        servicesRef.addValueEventListener(mValueListner);

    }

    @Override
    protected void onStart() {
        super.onStart();

        refusedServiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.seedetail) {
                    Service selectedItem = (Service) parent.getItemAtPosition(position);
                    Intent gointent=new Intent(getApplicationContext(),ServiceDetailActivity.class);
                    gointent.putExtra("service",selectedItem);
                    gointent.putExtra("activity","refused");
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
