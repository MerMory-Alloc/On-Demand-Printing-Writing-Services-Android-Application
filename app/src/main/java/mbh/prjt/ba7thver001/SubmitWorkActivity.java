package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;

public class SubmitWorkActivity extends AppCompatActivity {

    final long MAX_SIZE=100;
    final int CONVERT_NUM =1048576;

    FileAdapter filesAdapter;
    ArrayList<Uri> uris =new ArrayList<Uri>();
    ArrayList<Image> images=new ArrayList<Image>();
    Long size=0L;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference srvcFilesRef;

    ProgressBar progressBar;
    Button addFileBtn,submitBtn;
    NonScrollListView filesList;

    Integer serviceId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_work);

        progressBar=(ProgressBar) findViewById(R.id.prgs);
        progressBar.setVisibility(View.INVISIBLE);
        addFileBtn=(Button) findViewById(R.id.addfile);
        submitBtn=(Button) findViewById(R.id.confirm);
        filesList=(NonScrollListView) findViewById(R.id.filesList);


        filesAdapter =new FileAdapter(getApplicationContext(),R.layout.gridfileloks,uris);
        filesList.setAdapter(filesAdapter);

        Intent intent=getIntent();
        serviceId=intent.getIntExtra("serviceId",0);
        userName=intent.getStringExtra("username");

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        srvcFilesRef= storageRef.child("ServicesFiles/"+serviceId.toString()+"/"+userName);
    }

    @Override
    protected void onStart(){
        super.onStart();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uris.isEmpty()) {
                    Toast.makeText(getApplicationContext(),R.string.pleaseaddfilesfirst,Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadFiles(uris);
                }
            }
        });

        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size<=MAX_SIZE) {
                    ImagePicker.with(SubmitWorkActivity.this)
                            .setMultipleMode(true)
                            .setShowNumberIndicator(true)
                            .setMaxSize(20)
                            .setLimitMessage("You only can select up to 20 images")
                            .setSelectedImages(images)
                            .setRequestCode(0)
                            .start();
                }
                else {
                    Toast.makeText(getApplicationContext(),R.string.cantaddfiles,Toast.LENGTH_SHORT).show();
                }
            }
        });

        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.dlt) {
                    Uri file=uris.get(position);
                    uris.remove(position);
                    filesAdapter.notifyDataSetChanged();
                    File f=new File(file.getPath());
                    size= size -(f.length() / CONVERT_NUM);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 0)) {
            ArrayList<Image> images = ImagePicker.getImages(data);
            // Do stuff with image's path or id. For example:
            for (Image image : images ) {
                Uri file = image.getUri();
                File f = new File(file.getPath());
                size =size + (f.length() / CONVERT_NUM);
                if (size<=MAX_SIZE) {
                    if(!uris.contains(file)) {
                        uris.add(file);
                        filesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.fileexiste,Toast.LENGTH_LONG).show();
                        size= size -(f.length() / CONVERT_NUM);
                        return;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),R.string.selectedmore,Toast.LENGTH_LONG).show();
                    size= size -(f.length() / CONVERT_NUM);
                    return;
                }
            }
        }

    }

    public void uploadFiles(ArrayList<Uri> files){
        final int[] i = {0};
        for (Uri file : files){
            StorageReference curentOrderRef= srvcFilesRef.child(file.getLastPathSegment());
            curentOrderRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),R.string.firstfileuploaded,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),R.string.failed,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
