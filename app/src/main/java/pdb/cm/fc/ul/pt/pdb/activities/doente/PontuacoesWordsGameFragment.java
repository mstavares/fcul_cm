package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pdb.cm.fc.ul.pt.pdb.R;

/**
 * Created by nunonelas on 19/12/17.
 */

public class PontuacoesWordsGameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_doente_pontuacoes_wordsgame, container, false);

        return rootView;
    }
}
