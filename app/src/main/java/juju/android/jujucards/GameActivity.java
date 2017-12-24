package juju.android.jujucards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tzimonjic on 4/22/17.
 */

public class GameActivity extends BaseActivity {
    int startPosition = 0;
    int remain = 32;
    int choseCardId = -1;
    ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> firstDeal;
    ArrayList<Card> coverCards = new ArrayList<>();
    boolean isRemoved = false;
    private LinearLayout firstRow;
    private LinearLayout secondRow;
    private LinearLayout thirdRow;
    private Utils utils;
    private TextView interpretationView;
    private ImageView stack;
    private Button continueButton;
    private ImageView fakeStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        RelativeLayout main_view = findViewById(R.id.main_view);
        setBackground(getString(R.string.app_name), true, main_view);

        cards = new Card().initializeCards(GameActivity.this);

        firstRow = findViewById(R.id.ll_first_row);
        secondRow = findViewById(R.id.ll_second_row);
        thirdRow = findViewById(R.id.ll_third_row);
        interpretationView = findViewById(R.id.tv_interpetation);
        continueButton = findViewById(R.id.bt_continue);

        firstDeal = new ArrayList<>();
        stack = findViewById(R.id.iv_stack);
        changeCardStackViewParams();

        utils = new Utils();
        stack.setOnTouchListener((v, event) -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(stack);
            v.startDrag(data, shadow, null, 0);
            Log.v("Position TOUCH", "X: "+event.getX()+" Y: "+event.getY());
            return true;
        });

        stack.performClick();

        stack.setOnDragListener((v, event) -> {
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
                    if(startPosition<24){
                        showCardOnTableByGivenPosition(firstDeal, startPosition);
                        startPosition++;
                    }
                    if(startPosition==24){
                        choseCardId = takeChosenCardId(Variables.user.getCardPicked().name);
                        Log.v("Choose stack id", "" + choseCardId);
                        //provera da li je karta izasla i moze da se nastavi igra
                        if(choseCardId>-1){
                            showHelpDialog(getString(R.string.juju_short), getString(R.string.first_covered_card_message));
                            coverCardById(choseCardId);
                            addCoveredCard(takeCardFromDeck(choseCardId));
                            startPosition++;
                        }else{
                            showHelpDialog(getString(R.string.juju_short), getString(R.string.card_not_shown_message));
                        }
                    }
                    if(startPosition>24){
                        stack.setClickable(false);
                    }
                }
                return (true);

                default:
                    break;
            }
            return true;
        });

        coverPanelRowWithRandomCards(0, 8, firstRow);
        coverPanelRowWithRandomCards(8, 16, secondRow);
        coverPanelRowWithRandomCards(16, 24, thirdRow);
    }

    private void coverPanelRowWithRandomCards(int from, int to, final LinearLayout row) {
        for(int i=from; i<to; i++){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.card, null);
            final Card randomCard = getRandomCard();
            firstDeal.add(randomCard);
            final ImageView card = view.findViewById(R.id.card);
            changeCardViewParams(randomCard, card);
            card.setOnClickListener(v -> {
                continueButton.setVisibility(View.VISIBLE);
                Card clickedCardFromDeck = takeCardFromDeck(randomCard.id);
                //kraj prvog dela kada se preklope 8 karata
                if (startPosition == 31) {
                    coverCard(card, clickedCardFromDeck);
                    hideCardStack();
                    continueButton.setOnClickListener(v1 -> {
                        removeOtherCards(coverCards);
                        Variables.leftedCards = coverCards;
                        continueButton.setVisibility(View.GONE);
                    });
                }
                if(startPosition>24 && startPosition<32){
                    continueButton.setVisibility(View.GONE);
                    if (randomCard.isFlipped) {
                        //slucaj da se korisnik predomisli pa vrati pokrivenu kartu nazad u stek
                        uncoverCard(randomCard, card, clickedCardFromDeck);
                        startPosition--;
                    } else {
                        coverCard(card, clickedCardFromDeck);
                        choseCardId = -1;
                        startPosition++;
                    }
                    randomCard.isFlipped = !randomCard.isFlipped;
                }
            });
            view.setId(randomCard.id);
            row.addView(view);
        }
    }

    private void uncoverCard(Card randomCard, ImageView card, Card clickedCardFromDeck) {
        removeCoverCard(clickedCardFromDeck);
        int randomCardImageId = randomCard.imageId;
        int fade_in = R.anim.fade_in;
        changeCardViewAndShowAnimation(card, randomCardImageId, fade_in);
    }

    private void coverCard(ImageView card, Card clickedCardFromDeck) {
        addCoveredCard(clickedCardFromDeck);
        int card_back = R.drawable.card_back;
        int fade_out = R.anim.fade_out;
        changeCardViewAndShowAnimation(card, card_back, fade_out);
       }

    private void coverCardById(int cardIdForHiding) {
        View cardView = findViewById(cardIdForHiding);
        ImageView im = cardView.findViewById(R.id.card);
        Picasso.with(GameActivity.this).load(R.drawable.card_back).into(im);
    }

    private void removeCoverCard(Card randomCard) {
        coverCards.remove(randomCard);
    }

    private void changeCardViewAndShowAnimation(ImageView card, int cardImageId, int animation) {
        Picasso.with(GameActivity.this).load(cardImageId).into(card);
        Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(), animation);
        stack.startAnimation(animBounce);
    }

    private void hideCardStack() {
        stack.setVisibility(View.GONE);
        fakeStack.setVisibility(View.GONE);
    }

    private void changeCardViewParams(Card randomCard, ImageView cardView) {
        Picasso.with(GameActivity.this).load(randomCard.imageId).into(cardView);
        cardView.getLayoutParams().width = getWidth() / 10;
        cardView.setVisibility(View.INVISIBLE);
    }

    private void addCoveredCard(Card cardFromDeck) {
        coverCards.add(cardFromDeck);
    }

    private void showCardOnTableByGivenPosition(ArrayList<Card> firstDealCards, int position) {
        Card firstDealCard = firstDealCards.get(position);
        View cardView = findViewById(firstDealCard.id);
        ImageView im = cardView.findViewById(R.id.card);
        im.setVisibility(View.VISIBLE);
    }

    private void changeCardStackViewParams() {
        stack.getLayoutParams().width = getWidth() / 10;
        fakeStack = findViewById(R.id.iv_fake_stack);
        fakeStack.getLayoutParams().width = getWidth() / 10;
        fakeStack.setAlpha(0.5f);
    }

    private Card takeCardFromDeck(int choseCardId) {
        for (Card item :
                firstDeal) {
            if (item.id == choseCardId) {
                return item;
            }
        }
        return null;
    }

    private int takeChosenCardId(String cardName) {
        for (Card item :
                firstDeal) {
            if(item.name.equals(cardName)){
                return item.id;
            }
        }
        return -1;
    }

    private void showCoveredCardsOnly(final ArrayList<Card> leftedCards) {
        resetContentView();

        int i = 0;
        final int[] pairedClicked = {0};
        final String[] interpretation = {""};

        for(final Card card : leftedCards){
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View firstCard = mainInflater.inflate(R.layout.card, null);
            final View secondCard = mainInflater.inflate(R.layout.card, null);
            final ImageView cardView = (ImageView) firstCard.findViewById(R.id.card);
            final ImageView cardBackView = (ImageView) secondCard.findViewById(R.id.card);

            cardView.getLayoutParams().width = getWidth() / 10;
            cardBackView.getLayoutParams().width = getWidth() / 10;

            cardView.setVisibility(View.VISIBLE);
            cardBackView.setVisibility(View.GONE);
            Picasso.with(GameActivity.this).load(R.drawable.card_back).into(cardView);
            Picasso.with(GameActivity.this).load(R.drawable.card_back).into(cardBackView);

            if(i>=0 && i<4) {
                firstRow.addView(firstCard);
                firstRow.addView(secondCard);
                utils.SlideLeft(firstCard, GameActivity.this, "fast");

            }else{
                if(i<8) {
                    secondRow.addView(firstCard);
                    secondRow.addView(secondCard);
                    utils.SlideLeft(firstCard, GameActivity.this, "fast");
                }
            }
            firstCard.setId(card.id);
            firstCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pairedClicked[0] < 8) {
                        Card cardOver = getRandomCard();
                        Variables.leftedCards.add(cardOver);
                        Picasso.with(GameActivity.this).load(card.imageId).into(cardView);
                        Picasso.with(GameActivity.this).load(cardOver.imageId).into(cardBackView);
                        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(cardBackView.getLayoutParams());
                        marginParams.setMargins(0, 0, 25, 0);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
                        cardBackView.setLayoutParams(layoutParams);
                        cardBackView.setVisibility(View.VISIBLE);
                        utils.SlideLeft(cardView, GameActivity.this, "fast");
                        utils.SlideLeft(cardBackView, GameActivity.this, "slow");
                        interpretation[0] = interpretation[0] + card.desc + " / " + cardOver.desc + "\n";
                        interpretationView.setText(interpretation[0]);
                        firstCard.setClickable(false);
                        if (pairedClicked[0] == 7) {
//                            Button mixAgainBt = (Button) findViewById(R.id.bt_mix_again);
//                            mixAgainBt.setVisibility(View.VISIBLE);
                            continueButton.setText("PROMEŠAJ PONOVO");
                            continueButton.setVisibility(View.VISIBLE);
                            continueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MixAgain(v);
                                }
                            });
                        }
                        pairedClicked[0]++;
                    }
                }

            });
            i++;
        }
    }

    private void resetContentView() {
        firstRow.removeAllViews();
        secondRow.removeAllViews();
        thirdRow.removeAllViews();
    }

    private void removeOtherCards(final ArrayList<Card> leftedCards) {
        //obrisi sve karte sa view-a pa prerasporedi karte
        for (Card card :
                firstDeal) {
            final View v = findViewById(card.id);

            v.animate()
                    .translationY(v.getHeight())
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            showCoveredCardsOnly(leftedCards);
                        }
                    });
        }
        isRemoved = true;
    }

    private Card getRandomCard() {
        Log.v("Remain: ",""+remain);
        Card result = new Card();
        if(remain>0){
            Random r = new Random();
            int next = r.nextInt(remain);
            result = cards.get(next);
            cards.remove(next);
            remain--;
        }
        return result;
    }

    public void MixAgain(View view) {
        GameActivity.this.finish();
        Intent i = new Intent(GameActivity.this, SecondDealActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                showHelpDialog(getString(R.string.juju_first_part_title), getString(R.string.first_part_explanation));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
