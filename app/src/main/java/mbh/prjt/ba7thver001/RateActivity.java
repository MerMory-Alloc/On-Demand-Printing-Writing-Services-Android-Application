package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RateActivity extends AppCompatActivity {

    RatingBar rate;
    Button confirm;
    EditText review;

    Float ratingValue=0f,totalRate;
    Integer providerId,servicesDone;
    String key;

    Rating rating=new Rating(0,0,0,0.5f,"");

    FirebaseDatabase database;
    DatabaseReference adminRef;
    DatabaseReference rateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rate=(RatingBar) findViewById(R.id.rating);
        confirm=(Button)findViewById(R.id.confirm);
        review=(EditText) findViewById(R.id.review);

        Intent intent=getIntent();
        rating.setServiceId(intent.getIntExtra("serviceId",0));
        rating.setRequesterId(intent.getIntExtra("requesterId",0));
        providerId=intent.getIntExtra("providerId",0);
        rating.setProviderId(providerId);


        database=FirebaseDatabase.getInstance();
        adminRef =database.getReference("Users/Admins");
        rateRef= database.getReference("Ratings").push();
    }

    @Override
    protected void onStart(){
        super.onStart();

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue=rate.getRating();
                Log.d("Rate",ratingValue.toString());
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"please write a review before you proceed!",Toast.LENGTH_SHORT).show();
                else if (ratingValue==0){
                    Toast.makeText(getApplicationContext(),"please rate the service before you proceed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap map=new HashMap();
                            String rev=review.getText().toString();
                            Log.d("Review",rev);
                            for (DataSnapshot snap:snapshot.getChildren()){
                                if (snap.child("srvcProviderId").getValue(Integer.class)==providerId){
                                    key=snap.getKey();
                                    servicesDone=snap.child("servicesDone").getValue(Integer.class);
                                    if (servicesDone==0){
                                        servicesDone=servicesDone+1;
                                        Log.d("servicesdon",servicesDone.toString());
                                        totalRate=ratingValue;
                                        Log.d("ratingtotal",totalRate.toString());
                                        break;
                                    }
                                    else {
                                        servicesDone=servicesDone+1;
                                        Log.d("servicesdon",servicesDone.toString());
                                        totalRate=(snap.child("srvcProviderRating").getValue(Float.class)+ratingValue)/servicesDone;
                                        Log.d("ratingtotal",totalRate.toString());
                                        break;
                                    }
                                }
                            }
                            map.put("srvcProviderRating",totalRate);
                            map.put("servicesDone",servicesDone);
                            rating.setRate(ratingValue);
                            rating.setReview(rev);
                            Log.d("complet","complete1");
                            rateRef.setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("complet","complete2");
                                    if (task.isSuccessful()){
                                       adminRef.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                           @Override
                                           public void onComplete(@NonNull Task task) {
                                               if (task.isSuccessful()){
                                                   Log.d("complet","complete3");
                                                   Intent intent=new Intent();
                                                   setResult(RESULT_OK,intent);
                                                   finish();
                                               }
                                               else
                                                   Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                                           }
                                       });

                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}
