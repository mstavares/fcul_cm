package pdb.cm.fc.ul.pt.pdb.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;

import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.pos;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.score;

/**
 * Created by nunonelas on 19/12/17.
 */

public class PontuacoesListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> list;
    Activity activity;

    TextView mPosition;
    TextView mName;
    TextView mScore;

    public PontuacoesListAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
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

            convertView=inflater.inflate(R.layout.item_score, null);

            mPosition=(TextView) convertView.findViewById(R.id.str_position);
            mName=(TextView) convertView.findViewById(R.id.str_name);
            mScore=(TextView) convertView.findViewById(R.id.str_score);

        }

        HashMap<String, String> map=list.get(position);
        mPosition.setText(map.get(pos));
        mName.setText(map.get(name));
        mScore.setText(map.get(score));

        return convertView;
    }

}
