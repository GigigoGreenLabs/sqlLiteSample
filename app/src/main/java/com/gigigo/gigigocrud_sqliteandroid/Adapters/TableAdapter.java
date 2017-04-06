package com.gigigo.gigigocrud_sqliteandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.gigigo.gigigocrud_sqliteandroid.ColumnActivity;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.R;
import com.gigigo.gigigocrud_sqliteandroid.TableActivity;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by pablo.rojas on 6/4/17.
 */

public class TableAdapter extends ArrayAdapter<String> implements View.OnClickListener{

  private int layoutResource;
  private ImageButton btnDeleteTable;
  private TextView textViewTableName;
  private Context context;
  private ArrayList<String> tableList;
  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private String databaseName;


  public TableAdapter(Context context, int resource, ArrayList<String> tableList,
      SQLiteManager dbmanager, String databaseName) {
    super(context, resource, tableList);
    this.layoutResource = resource;
    this.context = context;
    this.tableList = tableList;
    this.dbmanager = dbmanager;
    this.databaseName = databaseName;
    this.db = dbmanager.getWritableDatabase();
  }


  @Override public View getView(int position, View convertView, ViewGroup parent) {

    View view = convertView;
    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    view = layoutInflater.inflate(layoutResource, null); //edition

    String tableName = getItem(position);

    textViewTableName = (TextView) view.findViewById(R.id.textViewTableName);
    btnDeleteTable = (ImageButton) view.findViewById(R.id.btnDeleteTable);

    btnDeleteTable.setTag(tableName);
    textViewTableName.setText(tableName);
    textViewTableName.setTag(tableName);
    btnDeleteTable.setOnClickListener(this);
    textViewTableName.setOnClickListener(this);


    return view;
  }

  @Override public void onClick(View v) {
    final String item = v.getTag().toString();
    if (v.getId() == R.id.btnDeleteTable){
      dbmanager.dropTable(db, item);
      tableList.remove(item);
      this.notifyDataSetChanged();

    }else if (v.getId() == R.id.textViewTableName){

      LinkedHashMap<String, String> columnList = dbmanager.getTableColumnNames(db, item);
      Intent intent = new Intent(context, ColumnActivity.class);
      intent.putExtra("databaseName", databaseName);
      intent.putExtra("tableName", item);
      context.startActivity(intent);

    }
  }
}