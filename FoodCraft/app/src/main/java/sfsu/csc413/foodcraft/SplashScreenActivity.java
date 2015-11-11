package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

//Reference : http://www.coderefer.com/android-splash-screen-example-tutorial//
//Slash screen image reference: https://wallpaperscraft.com/download/spices_dried_nuts_crockery_92083/800x1280

/**
 * Splash Screen activity for creating the animations for the splash screen image.
 *
 * @author Robert Chung
 * @version 1.0 October 15, 2015.
 */
public class SplashScreenActivity extends Activity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Method that sets the state of the activity when it's first created.
     * @param savedInstanceState Information about the current state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        StartFirstAnimation();
    }

    /**
     * Method that creates the first animation which drags the splash screen from the bottom to the top.
     */
    private void StartFirstAnimation() {
        Animation scrollUp = AnimationUtils.loadAnimation(this, R.anim.translate);
        scrollUp.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(scrollUp);

        scrollUp.setAnimationListener(new Animation.AnimationListener() {

            /**
             * Method that does not do anything when the first animation starts.
             * @param animation The first animation which scrolls up the page.
             */
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /**
             * Method which starts the second animation after the first is completed.
             * @param animation The first animation which scrolls up the page.
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                StartSecondAnimation();
            }

            /**
             * Method that does not do anything to repeat the animation.
             * @param animation The first animation which scrolls up the page.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Method that creates the second animation which causes the animation to persist for two seconds..
     */
    private void StartSecondAnimation() {
        Animation stay = AnimationUtils.loadAnimation(this, R.anim.alpha);
        stay.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(stay);

        stay.setAnimationListener(new Animation.AnimationListener() {

            /**
             * Method that does not do anything when the second animation starts.
             * @param animation The second animation which causes image to persist.
             */
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /**
             *  Method that starts the main activity once the second animation is completed.
             * @param animation The second animation which causes image to persist.
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }

            /**
             * Method that does not do anything to repeat the animation.
             * @param animation The second animation which causes image to persist.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}