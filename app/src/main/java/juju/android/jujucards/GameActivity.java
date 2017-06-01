package juju.android.jujucards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tzimonjic on 4/22/17.
 */

public class GameActivity extends Activity {
    int endPosition = 25;
    int startPosition = 0;
    int remain = 32;
    int choseCardId = -1;
    ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> firstDeal;
    ArrayList<Card>leftedCards = new ArrayList<>();
    boolean isRemoved = false;
    boolean isSelected = false;
    private LinearLayout firstRow;
    private LinearLayout secondRow;
    private LinearLayout thirdRow;
    private LinearLayout firstCell;
    private LinearLayout secondCell;
    private Utils utils;
    private TextView interpretationView;

    /* LOGIKA ZA PRVU KARTU
    * (razveden && male && age<35) || (ozenjen && male && age<35) - zandar pik
    * age>60 - kralj tref
    * age<35 && udata - 7 herc
    * age<35 && slobodna - dama karo
     * female && udata - dama herc
     * covek na polozaju - kralj pik ???
     * covek na visokom polozaju - kralj karo ???
     * (udovica || razvedena)&&female - dama pik
      * age>60 && female - sedam pik
      * OSTALA ZNACENJA
      * kuca - kec karo
      * zelja - 8 herc
      * ljubav - 7 karo
      * zdravlje i sreca - 8 karo
      * vrlo brzo - 7 tref
      * zivotni uspeh - 10 karo
      * pare - 10 herc
      * tajno ili iznenada - 9 karo
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        cards = new Card().initializeCards(GameActivity.this);

        firstRow = (LinearLayout)findViewById(R.id.ll_first_row);
        firstCell = (LinearLayout)findViewById(R.id.ll_first_cell);
        secondRow = (LinearLayout)findViewById(R.id.ll_second_row);
        secondCell = (LinearLayout)findViewById(R.id.ll_second_cell);
        thirdRow = (LinearLayout)findViewById(R.id.ll_third_row);
        LinearLayout cardStack = (LinearLayout)findViewById(R.id.ll_stack);
        interpretationView = (TextView)findViewById(R.id.tv_interpetation);

        firstDeal = new ArrayList<Card>();
        LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View mainView = mainInflater.inflate(R.layout.card, null);
        final ImageView card = (ImageView) findViewById(R.id.iv_stack);
        card.getLayoutParams().width = (int)(getWidth() /10);
//        Picasso.with(GameActivity.this).load(R.drawable.card_back).into(card);

        utils = new Utils();
        card.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(card);
                v.startDrag(data, shadow, null, 0);
                Log.v("Position TOUCH", "X: "+event.getX()+" Y: "+event.getY());
                return true;
            }

        });

        card.setOnDragListener(new View.OnDragListener() {

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
                        if(startPosition<24){
                            View cardView = findViewById(firstDeal.get(startPosition).id);
                            ImageView im = (ImageView)cardView.findViewById(R.id.card);
                            im.setVisibility(View.VISIBLE);
                            startPosition++;
                        }else{
                            if(startPosition==24){
                                choseCardId = getFirstCard(Variables.user);
                                Log.v("Choose card id", ""+choseCardId);
                                if(choseCardId>-1){
                                    View cardView = findViewById(choseCardId);
                                    ImageView im = (ImageView)cardView.findViewById(R.id.card);
                                    im.setBackground(getResources().getDrawable(R.drawable.border));
                                    startPosition++;
                                }else{
                                    Toast.makeText(GameActivity.this, "Promešajte ponovo.",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                if(startPosition>24 && startPosition<33){
                                    if(choseCardId >= 0){
                                        View cardView = findViewById(choseCardId);
                                        ImageView im = (ImageView)cardView.findViewById(R.id.card);
                                        leftedCards.add(getCardFromDeck(choseCardId));
                                        im.setBackgroundResource(0);
                                        Picasso.with(GameActivity.this).load(R.drawable.card_back).into(im);
                                        choseCardId = -1;
                                        startPosition++;
                                    }else {
                                        Toast.makeText(GameActivity.this, "Označite kartu koju želite da pokrijete", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    if(startPosition==33) {
                                        Variables.leftedCards = leftedCards;
                                        card.setVisibility(View.GONE);
                                        removeOtherCards(leftedCards);
                                        isRemoved = true;
                                    }
                                }
                            }
                        }
                        return (true);
                    }
                    default:
                        break;
                }
                return true;
            }

            private Card getCardFromDeck(int choseCardId) {
                for (Card item :
                        firstDeal) {
                    if (item.id == choseCardId) {
                        return item;
                    }
                }
                return null;
            }
        });
//        cardStack.addView(mainView);

        setCardPanelRow(0, 8, firstRow);
        setCardPanelRow(8, 16, secondRow);
        setCardPanelRow(16, 24, thirdRow);

    }

    private void setNewContentView(final ArrayList<Card> leftedCards) {
        firstRow.removeAllViews();
        secondRow.removeAllViews();
        thirdRow.removeAllViews();

        int i = 0;
        final int[] pairedClicked = {0};
        final String[] interpretation = {""};

        for(final Card card : leftedCards){
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View firstCard = mainInflater.inflate(R.layout.card, null);
            final View secondCard = mainInflater.inflate(R.layout.card, null);
            final ImageView cardView = (ImageView) firstCard.findViewById(R.id.card);
            final ImageView cardBackView = (ImageView) secondCard.findViewById(R.id.card);

            cardView.getLayoutParams().width = (int) (getWidth() / 10);
            cardBackView.getLayoutParams().width = (int) (getWidth() / 10 );

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
                        if (pairedClicked[0]<8){
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
                            interpretation[0] = interpretation[0] +card.desc+" / "+cardOver.desc+"\n";
                            interpretationView.setText(interpretation[0]);
                            firstCard.setClickable(false);
                            if(pairedClicked[0]==7) {
                                Button mixAgainBt = (Button)findViewById(R.id.bt_mix_again);
                                mixAgainBt.setVisibility(View.VISIBLE);
                            }
                            pairedClicked[0]++;
                        }
                    }

            });
            i++;
        }
    }


    private void removeOtherCards(final ArrayList<Card> leftedCards) {
        //obrisi sve karte sa view-a pa prerasporedi karte

        //firstDeal.removeAll(leftedCards);
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
                            //v.setVisibility(View.GONE);
                            setNewContentView(leftedCards);
                        }
                    });
        }

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

    private void setCardPanelRow(int from, int to, final LinearLayout row) {
        for(int i=from; i<to; i++){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.card, null);
            final ImageView card = (ImageView)view.findViewById(R.id.card);
            final Card cardFace = getRandomCard();
            firstDeal.add(cardFace);
            Picasso.with(GameActivity.this).load(cardFace.imageId).into(card);
            card.getLayoutParams().width = (int)(getWidth() /10);
            card.setVisibility(View.INVISIBLE);
            card.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if(startPosition>24 && startPosition<33){
                        card.setBackground(getResources().getDrawable(R.drawable.border));
                        choseCardId = cardFace.id;
                    }
                }
            });
            view.setId(cardFace.id);
//            baseList.add(i);
            row.addView(view);
        }
    }

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen width", "" + size.x);
        return size.x;
    }

    public int getHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen height", "" + size.y);
        return size.y;
    }

    public int getFirstCard(User user){
        /*

    * (razveden && male && age<35) || (ozenjen && male && age<35) - zandar pik
    * male age>60 - kralj tref
     * covek na polozaju - kralj pik ???
     * covek na visokom polozaju - kralj karo ???
     * female && udata - dama herc

         */
        int cardPosition = -1;

        String age = user.getAge();
        String relationship = user.getRelationshipStatus();
        String gender = user.getGender();
        Log.v("Gender", gender);
        switch (gender){
            case "female":{
                if(relationship.contains("udovica") || relationship.contains("razvedena")){
                    cardPosition = getCardPosition("dama_pik");
                }else{
                    //manje od 35
                    if(age.contains("manje")){
                        if(relationship.contains("udata")){
                            cardPosition = getCardPosition("sedam_herc");
                        }else{
                            if(relationship.contains("slobodna") || relationship.contains("vezi")) {
                                cardPosition = getCardPosition("dama_karo");
                            }
                        }
                    }else{
                        if((age.contains("35-60") || age.contains("od 60")) && relationship.contains("udata")){
                            cardPosition = getCardPosition("dama_herc");
                        }else{
                            cardPosition = getCardPosition("sedam_pik");
                        }
                    }
                }
                break;
            }
            case "male":{
                if((relationship.contains("razveden") ||
                        (relationship.contains("ozenjen"))&& age.contains("manje"))){
                    cardPosition = getCardPosition("zandar_pik");
                }else{
                    cardPosition = getCardPosition("kralj_tref");
                }
                break;
            }
        }
        return cardPosition;
    }

    private int getCardPosition(String cardName) {
        for (Card item :
                firstDeal) {
            if(item.name.equals(cardName)){
                return item.id;
            }
        }
        return -1;
    }

    public void MixAgain(View view) {
        Intent i = new Intent(GameActivity.this, SecondDealActivity.class);
        startActivity(i);
    }
}
