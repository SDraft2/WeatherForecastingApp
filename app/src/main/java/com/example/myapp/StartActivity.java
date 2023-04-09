package com.example.myapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StartActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_INTERNET = 1000;
    private final int PERMISSION_REQUEST_LOCATION = 1001;
    int internetPmsChk;
    int locationPmsChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        internetPmsChk = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        locationPmsChk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                   KillApp();
                }
            });
            alert.setMessage("인터넷이 연결되어 있지 않습니다");
            alert.show();
        }
        else
        {
            LoadActivity();
            System.out.println(internetPmsChk == PackageManager.PERMISSION_GRANTED);
            if(!(internetPmsChk == PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(this, "인터넷권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        PERMISSION_REQUEST_INTERNET);
            }
            if(!(locationPmsChk == PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(this, "위치를 조회하기위한 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case PERMISSION_REQUEST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "인터넷권한이 승인되었습니다", Toast.LENGTH_SHORT).show();
                    LoadActivity();
                }
                else
                {
                    Toast.makeText(this, "인터넷권한이 승인되지 않았습니다", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET},
                            PERMISSION_REQUEST_INTERNET);
                }
                break;

            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "위치권한이 승인되었습니다", Toast.LENGTH_SHORT).show();
                    LoadActivity();
                }
                else
                {
                    Toast.makeText(this, "위치를 조회하기위해 필요합니다", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                break;

            default:
                break;
        }
    }

    public void LoadActivity()
    {
        internetPmsChk = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        locationPmsChk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(internetPmsChk == PackageManager.PERMISSION_GRANTED && locationPmsChk == PackageManager.PERMISSION_GRANTED)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void KillApp()
    {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.runFinalization();
        System.exit(0);
    }

}
