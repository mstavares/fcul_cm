package pdb.cm.fc.ul.pt.pdb.presenters.doente;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Teclado;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

import static pdb.cm.fc.ul.pt.pdb.utilities.Utilities.getTimestamp;

public class TecladoPresenter implements Runnable, Teclado.Presenter {

    private static final int ONE_SECOND = 1000;
    private static final int GAME_TIME = 90;
    private boolean mIsShowingWinActivity = false;
    private Handler mHandler = new Handler();
    private int mScore, mFaults, mTime, mChar;
    private ArrayList<String> words;
    private String mInput, mWord;
    private Teclado.View mView;
    private Vibrator mVibrator;
    private Context mContext;

    public TecladoPresenter(Context context, Teclado.View view) {
        mContext = context;
        mView = view;
        setup();
    }

    private void setup() {
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        createListOfWords();
        prepareGame();
    }

    private void prepareGame() {
        resetData();
        chooseRandomWord();
        mView.onChangeScore(mScore);
        mView.onClearInput();
        mView.onChangeTime(mTime);
        if(!mIsShowingWinActivity)
            mHandler.postDelayed(this, ONE_SECOND);
    }

    private void resetData() {
        mTime = GAME_TIME;
        mFaults = 0;
        mChar = 0;
        mInput = "";
    }

    private void prepareNextWord() {
        mChar = 0;
        mInput = "";
        mView.onClearInput();
        chooseRandomWord();
    }

    private void createListOfWords() {
        words = new ArrayList<>();
        words.add("Carro");
        words.add("Escola");
        words.add("Familia");
        words.add("Vida");
        words.add("Felicidade");
    }

    private void chooseRandomWord() {
        mWord = words.get(new Random().nextInt(words.size()));
        mView.onSetWord(mWord);
    }

    @Override
    public void onCharPressed(View view) {
        String selectedChar = ((Button) view).getText().toString();
        compareChars(selectedChar);
        compareStrings();
    }

    private void compareChars(String selectedChar){
        if(selectedChar.charAt(0) == mWord.toUpperCase().charAt(mChar)){
            mInput += selectedChar;
            mView.onSetInput(mInput);
            mChar++;
            mView.onChangeScore(++mScore);
        } else {
            mFaults++;
            mVibrator.vibrate(1000);
        }
    }

    private void compareStrings() {
        if(mInput.equalsIgnoreCase(mWord)) {
            /** Win */
            prepareNextWord();
        }
    }

    private void timeUp() {
        String doente = UserPreferences.getUser(mContext);
        FirebaseDoente.sendWordsScore(doente, new WordScore(GAME_TIME, mScore, mFaults));
        mView.onWin(mScore, mFaults, GAME_TIME);
    }

    @Override
    public void onResume() {
        if(mIsShowingWinActivity) {
            mIsShowingWinActivity = false;
            mHandler.postDelayed(this, ONE_SECOND);
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(this);
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
