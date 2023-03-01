package mbh.prjt.ba7thver001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ShowThePicture extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_the_picture);

        image=(ImageView) findViewById(R.id.imagebigger);

        Intent intent=getIntent();
        Uri uri=intent.getData();

        Picasso.get().load(uri).into(image);

    }
}
