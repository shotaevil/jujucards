package juju.android.jujucards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tzimonjic on 6/6/17.
 */

public class ChoosingCardActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_card);

        LinearLayout firstRow = (LinearLayout) findViewById(R.id.ll_first_row);
        LinearLayout secondRow = (LinearLayout) findViewById(R.id.ll_second_row);
        LinearLayout thirdRow = (LinearLayout) findViewById(R.id.ll_third_row);

        final ArrayList<Card> basicCards = initiateBasicCards(new Card().initializeCards(ChoosingCardActivity.this));
        for (int i = 0; i < basicCards.size(); i++) {
            final int finalI = i;
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View card = mainInflater.inflate(R.layout.card, null);
            final ImageView cardView = (ImageView) card.findViewById(R.id.card);
            final TextView cardDesc = (TextView) card.findViewById(R.id.tv_card_desc);

            if (getRotation(ChoosingCardActivity.this).contains("portrait")) {
                cardView.getLayoutParams().width = getWidth() / 4;
                if (i < 3) {
                    firstRow.addView(card);
                }
                if (i > 2 && i < 6) {
                    secondRow.addView(card);
                }
                if (i > 5) {
                    thirdRow.addView(card);
                }
            } else {
                cardView.getLayoutParams().width = getWidth() / 8;
                firstRow.addView(card);
            }

            cardDesc.setVisibility(View.VISIBLE);
            card.setId(basicCards.get(i).id);
            Picasso.with(ChoosingCardActivity.this).load(basicCards.get(i).imageId).into(cardView);
            cardDesc.setText(basicCards.get(i).desc);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Variables.user.setCardPicked(basicCards.get(finalI));
                    Intent i = new Intent(ChoosingCardActivity.this, GameActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private ArrayList<Card> initiateBasicCards(ArrayList<Card> cards) {
        ArrayList<Card> results = new ArrayList<>();
        for (Card card :
                cards) {
            if (card.name.contains("zandar_pik") || card.name.contains("zandar_karo") ||
                    card.name.contains("dama_pik") || card.name.contains("dama_karo") ||
                    card.name.contains("dama_pik") || card.name.contains("dama_herc") ||
                    card.name.contains("sedam_pik") || card.name.contains("kralj_tref")) {
                results.add(card);
            }
        }
        return results;
    }

    public String getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen width", "" + size.x);
        return size.x;
    }
}
