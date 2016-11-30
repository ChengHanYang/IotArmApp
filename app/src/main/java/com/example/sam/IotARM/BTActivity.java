package com.example.sam.IotARM;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import tw.com.flag.api.FlagBt;
import tw.com.flag.api.OnFlagMsgListener;

import static android.widget.Toast.makeText;


public class BTActivity extends AppCompatActivity implements OnFlagMsgListener {


    private WebView web2;
    FlagBt bt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

        bt=new FlagBt(this);

        web2 = (WebView) findViewById(R.id.web2);

        web2.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
            }
        });

//        Intent it =getIntent();
//        String input2=it.getStringExtra("editText");


        web2.getSettings().setBuiltInZoomControls(true);//顯示放大縮小
        web2.getSettings().setJavaScriptEnabled(true);
        web2.getSettings().setSupportZoom(true);//可以縮放
        web2.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//預設縮放模式
        web2.invokeZoomPicker();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.connect){
            if(!bt.connect())
                Toast.makeText(BTActivity.this,"找不到裝置!",Toast.LENGTH_LONG).show();


        }

        if(id==R.id.input){
            LayoutInflater inflater = LayoutInflater.from(BTActivity.this);
            final View v2 = inflater.inflate(R.layout.bt_input_layout, null);
            new AlertDialog.Builder(this)
                    .setTitle("請輸入 IP")
                    .setIcon(android.R.drawable.ic_menu_edit)
                    .setView(v2)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText2 = (EditText) (v2.findViewById(R.id.ed2));
                            Toast.makeText(getApplicationContext(),editText2.getText().toString(), Toast.LENGTH_SHORT).show();
                            web2.loadUrl("http://"+editText2.getText().toString()+":8080/?action=stream");
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        if (id == R.id.reorganize) {
            this.web2.reload();
            return true;
        }
        if (id==R.id.shutdown){
            this.finish();
            return true;
        }
        if(id==R.id.disconnect){
            bt.stop();
            return true;
        }

        if(id==R.id.wifi){
            Intent intent = new Intent();                          //換wifi頁面
            intent.setClass(BTActivity.this, MainActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);


    }


    @Override
    public void onFlagMsg(Message msg) {
        switch (msg.what){
            case FlagBt.CONNECTING:
                Toast.makeText(BTActivity.this,"正連線到:"+bt.getDeviceName(),Toast.LENGTH_LONG).show();
                break;
            case FlagBt.CONNECTED:
                Toast.makeText(BTActivity.this,"已連線到:"+bt.getDeviceName(),Toast.LENGTH_LONG).show();
                break;
            case FlagBt.CONNECT_FAIL:
                Toast.makeText(BTActivity.this,"連線失敗，請重新連線",Toast.LENGTH_LONG).show();
                break;
            case  FlagBt.CONNECT_LOST:
                Toast.makeText(BTActivity.this,"與"+bt.getDeviceName()+"中斷連線",Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void up(View v){
        bt.write(new byte[]{(byte)'u'});
    }
    public void down(View v){
        bt.write(new byte[]{(byte)'d'});
    }
    public void right(View v){
        bt.write(new byte[]{(byte)'r'});
    }
    public void left(View v){
        bt.write(new byte[]{(byte)'l'});
    }


}
