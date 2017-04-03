package com.gigigo.gigigocrud_sqliteandroid.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ItemColumnAdapter;
import com.gigigo.gigigocrud_sqliteandroid.R;
import java.util.List;

/**
 * Created by pablo.rojas on 3/4/17.
 */

public class ColumnAdapter extends ArrayAdapter<ItemColumnAdapter> {

  private int layoutResource;

  public ColumnAdapter(Context context, int layoutResource,
      List<ItemColumnAdapter> listaItems) {
    super(context, layoutResource, listaItems);
    this.layoutResource = layoutResource;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    View view = convertView;

    if (view == null) {
      LayoutInflater layoutInflater = LayoutInflater.from(getContext());
      view = layoutInflater.inflate(layoutResource, null);
    }

    ItemColumnAdapter item = getItem(position);

    if (item != null) {
      TextView name = (TextView) view.findViewById(R.id.textViewName);
      TextView type = (TextView) view.findViewById(R.id.textViewType);

      if (name != null) {
        name.setText(item.getName());
      }

      if (type != null) {
        type.setText(item.getType());
      }
    }

    return view;
  }
}