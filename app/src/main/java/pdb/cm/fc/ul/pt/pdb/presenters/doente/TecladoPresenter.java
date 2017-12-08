package pdb.cm.fc.ul.pt.pdb.presenters.doente;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Teclado;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

import static pdb.cm.fc.ul.pt.pdb.utilities.Utilities.getTimestamp;

public class TecladoPresenter implements Runnable, Teclado.Presenter, AccelerometerListener.onShake {

    private static final int ONE_SECOND = 1000;
    private boolean mIsShowingWinActivity = false;
    private Handler mHandler = new Handler();
    private int mScore, mFaults, mTime, mChar;
    private ArrayList<String> words;
    private String mInput, mWord;
    private Teclado.View mView;

    public TecladoPresenter(Teclado.View view) {
        mView = view;
        setup();
    }

    private void setup() {
        createListOfWords();
        prepareGame();
        AccelerometerManager.registerListener(this);
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
        mTime = 0;
        mFaults = 0;
        mChar = 0;
        mInput = "";
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
        } else {
            mFaults++;
        }
    }

    private void compareStrings() {
        if(mInput.equalsIgnoreCase(mWord)) {
            mHandler.removeCallbacks(this);
            mIsShowingWinActivity = true;
            mScore++;
            mView.onWin(mScore, mTime);
            prepareGame();
        }
    }

    private void deleteLastChar() {
        if(mInput.length() > 0) {
            mInput = mInput.substring(0, mInput.length() - 1);
        } else {
            mInput = "";
        }
    }

    public void newScore(String doente){
        WordScore wordScore = new WordScore();
        wordScore.setDate(getTimestamp());
        wordScore.setScore(Integer.toString(mScore));
        wordScore.setTime(Integer.toString(mTime));
        wordScore.setFaults(Integer.toString(mFaults));
        wordScore.setWord(mWord);
        FirebaseDoente.setWordsScore(doente, wordScore);
    }

    @Override
    public void onShake() {
        mFaults++;
        deleteLastChar();
        mView.onSetInput(mInput);
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
        AccelerometerManager.unRegisterListener(this);
    }

    @Override
    public void run() {
        mView.onChangeTime(++mTime);
        mHandler.postDelayed(this, ONE_SECOND);
    }
}
