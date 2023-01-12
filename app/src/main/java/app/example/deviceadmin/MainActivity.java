package app.example.deviceadmin;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int DEVICE_ADMIN_ADD_RESULT_ENABLE = 1111;
    private TextView actionAdminActive;
    private boolean adminActive;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionAdminActive = findViewById(R.id.actionAdminActive);
        mContext = this;

        final TextView actionCamera = findViewById(R.id.actionCamera);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AppDeviceAdminReceiver.class);


        adminActive = devicePolicyManager.isAdminActive(componentName);

        if(adminActive) {
            actionAdminActive.setText("권한 해제");
            actionCamera.setVisibility(View.VISIBLE);
        }else{
            actionAdminActive.setText("권한 부여");
            actionCamera.setVisibility(View.INVISIBLE);
        }


        actionAdminActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminActive = devicePolicyManager.isAdminActive(componentName);
                if(adminActive) {
                    adminDisable();
                    actionAdminActive.setText("권한 부여");
                    actionCamera.setVisibility(View.INVISIBLE);
                }else{
                    adminActive();
                    actionAdminActive.setText("권한 해제");
                    actionCamera.setVisibility(View.VISIBLE);
                }
            }
        });


        if(adminActive){

        }else{
            actionCamera.setVisibility(View.INVISIBLE);

        }

        findViewById(R.id.un).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminActive = devicePolicyManager.isAdminActive(componentName);
                if(adminActive){
                    Toast.makeText(mContext, "디바이스 권한을 해제하고 삭제해주세요.", Toast.LENGTH_SHORT).show();
                }
                uninstall();
            }
        });

    }

    private void uninstall(){
        Uri packageURI = Uri.parse("package:" + "app.example.deviceadmin");
        startActivityForResult(new Intent(Intent.ACTION_DELETE, packageURI), 1);
    }

    private void adminActive() {
        ComponentName componentName = new ComponentName(this, AppDeviceAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        startActivityForResult(intent, DEVICE_ADMIN_ADD_RESULT_ENABLE);
    }

    private void adminDisable() {
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DEVICE_ADMIN_ADD_RESULT_ENABLE) {
            setCameraDisabled(true);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setCameraDisabled(boolean cameraDisabled) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(this, AppDeviceAdminReceiver.class);
        devicePolicyManager.setCameraDisabled(componentName, cameraDisabled);
    }
}