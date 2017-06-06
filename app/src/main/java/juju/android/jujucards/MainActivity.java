package juju.android.jujucards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private RadioGroup gender;
    private RadioButton femaleBt;
    private RadioButton maleBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

//         gender = (RadioGroup) findViewById(R.id.rg_gender);
//         femaleBt = (RadioButton)findViewById(R.id.female);
//         maleBt = (RadioButton)findViewById(R.id.male);
//
//        femaleBt.setChecked(true);

    }


    public void Submit(View v){
//        Spinner sp_age = (Spinner)findViewById(R.id.sp_age);
//        Spinner sp_relationship_status = (Spinner) findViewById(R.id.sp_relationship);
//       String sex = null;
//        if (femaleBt.isChecked()){
//            sex = "female";
//        }else{
//            sex = "male";
//        }

//        switch (gender.getCheckedRadioButtonId()){
//            case 2: {
//                sex = "female";
//                break;
//            }
//            case 1: {
//                sex = "male";
//                break;
//            }
//        }
//        User user = new User();
//        user.setName(et_name.getText().toString());
//        user.setGender(sex);
//        user.setAge(sp_age.getSelectedItem().toString());
//        user.setRelationshipStatus(sp_relationship_status.getSelectedItem().toString());

        EditText et_name = (EditText) findViewById(R.id.et_name);
        EditText et_dateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        User user = new User();
        user.setName(et_name.toString());
        //user.setCard()
        Variables.user = user;
        Log.v("User", user.toString());
        Intent i = new Intent(MainActivity.this, ChoosingCardActivity.class);
        startActivity(i);
    }


}
