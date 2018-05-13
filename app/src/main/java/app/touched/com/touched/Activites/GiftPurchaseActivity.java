package app.touched.com.touched.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import app.touched.com.touched.R;

public class GiftPurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_purchase);
        Button purchase=(Button)findViewById(R.id.btn_purchase);

    }
}
