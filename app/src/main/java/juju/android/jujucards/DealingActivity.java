package juju.android.jujucards;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tzimonjic on 5/11/17.
 */

public class DealingActivity extends Activity {


    private Random random;
    private ImageView cardView;
    private ObjectAnimator animation1;
    private ObjectAnimator animation2;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.ll_card);
        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View firstCard = mainInflater.inflate(R.layout.card, null);
        LinearLayout linearLayout = (LinearLayout) firstCard.findViewById(R.id.ll_card_layout);
        cardView = (ImageView) firstCard.findViewById(R.id.card);
        Picasso.with(DealingActivity.this).load(R.drawable.card_back).into(cardView);
        cardView.getLayoutParams().width = getHeight() / 10;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        linearLayout.setLayoutParams(params);
        random = new Random();

        layout.addView(firstCard);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createAnimation(cardView);
                //rotateAnimation(cardView);
                //TrailingAnimation(cardView);
                mixCards(v);
            }
        });
    }

    private void mixCards(final View v){
        ArrayList<Card> cards = new Card().initializeCards(DealingActivity.this);
        final int[] xPosition = {0};
        final int[] yPosition = {0};
        final int i = 10;
        final int intervalTime = 500; // 10 sec
        handler = new Handler();
        handler.postDelayed(new Runnable()  {
            @Override
            public void run() {
                if(xPosition[0]>=getWidth()){
                    xPosition[0] = xPosition[0]-i;
                }else{
                    xPosition[0] = xPosition[0] + i;
                }
                if(yPosition[0]>=getHeight()){
                    yPosition[0] = yPosition[0]-i;
                }else{
                    yPosition[0] = yPosition[0] + i;
                }
                translate(v, xPosition[0], yPosition[0]);
                runnable = this;
                handler.postDelayed(runnable, intervalTime);

            }
        }, intervalTime);
        super.onStart();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void translate(final View v, final int xPosition, final int yPosition){
        final TranslateAnimation anim = new TranslateAnimation( v.getX(), xPosition, v.getY(), yPosition);
        anim.setDuration(100);
        //anim.setRepeatCount(Animation.INFINITE);
        anim.setFillAfter( true );
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setX(xPosition);
                v.setY(yPosition);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(anim);
    }
    /*
    *   xPosition[0] = xPosition[0] + i[0];
                yPosition[0] = yPosition[0]+ i[0];
                final TranslateAnimation anim = new TranslateAnimation( v.getX(),v.getX()+ i[0],v.getY(), v.getY()+ i[0]);
                anim.setDuration(100);
                //anim.setRepeatCount(Animation.INFINITE);
                anim.setFillAfter( true );
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        i[0]++;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(anim);
    * */


    private void createAnimation(final View v, final int i) {
        final TranslateAnimation anim = new TranslateAnimation( v.getX(),v.getX()+i ,v.getY(), v.getY()+i );
        anim.setDuration(100);
        //anim.setRepeatCount(Animation.INFINITE);
        anim.setFillAfter( true );
        v.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setX(v.getX()+i);
                v.setY(v.getY()+i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void rotateAnimation(View v){
        ObjectAnimator rotation = ObjectAnimator.ofFloat(v, "rotation", 360).setDuration(2000);
        rotation.setRepeatCount(Animation.INFINITE);
        rotation.start();
    }
    private void moveViewToScreenCenter( View view )
    {
        LinearLayout root = (LinearLayout) findViewById( R.id.root_layout );
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );
        //int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);

        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        anim.setDuration(1000);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }


    public int getRandomX() {
        return random.nextInt(getWidth());
    }

    public int getRandomY(){
        return random.nextInt(getHeight());
    }
}
