package juju.android.jujucards;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.FragmentArg;


/**
 * Created by tzimonjic on 12/17/17.
 */

public class ConfirmationDialogFragment extends DialogFragment {

    String title;
    String description;

    public static ConfirmationDialogFragment newInstance(String title, String description) {
        ConfirmationDialogFragment f = new ConfirmationDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        description = getArguments().getString("description");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmation_dialog, null);
        TextView titleView = view.findViewById(R.id.dialog_title);
        TextView descriptionView = view.findViewById(R.id.dialog_description);
        Button confirmationButton = view.findViewById(R.id.dialog_confirmation_bt);

        titleView.setText(title);
        descriptionView.setText(description);

        confirmationButton.setOnClickListener(view1 -> getDialog().dismiss());

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
