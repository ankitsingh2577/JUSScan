package dilip.ankit.chandan.sagar.jusscan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permission_All;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permissions;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.hasPermissions;

public class ClickToPDF  extends AppCompatActivity implements View.OnClickListener {
    Button  btn_convert;
    TextView tappic;
    ImageView Camera_Click;
    boolean boolean_save;
    Bitmap bitmap;
    Intent invokeCam;
    final static int picbycamera = 12;
    Calendar calendar;

    SimpleDateFormat simpleDateFormat;
    String timesys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_to_pdf);

        btn_convert = findViewById(R.id.click_save_button);
        Camera_Click = findViewById(R.id.camera_click_pdf);
        tappic = findViewById(R.id.tappic);
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        listener();
    }

    private void listener() {
        btn_convert.setOnClickListener(this);
        tappic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_save_button:
                createPdf();
                break;
            case R.id.tappic:
                invokeCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(invokeCam, picbycamera);
                break;
        }
    }

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File folder = new File("/storage/emulated/0/JUSScan");
        if (!folder.exists()) {
            folder.mkdir();
        }

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTime());
        timesys = "Saved_PDF" + timesys + ".pdf";

        File myfile = new File(folder, timesys);

        try {
            document.writeTo(new FileOutputStream(myfile));
            boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
        Toast.makeText(this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            bitmap = (Bitmap) extra.get("data");
            Camera_Click.setImageBitmap(bitmap);
        }
    }
}
