package com.gigigo.gigigocrud_sqliteandroid.Objects;

import android.widget.EditText;

/**
 * Created by pablo.rojas on 31/3/17.
 */

public class EditTextRow {

  private String editTextName;
  private String editTextType;

  public String getEditTextName() {
    return editTextName;
  }

  public void setEditTextName(String editTextName) {
    this.editTextName = editTextName;
  }

  public String getEditTextType() {
    return editTextType;
  }

  public void setEditTextType(String editTextType) {
    this.editTextType = editTextType;
  }

  public EditTextRow(String editTextName, String editTextType) {
    this.editTextName = editTextName;
    this.editTextType = editTextType;
  }
}
