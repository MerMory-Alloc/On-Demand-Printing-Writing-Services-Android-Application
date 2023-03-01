package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class AdminListActivity extends AppCompatActivity implements OrderDialog.DialogListener , SearchDialog.SearchDialogListener, FilterDialog.FilterDialogListener {

    int activity;
    ListView admListView;
    Toolbar toolbar;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference adminsRef=myRef.child("Users").child("Admins");

    String country,province;

    ArrayList<Admin> listOfAdmns=new ArrayList<Admin>();
    ArrayList<Admin> arrayList=new ArrayList<Admin>();
    AdminListAdapter adminAdapter;

    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Admin admin=snapshot.getValue(Admin.class);
            if (admin.getSrvcProviderProvince().toLowerCase().equals(province.toLowerCase())
                    && admin.getSrvcProviderCountry().toLowerCase().equals(country.toLowerCase())) {
                listOfAdmns.add(admin);
                arrayList.add(admin);
                adminAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        Intent activityIntent=getIntent();
        activity=activityIntent.getIntExtra("activity",0);
        country=activityIntent.getStringExtra("country");
        province=activityIntent.getStringExtra("province");

        admListView=(ListView) findViewById(R.id.admlist);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adminAdapter=new AdminListAdapter(this,R.layout.admlistlooks,listOfAdmns);
        admListView.setAdapter(adminAdapter);



        adminsRef.addChildEventListener(childEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();





        admListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long viewId = view.getId();

                if (viewId == R.id.see_profile) {
                    Admin selectedAdmin = (Admin) parent.getItemAtPosition(position);
                    int admId = selectedAdmin.getSrvcProviderId();
                    String admName= selectedAdmin.getSrvcProviderName();
                    if (activity==0) {
                        Intent intent = new Intent(getApplicationContext(), requestActivity.class);
                        intent.putExtra("idAdmin", admId);
                        intent.putExtra("usernameAdmin", admName);
                        adminsRef.removeEventListener(childEventListener);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("idAdmin", admId);
                        resultIntent.putExtra("usernameAdmin", admName);
                        adminsRef.removeEventListener(childEventListener);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.search:
                showEditDialog();
                return true;
            case R.id.sort_order_settings:
                showAlertDialog();
                return true;
            case R.id.filter:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filter(int input){
        switch (input){
            case 0:filterOnlyFreelancers();
                break;
            case 1:filterOnlyBusiness();
                break;
        }
    }

    public void filterOnlyFreelancers(){
        listOfAdmns.clear();
        for (Admin i :arrayList){
            if (i.getSrvcProviderType().equals("Freelance"))
                listOfAdmns.add(i);
        }
        adminAdapter.notifyDataSetChanged();
    }

    public void filterOnlyBusiness(){
        listOfAdmns.clear();
        for (Admin i :arrayList){
            if (i.getSrvcProviderType().equals("Business Owner"))
                listOfAdmns.add(i);
        }
        adminAdapter.notifyDataSetChanged();
    }

    public void search(String inputText){
        listOfAdmns.clear();
        for (Admin i:arrayList){
            if (KMPSearch(inputText.toLowerCase(),i.getSrvcProviderName().toLowerCase())) {
                listOfAdmns.add(i);
            }
        }
        adminAdapter.notifyDataSetChanged();
    }

    public void sort(int input) {

        switch (input) {
            case 0:
                sortByNumberOfServices(listOfAdmns);
                break;
            case 1:
                sortByRating(listOfAdmns);
                break;
            default:
                listOfAdmns=arrayList;
        }

        adminAdapter.notifyDataSetChanged();
    }

    public void sortByRating(@NonNull ArrayList<Admin> arrayListOriginal) {
        Admin admin=new Admin();
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < arrayListOriginal.size()-1; i++) {
                if (arrayListOriginal.get(i).getSrvcProviderRating() < arrayListOriginal.get(i+1).getSrvcProviderRating()) {
                    admin = arrayListOriginal.get(i);
                    arrayListOriginal.set(i,arrayListOriginal.get(i+1));
                    arrayListOriginal.set(i+1,admin);
                    sorted = false;
                }
            }
        }
    }

    public void sortByNumberOfServices(@NonNull ArrayList<Admin> arrayListOriginal) {
        Admin admin=new Admin();
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < arrayListOriginal.size()-1; i++) {
                if (arrayListOriginal.get(i).getServicesDone() < arrayListOriginal.get(i+1).getServicesDone()) {
                    admin = arrayListOriginal.get(i);
                    arrayListOriginal.set(i,arrayListOriginal.get(i+1));
                    arrayListOriginal.set(i+1,admin);
                    sorted = false;
                }
            }
        }
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        OrderDialog alertDialog = OrderDialog.newInstance("Order by",R.array.orders);
        alertDialog.show(fm, "fragment_alert");
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchDialog editNameDialogFragment = SearchDialog.newInstance("Search");
        editNameDialogFragment.show(fm, "searchdialog");
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = FilterDialog.newInstance("Filter");
        filterDialog.show(fm, "searchdialog");
    }

    @Override
    public void onFinishDialog(int input) {
        sort(input);
    }

    @Override
    public void onFoundDialog(String inputText) {
        search(inputText);
    }

    @Override
    public void onFilter(int input){
        filter(input);
    }

    boolean KMPSearch(String pat, String txt)
    {
        int M = pat.length();
        int N = txt.length();
        boolean found=false;

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);

        int i = 0; // index for txt[]
        while (i < N) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                found=true;
                j = lps[j - 1];
            }

            // mismatch after j matches
            else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway

                if (j != 0) {
                    found=true;
                    j = lps[j - 1];
                }
                else {
                    found=false;
                    i = i + 1;
                }
            }
        }
        return found;
    }

    void computeLPSArray(String pat, int M, int lps[])
    {
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            }
            else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];

                    // Also, note that we do not increment
                    // i here
                }
                else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }
}
