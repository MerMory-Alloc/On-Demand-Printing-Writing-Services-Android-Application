package mbh.prjt.ba7thver001;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationsAdapter extends ArrayAdapter<Notification> {

    public NotificationsAdapter(@NonNull Context context, int resource, ArrayList<Notification> objects) {
        super(context, resource, objects);
    }

    private static class ViewHolder {
        TextView titleNot;
        TextView timeNot;
        TextView isNew;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        Notification notification=getItem(position);

        if (convertView==null) {
            viewHolder=new ViewHolder();

            convertView= LayoutInflater.from(getContext()).inflate(R.layout.notificationelm,parent,false);

            viewHolder.titleNot=(TextView) convertView.findViewById(R.id.titlenot);
            viewHolder.timeNot=(TextView) convertView.findViewById(R.id.timenot);
            viewHolder.isNew=(TextView) convertView.findViewById(R.id.newnew);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }

        GetTime getTime = new GetTime();
        String title;
        assert notification != null;
        title = notification.getFromId()+ notification.getNotificationSubject();

        viewHolder.titleNot.setText(title);
        viewHolder.timeNot.setText(getTime.convertFromTimeToString(notification.getNotificationcreatTime()));
        if (notification.getSeen())
            viewHolder.isNew.setVisibility(View.INVISIBLE);
        else
            viewHolder.isNew.setVisibility(View.VISIBLE);

        return convertView;
    }
}
