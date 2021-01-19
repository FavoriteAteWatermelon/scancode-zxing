package com.example.zxingtt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class CustomAcitivity extends AppCompatActivity {
    private  static  final  String TAG = "CustomAcitivity";
    private Button button ;
    private CaptureFragment captureFragment;
    private  static final  int REQUEST_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_acitivity);

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment,R.layout.my_camera );
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();

    }


    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
//        View view = getLayoutInflater().inflate(R.layout.my_camera,null);
//        button = view.findViewById(R.id.open);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: 12344");
//            }
//        });
//        return view;
//        return view;
        //        initView();

//    }

    public static boolean isOpen = false;

    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        LinearLayout linearLayout1 =  findViewById(R.id.open);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 23){
                    int i = ContextCompat.checkSelfPermission(CustomAcitivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 321);
                        // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 321);
                    } else {
                        Toast.makeText(CustomAcitivity.this, "已授权", Toast.LENGTH_SHORT).show();
                        // 第二次进入直接打开相机
//                        Intent intent = new Intent(CustomAcitivity.this, CaptureActivity.class);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_IMAGE);
                    }
                }

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CustomAcitivity.this.setResult(RESULT_OK, resultIntent);
            CustomAcitivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CustomAcitivity.this.setResult(RESULT_OK, resultIntent);
            CustomAcitivity.this.finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();

                CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        Log.d(TAG, "onAnalyzeSuccess: "+ result);

                        Toast.makeText(CustomAcitivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CustomAcitivity.this,MainActivity.class);

                        intent.putExtra("res",result);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(CustomAcitivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
    }}
}
