package com.gigigo.gigigocrud_sqliteandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pablo.rojas on 29/3/17.
 */

public class SQLiteManager extends SQLiteOpenHelper {

  private static int version = 1;
  private static SQLiteDatabase.CursorFactory factory = null;
  private String databaseName;
  private String tableName;
  String sqlCreate = "CREATE TABLE ";



  public SQLiteManager(Context context, String name) {
    super(context, name, factory, version);
    this.databaseName = name;
  }

  @Override public void onCreate(SQLiteDatabase db) {

  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


  }

  public void createTable(SQLiteDatabase db) {
    sqlCreate = sqlCreate.substring(0, sqlCreate.length() - 1) + ");";
    Log.v("-----------", "sql" + sqlCreate);
    db.execSQL("DROP TABLE IF EXISTS " + tableName);
    db.execSQL(sqlCreate);
  }

  public void insertColumn(String name, String type) {
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
    String updateStr = "UPDATE "+tableName+" set name='"
        + newItem.nombre
        + "' , age="
        + newItem.edad
        + " where id="
        + oldItem.id;
    db.execSQL(updateStr);
  }

  public ArrayList<ModelUser> load(SQLiteDatabase db) {
    ArrayList<ModelUser> userList  = new ArrayList<ModelUser>();
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
    String deleteStr = "DELETE FROM "+tableName+" WHERE id="+user.id+" ";
    db.execSQL(deleteStr);
  }

  public ArrayList<String> getTableList(SQLiteDatabase db) {
    ArrayList<String> tableNameList = new ArrayList<String>();
    String tableListStr = "SELECT name FROM sqlite_master;";
    Cursor cursor = db.rawQuery(tableListStr, null);
    cursor.moveToFirst(); // android_metadata
    cursor.moveToNext();  // sqlite_sequence
    while (cursor.moveToNext()){
      String tableName = cursor.getString(0);
      tableNameList.add(tableName);
    }
    return tableNameList;
  }

  public boolean checkIfTableExist(SQLiteDatabase db, String tablename) {
    Log.v("TABLEEXIST",""+ tablename);
    boolean tableExists = false;
    String tableListStr = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+tablename+"';";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()){
      tableExists = true;
    }
    return tableExists;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
    sqlCreate += tableName + "(";
  }

  public void dropTable(SQLiteDatabase db, String tableName) {
    String dropTableStr = "DROP TABLE IF EXISTS "+tableName +" ";
    db.execSQL(dropTableStr);
  }

  public void clearTableContent(SQLiteDatabase db, String tableName){
    db.delete(tableName, null, null);
    db.execSQL("DELETE FROM " + tableName);
  }

  public ArrayList<ModelUser> loadByName(SQLiteDatabase db, String userName){
    ArrayList<ModelUser> userList = new ArrayList<>();
    ModelUser userAux = new ModelUser();
    String selectByNameStr = "SELECT * FROM "+tableName+" WHERE name='"+userName+"' ";
    Cursor userRow = db.rawQuery(selectByNameStr, null);
    if (userRow.moveToFirst()){
      userAux.setId(Integer.parseInt(userRow.getString(0)));
      userAux.setNombre(userRow.getString(1));
      userAux.setEdad(Integer.parseInt(userRow.getString(2)));
      userAux.setDatetime(userRow.getString(3));
      Log.v("LOADNAME",""+ userRow.getString(0)+ " " + userRow.getString(1));
      userList.add(userAux);
      while(userRow.moveToNext()){
        userAux = new ModelUser();
        userAux.setId(Integer.parseInt(userRow.getString(0)));
        userAux.setNombre(userRow.getString(1));
        userAux.setEdad(Integer.parseInt(userRow.getString(2)));
        userAux.setDatetime(userRow.getString(3));
        Log.v("LOADNAME",""+ userRow.getString(0)+ " " + userRow.getString(1));
        userList.add(userAux);
      }
    }
    return userList;
  }

  public ArrayList<ModelUser> loadLikeName(SQLiteDatabase db, String userName){
    ArrayList<ModelUser> userList = new ArrayList<>();
    ModelUser userAux = null;
    String selectByNameStr = "SELECT * FROM "+tableName+" WHERE name like'%"+userName+"%' ";
    Cursor userRow = db.rawQuery(selectByNameStr, null);
    if (userRow.moveToFirst()){
      userAux = new ModelUser();
      userAux.setId(Integer.parseInt(userRow.getString(0)));
      userAux.setNombre(userRow.getString(1));
      userAux.setEdad(Integer.parseInt(userRow.getString(2)));
      userAux.setDatetime(userRow.getString(3));
      userList.add(userAux);

      while(userRow.moveToNext()){
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

  public void deleteDatabase(Context context, String dbusers) {
    context.deleteDatabase(dbusers);
  }

}