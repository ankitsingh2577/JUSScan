package dilip.ankit.chandan.sagar.jusscan;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Help_ScanText extends AppCompatActivity {

    ImageView Help_Scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help__scan_text);

        Help_Scan=findViewById(R.id.help_scan);

    }
}