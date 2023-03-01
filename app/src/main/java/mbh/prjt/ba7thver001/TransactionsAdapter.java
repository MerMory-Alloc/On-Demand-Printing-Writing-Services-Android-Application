package mbh.prjt.ba7thver001;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionsAdapter extends ArrayAdapter<TransL>{

    private static class ViewHolder{
        TextView amount;
    }

    public TransactionsAdapter(Context context, int resource, ArrayList<TransL> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TransL cutTrans=getItem(position);

        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.transactionlooks,parent,false);

            viewHolder.amount=(TextView) convertView.findViewById(R.id.amount);
            viewHolder.amount.setText(cutTrans.getAmount());

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }


        return convertView;
    }

}
