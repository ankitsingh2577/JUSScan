package dilip.ankit.chandan.sagar.jusscan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Screen extends AppCompatActivity implements View.OnClickListener {
    CircleImageView Qr_scan, Image_to_pdf, Speech_to_text, Image_to_text;
    public static final String[] Permissions = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,};
    public static int Permission_All = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);
        Qr_scan = findViewById(R.id.qr_scan);
        Image_to_pdf = findViewById(R.id.image_PDF);
        Speech_to_text = findViewById(R.id.speechtotext);
        Image_to_text = findViewById(R.id.imagetotext);


        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        OperationJUSScan();
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void OperationJUSScan() {
        Qr_scan.setOnClickListener(this);
        Image_to_text.setOnClickListener(this);
        Image_to_pdf.setOnClickListener(this);
        Speech_to_text.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qr_scan:
                Intent i = new Intent(Main_Screen.this, QR_Scan.class);
                startActivity(i);
                break;
            case R.id.image_PDF:
                Intent i1 = new Intent(Main_Screen.this, ImagetoPDFOption.class);
                startActivity(i1);
                break;
            case R.id.speechtotext:
                Intent i2 = new Intent(Main_Screen.this, SpeechText.class);
                startActivity(i2);
                break;
            case R.id.imagetotext:
                Intent i4 = new Intent(Main_Screen.this, ScanText.class);
                startActivity(i4);
                break;

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                startActivity(new Intent(Main_Screen.this, Help.class));
                return true;
            case R.id.about:
                startActivity(new Intent(Main_Screen.this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}