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

import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataShake;


/**
 * Created by nunonelas on 19/12/17.
 */

public class RawDataShakeAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> list;
    Activity activity;

    TextView mShake;
    TextView mDate;

    public RawDataShakeAdapter (Activity activity,ArrayList<HashMap<String, String>> list){
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

            convertView=inflater.inflate(R.layout.item_rawdata_shake, null);

            mShake = (TextView) convertView.findViewById(R.id.rawdata_shake);
            mDate = (TextView) convertView.findViewById(R.id.rawdata_date);

        }

        HashMap<String, String> map=list.get(position);
        mShake.setText("Shake: "+map.get(rawDataShake));
        mDate.setText(map.get(rawDataDate));

        return convertView;
    }

}
