package com.gigigo.gigigocrud_sqliteandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.gigigo.gigigocrud_sqliteandroid.Adapters.DatabaseAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {
  private ArrayList<String> databaseList;
  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private View dialogView;
  private EditText editTextFromDialog;
  private ListView listview;
  private ArrayAdapter adapter;
  private Dialog databaseDialog;
  private boolean dropDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_database);

    LayoutInflater inflater = getLayoutInflater();
    dialogView = inflater.inflate(R.layout.dialog_edittext, null);
    databaseDialog = createDialog(dialogView);

    getDatabaseList();

    listview = (ListView) findViewById(R.id.listAdapter);

    adapter = new DatabaseAdapter(this, R.layout.button_row_database_activity, databaseList);
    listview.setAdapter(adapter);

    editTextFromDialog = (EditText) dialogView.findViewById(R.id.editTextDialog);
  }

  private Dialog createDialog(View databaseView) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(databaseView)
        // Add action buttons
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            String dbname = editTextFromDialog.getText().toString().trim();
            if (!dropDatabase) {
              Log.v("CREATEBUTTON", "" + dbname);
              dbmanager = new SQLiteManager(getApplicationContext(), dbname);
              db = dbmanager.getWritableDatabase();
              databaseList.add(dbname);
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
      if (!s.endsWith("-journal")) {
        databaseList.add(s);
      }
    }
  }

  public void createDatabase(View view) {
    databaseDialog.show();
  }
}
