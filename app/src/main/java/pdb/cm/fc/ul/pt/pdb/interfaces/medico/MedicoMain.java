package pdb.cm.fc.ul.pt.pdb.interfaces.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;

public interface MedicoMain {

    interface View {
        void DoentesLoaded(ArrayList<Doente> doentes);
        void DoenteLoaded(Doente doente, Medico medico);
    }

    interface Presenter {
        void fetchDoentes(String email);
        void fetchDoente(int position);
        void fetchMedico(String email);
    }
}
