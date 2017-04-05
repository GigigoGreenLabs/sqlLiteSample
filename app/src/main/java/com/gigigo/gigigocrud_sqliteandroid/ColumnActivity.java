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
import android.widget.EditText;
import android.widget.ListView;

import com.gigigo.gigigocrud_sqliteandroid.Adapters.ColumnAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ItemColumn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ColumnActivity extends AppCompatActivity {

    private SQLiteManager dbmanager;
    private SQLiteDatabase db;
    private String databaseName;
    private String tableName;
    private LinkedHashMap<String, String> hmColumnType;
    private View dialogView;
    private Dialog columnDialog;
    private EditText editTextNameFromDialog;
    private EditText editTextTypeFromDialog;
    private ColumnAdapter adapter;
    private List<ItemColumn> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_double_edittext, null);
        columnDialog = createDialog(dialogView);

        editTextNameFromDialog = (EditText) dialogView.findViewById(R.id.editTextNameFromDialog);
        editTextTypeFromDialog = (EditText) dialogView.findViewById(R.id.editTextTypeFromDialog);

        final ListView listview = (ListView) findViewById(R.id.listColumns);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            databaseName = extras.getString("databaseName");
            tableName = extras.getString("tableName");
        }

        dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
        db = dbmanager.getWritableDatabase();

        hmColumnType = dbmanager.getTableColumnNames(db, tableName);

        Iterator it = hmColumnType.entrySet().iterator();

        listaItems = new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry row = (Map.Entry) it.next();
            String name = (String) row.getKey();
            String type = (String) row.getValue();
            ItemColumn item = new ItemColumn(name, type);
            listaItems.add(item);
            Log.v("hashmap", "" + name + " " + type);
        }

        adapter = new ColumnAdapter(this, R.layout.custom_column_item, listaItems);
        listview.setAdapter(adapter);
    }


    private Dialog createDialog(View databaseView) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(databaseView)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = editTextNameFromDialog.getText().toString().trim();
                        String type = editTextTypeFromDialog.getText().toString().trim();
                        Log.v("CREATEBUTTON", "" + tableName);
                        dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
                        db = dbmanager.getWritableDatabase();
                        dbmanager.insertColumnType(db, tableName, name, type);
                        ItemColumn item = new ItemColumn(name, type);
                        listaItems.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

    public void insertColumnType(View view) {
        columnDialog.show();
    }

    public void openDataActivity(View view) {
        Intent intent = new Intent(ColumnActivity.this, GridActivity.class);
        intent.putExtra("databaseName", databaseName);
        intent.putExtra("tableName", tableName);
        startActivity(intent);
    }
}
