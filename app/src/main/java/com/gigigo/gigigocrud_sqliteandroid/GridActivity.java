package com.gigigo.gigigocrud_sqliteandroid;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigigo.gigigocrud_sqliteandroid.Adapters.GridAdapter;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ModelObj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GridActivity extends AppCompatActivity {

  private GridView grdOpciones;

  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private String databaseName;
  private String tableName;

  //private ArrayList<ModelUser> datos;
  private LinkedHashMap<Integer, ModelObj> datos;

  private LinkedHashMap<String, String> hmColumnNameList;
  private LinkedHashMap<EditText, String> hmEditTextColumn;
  private ArrayList<String> datosString;

  private boolean update;
  private View dialogView;
  private Dialog rowDialog;
  private LinearLayout rowDialogParentLinear;

  private GridAdapter adapter;
  private ModelObj oldModelObj;
  private int columns;

  private final String ACTION_VALUE_COLUMN = "ACTION_VALUE_COLUMN";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_grid);

    LayoutInflater inflater = getLayoutInflater();
    dialogView = inflater.inflate(R.layout.dialog_row, null);
    rowDialog = createDialog(dialogView);

    rowDialogParentLinear = (LinearLayout) dialogView.findViewById(R.id.linearRow);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      databaseName = extras.getString("databaseName");
      tableName = extras.getString("tableName");
    }

    dbmanager = new SQLiteManager(getApplicationContext(), databaseName);
    db = dbmanager.getWritableDatabase();

    grdOpciones = (GridView) findViewById(R.id.GridOpciones);

    columns = dbmanager.getTableColumnCount(db, tableName);

    hmColumnNameList = dbmanager.getTableColumnNames(db, tableName);

    grdOpciones.setNumColumns(columns + 1);


    datos = dbmanager.loadObjectList(db, tableName);


    settingsDialog(hmColumnNameList);

    if (datos.size() <= 0) {
      Toast.makeText(this, "Columnas sin contenido", Toast.LENGTH_SHORT).show();
    }
    datosString = new ArrayList<>();
    modelUsertoString();
    adapter = new GridAdapter(this, datosString, columns, dbmanager, tableName);
    grdOpciones.setAdapter(adapter);
  }

  private void settingsDialog(LinkedHashMap<String, String> columns) {
    hmEditTextColumn = new LinkedHashMap<>();

    Iterator iterator = columns.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();

      LinearLayout linearAux = new LinearLayout(this);
      linearAux.setOrientation(LinearLayout.HORIZONTAL);
      linearAux.setWeightSum(2);

      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.WRAP_CONTENT);
      params.weight = 1.0f;

      TextView tvColumn = new TextView(this);
      tvColumn.setLayoutParams(params);
      tvColumn.setHint(entry.getKey().toString());

      EditText etColumn = new EditText(this);
      etColumn.setLayoutParams(params);

      hmEditTextColumn.put(etColumn, entry.getValue().toString());

      linearAux.addView(tvColumn);
      linearAux.addView(etColumn);
      rowDialogParentLinear.addView(linearAux, params);
    }
  }

  private Dialog createDialog(View databaseView) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(databaseView)
        // Add action buttons
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            if (!update) {
              dbmanager.insertRowHm(db, hmEditTextColumn, tableName);
              updateHM();
            } else {
              update = false;
              ModelObj newModelObj = createModelObjWithHm();
              dbmanager.updateRowObj(db, tableName, oldModelObj, newModelObj);
              reloadData();
            }
            resetEditText();
            adapter.notifyDataSetChanged();
          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        resetEditText();
      }
    });
    return builder.create();
  }

  private ModelObj createModelObjWithHm() {
    ModelObj modelObjAux = new ModelObj();
    ArrayList<String> alAux = new ArrayList<>();
    EditText editTextAux;
    Iterator iterator = hmEditTextColumn.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      editTextAux = (EditText) entry.getKey();
      alAux.add(editTextAux.getText().toString().trim());
    }
    modelObjAux.setList(alAux);

    return modelObjAux;
  }

  private void updateHM() {

    Iterator iterator = hmEditTextColumn.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      EditText newDataItem = (EditText) entry.getKey();

      datosString.add(newDataItem.getText().toString().trim());
    }
    datosString.add(ACTION_VALUE_COLUMN);
  }

  private void modelUsertoString() {
    Iterator itColumn = hmColumnNameList.entrySet().iterator();

    ModelObj modelObjAux;
    ArrayList<String> listRowStrings;

    while (itColumn.hasNext()) {
      Map.Entry entry = (Map.Entry) itColumn.next();
      String columnName = (String) entry.getKey();
      datosString.add(columnName);
    }

    datosString.add("ACTION");

    Iterator itModelObj = datos.entrySet().iterator();

    while (itModelObj.hasNext()) {
      Map.Entry entry = (Map.Entry) itModelObj.next();
      modelObjAux = (ModelObj) entry.getValue();
      listRowStrings = modelObjAux.getList();
      for (String listRowString : listRowStrings) {
        datosString.add(listRowString);
      }
      datosString.add(ACTION_VALUE_COLUMN);
    }
  }

  public void insertRow(View view) {
    rowDialog.show();
  }

  //public class GridAdapter extends ArrayAdapter<String> implements View.OnClickListener {
  //
  //  int mNumberOfCols = 0;
  //
  //  public GridAdapter(Context context, ArrayList<String> listaItems, int numberOfColumns) {
  //    super(context, android.R.layout.simple_list_item_1, listaItems);
  //    mNumberOfCols = numberOfColumns;
  //  }
  //
  //  @Override public View getView(int position, View convertView, ViewGroup parent) {
  //    View view = convertView;
  //    LayoutInflater layoutInflater = null;
  //
  //    LinearLayout linearLayout;
  //
  //    layoutInflater = LayoutInflater.from(getContext());
  //
  //    String item = getItem(position);
  //
  //    if (item.equals(ACTION_VALUE_COLUMN)) {
  //      view = layoutInflater.inflate(R.layout.buttons_row_item, null); //edition
  //      ImageButton imbBtnUpdate = (ImageButton) view.findViewById(R.id.btnUpdateRow);
  //      ImageButton btnDeleteRow = (ImageButton) view.findViewById(R.id.btnDeleteRow);
  //      imbBtnUpdate.setTag(position / (mNumberOfCols + 1));
  //      btnDeleteRow.setTag(position / (mNumberOfCols + 1));
  //      btnDeleteRow.setOnClickListener(this);
  //      imbBtnUpdate.setOnClickListener(this);
  //    } else {
  //
  //      view = layoutInflater.inflate(R.layout.grid_row_header, null);
  //      linearLayout = (LinearLayout) view.findViewById(R.id.lytBackGround);
  //      TextView value = (TextView) view.findViewById(R.id.textGridRowItem);
  //      if (position <= mNumberOfCols) {
  //        //view = layoutInflater.inflate(R.layout.grid_row_header, null);//normal value of colum
  //
  //      } else {
  //        linearLayout.setBackgroundColor(0xffffffff);
  //        value.setTextColor(0xff000000);
  //        value.setTypeface(null, Typeface.NORMAL);
  //        //view = layoutInflater.inflate(android.R.layout.simple_list_item_1,null);//normal value of colum
  //
  //      }
  //      value.setText(item);
  //    }
  //    /**/
  //
  //    return view;
  //  }
  //
  //  @Override public void onClick(View v) {
  //
  //    if (v.getId() == R.id.btnDeleteRow) {
  //      Toast.makeText(GridActivity.this,
  //          "Posi-Delete: " + v.getTag() + "Action->" + v.getId(), Toast.LENGTH_LONG).show();
  //      int rowid = getId((int) v.getTag());
  //      int id = Integer.parseInt(datosString.get(rowid));
  //
  //      dbmanager.deleteRowFromTable(db, tableName, id);
  //      for (int i = 0; i < (mNumberOfCols + 1); i++) {
  //        datosString.remove(rowid);
  //      }
  //      adapter.notifyDataSetChanged();
  //    } else if (v.getId() == R.id.btnUpdateRow) {
  //      int rowid = getId((int) v.getTag());
  //      Toast.makeText(GridActivity.this,
  //          "Pos-Update: " + v.getTag() + "Action->" + v.getId(), Toast.LENGTH_LONG).show();
  //      settingDialogwithUpdate(rowid);
  //      update = true;
  //      rowDialog.show();
  //    }
  //  }
  //
  //  public int getId(int tagId) {
  //    int positionIdByTag = tagId * (mNumberOfCols + 1);
  //    return positionIdByTag;
  //  }
  //}

  public void settingDialogwithUpdate(int rowid) {
    Iterator iterator = hmEditTextColumn.entrySet().iterator();
    EditText editTextAux;
    oldModelObj = new ModelObj();
    ArrayList<String> modelRow = new ArrayList<>();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      editTextAux = (EditText) entry.getKey();
      editTextAux.setText(datosString.get(rowid));
      modelRow.add(datosString.get(rowid));
      rowid++;
    }
    oldModelObj.setList(modelRow);
    update = true;
    rowDialog.show();
  }

  public void resetEditText() {
    Iterator iterator = hmEditTextColumn.entrySet().iterator();
    EditText editTextAux;

    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      editTextAux = (EditText) entry.getKey();
      editTextAux.setHint("");
      editTextAux.setText("");
    }
  }

  public void reloadData() {

    LinkedHashMap<Integer, ModelObj> integerModelObjLinkedHashMap = new LinkedHashMap<>();
    integerModelObjLinkedHashMap = dbmanager.loadObjectList(db, tableName);
    datos = integerModelObjLinkedHashMap;
    datosString.clear();
    modelUsertoString();
  }
}