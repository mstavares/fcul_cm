package pdb.cm.fc.ul.pt.pdb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

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
