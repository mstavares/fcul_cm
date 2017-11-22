package pdb.cm.fc.ul.pt.pdb.views;

/**
 * Created by nunonelas on 21/11/17.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pdb.cm.fc.ul.pt.pdb.R;


import static pdb.cm.fc.ul.pt.pdb.models.Constants.lastLogin;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.email;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtName;
    TextView txtEmail;
    TextView txtLastLogin;

    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){
            convertView=inflater.inflate(R.layout.list_doentes, null);
            txtName = (TextView) convertView.findViewById(R.id.name);
            txtEmail = (TextView) convertView.findViewById(R.id.email);
            txtLastLogin = (TextView) convertView.findViewById(R.id.lastlogin);
        }

        StringBuilder stringBuilder = new StringBuilder();

        HashMap<String, String> map=list.get(position);

        if(map.get(lastLogin).equals("")) {

            stringBuilder.append("Has never logged in to PDB");

        } else {

            stringBuilder.append("Last Login: ");
            stringBuilder.append(map.get(lastLogin));
        }

        String llogin = stringBuilder.toString();

        txtName.setText(map.get(name));
        txtEmail.setText(map.get(email));
        txtLastLogin.setText(llogin);

        return convertView;
    }

}
