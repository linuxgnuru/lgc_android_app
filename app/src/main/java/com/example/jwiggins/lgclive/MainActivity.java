package com.example.jwiggins.lgclive;

import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public TextView tmpTitle;
    String testUrl = "https://api.twitch.tv/kraken/streams/linuxgamecast";
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity WebActivity = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        String url = "http://linuxgamecast.com/shatpocalypse/index.php";
//        String url = "http://twitch.tv/callofduty/embed";
        String url = "http://twitch.tv/linuxgamecast/embed";
//        String url = "http://www.twitch.tv/bluephobes/embed";
//        String url = "http://twitch.tv/linuxgamecast/hls";
        String irc = "https://kiwiirc.com/client/irc.freenode.net/#lgc-weekly";
        String closedUrl = "http://linuxgamecast.com/wp-content/uploads/2012/11/lgcweek.jpg";
//        String irc = "https://webchat.freenode.net?randomnick=1&amp;channels=lgc-weekly&prompt=1&uio=MTE9NzIaa";
        WebView mWebView, ircView;
        tmpTitle = (TextView) findViewById(R.id.title_text);
        boolean isOn = true;
        if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
            // test for live stream
            try {
                String testD = Get(testUrl);
                //Toast.makeText(getApplicationContext(), testS[0], Toast.LENGTH_LONG).show();
                if (testD.equals("null")) {
                    // channel is closed
                    url = closedUrl;
                    tmpTitle.setText("LGC - Offline");
                    isOn = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mWebView = (WebView) findViewById(R.id.webview1);
            ircView = (WebView) findViewById(R.id.webview2);
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    tmpTitle.setText(R.string.loading_txt);
                    WebActivity.setProgress(progress * 100);
                    if (progress == 100) {
                        tmpTitle.setText(R.string.title_txt_new);
                    }
                }
            });
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.setWebChromeClient(new WebChromeClient());
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            mWebView.loadUrl(url);
            if (isOn) {
                ircView.setWebViewClient(new WebViewClient());
                ircView.setWebChromeClient(new WebChromeClient());
                ircView.getSettings().setJavaScriptEnabled(true); // enable javascript
                ircView.loadUrl(irc);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_net_txt, Toast.LENGTH_SHORT).show();
            tmpTitle.setText(R.string.no_net_txt);
        }
    }
/*
OkHttpClient client = new OkHttpClient();

String run(String url) throws IOException {
  Request request = new Request.Builder()
      .url(url)
      .build();

  Response response = client.newCall(request).execute();
  return response.body().string();
}
 */
    String Get(String url) throws IOException {
        String myData;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        myData = response.body().string();
        String[] fu = myData.split(",");
        String[] bar = fu[0].split(":");
        return bar[1];
    }
}

