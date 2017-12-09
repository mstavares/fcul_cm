package pdb.cm.fc.ul.pt.pdb.interfaces.doente;


public interface Teclado {

    interface View {
        void onClearInput();
        void onWin(int score, int time);
        void onChangeTime(int time);
        void onSetWord(String word);
        void onSetInput(String input);
        void onChangeScore(int score);
    }

    interface Presenter {
        void onResume();
        void onDestroy();
        void onCharPressed(android.view.View view);
    }

}
