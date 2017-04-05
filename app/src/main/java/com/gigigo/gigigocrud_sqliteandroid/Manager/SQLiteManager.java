package com.gigigo.gigigocrud_sqliteandroid.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.widget.EditText;
import android.widget.Toast;
import com.gigigo.gigigocrud_sqliteandroid.Objects.ModelObj;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pablo.rojas on 29/3/17.
 */

public class SQLiteManager extends SQLiteOpenHelper {

  private static int version = 1;
  private static SQLiteDatabase.CursorFactory factory = null;
  private String databaseName;
  private String tableName;
  private String sqlCreate = "CREATE TABLE ";
  private boolean createTableWithColumns;
  private Context context;

  public SQLiteManager(Context context, String name) {
    super(context, name, factory, version);
    this.databaseName = name;
    this.context = context;
  }

  @Override public void onCreate(SQLiteDatabase db) {

  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void createTable(SQLiteDatabase db) {
    if (createTableWithColumns) {
      sqlCreate = sqlCreate.substring(0, sqlCreate.length() - 1) + ");";
    } else {
      sqlCreate = sqlCreate + "id INTEGER PRIMARY KEY AUTOINCREMENT);";
    }
    Log.v("-----------", "sql" + sqlCreate);
    db.execSQL("DROP TABLE IF EXISTS " + tableName);
    db.execSQL(sqlCreate);
  }

  public void insertColumn(String name, String type) {
    createTableWithColumns = true;
    sqlCreate += name + " " + type + ",";
  }


  public void getClassNames() {
    Class<ModelObj> clazz = ModelObj.class;
    for (final java.lang.reflect.Field field : clazz.getDeclaredFields()) {
      System.out.println("----------" + field.getName());
    }
  }



  public LinkedHashMap<Integer, ModelObj> loadObjectList(SQLiteDatabase db, String tableName) {

    LinkedHashMap<Integer, ModelObj> rowUser = new LinkedHashMap<>();
    int id = 1;
    ArrayList<String> userList;
    int columns = getTableColumnCount(db, tableName);

    ModelObj obj;

    Cursor cursorDB = db.rawQuery("SELECT * from " + tableName + "", null);
    if (cursorDB.moveToFirst()) {
      userList = new ArrayList<String>();
      for (int i = 0; i < columns; i++) {
        userList.add(cursorDB.getString(i));
      }
      obj = new ModelObj();
      obj.setList(userList);
      rowUser.put(id, obj);
      id++;

      while (cursorDB.moveToNext()) {
        userList = new ArrayList<String>();
        for (int i = 0; i < columns; i++) {
          userList.add(cursorDB.getString(i));
        }
        obj = new ModelObj();
        obj.setList(userList);
        rowUser.put(id, obj);
        id++;
      }
    }
    return rowUser;
  }


  public ArrayList<String> getTableList(SQLiteDatabase db) {
    ArrayList<String> tableNameList = new ArrayList<String>();
    String tableListStr = "SELECT name FROM sqlite_master;";
    Cursor cursor = db.rawQuery(tableListStr, null);
    cursor.moveToFirst(); // android_metadata
    while (cursor.moveToNext()) {
      String tableName = cursor.getString(0);
        if (!tableName.equals("sqlite_sequence")){
            tableNameList.add(tableName);
        }

    }
    return tableNameList;
  }

  public boolean checkIfTableExist(SQLiteDatabase db, String tablename) {
    Log.v("TABLEEXIST", "" + tablename);
    boolean tableExists = false;
    String tableListStr =
        "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tablename + "';";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      tableExists = true;
    }
    return tableExists;
  }

  public LinkedHashMap<String, String> getTableColumnNames(SQLiteDatabase db, String tablename) {

    LinkedHashMap<String, String> columnNamesList = new LinkedHashMap<String, String>();
    String tableListStr = "PRAGMA table_info('" + tablename + "');";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      columnNamesList.put(cursor.getString(1), cursor.getString(2));
      //Log.v("Columns",""+ cursor.getString(0)+cursor.getString(1)+ cursor.getString(2));
      while (cursor.moveToNext()) {
        columnNamesList.put(cursor.getString(1), cursor.getString(2));
        Log.v("Columns", "" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));
      }
    }
    return columnNamesList;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
    sqlCreate += tableName + "(";
  }

  public void dropTable(SQLiteDatabase db, String tableName) {
    String dropTableStr = "DROP TABLE IF EXISTS " + tableName + " ";
    db.execSQL(dropTableStr);
  }

  public void clearTableContent(SQLiteDatabase db, String tableName) {
    db.delete(tableName, null, null);
    db.execSQL("DELETE FROM " + tableName);
  }




  public boolean checkIfTableHasContent(SQLiteDatabase db, String tableName) {
    boolean hasContent = false;
    String count = "SELECT count(*) FROM '" + tableName + "'";
    Cursor mcursor = db.rawQuery(count, null);
    mcursor.moveToFirst();
    int icount = mcursor.getInt(0);
    if (icount > 0) {
      hasContent = true;
    }
    return hasContent;
  }

  public void dropDatabase(Context context, String dbname) {
    context.deleteDatabase(dbname);
  }

  public boolean checkIfDatabaseExists(Context context, String dbName) {
    File dbFile = context.getDatabasePath(dbName);
    return dbFile.exists();
  }

  public void setCreateTableWithColumns(boolean createTableWithColumns) {
    this.createTableWithColumns = createTableWithColumns;
  }

  public ArrayList<String> getDatabaseNameList(SQLiteDatabase dbAux) {
    ArrayList<String> dbList = new ArrayList<>();

    String count = ".databases";
    Cursor mcursor = dbAux.rawQuery(count, null);
    if (mcursor.moveToFirst()) {
      Log.v("DATABASE", "" + mcursor.getString(0));
      dbList.add(mcursor.getString(0));
      while (mcursor.moveToNext()) {
        Log.v("DATABASE", "" + mcursor.getString(0));
        dbList.add(mcursor.getString(0));
      }
    }

    return dbList;
  }

  public int getTableColumnCount(SQLiteDatabase db, String tableName) {
    int columns = 0;
    String tableListStr = "PRAGMA table_info('" + tableName + "');";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      columns++;
      while (cursor.moveToNext()) {
        columns++;
      }
    }
    return columns;
  }

  public void insertColumnType(SQLiteDatabase db, String tableName, String columnName,
      String columnType) {
    String query =
        "ALTER TABLE '" + tableName + "' ADD COLUMN '" + columnName + "' '" + columnType + "'";
    db.execSQL(query);
  }

  public void insertRowHm(SQLiteDatabase db, LinkedHashMap<EditText, String> hmEditTextColumn,
      String tableName) {

    String query = "INSERT INTO '" + tableName + "' VALUES (";

    EditText editTextAux;
    Iterator iterator = hmEditTextColumn.entrySet().iterator();
    boolean id = true;
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      String type = (String) entry.getValue();
      editTextAux = (EditText) entry.getKey();

      if (type.equals("INTEGER") && id && editTextAux.getText().toString().trim().equals("")) {
        query = query + "NULL" + ",";
        id = false;
      } else if (type.equals("INTEGER")) {
        query = query + editTextAux.getText().toString().trim() + ",";
      } else if (type.equals("TEXT")) {
        query = query + "'" + editTextAux.getText().toString().trim() + "',";
      } else if (type.equals("DATETIME")) {
        query = query + "CURRENT_TIMESTAMP ,";
      }
    }
    query = query.substring(0, query.length() - 1) + ");";

    try {
      db.execSQL(query);
    } catch (SQLiteConstraintException e) {
      Toast.makeText(context, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }
  }

  public void deleteRowFromTable(SQLiteDatabase db, String tableName, Object tag) {
    String deleteStr = "DELETE FROM " + tableName + " WHERE id=" + tag + " ";
    db.execSQL(deleteStr);
  }

  public void updateRowObj(SQLiteDatabase db, String tableName, ModelObj oldModelObj,
      ModelObj newModelObj) {

    ArrayList<String> alNewModel = newModelObj.getList();
    ArrayList<String> alOldModel = oldModelObj.getList();

    String updateStr = "UPDATE " + tableName + " set ";

    LinkedHashMap<String, String> columnNameType = getTableColumnNames(db, tableName);
    Iterator iterator = columnNameType.entrySet().iterator();
    int i = 1;
    iterator.next();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      String name = (String) entry.getKey();
      String type = (String) entry.getValue();

      updateStr = updateStr + name + "=";
      if (type.equals("TEXT")) {
        updateStr = updateStr + "'" + alNewModel.get(i) + "' ,";
      } else if (type.equals("DATETIME")) {
        updateStr = updateStr + " CURRENT_TIMESTAMP ,";
      } else {
        updateStr = updateStr + alNewModel.get(i) + " ,";
      }

      i++;
    } updateStr = updateStr.substring(0, updateStr.length() - 1);
    updateStr = updateStr + "WHERE id=" + alOldModel.get(0);
    Log.v("QUERY", "" + updateStr);
    db.execSQL(updateStr);
  }
}