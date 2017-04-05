package com.gigigo.gigigocrud_sqliteandroid.Objects;

/**
 * Created by pablo.rojas on 3/4/17.
 */

public class ItemColumn {

  String name;
  String type;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ItemColumn(String name, String type) {
    this.name = name;
    this.type = type;

  }
}
