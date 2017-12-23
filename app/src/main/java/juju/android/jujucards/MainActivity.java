package juju.android.jujucards;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

@EActivity(R.layout.start_screen)
public class MainActivity extends BaseActivity {

    @ViewById
    LinearLayout main_view;

    @ViewById
    EditText et_name;

    @ViewById
    EditText et_dateOfBirth;

    @AfterViews
    public void afterViews(){
        setBackground(getString(R.string.app_name), false,  main_view);
    }

    public void Submit(View v){
        String name = et_name.getText().toString();
        String age = et_dateOfBirth.getText().toString();
        setUpUserParams(name, age);
        startChooseCardActivity();
    }

    private void setUpUserParams(String name, String age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        Variables.user = user;
        Log.v("User", user.toString());
    }

    private void startChooseCardActivity() {
        Intent i = new Intent(this, ChoosingCardActivity_.class);
        startActivity(i);
    }

}
