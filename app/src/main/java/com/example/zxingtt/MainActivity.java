package com.example.zxingtt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    private  static final  int REQUEST_CODE = 0;
    private  static  final  String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(this);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.btn);
        Button button = findViewById(R.id.customBtn);
        Intent intent = getIntent();

        String res = intent.getStringExtra("res");
        if(res!=null) {
            EditText editText = findViewById(R.id.editText);
            editText.setText(res);
            Log.d(TAG, "onCreate: "+ res);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CustomAcitivity.class);
                startActivityForResult(intent, REQUEST_CODE);

//                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if (Build.VERSION.SDK_INT >= 23) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        requestPermissions( new String[]{Manifest.permission.CAMERA}, 321);
                        // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 321);
                    } else {
                        Toast.makeText(MainActivity.this, "已授权", Toast.LENGTH_SHORT).show();
                        // 第二次进入直接打开相机
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent,REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "6.0以下", Toast.LENGTH_SHORT).show();
                    //打开相机
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);
                }
//                startActivityForResult(intent,REQUEST_CODE);
//                Intent intent = new Intent(MainActivity.this,CodeActivity.class);
//                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if(null != data) {
                Bundle bundle = data.getExtras();
                Log.d(TAG, "onActivityResult: "+ bundle);
                if (bundle == null) {
                    return;
                } else {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this,result,Toast.LENGTH_LONG).show();
                }


            }
        }
    }
}
