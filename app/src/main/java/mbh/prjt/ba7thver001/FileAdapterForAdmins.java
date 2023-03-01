package mbh.prjt.ba7thver001;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class FileAdapterForAdmins extends ArrayAdapter<Uri>  {

    private static class ViewHolder{
        TextView nameOfFile;
        ImageButton dltBtn;
    }


    public FileAdapterForAdmins(Context context, int id,ArrayList<Uri> list) {
        super(context, id,list);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        ViewHolder viewHolder;
        Uri uriname=getItem(position);

        if(convertView==null){
            viewHolder=new ViewHolder();

            convertView= LayoutInflater.from(getContext()).inflate(R.layout.fileslooksforadmin,parent,false);

            viewHolder.nameOfFile=(TextView) convertView.findViewById(R.id.file_name);
            viewHolder.dltBtn=(ImageButton) convertView.findViewById(R.id.dlt);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }

        File file=new File(uriname.getPath());
        viewHolder.nameOfFile.setText(file.getName());
        viewHolder.dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NonScrollListView) parent).performItemClick(v, position, 0);
            }
        });

        return convertView;
    }
}
