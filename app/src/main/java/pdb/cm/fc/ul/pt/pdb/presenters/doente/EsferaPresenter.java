package pdb.cm.fc.ul.pt.pdb.presenters.doente;


import android.content.Context;
import android.os.Handler;

import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Esfera;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

public class EsferaPresenter implements Esfera.Presenter, Runnable {

    private static final int ONE_SECOND = 1000;
    private static final int GAME_TIME = 90;
    private Handler mHandler = new Handler();
    private Esfera.View mView;
    private int mScore, mTime;
    private Context mContext;

    public EsferaPresenter(Context context, Esfera.View view) {
        mView = view;
        mContext = context;
        mHandler.postDelayed(this, ONE_SECOND);
        prepareGame();
    }

    private void prepareGame() {
        mScore = 0;
        mTime = GAME_TIME;
        mView.onChangeTime(GAME_TIME);
        mView.onChangeScore(mScore);
    }

    private void timeUp() {
        String doente = UserPreferences.getUser(mContext);
        FirebaseDoente.sendBallScore(doente, new BallScore(mScore, GAME_TIME));
        mView.onTimeUp(mScore, GAME_TIME);
    }

    @Override
    public void onGoal() {
        mView.onChangeScore(++mScore);
    }

    @Override
    public void run() {
        if(mTime > 0) {
            mView.onChangeTime(--mTime);
            mHandler.postDelayed(this, ONE_SECOND);
        } else {
            timeUp();
        }
    }

}
