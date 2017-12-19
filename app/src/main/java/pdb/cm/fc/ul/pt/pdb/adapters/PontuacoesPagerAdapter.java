package pdb.cm.fc.ul.pt.pdb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pdb.cm.fc.ul.pt.pdb.activities.doente.PontuacoesBallGameFragment;
import pdb.cm.fc.ul.pt.pdb.activities.doente.PontuacoesWordsGameFragment;

/**
 * Created by nunonelas on 19/12/17.
 */

public class PontuacoesPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;

    public PontuacoesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PontuacoesWordsGameFragment();
            case 1:
                return new PontuacoesBallGameFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
