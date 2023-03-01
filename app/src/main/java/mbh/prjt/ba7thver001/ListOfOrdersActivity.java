package mbh.prjt.ba7thver001;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListOfOrdersActivity extends AppCompatActivity {

    ListView settingListView;
    ArrayList<String> setListItems=new ArrayList<String>();
    ArrayAdapter<String> setListAdapte;

    Integer currentClientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_orders);

        setListItems.addAll(Arrays.asList(getResources().getStringArray(R.array.client_orders)));

        settingListView=(ListView) findViewById(R.id.settinglist);

        setListAdapte= new ArrayAdapter<String>(getApplicationContext(),R.layout.settingslistlooks,R.id.settingtitle,setListItems);
        settingListView.setAdapter(setListAdapte);

        Intent ourintent=getIntent();
        currentClientId=(Integer) ourintent.getIntExtra("clientId",0);
    }

    @Override
    protected void onStart(){
        super.onStart();

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String optionClicked= setListItems.get(position);
                switch (position){
                    case 0:
                        Intent piIntent=new Intent(getApplicationContext(),ClientPndingOrdersActivity.class);
                        piIntent.putExtra("clientId",currentClientId);
                        startActivity(piIntent);
                        break;
                    case 1:
                        Intent payIntent=new Intent(getApplicationContext(),ClientOnWaitForPaimentOrderActivity.class);
                        payIntent.putExtra("clientId",currentClientId);
                        startActivity(payIntent);
                        break;
                    case 2:
                        Intent locIntent=new Intent(getApplicationContext(), ClientWorkinOnActivity.class);
                        locIntent.putExtra("clientId",currentClientId);
                        startActivity(locIntent);
                        break;
                    case 3:
                        Intent liIntent=new Intent(getApplicationContext(), ClientRefusedActivity.class);
                        liIntent.putExtra("clientId",currentClientId);
                        startActivity(liIntent);
                        break;
                    case 4:
                        Intent fciIntent=new Intent(getApplicationContext(), ClientFinishedActivity.class);
                        fciIntent.putExtra("clientId",currentClientId);
                        startActivity(fciIntent);
                        break;
                    case 5:
                        Intent dlvIntent=new Intent(getApplicationContext(), ClientDeliveredActivity.class);
                        dlvIntent.putExtra("clientId",currentClientId);
                        startActivity(dlvIntent);
                        break;
                    case 6:
                        Intent clgIntent=new Intent(getApplicationContext(), ClientCanceledActivity.class);
                        clgIntent.putExtra("clientId",currentClientId);
                        startActivity(clgIntent);
                        break;
                }
            }
        });
    }
}
