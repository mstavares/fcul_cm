package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.R;

public class TecladoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvInputUser;
    private TextView tvWord;
    private TextView tvScore;
    private String inputUser;

    private Button btn_Q;
    private Button btn_W;
    private Button btn_E;
    private Button btn_R;
    private Button btn_T;
    private Button btn_Y;
    private Button btn_U;
    private Button btn_I;
    private Button btn_O;
    private Button btn_P;
    private Button btn_A;
    private Button btn_S;
    private Button btn_D;
    private Button btn_F;
    private Button btn_G;
    private Button btn_H;
    private Button btn_J;
    private Button btn_K;
    private Button btn_L;
    private Button btn_Z;
    private Button btn_X;
    private Button btn_C;
    private Button btn_V;
    private Button btn_B;
    private Button btn_N;
    private Button btn_M;
    private Button btn_done;
    private Button btn_del;

    private boolean isUserIsInTheMiddleOfTyping=false;

    private ArrayList<String> words;

    private int tries = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        createListOfWords();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teclado);

        tvWord = (TextView) findViewById(R.id.str_word);
        tvScore = (TextView) findViewById(R.id.score);
        getRandomWord();

        tvInputUser = (TextView) findViewById(R.id.str_input);
        tvInputUser.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                compareStrings();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        btn_Q = (Button) findViewById(R.id.btn_Q);
        btn_W = (Button) findViewById(R.id.btn_W);
        btn_E = (Button) findViewById(R.id.btn_E);
        btn_R = (Button) findViewById(R.id.btn_R);
        btn_T = (Button) findViewById(R.id.btn_T);
        btn_Y = (Button) findViewById(R.id.btn_Y);
        btn_U = (Button) findViewById(R.id.btn_U);
        btn_I = (Button) findViewById(R.id.btn_I);
        btn_O = (Button) findViewById(R.id.btn_O);
        btn_P = (Button) findViewById(R.id.btn_P);
        btn_A = (Button) findViewById(R.id.btn_A);
        btn_S = (Button) findViewById(R.id.btn_S);
        btn_D = (Button) findViewById(R.id.btn_D);
        btn_F = (Button) findViewById(R.id.btn_F);
        btn_G = (Button) findViewById(R.id.btn_G);
        btn_H = (Button) findViewById(R.id.btn_H);
        btn_J = (Button) findViewById(R.id.btn_J);
        btn_K = (Button) findViewById(R.id.btn_K);
        btn_L = (Button) findViewById(R.id.btn_L);
        btn_Z = (Button) findViewById(R.id.btn_Z);
        btn_X = (Button) findViewById(R.id.btn_X);
        btn_C = (Button) findViewById(R.id.btn_C);
        btn_V = (Button) findViewById(R.id.btn_V);
        btn_B = (Button) findViewById(R.id.btn_B);
        btn_N = (Button) findViewById(R.id.btn_N);
        btn_M = (Button) findViewById(R.id.btn_M);
        btn_done = (Button) findViewById(R.id.btn_done);
        btn_del = (Button) findViewById(R.id.btn_del);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Handle Clicks
            case R.id.btn_Q:
            case R.id.btn_W:
            case R.id.btn_E:
            case R.id.btn_R:
            case R.id.btn_T:
            case R.id.btn_Y:
            case R.id.btn_U:
            case R.id.btn_I:
            case R.id.btn_O:
            case R.id.btn_P:
            case R.id.btn_A:
            case R.id.btn_S:
            case R.id.btn_D:
            case R.id.btn_F:
            case R.id.btn_G:
            case R.id.btn_H:
            case R.id.btn_J:
            case R.id.btn_K:
            case R.id.btn_L:
            case R.id.btn_Z:
            case R.id.btn_X:
            case R.id.btn_C:
            case R.id.btn_V:
            case R.id.btn_B:
            case R.id.btn_N:
            case R.id.btn_M:
                touchDigit(view);
                break;
            case R.id.btn_done:
                compareStrings();
                break;
            case R.id.btn_del:
                delLastDigit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DoenteMainActivity.class));
        finish();
    }

    public void touchDigit(View view) {

        Button btPressed = (Button) view;
        String digito = btPressed.getText().toString();
        if (isUserIsInTheMiddleOfTyping) {
            tvInputUser.append(digito);
        }else {
            tvInputUser.setText(digito);
            isUserIsInTheMiddleOfTyping = true;
        }
    }

    public void compareStrings (){
        if (inputUser.equalsIgnoreCase(tvInputUser.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true)
                    .setTitle("Aviso")
                    .setMessage("Parabéns, acertou na palavra: " + inputUser + " em " + tries + " tentativa(s)!");

            AlertDialog alert = builder.create();
            alert.show();

            setScore();
            getRandomWord();
            clearScreen();
        }
    }

    public void delLastDigit (){
        String str = tvInputUser.getText().toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        tvInputUser.setText(str);
        tries++;
    }

    public void createListOfWords(){
        words = new ArrayList<String>();
        words.add("Carro");
        words.add("Escola");
        words.add("Familia");
        words.add("Vida");
        words.add("Felicidade");
    }

    public void getRandomWord(){
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(words.size());
        inputUser = words.get(index);
        tvWord.setText(inputUser);
    }

    public void clearScreen (){
        tvInputUser.setText("");
        tries = 0;
    }

    public void setScore(){
        score++;
        tvScore.setText(getString(R.string.score, score));
    }
}
