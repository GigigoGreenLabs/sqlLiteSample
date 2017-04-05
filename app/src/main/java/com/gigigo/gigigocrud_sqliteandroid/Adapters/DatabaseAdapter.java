package com.gigigo.gigigocrud_sqliteandroid.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.R;
import java.util.ArrayList;

/**
 * Created by pablo.rojas on 5/4/17.
 */

public class DatabaseAdapter extends ArrayAdapter<String>  implements View.OnClickListener{

  private int layoutResource;
  private ImageButton btnDeleteDatabase;
  private Context context;
  private ArrayList<String> databaseList;

  public DatabaseAdapter(Context context, int resource, ArrayList<String> databaseList) {
    super(context, resource, databaseList);
  this.layoutResource = resource;
    this.context = context;
    this.databaseList = databaseList;
  }


  @Override public View getView(int position, View convertView, ViewGroup parent) {

    View view = convertView;
    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    view = layoutInflater.inflate(layoutResource, null); //edition

    String databaseName = getItem(position);

    TextView textView = (TextView) view.findViewById(R.id.textViewDatabaseName);
    btnDeleteDatabase = (ImageButton) view.findViewById(R.id.btnDeleteDatabase);
    btnDeleteDatabase.setOnClickListener(this);
    btnDeleteDatabase.setTag(databaseName);
    textView.setText(databaseName);


    return view;
  }

  @Override public void onClick(View v) {
    Log.v("datatag","holaaaaaaa");
    if (v.getId() == R.id.btnDeleteDatabase){
      String databaseName = (String) v.getTag();
      Log.v("datatag",""+databaseName);

      SQLiteManager dbmanager = new SQLiteManager(context,null);
      dbmanager.dropDatabase(context,databaseName);
      databaseList.remove(databaseName);
      this.notifyDataSetChanged();
    }
  }
}