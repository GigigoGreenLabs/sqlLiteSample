package com.gigigo.gigigocrud_sqliteandroid;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class TableListActivity extends AppCompatActivity {

  SQLiteManager dbmanager;
  SQLiteDatabase db;
  ArrayList <String> tableList;
  HashMap<String, String> columnList;
  ArrayAdapter<String> adapter;
  String databaseName = "";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_table_list);

    final ListView listview = (ListView) findViewById(R.id.listTable);


    tableList = new ArrayList<>();
    columnList = new HashMap<>();
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      tableList = extras.getStringArrayList("tableList");
      databaseName = extras.getString("databaseName");
      // and get whatever type user account id is
    }
    dbmanager = new SQLiteManager(getApplicationContext(),databaseName);
    db = dbmanager.getWritableDatabase();

    if (tableList.size()<= 0){
      Toast.makeText(TableListActivity.this, "BASE DE DATOS SIN CONTENIDO",
          Toast.LENGTH_SHORT).show();
    }

    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tableList);
    listview.setAdapter(adapter);


    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final String item = (String) parent.getItemAtPosition(position);
        columnList = dbmanager.getTableColumnNames(db,item);
        Intent intent = new Intent(TableListActivity.this, ColumnListActivity.class);
        intent.putExtra("databaseName", databaseName);
        intent.putExtra("tableName", item);
        startActivity(intent);

      }
    });
  }

}