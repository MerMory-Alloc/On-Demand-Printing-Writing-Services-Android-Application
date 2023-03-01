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

public class ClientWorkinOnActivity extends AppCompatActivity {

    ListView workingOnServiceList;
    ArrayList<Service> listofworkingonservices=new ArrayList<Service>();
    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    Integer currentClientId;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference servicesRef=myRef.child("Services");

    ValueEventListener mValueListner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            listofworkingonservices.clear();
            for (DataSnapshot snap:snapshot.getChildren()) {
                Service value= snap.getValue(Service.class);
                if (value.getServiceRequesterId()==currentClientId) {
                    if (value.getServiceState().equals("workingOn")) {
                        listofworkingonservices.add(value);
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            final ServiceListAdapter workingOnServiceAdapter=new ServiceListAdapter(getApplicationContext(),R.layout.list_element,listofworkingonservices);
            workingOnServiceList.setAdapter(workingOnServiceAdapter);

            if (workingOnServiceAdapter.isEmpty())
                relativeLayout.setVisibility(View.VISIBLE);
            else
                workingOnServiceList.setVisibility(View.VISIBLE);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_workin_on);

        workingOnServiceList= (ListView) findViewById(R.id.listworkingonservices);
        relativeLayout = (RelativeLayout) findViewById(R.id.laynone);
        progressBar=(ProgressBar) findViewById(R.id.prgs);
        relativeLayout.setVisibility(View.INVISIBLE);
        workingOnServiceList.setVisibility(View.INVISIBLE);


        Intent intent=getIntent();
        currentClientId=(Integer) intent.getIntExtra("clientId",0);

        servicesRef.addValueEventListener(mValueListner);
    }

    @Override
    protected void onStart() {
        super.onStart();

        workingOnServiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.seedetail) {
                    Service selectedItem = (Service) parent.getItemAtPosition(position);
                    Intent gointent=new Intent(getApplicationContext(),ClientServiceDetailActivity.class);
                    gointent.putExtra("service",selectedItem);
                    gointent.putExtra("activity","workingOn");
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
