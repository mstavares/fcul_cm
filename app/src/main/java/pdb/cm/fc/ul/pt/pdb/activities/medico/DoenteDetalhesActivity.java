package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.DoenteDetalhesPagerAdapter;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

public class DoenteDetalhesActivity extends FragmentActivity implements ActionBar.TabListener,
        ViewPager.OnPageChangeListener {

    public static final String EXTRA_DOENTE = "doente";

    private DoenteDetalhesPagerAdapter mAppSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActionBar actionBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doente_detalhes);
        setup();
    }

    private void setup() {
        Doente doente = (Doente) getIntent().getExtras().getSerializable(EXTRA_DOENTE);
        mAppSectionsPagerAdapter = new DoenteDetalhesPagerAdapter(getSupportFragmentManager(), doente);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        initActionBar();
    }

    private void initActionBar() {
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    /*******************************************************************************************/

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

}