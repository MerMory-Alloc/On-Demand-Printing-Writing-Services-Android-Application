package mbh.prjt.ba7thver001;

import android.content.Context;
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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AdminServiceListAdapter extends ArrayAdapter<Service> {

    private static class ViewHolder{
        ImageView imageType;
        TextView requester;
        TextView title;
        TextView type;
        TextView pages;
        TextView startTime;
        TextView endTime;
        Button seeDetaillBtn;
    }

    public AdminServiceListAdapter(Context context, int viewid, ArrayList<Service> object){
        super(context,viewid,object);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service currentService=getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_services, parent, false);

            viewHolder.imageType=(ImageView) convertView.findViewById(R.id.imagetype);
            viewHolder.requester=(TextView) convertView.findViewById(R.id.service_requester);
            viewHolder.title=(TextView) convertView.findViewById(R.id.service_title);
            viewHolder.type=(TextView) convertView.findViewById(R.id.service_type);
            viewHolder.pages=(TextView) convertView.findViewById(R.id.service_pages1);
            viewHolder.startTime=(TextView) convertView.findViewById(R.id.starttime);
            viewHolder.endTime=(TextView) convertView.findViewById(R.id.endtime);
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

        viewHolder.requester.setText(currentService.getServiceRequesterUsername());
        viewHolder.title.setText(currentService.getServiceSubject());
        viewHolder.type.setText(currentService.getServiceType());
        viewHolder.pages.setText(String.format(Locale.ENGLISH,"%d",currentService.getPageNumber()));
        viewHolder.startTime.setText(currentService.getStartServiceTime());
        viewHolder.endTime.setText(currentService.getEndServiceTime());



        return convertView;
    }
}
