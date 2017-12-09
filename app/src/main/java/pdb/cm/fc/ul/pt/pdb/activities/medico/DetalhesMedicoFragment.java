package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.DoenteDetalhesActivity.EXTRA_DOENTE;


public class DetalhesMedicoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalhes, container, false);
        Doente doente = (Doente) getArguments().getSerializable(EXTRA_DOENTE);
        ((TextView) rootView.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) rootView.findViewById(R.id.age)).setText(doente.getAge());
        return rootView;
    }
}
