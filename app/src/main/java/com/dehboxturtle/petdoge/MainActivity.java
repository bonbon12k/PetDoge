package com.dehboxturtle.petdoge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    protected boolean state = false;
    FrameLayout mdogbox;
    ImageView mdog;
    RelativeLayout mdogpen;
    Random rng;
    GifListener listener;
    private double magnitude = 200;
    float x;
    float y;
    float dipwidth;
    float dipheight;
    float dipdogwidth;
    float dipdogheight;
    float startx;
    float starty;
    boolean first = true;
    float padding = 20;
    float paddingx = 8;
    float paddingbot = 65;
    float scale;
    long lastpress= 0;
    AnimatorSet current;
    MediaPlayer mp;

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
        x = 0;
        y = 0;
        mdogpen = (RelativeLayout) findViewById(R.id.dogpen);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.dogsong);
        mp.setLooping(true);
    }

    public void reset(View view) {
        x = 0;
        y = 0;
        state = false;
        current.end();
        mdogbox.setY(starty * scale);
        mdogbox.setX(startx * scale);
        mp.seekTo(0);
        mp.pause();
    }

    public void woof(View view) {
        view.setClickable(false);
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        if (first) {
            startx = mdogbox.getX() / scale;
            starty = mdogbox.getY() / scale;
            first = false;
        }
        state = !state;
        dipdogheight = mdogbox.getHeight()/scale;
        dipdogwidth = mdogbox.getWidth()/scale;
        dipwidth = mdogpen.getWidth()/scale;
        dipheight = mdogpen.getHeight()/scale;
        if (state) {
            mp.start();
            mdog.setImageResource(R.drawable.walkingdogright);
            mdog.setPadding(0, 0, 0, 0);
            AnimationDrawable animation = (AnimationDrawable) mdog.getDrawable();
            animation.start();
            listener = new GifListener();
            nextAnimation();
        }
        else {
        }
    }

    public void nextAnimation() {
        double theta = rng.nextDouble() * 2 * Math.PI;
        float dx = (float) (Math.cos(theta) * magnitude);
        float dy = (float) (Math.sin(theta) * magnitude);
        if (theta < Math.PI/2 || theta > 3 * Math.PI/2) {
            mdogbox.setScaleX(-1);
        }
        else {
            mdogbox.setScaleX(1);
        }
        dx = boundX(dx);
        dy = boundY(dy);
        ObjectAnimator anim = ObjectAnimator.ofFloat(mdogbox, "translationX", x, x + dx);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mdogbox, "translationY", y, y + dy);
        anim.setDuration((long) (500 + rng.nextDouble() * 650));
        anim2.setDuration((long) (500 + rng.nextDouble() * 650));
        AnimatorSet movement = new AnimatorSet();
        movement.playTogether(new ObjectAnimator[]{anim, anim2});
        movement.addListener(listener);
        movement.start();
        current = movement;
        x += dx;
        y += dy;
    }

    public float boundX(float dx) {
        float val = dx;
        if ((x + dx)/scale + dipdogwidth + startx + paddingx > dipwidth) {
            val = (dipwidth - x/scale - dipdogwidth - startx - paddingx) * scale;
        }
        else if ((x + dx)/scale  + startx - paddingx < 0) {
            val =  ((-x)/scale - startx + paddingx) * scale;
        }
        return val;
    }

    public float boundY(float dy) {
        float val = dy;
        if ((y + dy)/scale + dipdogheight + starty + paddingbot > dipheight) {
            val = (dipheight - y/scale - dipdogheight - starty - paddingbot) * scale;

        }
        else if ((y + dy)/scale  + starty - padding < 0) {
            val = ((-y)/scale - starty + padding) * scale;

        }
        return val;
    }


    public class GifListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            View temp = findViewById(R.id.button);
            temp.setClickable(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            if (state) {
                nextAnimation();
                animation.removeAllListeners();
            }
            else {
                mdog.setImageResource(R.drawable.sleepingdogleft);
                mdog.setPadding(0, 20, 0, 0);
                AnimationDrawable ani = (AnimationDrawable) mdog.getDrawable();
                ani.start();
                mp.seekTo(0);
                mp.pause();
            }
            View temp = findViewById(R.id.button);
            temp.setClickable(true);
        }
    }
}
