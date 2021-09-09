package dilip.ankit.chandan.sagar.jusscan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImagetoPDFOption extends AppCompatActivity {
    public CircleImageView p2p,c2p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_to_pdf);
        p2p=findViewById(R.id.pictopdf);
        c2p=findViewById(R.id.clicktopdf);
        p2p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(ImagetoPDFOption.this, ImageToPDF.class);
                startActivity(i1);
            }
        });
        c2p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(ImagetoPDFOption.this, ClickToPDF.class);
                startActivity(i1);
            }
        });
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
                startActivity(new Intent(ImagetoPDFOption.this, Help_ImagetoPDF.class));
                return true;
            case R.id.about:
                startActivity(new Intent(ImagetoPDFOption.this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
