package edu.carleton.COMP2601;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by haamedsultani on 2017-02-15.
 */

public class PlayerAdapter extends ArrayAdapter<String>
{
    ArrayList<String> playerNames = new ArrayList<String>();

    public PlayerAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    public PlayerAdapter(Context context, int resource, ArrayList<String> playerList)
    {
        super(context, resource, playerList);

        playerNames = playerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_row, null);

        TextView playerName = (TextView) convertView.findViewById(R.id.item);
        TextView subItem = (TextView) convertView.findViewById(R.id.subItem);

        playerName.setText(playerNames.get(position));
        subItem.setText("sub-item");

        return convertView;
    }


}
