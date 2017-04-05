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
import com.gigigo.gigigocrud_sqliteandroid.Adapters.DatabaseAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {
  ArrayList<String> databaseList;
  SQLiteManager dbmanager;
  SQLiteDatabase db;
  View dialogView;
  EditText editTextFromDialog;
  ListView listview;
  ArrayAdapter adapter;
  Dialog databaseDialog;
  boolean dropDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_database);

    LayoutInflater inflater = getLayoutInflater();
    dialogView = inflater.inflate(R.layout.dialog_edittext, null);
    databaseDialog = createDialog(dialogView);



    getDatabaseList();

    listview = (ListView) findViewById(R.id.listAdapter);

    adapter = new DatabaseAdapter(this,R.layout.button_row_item, databaseList);
    listview.setAdapter(adapter);

    editTextFromDialog = (EditText) dialogView.findViewById(R.id.editTextDialog);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final String item = (String) parent.getItemAtPosition(position);

        dbmanager = new SQLiteManager(getApplicationContext(), item);
        db = dbmanager.getWritableDatabase();
        ArrayList<String> tableList = new ArrayList<String>();
        Intent intent = new Intent(DatabaseActivity.this, TableActivity.class);
        tableList = dbmanager.getTableList(db);
        intent.putExtra("databaseName", item);
        intent.putStringArrayListExtra("tableList", tableList);
        if (tableList.size() > 0) {
          startActivity(intent);
        } else {
          Toast.makeText(DatabaseActivity.this, "BASE DE DATOS SIN CONTENIDO",
              Toast.LENGTH_SHORT).show();
          startActivity(intent);
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
            String datatext = editTextFromDialog.getText().toString().trim();
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

}
