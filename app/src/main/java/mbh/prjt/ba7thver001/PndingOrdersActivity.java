package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PndingOrdersActivity extends AppCompatActivity implements OrderDialog.DialogListener {

    ListView pendingServiceList;
    RelativeLayout relativeLayout;
    ArrayList<Service> listofpendingservices=new ArrayList<Service>();
    ProgressBar progressBar;
    MaterialToolbar toolbar;

    Integer currentAdminId;
    ControleServices controleServices=new ControleServices();
    AdminServiceListAdapter pendingServiceAdapter;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference servicesRef=myRef.child("Services");

    ValueEventListener mValueListner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            pendingServiceAdapter.clear();

            for (DataSnapshot snap:snapshot.getChildren()) {
                Service value= snap.getValue(Service.class);
                if (value.getServiceProviderId()==currentAdminId) {
                    if (value.getServiceState().equals("pending")) {
                        listofpendingservices.add(value);
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);

            if (pendingServiceAdapter.isEmpty())
                relativeLayout.setVisibility(View.VISIBLE);
            else {
                relativeLayout.setVisibility(View.INVISIBLE);
                pendingServiceList.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnding_orders);

        controleServices.setActivity(this);

        pendingServiceList= (ListView) findViewById(R.id.listpendingservices);
        relativeLayout = (RelativeLayout) findViewById(R.id.laynone);
        progressBar=(ProgressBar) findViewById(R.id.prgs);
        relativeLayout.setVisibility(View.INVISIBLE);
        pendingServiceList.setVisibility(View.INVISIBLE);
        toolbar=(MaterialToolbar) findViewById(R.id.topAppBar);

        Intent intent=getIntent();
        currentAdminId=(Integer) intent.getIntExtra("adminId",0);

        pendingServiceAdapter=new AdminServiceListAdapter(getApplicationContext(),R.layout.list_services,listofpendingservices);
        runOnUiThread(new Runnable() {
            public void run() {
                pendingServiceAdapter.notifyDataSetChanged();
            }
        });
        pendingServiceList.setAdapter(pendingServiceAdapter);


        servicesRef.addValueEventListener(mValueListner);
    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        break;
                    case R.id.sort_order_settings:
                        controleServices.showSortDialog(R.array.servicesorders);
                        break;
                }


                return false;
            }
        });

        pendingServiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.seedetail) {
                    Service selectedItem = (Service) parent.getItemAtPosition(position);
                    Intent gointent=new Intent(getApplicationContext(),ServiceDetailActivity.class);
                    gointent.putExtra("service",selectedItem);
                    gointent.putExtra("activity","pending");
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

    @Override
    public void onFinishDialog(int input) {
        controleServices.sort(listofpendingservices,input);
        pendingServiceAdapter.notifyDataSetChanged();
    }
}
