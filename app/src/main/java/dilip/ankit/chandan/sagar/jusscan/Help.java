package dilip.ankit.chandan.sagar.jusscan;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Help extends AppCompatActivity {

    ImageView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        help = findViewById(R.id.Help);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
