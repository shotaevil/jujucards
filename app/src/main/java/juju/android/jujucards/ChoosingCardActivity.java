package juju.android.jujucards;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
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
        setHeader();
        displayChooseCardFragment();
    }

    private void displayChooseCardFragment() {
        ChooseCardFragment chooseCardFragment = ChooseCardFragment_.builder().screenWidth(getWidth()).build();
        this.displayFragment(chooseCardFragment, R.id.fragmentContainer);
    }

    public void setHeader(){
        ImageView helpButton = findViewById(R.id.help_bt);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });
    }

    private void showHelpDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance("JuJu", getString(R.string.choose_card_instruction));
        confirmationDialogFragment.show(fragmentManager, "dialog");
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
