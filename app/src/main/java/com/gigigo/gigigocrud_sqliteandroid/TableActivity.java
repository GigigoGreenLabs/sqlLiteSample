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
import com.gigigo.gigigocrud_sqliteandroid.Adapters.TableAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TableActivity extends AppCompatActivity {

  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private ArrayList<String> tableList;
  private ArrayAdapter adapter;
  private String databaseName = "";
  private Dialog tableDialog;
  private View dialogView;
  private EditText editTextFromDialog;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_table);

    LayoutInflater inflater = getLayoutInflater();
    dialogView = inflater.inflate(R.layout.dialog_edittext, null);
    tableDialog = createDialog(dialogView);
    final ListView listview = (ListView) findViewById(R.id.listTable);
    tableList = new ArrayList<>();
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      tableList = extras.getStringArrayList("tableList");
      databaseName = extras.getString("databaseName");
    }
    dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
    db = dbmanager.getWritableDatabase();

    if (tableList.size() <= 0) {
      Toast.makeText(TableActivity.this, "BASE DE DATOS SIN CONTENIDO", Toast.LENGTH_SHORT).show();
    }

    adapter = new TableAdapter(this, R.layout.button_row_table_activity, tableList, dbmanager,
        databaseName);

    listview.setAdapter(adapter);

    editTextFromDialog = (EditText) dialogView.findViewById(R.id.editTextDialog);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

      }
    });
  }

  private Dialog createDialog(View databaseView) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(databaseView)
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            String tableName = editTextFromDialog.getText().toString().trim();
              Log.v("CREATEBUTTON", "" + tableName);
              dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
              db = dbmanager.getWritableDatabase();
              dbmanager.setTableName(tableName);
              dbmanager.createTable(db);
              tableList.add(tableName);
              Toast.makeText(TableActivity.this, "Created table with 1 value Integer Autoincrement",
                  Toast.LENGTH_SHORT).show();
              adapter.notifyDataSetChanged();

          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

          }
        });
    return builder.create();
  }

  public void createTable(View view) {
    tableDialog.show();
  }

}