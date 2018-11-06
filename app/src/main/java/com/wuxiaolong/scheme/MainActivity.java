package com.wuxiaolong.scheme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "wxl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (null != intent && null != intent.getData()) {
            // uri 就相当于 web 页面中的链接
            Uri uri = intent.getData();
            Log.e(TAG, "uri=" + uri);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            String key1 = uri.getQueryParameter("key1");
            String key2 = uri.getQueryParameter("key2");
            Log.e(TAG, "scheme=" + scheme + ",host=" + host
                    + ",port=" + port + ",path=" + path
                    + ",query=" + uri.getQuery()
                    + ",key1=" + key1 + "，key2=" + key2);
        }

    }


}
