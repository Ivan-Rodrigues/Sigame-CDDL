package sigame.com.br.sigame.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sigame.com.br.sigame.R;
import sigame.com.br.sigame.utils.PermissionUtils;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Solicita as permissÃµes
        String[] permissoes = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };

        boolean teste = PermissionUtils.validate(this, 0, permissoes);

        if (teste){
            startActivity(new Intent(this,MapsActivity.class));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {

                return;
            }
        }
        startActivity(new Intent(this,MapsActivity.class));
    }

}
