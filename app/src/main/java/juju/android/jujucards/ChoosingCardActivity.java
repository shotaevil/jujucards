package juju.android.jujucards;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
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
        setBackground(getString(R.string.app_name), true,  main_view);
        displayChooseCardFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                showHelpDialog(getString(R.string.juju_short), getString(R.string.choose_card_instruction));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void displayChooseCardFragment() {
        ChooseCardFragment chooseCardFragment = ChooseCardFragment_.builder().screenWidth(getWidth()).build();
        this.displayFragment(chooseCardFragment, R.id.fragmentContainer);
    }


    @Override
    public void onCardClickedListener(Card card) {
        startDealingActivity(card);
    }

    private void startDealingActivity(Card card) {
        Intent i = new Intent(this, DealingActivity.class).putExtra("CARD_ID", card.id);
        startActivity(i);
    }
}
