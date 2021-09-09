package dilip.ankit.chandan.sagar.jusscan;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends AppCompatActivity {

    ImageView aboutpic;
    TextView Quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutpic=findViewById(R.id.about_pic);
        Quote=findViewById(R.id.quote);

        //Text Transistion Title
        ObjectAnimator moveDownAnimation = ObjectAnimator.ofFloat(aboutpic, "y", 460.0f);
        moveDownAnimation.setDuration(3000);
        moveDownAnimation.setInterpolator(new DecelerateInterpolator());
        moveDownAnimation.addListener(new AnimatorListenerAdapter() {
        });
        moveDownAnimation.start();

        /*ObjectAnimator moveDownAnimation1 = ObjectAnimator.ofFloat(Quote, "y", -100.0f);
        moveDownAnimation1.setDuration(3000);
        moveDownAnimation1.setInterpolator(new DecelerateInterpolator());
        moveDownAnimation1.addListener(new AnimatorListenerAdapter() {
        });
        moveDownAnimation1.start();*/

    }
}
