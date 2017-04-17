package com.gigigo.gigigocrud_sqliteandroid.Objects;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.gigigo.gigigocrud_sqliteandroid.Adapters.GridAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;

/**
 * Created by pablo.rojas on 17/4/17.
 */

public class DialogClearContent extends DialogFragment {


  private SQLiteManager dbmanager;
  private String tableName;
  private SQLiteDatabase db;
  private GridAdapter adapter;

  public DialogClearContent(SQLiteManager dbmanager, String tableName, GridAdapter adapter) {
    this.dbmanager = dbmanager;
    this.db = dbmanager.getWritableDatabase();
    this.tableName = tableName;
    this.adapter = adapter;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("¿Seguro que desea borrar el contenido?")
        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dbmanager.clearTableContent(db, tableName);
            adapter.clearListContent();

          }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

          }
        });

    return builder.create();
  }
}