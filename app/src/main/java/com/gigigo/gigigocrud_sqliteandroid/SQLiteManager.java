package com.gigigo.gigigocrud_sqliteandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gigigo.gigigocrud_sqliteandroid.Objects.ModelUser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pablo.rojas on 29/3/17.
 */

public class SQLiteManager extends SQLiteOpenHelper {

  private static int version = 1;
  private static SQLiteDatabase.CursorFactory factory = null;
  private String databaseName;
  private String tableName;
  String sqlCreate = "CREATE TABLE ";
  private boolean createTableWithColumns;

  public SQLiteManager(Context context, String name) {
    super(context, name, factory, version);
    this.databaseName = name;
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

  public void insertRow(SQLiteDatabase db, ModelUser item) {
    String insertSQL = "INSERT INTO "
        + tableName
        + "(name, age, date) VALUES('"
        + item.nombre
        + "',"
        + item.edad
        + ", "
        + item.datetime
        + ")";
    db.execSQL(insertSQL);

    Cursor cursorDB =
        db.rawQuery("SELECT Id from " + tableName + " WHERE name ='" + item.nombre + "'", null);
    if (cursorDB.moveToFirst()) {
      String id = cursorDB.getString(0);
      item.setId(Integer.parseInt(id));
    }
    Log.v("ID", "-------" + item.id);
  }

  public void getClassNames() {
    Class<ModelUser> clazz = ModelUser.class;
    for (final java.lang.reflect.Field field : clazz.getDeclaredFields()) {
      System.out.println("----------" + field.getName());
    }
  }

  public void updateRow(SQLiteDatabase db, ModelUser oldItem, ModelUser newItem) {
    String updateStr = "UPDATE "
        + tableName
        + " set name='"
        + newItem.nombre
        + "' , age="
        + newItem.edad
        + " where id="
        + oldItem.id;
    db.execSQL(updateStr);
  }

  public ArrayList<ModelUser> load(SQLiteDatabase db, String tableName) {
    ArrayList<ModelUser> userList = new ArrayList<ModelUser>();
    ModelUser userAux = new ModelUser();
    Cursor cursorDB = db.rawQuery("SELECT * from " + tableName + "", null);
    if (cursorDB.moveToFirst()) {
      userAux.setId(Integer.parseInt(cursorDB.getString(0)));
      userAux.setNombre(cursorDB.getString(1));
      userAux.setEdad(Integer.parseInt(cursorDB.getString(2)));
      userAux.setDatetime(cursorDB.getString(3));
      userList.add(userAux);
      while (cursorDB.moveToNext()) {
        userAux = new ModelUser();
        userAux.setId(Integer.parseInt(cursorDB.getString(0)));
        userAux.setNombre(cursorDB.getString(1));
        userAux.setEdad(Integer.parseInt(cursorDB.getString(2)));
        userAux.setDatetime(cursorDB.getString(3));
        userList.add(userAux);
      }
    }
    return userList;
  }

  public void deleteRow(SQLiteDatabase db, ModelUser user) {
    String deleteStr = "DELETE FROM " + tableName + " WHERE id=" + user.id + " ";
    db.execSQL(deleteStr);
  }

  public ArrayList<String> getTableList(SQLiteDatabase db) {
    ArrayList<String> tableNameList = new ArrayList<String>();
    String tableListStr = "SELECT name FROM sqlite_master;";
    Cursor cursor = db.rawQuery(tableListStr, null);
    cursor.moveToFirst(); // android_metadata
    cursor.moveToNext();  // sqlite_sequence
    while (cursor.moveToNext()) {
      String tableName = cursor.getString(0);
      tableNameList.add(tableName);
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

  public HashMap<String, String> getTableColumnNames(SQLiteDatabase db, String tablename) {

    HashMap<String, String>  columnNamesList = new HashMap<String, String>();
    String tableListStr = "PRAGMA table_info('" + tablename + "');";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      columnNamesList.put(cursor.getString(1), cursor.getString(2));
      //Log.v("Columns",""+ cursor.getString(0)+cursor.getString(1)+ cursor.getString(2));
      while (cursor.moveToNext()) {
        columnNamesList.put(cursor.getString(1), cursor.getString(2));
        Log.v("Columns",""+ cursor.getString(0)+cursor.getString(1)+ cursor.getString(2));
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

  public ArrayList<ModelUser> loadByName(SQLiteDatabase db, String userName) {
    ArrayList<ModelUser> userList = new ArrayList<>();
    ModelUser userAux = new ModelUser();
    String selectByNameStr = "SELECT * FROM " + tableName + " WHERE name='" + userName + "' ";
    Cursor userRow = db.rawQuery(selectByNameStr, null);
    if (userRow.moveToFirst()) {
      userAux.setId(Integer.parseInt(userRow.getString(0)));
      userAux.setNombre(userRow.getString(1));
      userAux.setEdad(Integer.parseInt(userRow.getString(2)));
      userAux.setDatetime(userRow.getString(3));
      Log.v("LOADNAME", "" + userRow.getString(0) + " " + userRow.getString(1));
      userList.add(userAux);
      while (userRow.moveToNext()) {
        userAux = new ModelUser();
        userAux.setId(Integer.parseInt(userRow.getString(0)));
        userAux.setNombre(userRow.getString(1));
        userAux.setEdad(Integer.parseInt(userRow.getString(2)));
        userAux.setDatetime(userRow.getString(3));
        Log.v("LOADNAME", "" + userRow.getString(0) + " " + userRow.getString(1));
        userList.add(userAux);
      }
    }
    return userList;
  }

  public ArrayList<ModelUser> loadLikeName(SQLiteDatabase db, String userName) {
    ArrayList<ModelUser> userList = new ArrayList<>();
    ModelUser userAux = null;
    String selectByNameStr = "SELECT * FROM " + tableName + " WHERE name like'%" + userName + "%' ";
    Cursor userRow = db.rawQuery(selectByNameStr, null);
    if (userRow.moveToFirst()) {
      userAux = new ModelUser();
      userAux.setId(Integer.parseInt(userRow.getString(0)));
      userAux.setNombre(userRow.getString(1));
      userAux.setEdad(Integer.parseInt(userRow.getString(2)));
      userAux.setDatetime(userRow.getString(3));
      userList.add(userAux);

      while (userRow.moveToNext()) {
        userAux = new ModelUser();
        userAux.setId(Integer.parseInt(userRow.getString(0)));
        userAux.setNombre(userRow.getString(1));
        userAux.setEdad(Integer.parseInt(userRow.getString(2)));
        userAux.setDatetime(userRow.getString(3));
        userList.add(userAux);
      }
    }
    return userList;
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
   if (mcursor.moveToFirst()){
      Log.v("DATABASE",""+mcursor.getString(0));
     dbList.add(mcursor.getString(0));
     while (mcursor.moveToNext()){
       Log.v("DATABASE",""+mcursor.getString(0));
       dbList.add(mcursor.getString(0));
     }
   }

    return dbList;
  }
}