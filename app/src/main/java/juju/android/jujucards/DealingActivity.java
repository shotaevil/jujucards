package juju.android.jujucards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
        RelativeLayout main_view = findViewById(R.id.main_view);
        final RelativeLayout layout = findViewById(R.id.rl_card);
        setBackground(getString(R.string.app_name), true, main_view);
        final Random rand = new Random();

        int i = 0;
        final Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        while (i < 32) {
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View firstCard = mainInflater.inflate(R.layout.card, null);
            firstCard.setId(i);
            cardView = firstCard.findViewById(R.id.card);
            cardView.setAnimation(animBounce);
            Picasso.with(DealingActivity.this).load(R.drawable.card_back).into(cardView);
            changeCardViewSize();
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
                    final View v = findViewById(rand.nextInt(32));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    params.setMargins(rand.nextInt(getHeight()), rand.nextInt(getWidth()), 0, 0);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        Log.e("DealingActivity", e.getMessage());
                    }
                    v.post(() -> {
                        if (isRunning) {
                            v.requestLayout(); //has to be called in UI thread
                        }
                    });
                }
            }
        });

        thread.start();

        layout.setOnClickListener(v -> {
            isRunning = false;
            layout.setVisibility(View.GONE);
            final RelativeLayout secondLayout = findViewById(R.id.rl_secondcard);
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View firstCard = mainInflater.inflate(R.layout.card, null);
            cardView = firstCard.findViewById(R.id.card);
            Picasso.with(DealingActivity.this).load(R.drawable.card_back).into(cardView);
            changeCardViewSize();
            secondLayout.addView(firstCard);
            cardView.setOnClickListener(v12 -> {
                ArrayList<Card> cards = new Card().initializeCards(DealingActivity.this);
                Card randomCard = cards.get(rand.nextInt(cards.size() - 1));
                changeCardViewSize();
                Picasso.with(DealingActivity.this).load(randomCard.imageId).into(cardView);
                showContinueButton();
            });

        });

    }

    private void showContinueButton() {
        Button continueBt = findViewById(R.id.bt_continue);
        continueBt.setVisibility(View.VISIBLE);
        continueBt.setOnClickListener(v1 -> startGameActivity());
    }

    private void changeCardViewSize() {
        if (getRotation(DealingActivity.this).contains("portrait")) {
            cardView.getLayoutParams().width = getWidth() / 4;
        } else {
            cardView.getLayoutParams().width = getHeight() / 6;
        }
    }

    private void startGameActivity() {
        DealingActivity.this.finish();
        Intent i = new Intent(DealingActivity.this, GameActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                showHelpDialog(getString(R.string.juju_short), getString(R.string.mix_card_instructions));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
