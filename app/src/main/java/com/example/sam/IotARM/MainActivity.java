package com.example.sam.IotARM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import static com.example.sam.IotARM.R.styleable.AlertDialog;


public class MainActivity extends AppCompatActivity {

    private WebView web;
    private ProgressBar pb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = (WebView) findViewById(R.id.web);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        web.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
            }
        });
        //web.loadUrl("http://192.168.137.24/iotARM/app.php");

        web.getSettings().setBuiltInZoomControls(true);//顯示放大縮小
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);//可以縮放
        web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//預設縮放模式
        web.invokeZoomPicker();

        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pb.setProgress(progress);
                pb.setVisibility(progress<100? View.VISIBLE:View.GONE);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main,menu);
//        menu.add(0,reorganize,0,"重新整理");
//        menu.add(0,shutdown,1,"結束");
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reorganize) {
            this.web.reload();
            return true;
        }
        if (id==R.id.shutdown){
            this.finish();
            return true;
        }
        if (id==R.id.bluetooth){
            Intent intent = new Intent();                          //換BT頁面
            intent.setClass(MainActivity.this, BTActivity.class);
            startActivity(intent);
        }
        if(id==R.id.input){
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View v = inflater.inflate(R.layout.input_layout, null);
            new AlertDialog.Builder(this)
                    .setTitle("請輸入 IP")
                    .setIcon(android.R.drawable.ic_menu_edit)
                    .setView(v)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText) (v.findViewById(R.id.ed));
                            Toast.makeText(getApplicationContext(),editText.getText().toString(), Toast.LENGTH_SHORT).show();
                            web.loadUrl("http://"+editText.getText().toString());
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (web.canGoBack()) {
            web.goBack();
            return;
        }
        super.onBackPressed();
    }




}
