package juju.android.jujucards;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by tzimonjic on 10/30/17.
 */

@EFragment(R.layout.choose_card_fragment)
public class ChooseCardFragment extends Fragment {
    @ViewById
    LinearLayout firstRow;

    @ViewById
    LinearLayout secondRow;

    @ViewById
    LinearLayout thirdRow;

    @FragmentArg
    protected int screenWidth;

    @Bean
    public BaseActivity baseActivity;

    private OnCardClickedListener listener;


    @AfterViews
    public void afterViews() {
        final ArrayList<Card> basicCards = new Card().getBasicCards(getActivity());

        for (int i = 0; i < basicCards.size(); i++) {
            final int finalI = i;
            LayoutInflater mainInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View card = mainInflater.inflate(R.layout.card, null);
            final ImageView cardView = (ImageView) card.findViewById(R.id.card);
            final TextView cardDesc = (TextView) card.findViewById(R.id.tv_card_desc);

            if (baseActivity.getRotation(getActivity()).contains("portrait")) {
                cardView.getLayoutParams().width = screenWidth / 4;
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
                cardView.getLayoutParams().width = screenWidth / 8;
                firstRow.addView(card);
            }

            cardDesc.setVisibility(View.VISIBLE);
            card.setId(basicCards.get(i).id);
            Picasso.with(getActivity()).load(basicCards.get(i).imageId).into(cardView);
            cardDesc.setText(basicCards.get(i).desc);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: VARIABLES - get rid of this
                    Variables.user.setCardPicked(basicCards.get(finalI));
                    listener.onCardClickedListener(basicCards.get(finalI));
                }
            });
        }
    }
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            listener = (OnCardClickedListener) context;
        } catch (ClassCastException castException) {
            throw new ClassCastException(context.toString() + " must implement OnCardClickedListener");
        }
    }

    public interface OnCardClickedListener {
        void onCardClickedListener(Card card);
    }
}
