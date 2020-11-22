package com.baliuniq.paymentmidtrans;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback {
    Button clickPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickPay = findViewById(R.id.click_pay);
        initMidtranSdk();
        clickPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPay();
            }
        });
    }

    private void initMidtranSdk() {
            SdkUIFlowBuilder.init()
                    .setContext(this)
                    .setMerchantBaseUrl(BuildConfig.BASE_URL)
                    .setClientKey(BuildConfig.CLIENT_KEY)
                    .setTransactionFinishedCallback(this)
                    .enableLog(true)
                    .setColorTheme(new CustomColorTheme("#FC585C", "#F5B55F", "#EFF0F2"))
                    .buildSDK();
    }

    private void clickPay() {
        final int min = 20;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String iddata = String.valueOf(random);
        int price = 50000;
        int qty = 1;
        String product_name = "sepatu shoes";
        MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata,price,qty,product_name));
        MidtransSDK.getInstance().startPaymentUiFlow(this);
    }


    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}