package net.alicmp.android.cardzilla;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by ali on 7/23/16.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(net.alicmp.android.cardzilla.R.id.lv_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","aliicmp@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send Email..."));
            }
        });

        findViewById(net.alicmp.android.cardzilla.R.id.lv_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.alicmp.net");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);            }
        });
    }
}
