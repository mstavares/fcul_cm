package pdb.cm.fc.ul.pt.pdb.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;

import static pdb.cm.fc.ul.pt.pdb.models.Constants.email;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.lastLogin;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteName;

/**
 * Created by nunonelas on 22/11/17.
 */

public class NotesViewAdapter extends BaseAdapter
{
    public ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private TextView txtNote;
    private TextView txtDate;

    public NotesViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
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
            convertView=inflater.inflate(R.layout.list_notes, null);
            txtNote = (TextView) convertView.findViewById(R.id.note);
            txtDate = (TextView) convertView.findViewById(R.id.date);
        }

        HashMap<String, String> map=list.get(position);

        txtNote.setText(map.get(noteName));
        txtDate.setText(map.get(noteDate));

        return convertView;
    }

}
