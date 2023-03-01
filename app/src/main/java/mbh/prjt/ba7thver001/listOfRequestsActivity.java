package mbh.prjt.ba7thver001;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class listOfRequestsActivity extends AppCompatActivity {

    ListView settingListView;
    ArrayList<String> setListItems=new ArrayList<String>();
    ArrayAdapter<String> setListAdapte;

    Integer currentAdminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests);

        setListItems.addAll(Arrays.asList(getResources().getStringArray(R.array.admin_orders)));

        settingListView=(ListView) findViewById(R.id.settinglistKKK);

        setListAdapte= new ArrayAdapter<String>(getApplicationContext(),R.layout.settingslistlooks,R.id.settingtitle,setListItems);
        settingListView.setAdapter(setListAdapte);

        Intent ourintent=getIntent();
        currentAdminId=(Integer) ourintent.getIntExtra("adminId",0);
        Log.d("admin",currentAdminId.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();


        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String optionClicked= setListItems.get(position);
                switch (position) {
                    case 0:
                        Intent piIntent = new Intent(getApplicationContext(), PndingOrdersActivity.class);
                        piIntent.putExtra("adminId", currentAdminId);
                        startActivity(piIntent);
                        break;
                    case 1:
                        Intent payIntent = new Intent(getApplicationContext(), OnWaitForPaimentOrderActivity.class);
                        payIntent.putExtra("adminId", currentAdminId);
                        startActivity(payIntent);
                        break;
                    case 2:
                        Intent locIntent = new Intent(getApplicationContext(), WorkinOnActivity.class);
                        locIntent.putExtra("adminId", currentAdminId);
                        startActivity(locIntent);
                        break;
                    case 3:
                        Intent liIntent = new Intent(getApplicationContext(), RefusedActivity.class);
                        liIntent.putExtra("adminId", currentAdminId);
                        startActivity(liIntent);
                        break;
                    case 4:
                        Intent fciIntent = new Intent(getApplicationContext(), FinishedActivity.class);
                        fciIntent.putExtra("adminId", currentAdminId);
                        startActivity(fciIntent);
                        break;
                    case 5:
                        Intent clgIntent = new Intent(getApplicationContext(), DeliveredActivity.class);
                        clgIntent.putExtra("adminId", currentAdminId);
                        startActivity(clgIntent);
                        break;
                }
            }
        });
    }
}
