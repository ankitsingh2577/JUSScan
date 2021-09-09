package dilip.ankit.chandan.sagar.jusscan;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permission_All;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.Permissions;
import static dilip.ankit.chandan.sagar.jusscan.Main_Screen.hasPermissions;


public class SpeechText extends AppCompatActivity implements View.OnClickListener {
    Button pdfbutton, textbutton, docbutton;
    ImageView speek_btn;
    private TextView voice_in;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timesys;
    private int STORAGE_PERMISSION_CODE = 23;
    private String inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_text);
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        voice_in = findViewById(R.id.results);
        speek_btn = findViewById(R.id.imageView);
        pdfbutton = findViewById(R.id.PDF_button);
        textbutton = findViewById(R.id.text_button);
        docbutton = findViewById(R.id.DOC_button);
        speek_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bolotaraaraaraa();
            }
        });

        textbutton.setOnClickListener(this);
        pdfbutton.setOnClickListener(this);
        docbutton.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voice_in.setText(result.get(0));
                    String text = result.get(0);

                }
                break;
            }
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
        Toast.makeText(SpeechText.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    private void writeData(File myFile, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(SpeechText.this, "" + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

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
        voice_in.setText("");
    }
    //Create PDF CODE

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
            document.add(new Paragraph(voice_in.getText().toString()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
        Toast.makeText(SpeechText.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();

        voice_in.setText("");
    }

    private void bolotaraaraaraa() {
        Intent it = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        it.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now !!");
        try {
            startActivityForResult(it, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PDF_button:
                inp = voice_in.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    createPdf();
                } else {
                    Toast.makeText(SpeechText.this, "No Text Found Speak Again!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_button:
                inp = voice_in.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    try {
                        writedatainfile(voice_in.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SpeechText.this, "No Text Found Speak Again!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.DOC_button:
                inp = voice_in.getText().toString();
                if (!TextUtils.isEmpty(inp)) {
                    try {
                        CreateDOC(voice_in.getText().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SpeechText.this, "No Text Found Speak Again !!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void CreateDOC(String d) throws FileNotFoundException, DocumentException {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        timesys = simpleDateFormat.format(calendar.getTimeInMillis());
        timesys = "Saved_DOC" + timesys + ".doc";
        File textFolder = new File("/storage/emulated/0/JUSScan");
        if (!textFolder.exists()) {
            textFolder.mkdir();
        }
        File myfile = new File(textFolder, timesys);
        writeDOC(myfile, d);
        Toast.makeText(SpeechText.this, "File has been save to /storage/emulated/0/JUSScan", Toast.LENGTH_SHORT).show();
    }

    private void writeDOC(File myfile, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myfile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(SpeechText.this, "" + myfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

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
        voice_in.setText("");
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
                startActivity(new Intent(SpeechText.this, Help_speech.class));
                return true;
            case R.id.about:
                startActivity(new Intent(SpeechText.this, About.class));
                return true;
            case R.id.clear:
                voice_in.setText("");
                Toast.makeText(this, "Result has been clear", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}