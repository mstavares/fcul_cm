package pdb.cm.fc.ul.pt.pdb.presenters.medico;


import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.interfaces.medico.MedicoMain;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

public class MedicoMainActivityPresenter implements MedicoMain.Presenter, Firebase.LoadDoentes, Firebase.LoadMedico {

    private ArrayList<Doente> mDoentes;
    private MedicoMain.View mView;
    private Medico mMedico;

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
        mView.DoenteLoaded(mDoentes.get(position), mMedico);
    }

    @Override
    public void fetchMedico(String email){
        FirebaseMedico.fetchMedico(this, email);
    }

    @Override
    public void loadMedico(Medico medico) {
        mMedico = medico;
    }
}
