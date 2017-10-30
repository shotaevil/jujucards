package juju.android.jujucards;

import android.content.Intent;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tzimonjic on 6/6/17.
 */
@EActivity(R.layout.choose_card)
public class ChoosingCardActivity extends BaseActivity implements ChooseCardFragment.OnCardClickedListener {

    @ViewById
    LinearLayout main_view;

    @AfterViews
    public void afterViews(){
        setBackground();
        displayChooseCardFragment();
    }

    private void displayChooseCardFragment() {
        ChooseCardFragment chooseCardFragment = ChooseCardFragment_.builder().screenWidth(getWidth()).build();
        this.displayFragment(chooseCardFragment, R.id.fragmentContainer);
    }

    public void setBackground(){
        main_view.setBackground(getResources().getDrawable(R.drawable.beige_background));
    }

    @Override
    public void onCardClickedListener(Card card) {
        Intent i = new Intent(this, DealingActivity.class).putExtra("CARD_ID", card.id);
        startActivity(i);
    }
}
