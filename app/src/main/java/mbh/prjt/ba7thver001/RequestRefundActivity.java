package mbh.prjt.ba7thver001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class RequestRefundActivity extends AppCompatActivity {

    final Integer TAX_AMOUNT=6;

    EditText refundReason;
    TextView priceView,refundPercentView,refundConditionsView,refundAmountView,afterTexView;
    Button requestBtn;

    Double amount,refundAmount;
    Integer refundPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_refund);

        refundReason=(EditText) findViewById(R.id.refundreason);
        priceView=(TextView) findViewById(R.id.priceoffered);
        refundPercentView=(TextView) findViewById(R.id.refundoffered);
        refundConditionsView=(TextView) findViewById(R.id.refundcondition);
        refundAmountView=(TextView) findViewById(R.id.refundamount);
        afterTexView=(TextView) findViewById(R.id.priceaftertax);
        requestBtn=(Button) findViewById(R.id.refundrequest);

        Intent intent=getIntent();
        amount=intent.getDoubleExtra("amount",0.00);
        priceView.setText(String.format(Locale.ENGLISH,"%.2f DA",amount));

        refundPercent=intent.getIntExtra("refundPercent",0);
        refundPercentView.setText(String.format(Locale.ENGLISH,"%d%%",refundPercent));

        refundConditionsView.setText(intent.getStringExtra("refundConditions"));

        refundAmount=calculatedRefundAmount(amount,refundPercent);
        refundAmountView.setText(String.format(Locale.ENGLISH,"%.2f DA",refundAmount));

        afterTexView.setText(String.format(Locale.ENGLISH,"%.2f DA",(refundAmount-calculatedRefundAmount(refundAmount,TAX_AMOUNT))));
    }

    @Override
    protected void onStart(){
        super.onStart();

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refundReason.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),R.string.pleasesetreason,Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent();
                    intent.putExtra("refundReason",refundReason.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });
    }

    private Double calculatedRefundAmount(Double amount,Integer refundPercent){
        return  (amount*refundPercent)/100;
    }
}
