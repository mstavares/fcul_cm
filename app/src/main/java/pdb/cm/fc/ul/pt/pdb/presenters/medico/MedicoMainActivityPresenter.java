package pdb.cm.fc.ul.pt.pdb.presenters.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.interfaces.medico.MedicoMain;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

public class MedicoMainActivityPresenter implements MedicoMain.Presenter, Firebase.LoadDoentes {

    private ArrayList<Doente> mDoentes;
    private MedicoMain.View mView;

    public MedicoMainActivityPresenter(MedicoMain.View view) {
        mView = view;
    }

    @Override
    public void fetchDoentes(String email) {
        FirebaseMedico.fetchAllDoentes(this, email);
    }

    @Override
    public void loadDoentes(ArrayList<Doente> doentes) {
        mDoentes = doentes;
        mView.DoentesLoaded(doentes);
    }

    @Override
    public void fetchDoente(int position) {
        mView.DoenteLoaded(mDoentes.get(position));
    }

}
