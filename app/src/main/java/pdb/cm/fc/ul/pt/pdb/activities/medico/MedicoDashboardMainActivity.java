package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard.MedicoDashboardFragment;
import pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard.MedicoNotesFragment;
import pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard.MedicoPatientSettingsFragment;
import pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard.MedicoRawDataFragment;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;

public class MedicoDashboardMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_DOENTE = "doente";
    public static final String EXTRA_MEDICO = "medico";

    private Doente doente;
    private Medico medico;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_dashboard);
        doente = (Doente) getIntent().getExtras().getSerializable(EXTRA_DOENTE);
        medico = (Medico) getIntent().getExtras().getSerializable(EXTRA_MEDICO);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.medicoNome)).setText(medico.getName());
        ((TextView) headerView.findViewById(R.id.medicoEmail)).setText(medico.getEmail());

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DOENTE, doente);
        Fragment fragment = MedicoDashboardFragment.newInstance();
        fragment.setArguments(bundle);
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MedicoMainActivity.class));
        } else if (id == R.id.nav_dashboard){
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_DOENTE, doente);
            fragment = MedicoDashboardFragment.newInstance();
            fragment.setArguments(bundle);

            replaceContent(fragment);
        } else if (id == R.id.nav_notes) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_DOENTE, doente);
            fragment = MedicoNotesFragment.newInstance();
            fragment.setArguments(bundle);

            replaceContent(fragment);
        } else if (id == R.id.nav_patient_settings) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_DOENTE, doente);
            fragment = MedicoPatientSettingsFragment.newInstance();
            fragment.setArguments(bundle);

            replaceContent(fragment);
        } else if (id == R.id.nav_rawData) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_DOENTE, doente);
            fragment = MedicoRawDataFragment.newInstance();
            fragment.setArguments(bundle);

            replaceContent(fragment);
        }  else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceContent(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }


}