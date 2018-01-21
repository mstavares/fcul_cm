package pdb.cm.fc.ul.pt.pdb.services.firebase;


import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;
import pdb.cm.fc.ul.pt.pdb.models.Note;

public interface Firebase {

    interface LoadDoentes {
        void loadDoentes(ArrayList<Doente> doentes);
    }

    interface LoadDoente {
        void loadDoente(Doente doente);
    }

    interface LoadNotes {
        void loadNotes(ArrayList<Note> notes);
    }

    interface LoadMedico {
        void loadMedico(Medico medico);
    }

    interface LoadWords {
        void loadWords(ArrayList<String> wordsList);
    }

    interface LoadDashboardData {
        void loadWordsGameData(ArrayList<Entry> scoreWords, ArrayList<Entry> timeWords, ArrayList<Entry> faultsWords);
        void loadBallGameData(ArrayList<Entry> scoreBall, ArrayList<Entry> timeBall);
        void loadShakeData(ArrayList<Entry> shakes);
    }

    interface LoadRawData {
        void loadRawDataWords(ArrayList<HashMap<String, String>> listWords);
        void loadRawDataBall(ArrayList<HashMap<String, String>> listBall);
        void loadRawDataShake(ArrayList<HashMap<String, String>> listShake);
    }

    interface LoadLastPatient {
        void loadLastPatient(Doente lastDoenteOnFirebase);
    }

    interface LoadScores {
        void loadScores();
    }
}
