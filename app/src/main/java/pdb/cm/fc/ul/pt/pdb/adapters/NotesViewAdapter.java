package pdb.cm.fc.ul.pt.pdb.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Note;


public class NotesViewAdapter extends ArrayAdapter<Note> {

    private TextView mNoteText;
    private TextView mDateText;

    public NotesViewAdapter(Context context, ArrayList<Note> data){
        super(context, R.layout.item_note, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_note, null);
            mNoteText = (TextView) convertView.findViewById(R.id.note);
            mDateText = (TextView) convertView.findViewById(R.id.date);
        }
        mNoteText.setText(getItem(position).getNote());
        mDateText.setText(getItem(position).getDate());
        return convertView;
    }

}
