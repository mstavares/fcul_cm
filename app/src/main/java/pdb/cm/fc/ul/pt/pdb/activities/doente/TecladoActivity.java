package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.dialog.MessageDialogActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Teclado;
import pdb.cm.fc.ul.pt.pdb.presenters.doente.TecladoPresenter;

public class TecladoActivity extends AppCompatActivity implements Teclado.View, View.OnClickListener {

    private static final String EXTRA_EMAIL = "email";

    @BindView(R.id.score) TextView mScoreView;
    @BindView(R.id.time) TextView mTimeView;
    @BindView(R.id.input) TextView mInputView;
    @BindView(R.id.word) TextView mWordView;

    private TecladoPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teclado);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mPresenter = new TecladoPresenter(this);
    }

    private String loadEmailFromExtras() {
        return getIntent().getExtras().getString(EXTRA_EMAIL);
    }

    @Override
    public void onClick(View view) {
        mPresenter.onCharPressed(view);
    }

    @Override
    public void onSetWord(String word) {
        mWordView.setText(word);
    }

    @Override
    public void onSetInput(String input) {
        mInputView.setText(input);
    }

    @Override
    public void onWin(int score, int time) {
        mPresenter.newScore(loadEmailFromExtras());
        startActivity(new Intent(this, MessageDialogActivity.class)
                .putExtra(MessageDialogActivity.TITLE, getString(R.string.win_title))
                .putExtra(MessageDialogActivity.MESSAGE, getString(R.string.win_message, score))
                .putExtra(MessageDialogActivity.TIME, getString(R.string.win_time, time))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    @Override
    public void onClearInput() {
        mInputView.setText("");
    }

    @Override
    public void onChangeTime(int time) {
        mTimeView.setText(getString(R.string.time, time));
    }

    @Override
    public void onChangeScore(int score) {
        mScoreView.setText(getString(R.string.score, score));
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DoenteMainActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
