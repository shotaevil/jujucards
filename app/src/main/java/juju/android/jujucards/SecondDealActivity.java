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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tzimonjic on 5/30/17.
 */

public class SecondDealActivity extends Activity {

    private ArrayList<Card> cards;
    private GridLayout gridLayout;
    private ImageView stack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_second_deal_activity);

        cards = Variables.leftedCards;
        gridLayout = (GridLayout)findViewById(R.id.GridLayout1);
        stack = (ImageView)findViewById(R.id.iv_stack);

        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mainView = mainInflater.inflate(R.layout.card, null);
        final ImageView card = (ImageView)mainView.findViewById(R.id.card);
        card.getLayoutParams().width = (int)(getWidth() /10);
        Picasso.with(SecondDealActivity.this).load(cards.get(0).imageId).into(card);

        gridLayout.addView(mainView, 4);
        Log.v("Cards", Variables.leftedCards.toString());


//        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View mainView = mainInflater.inflate(R.layout.card, null);
//        final ImageView card = (ImageView)mainView.findViewById(R.id.card);
//        card.getLayoutParams().width = (int)(getWidth() /10);
//        Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(card);
//

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


    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen width", "" + size.x);
        return size.x;
    }
}
