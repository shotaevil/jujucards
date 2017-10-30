package juju.android.jujucards;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tzimonjic on 5/11/17.
 */

public class DealingActivity extends BaseActivity {


    private ImageView cardView;
    boolean isRunning = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setBackground();
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_card);
        int i = 0;
        final Random rand = new Random();
        final Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        while (i < 32) {
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View firstCard = mainInflater.inflate(R.layout.card, null);
            firstCard.setId(i);
            cardView = (ImageView) firstCard.findViewById(R.id.card);
            cardView.setAnimation(animBounce);
            Picasso.with(DealingActivity.this).load(R.drawable.card_back).into(cardView);
            if (getRotation(DealingActivity.this).contains("portrait")) {
                cardView.getLayoutParams().width = getWidth() / 4;
            } else {
                cardView.getLayoutParams().width = (int) (getHeight() / 6);
            }
            layout.addView(firstCard);
            i++;
        }


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                while (true) {
                    Log.e("TAG", "thread");
                    final View v = findViewById(rand.nextInt(32));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    params.setMargins(rand.nextInt(getHeight()), rand.nextInt(getWidth()), 0, 0);

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isRunning) {
                                v.requestLayout(); //has to be called in UI thread
                            }
                        }
                    });
                }
            }
        });

        thread.start();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                layout.setVisibility(View.GONE);
                final RelativeLayout secondLayout = (RelativeLayout) findViewById(R.id.rl_secondcard);

                LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View firstCard = mainInflater.inflate(R.layout.card, null);
                cardView = (ImageView) firstCard.findViewById(R.id.card);
                Picasso.with(DealingActivity.this).load(R.drawable.card_back).into(cardView);
                if (getRotation(DealingActivity.this).contains("portrait")) {
                    cardView.getLayoutParams().width = getWidth() / 4;
                } else {
                    cardView.getLayoutParams().width = (int) (getHeight() / 6);
                }
               secondLayout.addView(firstCard);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Card> cards = new Card().initializeCards(DealingActivity.this);
                        Card randomCard = cards.get(rand.nextInt(cards.size() - 1));
                        if (getRotation(DealingActivity.this).contains("portrait")) {
                            cardView.getLayoutParams().width = getWidth() / 4;
                        } else {
                            cardView.getLayoutParams().width = (int) (getHeight() / 6);
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardView.getLayoutParams();
                        Picasso.with(DealingActivity.this).load(randomCard.imageId).into(cardView);
                        Button continueBt = (Button) findViewById(R.id.bt_continue);
                        continueBt.setVisibility(View.VISIBLE);
                        continueBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DealingActivity.this.finish();
                                Intent i = new Intent(DealingActivity.this, GameActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                });

            }
        });

//        final Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.together);
//        animBounce.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (animation == animBounce) {
//                    Toast.makeText(getApplicationContext(), "Animation Stopped",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//       // cardView.startAnimation(animBounce);
    }

}
