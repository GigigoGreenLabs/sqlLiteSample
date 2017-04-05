package com.gigigo.gigigocrud_sqliteandroid.Objects;

import java.util.ArrayList;

/**
 * Created by pablo.rojas on 5/4/17.
 */

public class ModelObj {

  private ArrayList<String> list;


  public ArrayList<String> getList() {
    return list;
  }

  public void setList(ArrayList<String> list) {
    this.list = list;
  }

  public ModelObj() {
    list = new ArrayList<>();

  }
}
