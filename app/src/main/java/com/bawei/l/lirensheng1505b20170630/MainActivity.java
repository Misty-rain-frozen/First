package com.bawei.l.lirensheng1505b20170630;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**
 * 类描述：设置网络
 * 创建人: 李仁晟
 * 创建时间 L on 2017/6/30.
 */

public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //寻找控件ID
        button = (Button) findViewById(R.id.bu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "网络可用", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,M2Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("当前网络不可用，是否前去设置")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                    builder.create().show();
                }
            }

        });
    }
    //判断是否有网络

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
