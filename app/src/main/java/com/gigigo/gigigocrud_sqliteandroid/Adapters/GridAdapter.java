package com.gigigo.gigigocrud_sqliteandroid.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gigigo.gigigocrud_sqliteandroid.GridActivity;
import com.gigigo.gigigocrud_sqliteandroid.Manager.SQLiteManager;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ModelObj;
import com.gigigo.gigigocrud_sqliteandroid.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pablo.rojas on 6/4/17.
 */


public class GridAdapter extends ArrayAdapter<String> implements View.OnClickListener {

  private final String ACTION_VALUE_COLUMN = "ACTION_VALUE_COLUMN";
  int mNumberOfCols = 0;
  private ArrayList<String> listaItems;
  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private String tablename;
  private GridActivity gridActivity;

  public GridAdapter(GridActivity context, ArrayList<String> listaItems, int numberOfColumns,
      SQLiteManager dbmanager, String tablename) {
    super(context, android.R.layout.simple_list_item_1, listaItems);
    this.gridActivity = context;
    this.mNumberOfCols = numberOfColumns;
    this.listaItems = listaItems;
    this.dbmanager = dbmanager;
    this.db = dbmanager.getWritableDatabase();
    this.tablename = tablename;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    LayoutInflater layoutInflater = null;

    LinearLayout linearLayout;

    layoutInflater = LayoutInflater.from(getContext());

    String item = getItem(position);

    if (item.equals(ACTION_VALUE_COLUMN)) {
      view = layoutInflater.inflate(R.layout.buttons_row_item, null); //edition
      ImageButton imbBtnUpdate = (ImageButton) view.findViewById(R.id.btnUpdateRow);
      ImageButton btnDeleteRow = (ImageButton) view.findViewById(R.id.btnDeleteRow);
      imbBtnUpdate.setTag(position / (mNumberOfCols + 1));
      btnDeleteRow.setTag(position / (mNumberOfCols + 1));
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

    if (v.getId() == R.id.btnDeleteRow) {
      Toast.makeText(getContext(),
          "Posi-Delete: " + v.getTag() + "Action->" + v.getId(), Toast.LENGTH_LONG).show();
      int rowid = getId((int) v.getTag());
      int id = Integer.parseInt(listaItems.get(rowid));

      dbmanager.deleteRowFromTable(db, tablename, id);
      for (int i = 0; i < (mNumberOfCols + 1); i++) {
        listaItems.remove(rowid);
      }
      this.notifyDataSetChanged();
    } else if (v.getId() == R.id.btnUpdateRow) {
      int rowid = getId((int) v.getTag());
      Toast.makeText(getContext(),
          "Pos-Update: " + v.getTag() + "Action->" + v.getId(), Toast.LENGTH_LONG).show();
      gridActivity.settingDialogwithUpdate(rowid);

    }
  }

  public int getId(int tagId) {
    int positionIdByTag = tagId * (mNumberOfCols + 1);
    return positionIdByTag;
  }

  public void clearListContent() {
    listaItems.clear();
    listaItems.addAll(gridActivity.getColumnList());
    this.notifyDataSetChanged();
  }
}