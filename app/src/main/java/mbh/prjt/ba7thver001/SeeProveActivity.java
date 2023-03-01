package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SeeProveActivity extends AppCompatActivity {

    GridView gridView;
    ProgressBar progressBar;
    ImagesAdapter imagesAdapter;
    ArrayList<StorageReference> images=new ArrayList<StorageReference>();

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference srvcFilesRef;

    Integer serviceId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_prove);

        gridView=(GridView) findViewById(R.id.images);
        progressBar=(ProgressBar) findViewById(R.id.prgs);

        Intent intent=getIntent();
        serviceId=intent.getIntExtra("serviceId",0);
        userName=intent.getStringExtra("username");

        imagesAdapter=new ImagesAdapter(this,R.layout.imagesproveslokslike,images);
        gridView.setAdapter(imagesAdapter);


        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        srvcFilesRef= storageRef.child("ServicesFiles/"+serviceId.toString()+"/"+userName);


        srvcFilesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                                    images.add(item);
                                    imagesAdapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Toast.makeText(getApplicationContext(),R.string.couldntdownload,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

    }
    @Override
    protected void onStart() {
        super.onStart();

       /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StorageReference curImage=images.get(position);

                curImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent intent=new Intent(SeeProveActivity.this,ShowThePicture.class);
                        intent.putExtra("image",uri);
                        startActivity(intent);
                    }
                });

            }
        });*/
    }
}
