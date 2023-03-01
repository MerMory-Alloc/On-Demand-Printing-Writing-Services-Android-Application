package mbh.prjt.ba7thver001;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ServiceListAdapter extends ArrayAdapter<Service> {

    private static class ViewHolder{
        ImageView imageType;
        TextView name;
        TextView title;
        TextView type;
        TextView time;
        Button seeDetaillBtn;
    }


    public ServiceListAdapter(Context context, int viewid, ArrayList<Service> object){
        super(context,viewid,object);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service currentService=getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);

            viewHolder.imageType=(ImageView) convertView.findViewById(R.id.imagetype);
            viewHolder.name=(TextView) convertView.findViewById(R.id.service_provider);
            viewHolder.title=(TextView) convertView.findViewById(R.id.service_title);
            viewHolder.type=(TextView) convertView.findViewById(R.id.service_type);
            viewHolder.time=(TextView) convertView.findViewById(R.id.time);
            viewHolder.seeDetaillBtn=(Button) convertView.findViewById(R.id.seedetail);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.seeDetaillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

      if (currentService.getServiceType().equals("Printing"))
           viewHolder.imageType.setBackgroundResource(R.drawable.ppp);
       else
           viewHolder.imageType.setBackgroundResource(R.drawable.www);

        viewHolder.name.setText(currentService.getServiceProviderUsername());
        viewHolder.title.setText(currentService.getServiceSubject());
        viewHolder.type.setText(currentService.getServiceType());
        viewHolder.time.setText(currentService.getStartServiceTime());
        return convertView;
    }
}
