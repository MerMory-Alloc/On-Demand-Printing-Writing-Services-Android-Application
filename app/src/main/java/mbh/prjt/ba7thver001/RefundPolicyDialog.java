package mbh.prjt.ba7thver001;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RefundPolicyDialog extends DialogFragment {

    private RefundDialogListner mListner;
    private TextInputEditText refundPercent,refundConditions;

    public RefundPolicyDialog() {
    }

    public static RefundPolicyDialog newInstance(String title) {
        RefundPolicyDialog frag=new RefundPolicyDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.refundpolicy, null);
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.confirm,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                refundPercent=(TextInputEditText) view.findViewById(R.id.refund_percent);
                refundConditions=(TextInputEditText) view.findViewById(R.id.refund_conditiond);

                if (!refundPercent.getText().toString().isEmpty()) {
                    Integer percentage=Integer.parseInt(refundPercent.getText().toString());
                    String guidlines=refundConditions.getText().toString();

                    mListner.onConfirm(percentage, guidlines);
                }
                else
                    Toast.makeText(getContext(), R.string.refundempty,Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner = (RefundDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface RefundDialogListner {
        void onConfirm(Integer percent,String guid);
    }
}
