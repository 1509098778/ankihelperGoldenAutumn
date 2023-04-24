package com.mmjang.ankihelper.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.util.ColorThemeUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Settings.getInstance(this).getPinkThemeQ()){
//            setTheme(R.style.AppThemePink);
//        }else{
//            setTheme(R.style.AppTheme);
//        }
        ColorThemeUtils.setColorTheme(AboutActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageButton alipay10 = (ImageButton) findViewById(R.id.alipay10);
        ImageButton alipay20 = (ImageButton) findViewById(R.id.alipay20);
        ImageButton alipayany = (ImageButton) findViewById(R.id.alipayany);
        TextView supportMessage = (TextView) findViewById(R.id.support_message);
        TextView alipayanyMidsummer = (TextView) findViewById(R.id.alipayany_midsummer);
        supportMessage.setMovementMethod(LinkMovementMethod.getInstance());

        setAlipayListener(alipay10, "HTTPS://QR.ALIPAY.COM/FKX00121JPEUHZAMZ3LCB7");
        setAlipayListener(alipay20, "HTTPS://QR.ALIPAY.COM/FKX07815DLYKHMJO06FT9D");
        setAlipayListener(alipayany, "HTTPS://QR.ALIPAY.COM/FKX011406PTCIHXZJPW7A1");
        setAlipayListener(alipayanyMidsummer, "https://qr.alipay.com/fkx15531pvahm9jqrluxf97");

    }

    private void setAlipayListener(View view, final String url){
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String payUrl = url;
                        //String payUrl = "HTTPS://QR.ALIPAY.COM/A6X00376AFOZWZUHWTDNDF4"; //any
                        intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            intent.setData(Uri.parse(payUrl.toLowerCase()));
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
