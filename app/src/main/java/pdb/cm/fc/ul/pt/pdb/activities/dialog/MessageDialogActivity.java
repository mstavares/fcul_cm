package pdb.cm.fc.ul.pt.pdb.activities.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.doente.DoenteMainActivity;

public class MessageDialogActivity extends AppCompatActivity {

    /** This variable is used to fetch the activity title from the Intent */
    public static final String TITLE = "title";

    /** This variable is used to fetch the activity message from the Intent */
    public static final String MESSAGE = "message";

    /** This variable is used to fetch the activity message from the Intent */
    public static final String TIME = "time";

    /** This TextView holds the title of the dialog when the exception is triggered */
    @BindView(R.id.title) TextView title;

    /** This TextView holds the error message of the dialog when the exception is triggered */
    @BindView(R.id.message) TextView message;

    @BindView(R.id.time) TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_dialog);
        ButterKnife.bind(this);
        title.setText(getIntent().getExtras().getString(TITLE));
        message.setText(getIntent().getExtras().getString(MESSAGE));
        time.setText(getIntent().getExtras().getString(TIME));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DoenteMainActivity.class));
        finishAffinity();
        super.onBackPressed();
    }

}
