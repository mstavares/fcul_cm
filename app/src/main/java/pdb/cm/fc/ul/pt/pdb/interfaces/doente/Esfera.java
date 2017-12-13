package pdb.cm.fc.ul.pt.pdb.interfaces.doente;


public interface Esfera {

    interface View {
        void onChangeTime(int time);
        void onChangeScore(int score);
        void onTimeUp(int score, int time);
    }

    interface Presenter {
        void onGoal();
    }
}
