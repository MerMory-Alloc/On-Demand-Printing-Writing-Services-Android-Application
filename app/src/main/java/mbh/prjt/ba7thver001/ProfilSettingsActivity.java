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

public class ProfilSettingsActivity extends AppCompatActivity {

    ListView settingListView;
    ArrayList<String> setListItems=new ArrayList<String>();
    ArrayAdapter<String> setListAdapte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_settings);

        setListItems.addAll(Arrays.asList(getResources().getStringArray(R.array.admin_settings)));

        settingListView=(ListView) findViewById(R.id.settinglistggg);

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
                switch (position){
                    case 0:
                        Intent piIntent=new Intent(getApplicationContext(),PersonalInfoActivity.class);
                        startActivity(piIntent);
                        break;
                    case 1:
                        Intent payIntent=new Intent(getApplicationContext(),PaimentsInfoActivity.class);
                        startActivity(payIntent);
                        break;
                    case 2:
                        Intent locIntent=new Intent(getApplicationContext(), ChangeLocationActivity.class);
                        startActivity(locIntent);
                        break;
                    case 3:
                        Intent liIntent=new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        startActivity(liIntent);
                        break;
                    case 4:
                        Intent fciIntent=new Intent(getApplicationContext(), ChangeLanguageActivity.class);
                        startActivity(fciIntent);
                        break;
                    case 5:
                        Intent clgIntent=new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(clgIntent);
                        break;
                }
            }
        });
    }
}
