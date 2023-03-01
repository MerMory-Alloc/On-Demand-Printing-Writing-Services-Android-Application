package mbh.prjt.ba7thver001;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {

    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    ListView settingListView;
    ArrayList<String> setListItems=new ArrayList<String>();
    ArrayAdapter<String> setListAdapte;

    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        settingListView=(ListView) findViewById(R.id.languagelist);

        Resources res = getResources();
        final String[] settings = res.getStringArray(R.array.settings_language);

        setListItems.addAll(Arrays.asList(settings));
        //loadLocale();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setListAdapte= new ArrayAdapter<String>(getApplicationContext(),R.layout.settingslistlooks,R.id.settingtitle,setListItems);
        settingListView.setAdapter(setListAdapte);

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        lang = "en";
                        Log.d("Fuck",lang);
                        changeLocale(lang);
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                        break;
                    case 1:
                        lang= "ar";
                        changeLocale(lang);
                        Intent intent2 = new Intent(getApplicationContext(), Main2Activity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setSystemLocale(Configuration config, Locale locale){
        config.setLocale(locale);
    }

    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;

        Log.d("Fuck2",lang);
        Locale locale = new Locale(lang);
        Log.d("Fuck2",locale.getCountry());
        //saveLocale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale);
        }else{
            setSystemLocaleLegacy(config, locale);
        }

        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, ChangeLanguageActivity.class);
        finish();
        startActivity(refresh);
    }
}
