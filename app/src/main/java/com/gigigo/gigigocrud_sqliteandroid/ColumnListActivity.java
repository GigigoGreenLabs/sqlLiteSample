package com.gigigo.gigigocrud_sqliteandroid;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.gigigo.gigigocrud_sqliteandroid.Adapters.ColumnAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ItemColumnAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ColumnListActivity extends AppCompatActivity {


  SQLiteManager dbmanager;
  SQLiteDatabase db;
  String databaseName;
  String tableName;
  HashMap<String, String> hmColumnType;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_column_list);




    final ListView listview = (ListView) findViewById(R.id.listColumns);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      databaseName = extras.getString("databaseName");
      tableName = extras.getString("tableName");
    }

    dbmanager = new SQLiteManager(getApplicationContext(),databaseName);
    db = dbmanager.getWritableDatabase();

    hmColumnType = dbmanager.getTableColumnNames(db,tableName);

    Iterator it = hmColumnType.entrySet().iterator();

    List<ItemColumnAdapter> listaItems = new ArrayList<>();


    while (it.hasNext()){
      Map.Entry row = (Map.Entry) it.next();
      String name = (String) row.getKey();
      String type = (String) row.getValue();
      ItemColumnAdapter item = new ItemColumnAdapter(name,type);
      listaItems.add(item);
      Log.v("hashmap",""+name + " "+ type);
    }


    ColumnAdapter itemColumns = new ColumnAdapter(this, R.layout.custom_column_item, listaItems);
    listview.setAdapter(itemColumns);


  }

}
