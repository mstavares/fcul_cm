package pdb.cm.fc.ul.pt.pdb.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.activities.medico.DetalhesMedicoFragment;
import pdb.cm.fc.ul.pt.pdb.activities.medico.NotesMedicoFragment;
import pdb.cm.fc.ul.pt.pdb.activities.medico.SettingsMedicoFragment;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.DoenteDetalhesActivity.EXTRA_DOENTE;

public class DoenteDetalhesPagerAdapter extends FragmentPagerAdapter {

    private Doente mDoente;

    public DoenteDetalhesPagerAdapter(FragmentManager fm, Doente doente) {
        super(fm);
        mDoente = doente;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DOENTE, mDoente);

        switch (position) {
            case 0:
                NotesMedicoFragment mNotesMedicoFragment = new NotesMedicoFragment();
                mNotesMedicoFragment.setArguments(bundle);
                return mNotesMedicoFragment;
            case 2:
                SettingsMedicoFragment mSettingsMedicoFragment = new SettingsMedicoFragment();
                mSettingsMedicoFragment.setArguments(bundle);
                return mSettingsMedicoFragment;
            case 1:
            default:
                DetalhesMedicoFragment mDetalhesMedicoFragment = new DetalhesMedicoFragment();
                mDetalhesMedicoFragment.setArguments(bundle);
                return mDetalhesMedicoFragment;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + (position + 1);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
