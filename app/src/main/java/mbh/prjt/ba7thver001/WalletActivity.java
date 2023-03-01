package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.VirtualLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.checkout.android_sdk.CheckoutAPIClient;
import com.checkout.android_sdk.Request.CardTokenisationRequest;
import com.checkout.android_sdk.Response.CardTokenisationFail;
import com.checkout.android_sdk.Response.CardTokenisationResponse;
import com.checkout.android_sdk.Utils.Environment;
import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class WalletActivity extends AppCompatActivity {

    TextView balance;
    MaterialCardView nextBtnToMoney,nextBtnToHistory;
    LineChart chart;

    final int GET_NEW_CARD = 1;

    Double money;

    private CheckoutAPIClient mCheckoutAPIClient;

    Transaction transaction=new Transaction();

    String transactionid;

    ArrayList<TransL> transactions=new ArrayList<TransL>();

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference transactionsRef;//oncreat only
    DatabaseReference transactionRef;
    DatabaseReference paymentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        balance=(TextView) findViewById(R.id.balance) ;
        nextBtnToHistory=(MaterialCardView) findViewById(R.id.nexttohistory) ;
        nextBtnToMoney=(MaterialCardView) findViewById(R.id.nexttomoney) ;
        chart = (LineChart) findViewById(R.id.chart);
        chart.setBackgroundColor(getColor(R.color.bootstrap_gray_lightest));
        chart.setNoDataText("No Transactions Happend");
        chart.setBorderColor(getColor(R.color.colorPrimary));

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Wallets/"+mAuth.getCurrentUser().getUid());
        transactionsRef=database.getReference("Transactions");
        paymentRef=database.getReference("Payments");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                balance.setText(String.format(Locale.ENGLISH,"DA %.2f",snapshot.child("balance").getValue(Double.class)));
                money=snapshot.child("balance").getValue(Double.class);
                transactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap:snapshot.getChildren()){
                            if (snap.child("userId").equals(mAuth.getCurrentUser().getUid())){
                                transactions.add(new TransL(""
                                        ,String.format(Locale.getDefault(),"-%.2f",snap.child("amount").getValue(Double.class))
                                ,new GetTime().convertFromTimeToString(snap.child("createdDate").getValue(Long.class))));
                            }
                        }

                        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for (DataSnapshot snapi:snapshot1.getChildren()){
                                    if (snapi.child("providerId").equals(mAuth.getCurrentUser().getUid())){
                                        if (snapi.child("paymentState").equals("done")) {
                                            transactions.add(new TransL(snapi.child("requesterId").getValue(String.class)
                                                    , String.format(Locale.getDefault(), "%.2f", snapi.child("amount").getValue(Double.class))
                                                    , new GetTime().convertFromTimeToString(snapi.child("finishedDay").getValue(Long.class))));
                                        }
                                        else if (snapi.child("paymentState").equals("done_refund")){
                                            transactions.add(new TransL(snapi.child("requesterId").getValue(String.class)
                                                    , String.format(Locale.getDefault(), "-%.2f", snapi.child("amount").getValue(Double.class))
                                                    , new GetTime().convertFromTimeToString(snapi.child("finishedDay").getValue(Long.class))));
                                        }
                                    }
                                }
                                sort(transactions);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        nextBtnToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(TransactionsHistoryFragment.newInstance(transactions));
            }
        });

        nextBtnToMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money<50.00){
                    Toast.makeText(getApplicationContext(),"You cant withdraw less than 50 DA",Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getApplicationContext());
                    alertDialog.setTitle(R.string.confirmation);
                    alertDialog.setMessage("You are now about to enter your credit card,if the card info are right the withdraw will be completed by 6 days");
                    alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(WalletActivity.this, CardEditActivity.class);
                            startActivityForResult(intent, GET_NEW_CARD);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void sort(ArrayList<TransL> arrayList) {
        TransL string = new TransL();
        boolean sorted = false;

        while(!sorted) {
            sorted = true;
            for (int i = 0; i < arrayList.size()-1; i++) {
                Calendar cal1=Calendar.getInstance();
                cal1.setTimeInMillis(new GetTime().convertFromStringToTimestamp(arrayList.get(i).getDate()).getTime());
                Calendar cal2=Calendar.getInstance();
                cal2.setTimeInMillis(new GetTime().convertFromStringToTimestamp(arrayList.get(i+1).getDate()).getTime());
                if (cal1.before(cal2)){
                    string = arrayList.get(i);
                    arrayList.set(i,arrayList.get(i+1));
                    arrayList.set(i+1,string);
                    sorted = false;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
        String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
        String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
        String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
        // Your processing goes here.

        String[] string=expiry.split("/");

        if (!CardValidator.validateCardNumber(cardNumber)) {
            Toast.makeText(getApplicationContext(),R.string.invalidcardnumber, Toast.LENGTH_LONG).show();

        }
        else if (!CardValidator.validateExpiryDate(string[0],string[1])) {
            Toast.makeText(getApplicationContext(),R.string.error_card_expired, Toast.LENGTH_LONG).show();

        }
        else {
            CheckoutAPIClient.OnTokenGenerated mTokenListener = new CheckoutAPIClient.OnTokenGenerated() {
                @Override
                public void onTokenGenerated(CardTokenisationResponse token) {
                    // your token

                    transaction.setCardToken(token.getToken());
                    new GetTime().getServerTimeWithCall(new GetTime.TimeCallback() {
                        @Override
                        public void timecallback(Long value, String key) {
                            transaction.setCreatedDate(value);

                            transactionRef.setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                                    HashMap map2 = new HashMap();
                                                    map2.put("balance", 0.00);
                                                    myRef.updateChildren(map2).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()){
                                                                HashMap map3 = new HashMap();
                                                                map3.put("walletBalance",0.00);
                                                                myRef.child("Users").child("Admins").child(mAuth.getCurrentUser().getUid()).updateChildren(map3).addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        if (task.isSuccessful()){
                                                                            database.getReference("timestamp").child(key).removeValue();
                                                                            finish();
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    });
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(CardTokenisationFail error) {
                    // your error
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNetworkError(VolleyError error) {
                    // your network error
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                    finish();
                }
            };

            transactionRef=myRef.child("Transactions").push();
            transactionid=transactionRef.getKey();
            transaction.setTransactionId(transactionid);
            transaction.setAmount(0.00-money);
            transaction.setStatus("authorized");
            transaction.setUserId(mAuth.getCurrentUser().getUid());

            mCheckoutAPIClient = new CheckoutAPIClient(
                    this,                // context
                    "pk_test_aec487a9-a39f-4df9-abcc-30bd68d96cec",          // your public key
                    Environment.SANDBOX  // the environment
            );
            mCheckoutAPIClient.setTokenListener(mTokenListener);

            mCheckoutAPIClient.generateToken(new CardTokenisationRequest(cardNumber, cardHolderName, string[0], string[1], cvv));
        }
    }
}
