package juju.android.jujucards;

import java.util.ArrayList;

/**
 * Created by tzimonjic on 12/25/17.
 */

public class CardPair {
    private Card card;
    private Card cardOver;
    private ArrayList<CardPair> cardPairs;


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCardOver() {
        return cardOver;
    }

    public void setCardOver(Card cardOver) {
        this.cardOver = cardOver;
    }

    public CardPair(Card card, Card cardOver) {
        this.card = card;
        this.cardOver = cardOver;
    }

}


