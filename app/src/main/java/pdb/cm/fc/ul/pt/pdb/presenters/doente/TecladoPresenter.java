package pdb.cm.fc.ul.pt.pdb.presenters.doente;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Teclado;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

import static android.content.ContentValues.TAG;

public class TecladoPresenter implements Runnable, Teclado.Presenter {

    private static final int ONE_SECOND = 1000;
    private int GAME_TIME = 90;
    private boolean mIsShowingWinActivity = false;
    private Handler mHandler = new Handler();
    private int mScore, mFaults, mTime, mChar;
    private ArrayList<String> words;
    private String mInput, mWord;
    private Teclado.View mView;
    private Vibrator mVibrator;
    private Context mContext;

    private static final String TBL_DOENTES = "doentes";
    private static final String TBL_WORDS = "words";

    private static final String TAG = TecladoPresenter.class.getSimpleName();

    public TecladoPresenter(Context context, Teclado.View view) {
        mContext = context;
        mView = view;
        setupTime();
    }

    private void setup() {
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        createListOfWords();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareGame();
            }
        }, ONE_SECOND);
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
        getWordsFromFirebase();
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

    private void setupTime(){
        String doente = UserPreferences.getUser(mContext);
        getTimeWordsGame(doente);
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

    //pls don't hit me
    //o setup está a ser chamado a partir da função para dar tempo ao firebase de ir buscar o "tempo de jogo"
    private void getTimeWordsGame(final String doenteID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot registosSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = registosSnapshot.getValue(Doente.class);
                    if (doente.getId().equals(doenteID)) {
                        GAME_TIME = Integer.parseInt(doente.getTimeWords());
                        setup();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private void getWordsFromFirebase() {
        words = new ArrayList<String>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot wordsSnapshot : dataSnapshot.getChildren()) {
                    words.add(wordsSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }
}
