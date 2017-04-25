package se.edinjakupovic.mobilescraper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edinj on 25/04/2017.
 */

public class SummaryAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> summaries;

    public SummaryAdapter(Context context, ArrayList<String> summaries){
        super(context, R.layout.summary_item,summaries);
        this.context = context;
        this.summaries = summaries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.summary_item,parent,false);
        }


        String summary = summaries.get(position);
        if(summary != null){
            TextView x = (TextView) v.findViewById(R.id.row_id);
            x.setText(summary);
        }

       // TextView text = (TextView) row.findViewById(R.id.sumList);

        return v;

    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        String text = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.summary_item,parent,false);
        }

        return convertView;
    }*/

}
