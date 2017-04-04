package com.gigigo.gigigocrud_sqliteandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TableListActivity extends AppCompatActivity {

  SQLiteManager dbmanager;
  SQLiteDatabase db;
  ArrayList<String> tableList;
  LinkedHashMap<String, String> columnList;
  ArrayAdapter<String> adapter;
  String databaseName = "";
  Dialog tableDialog;
  View dialogView;
  EditText editTextFromDialog;
  boolean dropTable;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_table_list);

    LayoutInflater inflater = getLayoutInflater();
    dialogView = inflater.inflate(R.layout.dialog_edittext, null);
    tableDialog = createDialog(dialogView);

    final ListView listview = (ListView) findViewById(R.id.listTable);

    tableList = new ArrayList<>();
    columnList = new LinkedHashMap<>();
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      tableList = extras.getStringArrayList("tableList");
      databaseName = extras.getString("databaseName");
      // and get whatever type user account id is
    }
    dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
    db = dbmanager.getWritableDatabase();

    if (tableList.size() <= 0) {
      Toast.makeText(TableListActivity.this, "BASE DE DATOS SIN CONTENIDO", Toast.LENGTH_SHORT)
          .show();
    }

    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tableList);
    listview.setAdapter(adapter);

    editTextFromDialog  = (EditText) dialogView.findViewById(R.id.editTextDialog);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final String item = (String) parent.getItemAtPosition(position);
        columnList = dbmanager.getTableColumnNames(db, item);
        Intent intent = new Intent(TableListActivity.this, ColumnListActivity.class);
        intent.putExtra("databaseName", databaseName);
        intent.putExtra("tableName", item);
        startActivity(intent);
      }
    });
  }

  private Dialog createDialog(View databaseView) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(databaseView)
        // Add action buttons
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            String tableName = editTextFromDialog.getText().toString().trim();
            if (!dropTable){
              Log.v("CREATEBUTTON",""+tableName);
              dbmanager = new SQLiteManager(getApplicationContext(),databaseName);
              db = dbmanager.getWritableDatabase();
              dbmanager.setTableName(tableName);
              dbmanager.createTable(db);
              tableList.add(tableName);
              Toast.makeText(TableListActivity.this, "Created table with 1 value Integer Autoincrement", Toast.LENGTH_SHORT).show();
            }else{
              dbmanager.dropTable(db,tableName);
              tableList.remove(tableName);
              dropTable = false;
            }

            adapter.notifyDataSetChanged();

          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {

      }
    });
    return builder.create();
  }

  public void createTable(View view) {
    tableDialog.show();

  }

  public void dropTable(View view) {
    dropTable = true;
    tableDialog.show();
  }
}