package thulasi.hemanthkumar.foody;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.ui.notifications.NotificationsFragment;

public class SuccessfulPaymentActivity extends AppCompatActivity {

    private TextView payid;
    private Button finishIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        payid = findViewById(R.id.payid);
        payid.setText("PAYMENT SUCCESSFUL \n Your Transaction ID : "+getIntent().getStringExtra("id"));
        CartHandler db = new CartHandler(this);

        finishIt = findViewById(R.id.got);
        finishIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsFragment.RefreshCart(NotificationsFragment.notificationsFragmentStatic);
                finish();
            }
        });
    }
}