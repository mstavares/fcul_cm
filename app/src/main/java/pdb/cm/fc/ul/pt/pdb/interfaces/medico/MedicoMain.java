package pdb.cm.fc.ul.pt.pdb.interfaces.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;

public interface MedicoMain {

    interface View {
        void loadDoentes(ArrayList<Doente> doentes);
    }

    interface Presenter {
        void fetchDoentes(String email);
    }
}
