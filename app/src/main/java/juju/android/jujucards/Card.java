package juju.android.jujucards;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by arjun on 4/25/16.
 */
public class Card {
    public int id;
    public String name;
    public String desc;
    public int imageId;

    public Card(String name, int imageId, String desc) {
        this.name = name;
        this.imageId = imageId;
        this.desc = desc;
    }

    public Card(int id, int cardImage, String cardName, String cardDesc) {
        this.id = id;
        this.name = cardName;
        this.imageId = cardImage;
        this.desc = cardDesc;
    }

    public Card() {

    }

    public ArrayList<Card> initializeCards(Context ctx){
        ArrayList<Card> results = new ArrayList<>();
        ArrayList<Integer> cardImages = new ArrayList<>(Arrays.asList(R.drawable.kec_herc, R.drawable.sedam_herc, R.drawable.osam_herc, R.drawable.devet_herc, R.drawable.deset_herc, R.drawable.zandar_herc, R.drawable.dama_herc, R.drawable.kralj_herc,
                R.drawable.kec_pik, R.drawable.sedam_pik, R.drawable.osam_pik, R.drawable.devet_pik, R.drawable.deset_pik, R.drawable.zandar_pik, R.drawable.dama_pik, R.drawable.kralj_pik,
                R.drawable.kec_karo, R.drawable.sedam_karo, R.drawable.osam_karo, R.drawable.devet_karo, R.drawable.deset_karo, R.drawable.zandar_karo, R.drawable.dama_karo, R.drawable.kralj_karo,
                R.drawable.kec_tref, R.drawable.sedam_tref, R.drawable.osam_tref, R.drawable.devet_tref, R.drawable.deset_tref, R.drawable.zandar_tref, R.drawable.dama_tref, R.drawable.kralj_tref));
        ArrayList<String> cardNames = new ArrayList<>(Arrays.asList("kec_herc", "sedam_herc", "osam_herc", "devet_herc", "deset_herc", "zandar_herc", "dama_herc", "kralj_herc",
                "kec_pik", "sedam_pik", "osam_pik", "devet_pik", "deset_pik", "zandar_pik", "dama_pik", "kralj_pik",
                "kec_karo", "sedam_karo", "osam_karo", "devet_karo", "deset_karo", "zandar_karo", "dama_karo", "kralj_karo",
                "kec_tref", "sedam_tref", "osam_tref", "devet_tref", "deset_tref", "zandar_tref", "dama_tref", "kralj_tref"));
        ArrayList<Integer> cardDesc = new ArrayList<>(Arrays.asList(R.string.kec_herc, R.string.sedam_herc, R.string.osam_herc, R.string.devet_herc, R.string.deset_herc, R.string.zandar_herc, R.string.dama_herc, R.string.kralj_herc,
                R.string.kec_pik, R.string.sedam_pik, R.string.osam_pik, R.string.devet_pik, R.string.deset_pik, R.string.zandar_pik, R.string.dama_pik, R.string.kralj_pik,
                R.string.kec_karo, R.string.sedam_karo, R.string.osam_karo, R.string.devet_karo, R.string.deset_karo, R.string.zandar_karo, R.string.dama_karo, R.string.kralj_karo,
                R.string.kec_tref, R.string.sedam_tref, R.string.osam_tref, R.string.devet_tref, R.string.deset_tref, R.string.zandar_tref, R.string.dama_tref, R.string.kralj_tref));


        int counter = 0;
        for (int cardImage :
                cardImages) {
            Card card = new Card(counter, cardImage, cardNames.get(counter), ctx.getString(cardDesc.get(counter)) );
            results.add(card);
            counter++;
        }
        return results;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}