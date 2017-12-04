package pdb.cm.fc.ul.pt.pdb.presenters.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.interfaces.medico.MedicoMain;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

public class MedicoMainActivityPresenter implements MedicoMain.Presenter, Firebase.Doente {

    private MedicoMain.View mView;

    public MedicoMainActivityPresenter(MedicoMain.View view) {
        mView = view;
    }

    @Override
    public void fetchDoentes(String email) {
        FirebaseDoente.fetchAllDoentes(this, email);
    }

    @Override
    public void doentesLoaded(ArrayList<Doente> doentes) {
        mView.loadDoentes(doentes);
    }
}
