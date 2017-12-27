package pdb.cm.fc.ul.pt.pdb.presenters.doente;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Esfera;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

public class EsferaPresenter implements Esfera.Presenter, Runnable {

    private static final int ONE_SECOND = 1000;
    private int GAME_TIME = 90;
    private Handler mHandler = new Handler();
    private Esfera.View mView;
    private int mScore, mTime;
    private Context mContext;

    private static final String TBL_DOENTES = "doentes";
    private static final String TAG = EsferaPresenter.class.getSimpleName();

    public EsferaPresenter(Context context, Esfera.View view) {
        mView = view;
        mContext = context;
        mHandler.postDelayed(this, ONE_SECOND);
        setup();
    }

    private void setup(){
        String doente = UserPreferences.getUser(mContext);
        getTimeBallGame(doente);
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


    //pls don't hit me
    //o setup está a ser chamado a partir da função para dar tempo ao firebase de ir buscar o "tempo de jogo"
    private void getTimeBallGame(final String doenteID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot registosSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = registosSnapshot.getValue(Doente.class);
                    if (doente.getId().equals(doenteID)) {
                        GAME_TIME = Integer.parseInt(doente.getTimeBall());
                        prepareGame();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }
}
