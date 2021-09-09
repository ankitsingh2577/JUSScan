package dilip.ankit.chandan.sagar.jusscan;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class Welcome_Screen extends AppCompatActivity {
    ImageView img;
    TextView Title;
    Thread timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);
        img=findViewById(R.id.logo);
        Title=findViewById(R.id.title);
        Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        img.startAnimation(aniRotateClk);
        timer=new Thread() {
            public void run() {
                try {
                    sleep(3000);
                }
                catch (Exception e)
                {

                }finally {
                    Intent on =new Intent(getApplicationContext(),Main_Screen.class);
                    startActivity(on);
                    finish();
                }
            }
        };
        timer.start();
        //Text Transistion Title
        ObjectAnimator moveDownAnimation = ObjectAnimator.ofFloat(Title, "y", 300.0f);
        moveDownAnimation.setDuration(3000);
        moveDownAnimation.setInterpolator(new DecelerateInterpolator());
        moveDownAnimation.addListener(new AnimatorListenerAdapter() {
        });
        moveDownAnimation.start();

    }
}
