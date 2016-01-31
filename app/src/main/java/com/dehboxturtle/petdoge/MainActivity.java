package com.dehboxturtle.petdoge;

import android.animation.Animator;
import android.app.ActionBar;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    protected boolean state = false;
    FrameLayout mdogbox;
    ImageView mdog;
    Animation current;
    Random rng;
    GifListener listener;
    private double magnitude = 100;
    float x;
    float y;

    public void woof(View view) {
        state = !state;
        if (state) {
            x = 0;
            y = 0;
            mdog.setImageResource(R.drawable.walkingdogright);
            mdog.setPadding(0, 0, 0, 0);
            AnimationDrawable animation = (AnimationDrawable) mdog.getDrawable();
            animation.start();
            Animation temp = nextAnimation();
            listener = new GifListener();
            mdogbox.setAnimation(temp);
            temp.setAnimationListener(listener);
            temp.start();
        }
        else {
        }
    }

    public Animation nextAnimation() {

        double theta = rng.nextDouble() * 2 * Math.PI;
        float dx = (float) (Math.cos(theta) * magnitude);
        float dy = (float) (Math.sin(theta) * magnitude);
        Animation ani = new TranslateAnimation(x, x + dx, y, y + dy);
        ani.setDuration((long) (750 + (rng.nextDouble() * 500)));
        x += dx;
        y += dy;
        return ani;
    }

/*    AnimationDrawable old = (AnimationDrawable) mdog.getDrawable();
    old.stop();
    mdog.setImageResource(R.drawable.walkingdogleft);
    mdog.setPadding(0,0,0,0);
    AnimationDrawable animation = (AnimationDrawable) mdog.getDrawable();
    animation.start();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdog = (ImageView) findViewById(R.id.theDog);
        mdog.setAdjustViewBounds(true);
        AnimationDrawable animation = (AnimationDrawable) mdog.getDrawable();
        animation.start();
        rng = new Random();
        mdogbox = (FrameLayout) findViewById(R.id.dogbox);
    }



    public class GifListener implements Animation.AnimationListener {

        public GifListener(){

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (state) {
                current = nextAnimation();
                mdogbox.setAnimation(current);
                current.setAnimationListener(listener);
                current.start();
            }
            else {
                mdogbox.setVisibility(View.INVISIBLE);
                mdogbox.setTranslationX(mdogbox.getTranslationX() + x);
                mdogbox.setTranslationY(mdogbox.getTranslationY() + y);
                mdog.setImageResource(R.drawable.sleepingdogleft);
                mdog.setPadding(0, 20, 0, 0);
                AnimationDrawable ani = (AnimationDrawable) mdog.getDrawable();
                ani.start();
                mdogbox.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
