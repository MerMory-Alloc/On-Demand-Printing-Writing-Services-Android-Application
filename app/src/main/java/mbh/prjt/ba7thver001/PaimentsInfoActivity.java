package mbh.prjt.ba7thver001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class PaimentsInfoActivity extends AppCompatActivity {

    ListView settingListView;
    ArrayList<String> setListItems=new ArrayList<String>();
    ArrayAdapter<String> setListAdapte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiments_info);

        settingListView=(ListView) findViewById(R.id.settinglist);

        setListItems.add("Payment History");
        setListItems.add("Payment Policy");

        setListAdapte= new ArrayAdapter<String>(getApplicationContext(),R.layout.settingslistlooks,R.id.settingtitle,setListItems);
        settingListView.setAdapter(setListAdapte);
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
                        Intent hiintent=new Intent(PaimentsInfoActivity.this,PaymentHistoryActivity.class);
                        startActivity(hiintent);
                        break;
                    case 1:
                        Intent intent=new Intent(PaimentsInfoActivity.this,PaymentPolicyActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}

