package mbh.prjt.ba7thver001;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminListAdapter extends ArrayAdapter<Admin> {

    private static class ViewHolder{
        ImageView profilimg;
        TextView name;
        TextView type;
        TextView location;
        TextView availablePapers;
        TextView nOrders;
        RatingBar ratingBar;
        Button seeProfileBtn;
    }

    public AdminListAdapter(Context context, int resource, ArrayList<Admin> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        Admin curentadmin=getItem(position);

        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.admlistlooks,parent,false);

            viewHolder.profilimg=(ImageView) convertView.findViewById(R.id.admprofileimg);
            viewHolder.name=(TextView) convertView.findViewById(R.id.admname);
            viewHolder.type=(TextView) convertView.findViewById(R.id.admtype);
            viewHolder.location=(TextView) convertView.findViewById(R.id.admlocation);
            viewHolder.availablePapers=(TextView) convertView.findViewById(R.id.available_papers);
            viewHolder.nOrders=(TextView) convertView.findViewById(R.id.number_of_orders);
            viewHolder.ratingBar=(RatingBar) convertView.findViewById(R.id.rating);
            viewHolder.seeProfileBtn=(Button) convertView.findViewById(R.id.see_profile);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        Uri imageUri=Uri.parse(curentadmin.getSrvcProviderProfilImage());

        Picasso.get().load(imageUri).placeholder(R.drawable.iconsnoimage).into(viewHolder.profilimg);
        viewHolder.name.setText(curentadmin.getSrvcProviderName());
        viewHolder.type.setText(curentadmin.getSrvcProviderType());
        viewHolder.location.setText(String.format("%s/%s/%s",curentadmin.getSrvcProviderCountry(),curentadmin.getSrvcProviderProvince(),curentadmin.getSrvcProviderCity()));
        viewHolder.ratingBar.setRating(curentadmin.getSrvcProviderRating());
        viewHolder.nOrders.setText("Services Done: "+curentadmin.getServicesDone().toString());
        viewHolder.seeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        ArrayList<String> arrayList=new ArrayList<String>();
        //arrayList=curentadmin.getAvailablePapersSizes();
        arrayList.add("A1");
        arrayList.add("A4");
        arrayList.add("A3");

        String papersStr="";

        for (String i:arrayList) {
            papersStr=papersStr+" "+i;
        }

        viewHolder.availablePapers.setText("Available Papers: "+papersStr);

        return convertView;
    }
}
