package pdb.cm.fc.ul.pt.pdb.services.firebase;


import java.util.ArrayList;

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

}
