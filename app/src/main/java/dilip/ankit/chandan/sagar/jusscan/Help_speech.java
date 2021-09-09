package dilip.ankit.chandan.sagar.jusscan;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Help_speech extends AppCompatActivity {

    ImageView speech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_speech);

        speech=findViewById(R.id.help_speech);

    }
}
