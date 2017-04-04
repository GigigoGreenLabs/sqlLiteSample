package com.gigigo.gigigocrud_sqliteandroid;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.gigigo.gigigocrud_sqliteandroid.Objects.ModelUser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataContentGridActivity extends AppCompatActivity {


  private GridView grdOpciones;

  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private String databaseName;
  private String tableName;

  private ArrayList<ModelUser> datos;
  private LinkedHashMap<String, String> hmColumnNameList;
  private LinkedHashMap<EditText, String> hmEditTextColumn;
  private String[] datosString;

  private View dialogView;
  private Dialog rowDialog;
  private LinearLayout rowDialogParentLinear;

  private final String ACTION_VALUE_COLUMN = "ACTION_VALUE_COLUMN";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_data_content_grid);

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

    int columns = dbmanager.getTableColumnCount(db, tableName);

    hmColumnNameList = dbmanager.getTableColumnNames(db, tableName);

    grdOpciones.setNumColumns(columns + 1);

    datos = dbmanager.load(db, tableName);

    settingsDialog(hmColumnNameList);

    if (datos.size() > 0) {
      modelUsertoString(columns);

      GridAdapter adapter = new GridAdapter(this, datosString, columns);

      grdOpciones.setAdapter(adapter);
    } else {
      Toast.makeText(this, "Columnas sin contenido", Toast.LENGTH_SHORT).show();
    }
  }

  private void settingsDialog(LinkedHashMap<String, String> columns) {
    hmEditTextColumn = new LinkedHashMap<>();

    Iterator iterator = columns.entrySet().iterator();

    while (iterator.hasNext()){
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

      hmEditTextColumn.put(etColumn,entry.getValue().toString());

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
            dbmanager.insertRowHm(db,hmEditTextColumn, tableName);


          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {

      }
    });
    return builder.create();
  }



  private void modelUsertoString(int columns) {

    //datosString = new String[(datos.size() * columns) + columns];

    int i = 0 + columns + 1;

    datosString = new String[(datos.size() * i) + i];

    Iterator iterator = hmColumnNameList.entrySet().iterator();

    int j = 0;
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      datosString[j] = (String) entry.getKey();
      j++;
    }
    datosString[j] = "ACTION";

    for (ModelUser dato : datos) {
      datosString[i] = Integer.toString(dato.getId());
      i++;
      datosString[i] = dato.getNombre();
      i++;
      datosString[i] = Integer.toString(dato.getEdad());
      i++;
      datosString[i] = dato.getDatetime();
      i++;
      datosString[i] = ACTION_VALUE_COLUMN;
      i++;
    }
  }

  public void insertRow(View view) {
    rowDialog.show();
  }

  public void deleteRow(View view) {

  }

  public void updateRow(View view) {

  }

  public class GridAdapter extends ArrayAdapter<String> implements View.OnClickListener {

    int mNumberOfCols = 0;

    public GridAdapter(Context context, String[] listaItems, int numberOfColumns) {
      super(context, android.R.layout.simple_list_item_1, listaItems);
      mNumberOfCols = numberOfColumns;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      LayoutInflater layoutInflater = null;

      LinearLayout linearLayout;


      layoutInflater = LayoutInflater.from(getContext());

      String item = getItem(position);

      if (item.equals(ACTION_VALUE_COLUMN)) {
        view = layoutInflater.inflate(R.layout.grid_row_item, null); //edition
        ImageButton imbBtnUpdate = (ImageButton) view.findViewById(R.id.btnUpdateRow);
        ImageButton btnDeleteRow = (ImageButton) view.findViewById(R.id.btnDeleteRow);
        imbBtnUpdate.setTag(position / (mNumberOfCols+1));
        btnDeleteRow.setTag(position / (mNumberOfCols+1));
        btnDeleteRow.setOnClickListener(this);
        imbBtnUpdate.setOnClickListener(this);
      } else {

        view = layoutInflater.inflate(R.layout.grid_row_header, null);
        linearLayout = (LinearLayout) view.findViewById(R.id.lytBackGround);
        TextView value = (TextView) view.findViewById(R.id.textGridRowItem);
        if (position <= mNumberOfCols) {
          //view = layoutInflater.inflate(R.layout.grid_row_header, null);//normal value of colum

        } else {
          linearLayout.setBackgroundColor(0xffffffff);
          value.setTextColor(0xff000000);
          value.setTypeface(null, Typeface.NORMAL);
          //view = layoutInflater.inflate(android.R.layout.simple_list_item_1,null);//normal value of colum

        }
        value.setText(item);
      }
      /**/

      return view;
    }

    @Override public void onClick(View v) {

      Toast.makeText(DataContentGridActivity.this,
          "Position: " + v.getTag() + "Action->" + v.getId(), Toast.LENGTH_LONG).show();
    }
  }
}