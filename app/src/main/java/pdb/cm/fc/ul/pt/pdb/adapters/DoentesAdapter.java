package pdb.cm.fc.ul.pt.pdb.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;


import static pdb.cm.fc.ul.pt.pdb.models.Constants.lastLogin;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.email;

public class DoentesAdapter extends ArrayAdapter<Doente> {

    public DoentesAdapter(Context context , ArrayList<Doente> doentes){
        super(context, R.layout.item_doente, doentes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Doente doente = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_doente, null, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) convertView.findViewById(R.id.email)).setText(doente.getEmail());
        ((TextView) convertView.findViewById(R.id.lastlogin)).setText(doente.getLastLogin());
        return convertView;
    }

}
