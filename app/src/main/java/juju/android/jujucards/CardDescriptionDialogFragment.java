package juju.android.jujucards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by tzimonjic on 12/17/17.
 */

public class CardDescriptionDialogFragment extends DialogFragment {

    String title;
    ArrayList<Card> cards = new ArrayList<>();

    public static CardDescriptionDialogFragment newInstance(String title, ArrayList<Card> cards) {
        CardDescriptionDialogFragment f = new CardDescriptionDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelableArrayList("cards", cards);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        cards = getArguments().getParcelableArrayList("cards");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_card_description_dialog, null);
        TextView titleView = view.findViewById(R.id.dialog_title);
        TextView descriptionView = view.findViewById(R.id.dialog_description);
        LinearLayout cardsLayout = view.findViewById(R.id.cards_view);
        Button confirmationButton = view.findViewById(R.id.dialog_confirmation_bt);
        cardsLayout.setGravity(Gravity.CENTER);

        for (Card card :
                cards) {
            LayoutInflater cardInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View cardView = cardInflater.inflate(R.layout.card, null);
            final ImageView cardImageView = cardView.findViewById(R.id.card);
            Picasso.with(getActivity()).load(card.imageId).into(cardImageView);
            cardImageView.getLayoutParams().width = ((BaseActivity)getActivity()).getWidth() / 8;
            cardsLayout.addView(cardView);
        }

        titleView.setText(title);
        descriptionView.setText(getCardsInterpetation(cards));

        confirmationButton.setOnClickListener(view1 -> getDialog().dismiss());

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return view;
    }

    private String getCardsInterpetation(ArrayList<Card> cards) {
        String result = "";
        for (Card card :
                cards) {
            result = result
                    .concat("- ")
                    .concat(card.desc)
                    .concat("\n");
        }
        return result.substring(0, result.length()-1);
    }
}
