package pdb.cm.fc.ul.pt.pdb.services.firebase;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;

public interface Firebase {

    interface Doente {
        void doentesLoaded(ArrayList<pdb.cm.fc.ul.pt.pdb.models.Doente> doentes);
    }
}
