package mbh.prjt.ba7thver001;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;
import java.util.ArrayList;

public class ImagesAdapter extends ArrayAdapter<StorageReference> {
    Uri imageUri;

    private static class ViewHolder{
        CardView cardView;
        ImageView imageElement;
    }

    public ImagesAdapter(Context context, int id, ArrayList<StorageReference> obj) {
        super(context,id,obj);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        StorageReference image=getItem(position);


        if(convertView==null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.imagesproveslokslike, parent, false);

            viewHolder.imageElement = (ImageView) convertView.findViewById(R.id.imageprove);
            viewHolder.cardView=(CardView) convertView.findViewById(R.id.card);
            image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).placeholder(R.drawable.iconsnoimage).into(viewHolder.imageElement);
                    imageUri=uri;
                }
            });

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    /*public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }*/
}
