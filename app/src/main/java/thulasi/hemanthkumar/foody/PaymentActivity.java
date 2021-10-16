package thulasi.hemanthkumar.foody;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private TextView cashtxt;
    private TextView cardtxt;
    private Integer amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cashtxt = findViewById(R.id.ta1);
        cardtxt = findViewById(R.id.ta2);

        amount = getIntent().getIntExtra("amount",0);

        cashtxt.setText("₹ "+amount.toString());
        cardtxt.setText("₹ "+amount.toString());
    }
}