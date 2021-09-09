package dilip.ankit.chandan.sagar.jusscan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permission_All;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permissions;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.hasPermissions;

public class QR_Scan extends AppCompatActivity implements View.OnClickListener {

    Button pdfbutton, textbutton, docbutton;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timesys;
    private int STORAGE_PERMISSION_CODE = 23;
    private String inp;


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scan);
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        pdfbutton = findViewById(R.id.pdf_button);
        textbutton = findViewById(R.id.text_button);
        docbutton = findViewById(R.id.Doc_button);

        textbutton.setOnClickListener(this);
        pdfbutton.setOnClickListener(this);
        docbutton.setOnClickListener(this);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QR_Scan.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QR_Scan.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                // btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                // btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pdf_button:
                inp = txtBarcodeValue.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    createPdf();
                } else {
                    Toast.makeText(QR_Scan.this, "Please Scan The Barcode And Try Again !!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_button:
                inp = txtBarcodeValue.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    try {
                        writedatainfile(txtBarcodeValue.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(QR_Scan.this, "Please Scan The Barcode And Try Again !!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Doc_button:
                inp = txtBarcodeValue.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    CreateDOC(txtBarcodeValue.getText().toString());
                } else {
                    Toast.makeText(QR_Scan.this, "Please Scan The Barcode And Try Again !!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void writedatainfile(String s) throws FileNotFoundException, DocumentException {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTimeInMillis());
        timesys = "Saved_Text" + timesys + ".txt";
        File textFolder = new File("/storage/emulated/0/JUSScan");
        if (!textFolder.exists()) {
            textFolder.mkdir();
        }
        File myfile = new File(textFolder, timesys);
        writeData(myfile, s);
        Toast.makeText(QR_Scan.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    private void writeData(File myFile, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(QR_Scan.this, "" + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException p) {
                    p.printStackTrace();
                }
            }
        }
        txtBarcodeValue.setText("");
    }


    private void createPdf() {
        File folder = new File("/storage/emulated/0/JUSScan");
        if (!folder.exists()) {
            folder.mkdir();
        }

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTimeInMillis());
        timesys = "Saved_PDF" + timesys + ".pdf";

        File myfile = new File(folder, timesys);

        OutputStream output = null;
        try {
            output = new FileOutputStream(myfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, output);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        try {
            document.add(new Paragraph(txtBarcodeValue.getText().toString()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
        Toast.makeText(QR_Scan.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();

        txtBarcodeValue.setText("");
    }

    private void CreateDOC(String s) {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTimeInMillis());
        timesys = "Saved_DOC" + timesys + ".doc";
        File textFolder = new File("/storage/emulated/0/JUSScan");
        if (!textFolder.exists()) {
            textFolder.mkdir();
        }
        File myfile = new File(textFolder, timesys);
        writeDOC(myfile, s);
        Toast.makeText(QR_Scan.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    private void writeDOC(File myfile, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myfile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(QR_Scan.this, "" + myfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException p) {
                    p.printStackTrace();
                }
            }
        }
        txtBarcodeValue.setText("");
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                startActivity(new Intent(QR_Scan.this, Help_QR.class));
                return true;
            case R.id.about:
                startActivity(new Intent(QR_Scan.this, About.class));
                return true;
            case R.id.clear:
                txtBarcodeValue.setText("");
                Toast.makeText(this, "Result has been clear", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
