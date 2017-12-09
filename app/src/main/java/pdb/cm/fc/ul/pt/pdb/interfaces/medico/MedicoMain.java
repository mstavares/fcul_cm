package pdb.cm.fc.ul.pt.pdb.interfaces.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;

public interface MedicoMain {

    interface View {
        void DoentesLoaded(ArrayList<Doente> doentes);
        void DoenteLoaded(Doente doente);
    }

    interface Presenter {
        void fetchDoentes(String email);
        void fetchDoente(int position);
    }
}