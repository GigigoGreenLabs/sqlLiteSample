package com.gigigo.gigigocrud_sqliteandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.R;
import com.gigigo.gigigocrud_sqliteandroid.TableActivity;

import java.util.ArrayList;

/**
 * Created by pablo.rojas on 5/4/17.
 */

public class DatabaseAdapter extends ArrayAdapter<String>  implements View.OnClickListener{

  private int layoutResource;
  private ImageButton btnDeleteDatabase;
  private TextView textViewDatabaseName;
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

    textViewDatabaseName = (TextView) view.findViewById(R.id.textViewDatabaseName);
    btnDeleteDatabase = (ImageButton) view.findViewById(R.id.btnDeleteDatabase);

    btnDeleteDatabase.setTag(databaseName);
    textViewDatabaseName.setText(databaseName);
    textViewDatabaseName.setTag(databaseName);
    btnDeleteDatabase.setOnClickListener(this);
    textViewDatabaseName.setOnClickListener(this);


    return view;
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.btnDeleteDatabase){
      String databaseName = (String) v.getTag();
      SQLiteManager dbmanager = new SQLiteManager(context,null);
      dbmanager.dropDatabase(context,databaseName);
      databaseList.remove(databaseName);
      this.notifyDataSetChanged();
    }else if (v.getId() == R.id.textViewDatabaseName){
      final String item = v.getTag().toString();
      SQLiteManager dbmanager = new SQLiteManager(context, item);
      SQLiteDatabase db = dbmanager.getWritableDatabase();
      ArrayList<String> tableList = new ArrayList<String>();
      Intent intent = new Intent(context, TableActivity.class);
      tableList = dbmanager.getTableList(db);
      intent.putExtra("databaseName", item);
      intent.putStringArrayListExtra("tableList", tableList);
      if (tableList.size() > 0) {
        context.startActivity(intent);
      } else {
        Toast.makeText(context, "BASE DE DATOS SIN CONTENIDO",
                Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
      }
    }
  }
}