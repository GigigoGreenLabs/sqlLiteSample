package com.gigigo.gigigocrud_sqliteandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  private ArrayList<ModelUser> userList;
  private ArrayList<String> tableList;
  private boolean tableExists;
  private LinearLayout linearLayout;
  private int editTextId;
  private HashMap<Integer, EditText> hashMapEditText;
  private View view;

  private Button btnCreateDatabase;
  private Button btnExistsDatabase;
  private Button btnDropDatabase;
  private Button btnCreateTable;
  private Button btnDropTable;
  private Button btnClearTable;
  private Button btnInsertItem;
  private Button btnUpdateItem;
  private Button btnDeleteItem;
  private Button btnSelectItemsFromTable;
  private Button btnFindItemByName;
  private Button btnFindItemLikeName;
  private Button btnAddRowEditText;

  private EditText editTextDatabase;
  private EditText editTextTable;
  private EditText editTextType;
  private EditText editTextName;

  private  SQLiteManager dbmanager;
  private SQLiteDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LayoutInflater inflater = getLayoutInflater();
    view = inflater.inflate(R.layout.dialog_settings, null);
    settings();
    settingsFromDialogView(view);


    final Dialog dialog = onCreateDialog(view);

    btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.show();


      }
    });

    btnInsertItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.v("-----","insert");
        //String test = "data";
        //dbmanager.insertRow(db, user);
      }
    });




    test();



  }

  private void test() {

    ModelUser user = new ModelUser("pepe", 20);
    ModelUser user1 = new ModelUser("pepe2", 20);
    ModelUser user2 = new ModelUser("pepe", 20);
    ModelUser user3 = new ModelUser("ana", 30);
    ModelUser user4 = new ModelUser("luis", 40);


    userList = new ArrayList<ModelUser>();
    dbmanager = new SQLiteManager(this, "DBUSERS");
    db = dbmanager.getWritableDatabase();

    dbmanager.setTableName("USERS");
    dbmanager.insertColumn("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
    dbmanager.insertColumn("name", "TEXT");
    dbmanager.insertColumn("age", "INTEGER");
    dbmanager.insertColumn("date", "DATETIME DEFAULT CURRENT_TIMESTAMP");
    dbmanager.createTable(db);

    dbmanager.insertRow(db, user);
    dbmanager.insertRow(db, user1);
    dbmanager.insertRow(db, user2);
    dbmanager.insertRow(db, user3);
    dbmanager.insertRow(db, user4);

    userList = dbmanager.load(db);
    printListUsers(userList);

    ModelUser updatedUser = new ModelUser("maria", 30);
    dbmanager.updateRow(db, user3, updatedUser);

    userList = dbmanager.load(db);
    printListUsers(userList);

    dbmanager.deleteRow(db, user3);

    userList = dbmanager.load(db);
    printListUsers(userList);

    tableList = dbmanager.getTableList(db);
    printListTables(tableList);

    tableExists = dbmanager.checkIfTableExist(db, "USERS");
    Log.v("TABLEEXISTS", "" + tableExists);


    tableExists = dbmanager.checkIfTableExist(db, "FAIL");
    Log.v("TABLEEXISTS", "" + tableExists);


    ArrayList<ModelUser> userList2 = dbmanager.loadByName(db, "pepe");
    printListUsers(userList2);

    ArrayList<ModelUser> userList3 = dbmanager.loadLikeName(db, "ep");
    printListUsers(userList3);


    dbmanager.clearTableContent(db, "USERS");
    userList = dbmanager.load(db);
    printListUsers(userList);

    dbmanager.dropTable(db, "USERS");
    tableExists = dbmanager.checkIfTableExist(db, "USERS");
    Log.v("TABLEEXISTS", "" + tableExists);

    //dbmanager.deleteDatabase(this, "DBUSERS");
    Log.v("", "" + this.databaseList());

  }

  private void createDatabase(String database, String table, String name, String type) {
    dbmanager = new SQLiteManager(this, database);
    db = dbmanager.getWritableDatabase();
    dbmanager.setTableName(table);
    dbmanager.insertColumn(name, type);
    dbmanager.createTable(db);
  }

  private void settingsFromDialogView(View view) {
    editTextDatabase = (EditText) view.findViewById(R.id.editTextDatabase);
    editTextTable = (EditText) view.findViewById(R.id.editTextTable);
    editTextName = (EditText) view.findViewById(R.id.editTextName);
    editTextType = (EditText) view.findViewById(R.id.editTextType);
    btnAddRowEditText = (Button) view.findViewById(R.id.btnAddRowEditText);
  }


  public void addEditText(View view) {

    LinearLayout linearAux = new LinearLayout(this);
    linearAux.setOrientation(LinearLayout.HORIZONTAL);
    linearAux.setWeightSum(2);


    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    params.weight = 1.0f;


    EditText etName = new EditText(this);
    etName.setLayoutParams(params);
    etName.setId(editTextId);
    etName.setHint("NAME");

    hashMapEditText.put(editTextId, etName);

    editTextId++;


    EditText etType = new EditText(this);
    etType.setLayoutParams(params);
    etType.setId(editTextId);
    etType.setHint("TYPE");


    hashMapEditText.put(editTextId, etType);


    editTextId++;
    Log.v("---------------", "" + etName.getId() + etType.getId());

    linearAux.addView(etName);
    linearAux.addView(etType);
    linearLayout.addView(linearAux, params);


    Iterator iterator = hashMapEditText.entrySet().iterator();


    while (iterator.hasNext()) {
      Map.Entry thisEntry = (Map.Entry) iterator.next();
      int key = (int) thisEntry.getKey();
      EditText value = (EditText) thisEntry.getValue();
      Log.v("HASHMAP", "--------" + key + " " + value);
    }

  }

  private void printListUsers(ArrayList<ModelUser> userList) {
    Iterator it = userList.iterator();
    ModelUser userAux = new ModelUser();
    while (it.hasNext()) {
      userAux = (ModelUser) it.next();
      Log.v("USERDATA", " ID=" + userAux.id + " NAME=" + userAux.nombre + " AGE=" + userAux.edad + " DATE=" + userAux.datetime);
    }
  }

  private void printListTables(ArrayList<String> tableList) {
    Iterator it = tableList.iterator();
    while (it.hasNext()) {
      String tableName = (String) it.next();
      Log.v("TABLENAME", " " + tableName);
    }
  }

  public Dialog onCreateDialog(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    // Get the layout inflater

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(view)
        // Add action buttons
        .setPositiveButton("Create DB", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            Log.v("ONCLICK", "CREATE");
            String database = editTextDatabase.getText().toString().trim();
            String table = editTextTable.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String type = editTextType.getText().toString().trim();
            Log.v("CREATEDATABASE", "" + database + table + name + type);
            if (!TextUtils.isEmpty(database) && !TextUtils.isEmpty(table) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(type)) {
              createDatabase(database, table, name, type);
            }
            tableExists = dbmanager.checkIfTableExist(db, "TEST");
            Log.v("TABLEEXISTS", "" + tableExists);

          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

          }
        });
    return builder.create();
  }

  private void settings() {

    editTextId = 0;
    hashMapEditText = new HashMap<>();
    linearLayout = (LinearLayout) view.findViewById(R.id.linearParent);
    btnCreateDatabase = (Button) findViewById(R.id.btnCreateDatabase);
    btnExistsDatabase = (Button) findViewById(R.id.btnExistsDatabase);
    btnDropDatabase = (Button) findViewById(R.id.btnDropDatabase);
    btnCreateTable = (Button) findViewById(R.id.btnCreateTable);
    btnDropTable = (Button) findViewById(R.id.btnDropTable);
    btnClearTable = (Button) findViewById(R.id.btnClearTable);
    btnInsertItem = (Button) findViewById(R.id.btnInsertItem);
    btnUpdateItem = (Button) findViewById(R.id.btnUpdateItem);
    btnDeleteItem = (Button) findViewById(R.id.btnDeleteItem);
    btnSelectItemsFromTable = (Button) findViewById(R.id.btnSelectItemsFromTable);
    btnFindItemByName = (Button) findViewById(R.id.btnFindItemByName);
    btnFindItemLikeName = (Button) findViewById(R.id.btnFindItemLikeName);

  }
}