package dilip.ankit.chandan.sagar.jusscan;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
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



public class ScanText extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "Text API";
    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private Uri imageUri;
    private  Button button,Text,PDF,DOC;
    private String inp;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timesys;
    private int STORAGE_PERMISSION_CODE = 23;
    private TextRecognizer detector;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static String SAVED_INSTANCE_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_text);
        button = findViewById(R.id.scan_buttonn);
        Text = findViewById(R.id.text_button);
        PDF=findViewById(R.id.PDF_button);
        DOC=findViewById(R.id.DOC_button);
        scanResults = findViewById(R.id.jusscanres);
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ScanText.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                Toast.makeText(ScanText.this, "Click image to get text", Toast.LENGTH_SHORT).show();
                File textFolder= new File("/storage/emulated/0/JUSScan");
                if (!textFolder.exists()) {
                    textFolder.mkdir();
                }
            }
        });
        Text.setOnClickListener(this);
        PDF.setOnClickListener(this);
        DOC.setOnClickListener(this);

    }

    //Create PDF CODE
    private void createPdf() throws FileNotFoundException ,DocumentException{
        File folder = new File("/storage/emulated/0/JUSScan");
        if (!folder.exists()) {
            folder.mkdir();
        }

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTime());
        timesys = "Saved_PDF" + timesys + ".pdf";

        File myfile = new File(folder, timesys);

        OutputStream output = new FileOutputStream(myfile);

        Document document = new Document();

        PdfWriter.getInstance(document, output);

        document.open();

        document.add(new Paragraph(scanResults.getText().toString()));

        document.close();
        Toast.makeText(ScanText.this,"File has been save to /storage/emulated/0/JUSScan",Toast.LENGTH_SHORT).show();

        scanResults.setText("");
    }
    //

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(ScanText.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    String words = "";
                    for (int index = 0; index < textBlocks.size(); index++) {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            //extract scanned text lines here
                            lines = lines + line.getValue() + "\n";
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                words = words + element.getValue() + ", ";
                            }
                        }
                    }
                    if (textBlocks.size() == 0) {
                        scanResults.setText("Scan Failed: Found nothing to scan");
                    } else {
                        scanResults.setText(scanResults.getText() + "Lines: " + "\n");
                        scanResults.setText(scanResults.getText() + lines + "\n");
                        scanResults.setText(scanResults.getText() + "---------" + "\n");
                    }
                } else {
                    scanResults.setText("Could not set up the detector!");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory()+ "/JUSScan", "picture.jpg");
        imageUri = FileProvider.getUriForFile(ScanText.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, scanResults.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PDF_button:
                try {
                    inp=scanResults.getText().toString();
                    if (!TextUtils.isEmpty(inp))
                    {
                        createPdf();
                    }
                    else {
                        Toast.makeText(ScanText.this, "No Text Found Please Click On Scan Button!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.text_button:
                inp=scanResults.getText().toString();
                if (!TextUtils.isEmpty(inp))
                {
                    try {
                        writedatainfile(scanResults.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ScanText.this, "No Text Found Please Click On Scan Button!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.DOC_button:
                inp=scanResults.getText().toString();
                if (!TextUtils.isEmpty(inp))
                {
                    try {
                        CreateDOC(scanResults.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ScanText.this, "No Text Found Please Click On Scan Button!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void writedatainfile(String s) throws FileNotFoundException ,DocumentException{

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTime());
        timesys = "Saved_Text" + timesys + ".txt";
        ActivityCompat.requestPermissions(ScanText.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        File textFolder= new File("/storage/emulated/0/JUSScan");
        if (!textFolder.exists()) {
            textFolder.mkdir();
        }
        File myfile = new File(textFolder, timesys);
        writeData(myfile, s);
        Toast.makeText(ScanText.this,"File has been save to /storage/emulated/0/JUSScan",Toast.LENGTH_SHORT).show();
    }
    private void writeData(File myFile, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream =new FileOutputStream(myFile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(ScanText.this,""+myFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                }
                catch (IOException p){
                    p.printStackTrace();
                }
            }
        }
        scanResults.setText("");
    }

    private void CreateDOC( String s) throws FileNotFoundException ,DocumentException{

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTime());
        timesys = "Saved_DOC" + timesys + ".doc";
        ActivityCompat.requestPermissions(ScanText.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        File textFolder = new File("/storage/emulated/0/JUSScan");
        if (!textFolder.exists()) {
            textFolder.mkdir();
        }
        File myfile = new File(textFolder, timesys);
        writeDOC(myfile, s);
        Toast.makeText(this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    private void writeDOC(File myfile, String s) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream =new FileOutputStream(myfile);
            fileOutputStream.write(s.getBytes());
            Toast.makeText(ScanText.this,""+myfile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                }
                catch (IOException p){
                    p.printStackTrace();
                }
            }
        }
        scanResults.setText("");
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
                startActivity(new Intent(ScanText.this, Help_ScanText.class));
                return true;
            case R.id.about:
                startActivity(new Intent(ScanText.this, About.class));
                return true;
            case R.id.clear:
                scanResults.setText("");
                Toast.makeText(this, "Result has been clear", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
