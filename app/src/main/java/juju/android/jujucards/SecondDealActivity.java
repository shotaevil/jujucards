package juju.android.jujucards;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by tzimonjic on 5/30/17.
 */

public class SecondDealActivity extends Activity {

    private ArrayList<Card> cards;
    private ImageView stack;
    private GridLayout.Spec row1, row2, row3, col1a, col1c, col1b, col2a, col2b, col2c, col3a, col3b, col3c;
    private LinearLayout topLayout;
    private LinearLayout middleLayout;
    private LinearLayout bottomLayout;
    private int cardThrown = 0;
    private ArrayList<Integer> position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_second_deal_activity);

        cards = Variables.leftedCards;
        topLayout = (LinearLayout) findViewById(R.id.ll_top);
        middleLayout = (LinearLayout) findViewById(R.id.ll_middle);
        bottomLayout = (LinearLayout) findViewById(R.id.ll_bottom);

        stack = (ImageView)findViewById(R.id.iv_stack);

        setDefaultPositions();

        //stavlja se prva karta na sredinu
        View v = findViewById(4);
        ImageView firstCard = (ImageView) v.findViewById(R.id.card);
        firstCard.setVisibility(View.VISIBLE);
        Picasso.with(SecondDealActivity.this).load(cards.get(0).imageId).into(firstCard);
        cards.remove(0);

        Integer[] mladaZenaBasePosition = {1, 7, 3, 5};
        position = new ArrayList<Integer>(Arrays.asList(mladaZenaBasePosition));

        stack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(stack);
                v.startDrag(data, shadow, null, 0);
                Log.v("Position TOUCH", "X: "+event.getX()+" Y: "+event.getY());
                return true;
            }

        });

        stack.setOnDragListener(new View.OnDragListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP: {

                        setCardSurface();

                        return (true);
                    }

                    case DragEvent.ACTION_DRAG_ENDED: {

                        return (true);
                    }
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void setCardSurface() {
        if (position.size() > 0) {
            View v = findViewById(position.get(0));
            Card temp = getRandomCard();
            ImageView firstCard = (ImageView) v.findViewById(R.id.card);
            firstCard.setVisibility(View.VISIBLE);
            Picasso.with(SecondDealActivity.this).load(temp.imageId).into(firstCard);
            position.remove(0);
        } else {
            if (cardThrown == 5) {
                Log.v("Ostalo karata: ", cards.size() + " " + cards.toString());
            }
        }
        cardThrown++;
    }

    private Card getRandomCard() {
        int remain = cards.size() - 1;
        Log.v("Remain: ", "" + remain);
        Card result = new Card();
        if (remain > 0) {
            Random r = new Random();
            int next = r.nextInt(remain);
            result = cards.get(next);
            cards.remove(next);
        }
        return result;
    }

    private void setDefaultPositions() {
        for (int i = 0; i < 9; i++) {
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = mainInflater.inflate(R.layout.card, null);
            final ImageView card = (ImageView) mainView.findViewById(R.id.card);

            card.getLayoutParams().width = (int) (getWidth() / 10);
            card.setVisibility(View.INVISIBLE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(card.getLayoutParams());
            lp.setMargins(50, 0, 50, 0);
            card.setLayoutParams(lp);

            Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(card);
            mainView.setId(i);
            if (i < 3) {
                topLayout.addView(mainView);
            } else {
                if (i < 6) {
//                    if(i==4){
//                        View v1 = getNewCard();
//                        View v2 = getNewCard();
//                        View v3 = getNewCard();
//
//                        v1.setId(9);
//                        v2.setId(10);
//                        v3.setId(11);
//
//                        middleLayout.addView(v1);
//                        middleLayout.addView(v2);
//                        middleLayout.addView(v3);
//
//                    }
                    middleLayout.addView(mainView);
                } else {
                    bottomLayout.addView(mainView);
                }
            }
        }
    }

    private View getNewCard() {
        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mainView = mainInflater.inflate(R.layout.card, null);
        final ImageView card = (ImageView) mainView.findViewById(R.id.card);

        card.getLayoutParams().width = (int) (getWidth() / 10);
        card.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(card.getLayoutParams());
        lp.setMargins(2, 0, 0, 0);
        card.setLayoutParams(lp);

        Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(card);
        return mainView;
    }

    private View getCardView() {
        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mainView = mainInflater.inflate(R.layout.card, null);
        final ImageView card = (ImageView) mainView.findViewById(R.id.card);
        card.getLayoutParams().width = (int) (getWidth() / 10);
        return mainView;
    }


    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen width", "" + size.x);
        return size.x;
    }
}
