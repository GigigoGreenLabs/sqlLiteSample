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

public class DataBaseListActivity extends AppCompatActivity {
  ArrayList<String> databaseList;
  SQLiteManager dbmanager;
  SQLiteDatabase db;
  View databaseView;
  EditText editTextDatabaseDialog;
  ListView listview;
  ArrayAdapter adapter;
  Dialog databaseDialog;
  boolean dropDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_database_list);

    LayoutInflater inflater = getLayoutInflater();
    databaseView = inflater.inflate(R.layout.dialog_database, null);
    databaseDialog = createDialog(databaseView);




    getDatabaseList();

    listview = (ListView) findViewById(R.id.listAdapter);

    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseList );
    listview.setAdapter(adapter);

    editTextDatabaseDialog = (EditText) databaseView.findViewById(R.id.editTextDatabaseDialog);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final String item = (String) parent.getItemAtPosition(position);

        dbmanager = new SQLiteManager(getApplicationContext(), item);
        db = dbmanager.getWritableDatabase();
        ArrayList<String> tableList = new ArrayList<String>();
        Intent intent = new Intent(DataBaseListActivity.this, TableListActivity.class);
        tableList = dbmanager.getTableList(db);
        if (tableList.size() > 0) {
          intent.putExtra("databaseName", item);
          intent.putStringArrayListExtra("tableList", tableList);
          startActivity(intent);
        } else {
          Toast.makeText(DataBaseListActivity.this, "BASE DE DATOS SIN CONTENIDO",
              Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private Dialog createDialog(View databaseView) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(databaseView)
        // Add action buttons
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            String datatext = editTextDatabaseDialog.getText().toString().trim();
            if (!dropDatabase){
              Log.v("CREATEBUTTON",""+datatext);
              dbmanager = new SQLiteManager(getApplicationContext(),datatext);
              db = dbmanager.getWritableDatabase();
              databaseList.add(datatext);
            }else{
              dbmanager.dropDatabase(getApplicationContext(),datatext);
              databaseList.remove(datatext);
              dropDatabase = false;
            }

            adapter.notifyDataSetChanged();

          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {

      }
    });
    return builder.create();
  }

  private void getDatabaseList() {
    String[] dbList = getApplicationContext().databaseList(); // devuelve lista de db y db-journal

    databaseList = new ArrayList<String>();
    int i = 0;
    for (String s : dbList) {
      if (i % 2 == 0) {
        databaseList.add(s);
      }
      i++;
    }
  }


  public void createDatabase(View view) {
    databaseDialog.show();
  }

  public void dropDatabase(View view){
    dropDatabase = true;
    databaseDialog.show();
  }
}
