package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pdb.cm.fc.ul.pt.pdb.R;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoAddWordFragment extends Fragment {

    public static MedicoAddWordFragment newInstance() {
        MedicoAddWordFragment fragment = new MedicoAddWordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_addwords, container, false);

        return view;
    }
}
