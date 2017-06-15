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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private LinearLayout topLayout;
    private LinearLayout middleLayout;
    private LinearLayout bottomLayout;
    private int cardThrown = 0;
    private ArrayList<Integer> position;
    Integer[] zenaBasePosition = {4, 1, 7, 3, 5};
    Integer[] muskaracBasePosition = {4, 1, 7, 5, 3};
    ArrayList<Card> cardsDrawn = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_second_deal_activity);

        cards = Variables.leftedCards;
        topLayout = (LinearLayout) findViewById(R.id.ll_top);
        middleLayout = (LinearLayout) findViewById(R.id.ll_middle);
        bottomLayout = (LinearLayout) findViewById(R.id.ll_bottom);

        stack = (ImageView)findViewById(R.id.iv_stack);
        stack.getLayoutParams().width = getWidth() / 10;

        //redosled po kome se redjaju karte
        position = getCardPosition();

        setDefaultPositions();

        setCardSurface();

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
                        setCardSurface();
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
        if (cardThrown % 5 == 0) {
            position = getCardPosition();
        }
        int pos = position.get(0);
        View v = findViewById(pos);
        ImageView firstCard, secondCard, thirdCard, forthCard;
        if (cardThrown < 5) {
            //postavi default slike
            Card temp;
            if (pos == 4) {
                temp = cards.get(0);
                cards.remove(temp);
            } else {
                temp = getRandomCard();
            }
            temp.parentViewId = pos;
            cardsDrawn.add(temp);
            firstCard = (ImageView) v.findViewById(R.id.second_card);
            firstCard.setVisibility(View.VISIBLE);
            Picasso.with(SecondDealActivity.this).load(temp.imageId).into(firstCard);
        } else {
            Log.v("Card remain", "" + cards.size());
            //preklopi karte i na svaki 5 ciklizam resetuj position
            if (pos == 4) {
                if (cardThrown == 5) {
                    secondCard = (ImageView) v.findViewById(R.id.first_card);
                    secondCard.getLayoutParams().width = getWidth() / 10;
                    secondCard.setVisibility(View.VISIBLE);
                    addRandomCard(pos);
                    Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(secondCard);
                } else {
                    if (cardThrown == 10) {
                        thirdCard = (ImageView) v.findViewById(R.id.third_card);
                        thirdCard.getLayoutParams().width = getWidth() / 10;
                        thirdCard.setVisibility(View.VISIBLE);
                        addRandomCard(pos);
                        Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(thirdCard);
                    } else {
                        forthCard = (ImageView) v.findViewById(R.id.forth_card);
                        forthCard.getLayoutParams().width = getWidth() / 10;
                        forthCard.setVisibility(View.VISIBLE);
                        addRandomCard(pos);
                        Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(forthCard);
                        stack.setVisibility(View.GONE);
                    }
                }
            } else {
                if (cardThrown < 11) {
                    secondCard = (ImageView) v.findViewById(R.id.third_card);
                    secondCard.getLayoutParams().width = getWidth() / 10;
                    secondCard.setVisibility(View.VISIBLE);
                    addRandomCard(pos);
                    Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(secondCard);
                } else {
                    thirdCard = (ImageView) v.findViewById(R.id.forth_card);
                    thirdCard.getLayoutParams().width = getWidth() / 10;
                    thirdCard.setVisibility(View.VISIBLE);
                    addRandomCard(pos);
                    Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(thirdCard);
                }
            }
        }
        position.remove(0);
        cardThrown++;
    }

    private ArrayList<Integer> getCardPosition() {
        String cardName = Variables.user.getCardPicked().name;
        if (cardName.equals("dama_herc") || cardName.equals("sedam_pik") || cardName.equals("dama_pik") || cardName.equals("dama_karo")) {
            return new ArrayList<>(Arrays.asList(zenaBasePosition));
        }
        return new ArrayList<>(Arrays.asList(muskaracBasePosition));
    }

    private void addRandomCard(int position) {
        Card randomCard = getRandomCard();
        randomCard.parentViewId = position;
        cardsDrawn.add(randomCard);
    }

    private Card getRandomCard() {
        int remain = cards.size() - 1;
        Log.v("Remain: ", "" + remain);
        Card result;
        if (remain > 0) {
            Random r = new Random();
            int next = r.nextInt(remain);
            result = cards.get(next);
            cards.remove(next);
        } else {
            result = cards.get(0);
        }
        return result;
    }

    private void setDefaultPositions() {
        for (int i = 0; i < 9; i++) {
            LayoutInflater mainInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = mainInflater.inflate(R.layout.card_stack, null);
            final ImageView firstCard = (ImageView) mainView.findViewById(R.id.forth_card);
            final ImageView secondCard = (ImageView) mainView.findViewById(R.id.third_card);
            final ImageView thirdCard = (ImageView) mainView.findViewById(R.id.second_card);
            final ImageView forthCard = (ImageView) mainView.findViewById(R.id.first_card);

            firstCard.getLayoutParams().width = getWidth() / 10;
            secondCard.getLayoutParams().width = getWidth() / 10;
            thirdCard.getLayoutParams().width = getWidth() / 10;
            forthCard.getLayoutParams().width = getWidth() / 10;

            firstCard.setVisibility(View.INVISIBLE);
            secondCard.setVisibility(View.INVISIBLE);
            thirdCard.setVisibility(View.INVISIBLE);
            forthCard.setVisibility(View.INVISIBLE);

            Picasso.with(SecondDealActivity.this).load(R.drawable.card_back).into(firstCard);
            mainView.setId(i);
            if (i < 3) {
                topLayout.addView(mainView);
            } else {
                if (i < 6) {
                    middleLayout.addView(mainView);
                } else {
                    bottomLayout.addView(mainView);
                }
            }

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = mainView.getId();
                    ArrayList<Integer> positions = getCardPosition();
                    if (positions.contains(viewId)) {
                        if (stack.getVisibility() == View.GONE) {
                            showCardsOver(mainView.getId());
                            Toast.makeText(getApplicationContext(), getInterpetation(mainView.getId()), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                private void showCardsOver(int id) {
                    ArrayList<Card> cards = new ArrayList<Card>();
                    for (Card card :
                            cardsDrawn) {
                        if (card.parentViewId == id) {
                            cards.add(card);
                        }
                    }
                    ImageView secondCard = (ImageView) findViewById(id).findViewById(R.id.third_card);
                    ImageView thirdCard = (ImageView) findViewById(id).findViewById(R.id.forth_card);
                    Picasso.with(SecondDealActivity.this).load(cards.get(1).imageId).into(secondCard);
                    Picasso.with(SecondDealActivity.this).load(cards.get(2).imageId).into(thirdCard);
                    if (id == 4) {
                        ImageView backCard = (ImageView) findViewById(id).findViewById(R.id.first_card);
                        Picasso.with(SecondDealActivity.this).load(cards.get(3).imageId).into(backCard);
                    }
                }

                private String getInterpetation(int id) {
                    String result = "";
                    for (Card card :
                            cardsDrawn) {
                        if (card.parentViewId == id) {
                            if (result.isEmpty()) {
                                result = card.desc;
                            } else {
                                result = result + " / " + card.desc;
                            }
                        }
                    }
                    return result;
                }
            });
        }
    }

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
