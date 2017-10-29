package pdb.cm.fc.ul.pt.pdb.activities.dialog;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;

public class MessageDialogActivity extends ActionBarActivity {

    /** This variable is used to fetch the activity title from the Intent */
    public static final String EXTRA_TITLE = "title";

    /** This variable is used to fetch the activity message from the Intent */
    public static final String EXTRA_MESSAGE = "message";

    /** This TextView holds the title of the dialog when the exception is triggered */
    @BindView(R.id.title) TextView title;

    /** This TextView holds the error message of the dialog when the exception is triggered */
    @BindView(R.id.message) TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_dialog);
        ButterKnife.bind(this);
        title.setText(getIntent().getExtras().getString(EXTRA_TITLE));
        message.setText(getIntent().getExtras().getString(EXTRA_MESSAGE));
    }

}
