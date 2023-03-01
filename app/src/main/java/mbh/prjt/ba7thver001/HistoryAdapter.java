package mbh.prjt.ba7thver001;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<Card> {

    private static class ViewHolder{
        TextView cardelement;
    }

    public HistoryAdapter(Context context, int id, ArrayList<Card> obj) {
        super(context,id,obj);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        Card card=getItem(position);

        if(convertView==null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settingslistlooks, parent, false);

            viewHolder.cardelement = (TextView) convertView.findViewById(R.id.settingtitle);
            //

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }

        viewHolder.cardelement.setText(card.getCardNumber()+"/"+card.getCvvCode());

        return convertView;
    }

}
