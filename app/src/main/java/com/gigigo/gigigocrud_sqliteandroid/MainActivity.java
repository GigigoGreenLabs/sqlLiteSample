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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  //region
  private ArrayList<ModelUser> userList;
  private ArrayList<String> tableList;
  private ArrayList<String> columnNames;
  private boolean tableExists;
  private LinearLayout linearLayout;
  private int editTextId;
  private HashMap<Integer, EditTextRow> hashMapEditText;

  private View viewCreateDB;
  private View viewCreateTable;
  private View viewSetDatabase;
  private View viewSearchItem;
  private View viewDropItem;
  private View viewClearTable;

  private Button btnSetDatabase;
  private Button btnCreateDatabase;
  private Button btnExistsDatabase;
  private Button btnDropDatabase;
  private Button btnCreateTable;
  private Button btnExistTable;
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
  private EditText editTextTableFromDialogTable;
  private EditText editTextSetDatabase;
  private EditText editTextSearchItem;
  private EditText editTextDropItem;
  private EditText editTextClearTableContent;

  private SQLiteManager dbmanager;
  private SQLiteDatabase db;
  private SQLiteManager dbmanagerAux;

  boolean searchItemIsDataBase;
  boolean dropItemIsDataBase;
  //endregion

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    settingDialogViews();
    settings();
    settingsFromDialogViewSetDatabase(viewSetDatabase);
    settingsFromDialogViewCreateDB(viewCreateDB);
    settingsFromDialogViewCreateTable(viewCreateTable);
    settingsFromDialogViewSearchItem(viewSearchItem);
    settingsFromDialogViewDropItem(viewDropItem);
    settingsFromDialogViewClearTable(viewClearTable);

    final Dialog dialogSetDataBase = onCreateDialog(viewSetDatabase);
    final Dialog dialogCreateDB = onCreateDialog(viewCreateDB);
    final Dialog dialogCreateTable = onCreateDialog(viewCreateTable);
    final Dialog dialogSearchItem = onCreateDialog(viewSearchItem);
    final Dialog dialogDropItem = onCreateDialog(viewDropItem);
    final Dialog dialogClearTable = onCreateDialog(viewClearTable);

    btnSetDatabase.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialogSetDataBase.show();
      }
    });

    btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialogCreateDB.show();
      }
    });

    btnInsertItem.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

      }
    });

    btnExistsDatabase.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        searchItemIsDataBase = true;
        dialogSearchItem.show();
      }
    });

    btnDropDatabase.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dropItemIsDataBase = true;
        dialogDropItem.show();
      }
    });

    btnCreateTable.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (dbmanager != null) {
          dbmanager.setCreateTableWithColumns(false);
          dialogCreateTable.show();
        } else {
          needDataBase();
        }
      }
    });

    btnExistTable.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (dbmanager != null) {
          searchItemIsDataBase = false;
          dialogSearchItem.show();
        } else {
          needDataBase();
        }
      }
    });

    btnDropTable.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (dbmanager != null) {
          dropItemIsDataBase = false;
          dialogDropItem.show();
        } else {
          needDataBase();
        }
      }
    });

    btnClearTable.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (dbmanager != null) {
          dialogClearTable.show();
        } else {
          needDataBase();
        }
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

    columnNames = dbmanager.getTableColumnNames(db, "users");
    printListTables(columnNames);

    dbmanager.insertRow(db, user);
    dbmanager.insertRow(db, user1);
    dbmanager.insertRow(db, user2);
    dbmanager.insertRow(db, user3);
    dbmanager.insertRow(db, user4);

    userList = dbmanager.load(db, dbmanager.getTableName());
    printListUsers(userList);

    ModelUser updatedUser = new ModelUser("maria", 30);
    dbmanager.updateRow(db, user3, updatedUser);

    userList = dbmanager.load(db, dbmanager.getTableName());
    printListUsers(userList);

    dbmanager.deleteRow(db, user3);

    userList = dbmanager.load(db, dbmanager.getTableName());
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

    //dbmanager.clearTableContent(db, "USERS");
    userList = dbmanager.load(db, dbmanager.getTableName());
    printListUsers(userList);

    //dbmanager.dropTable(db, "USERS");
    tableExists = dbmanager.checkIfTableExist(db, "USERS");
    Log.v("TABLEEXISTS", "" + tableExists);

    //dbmanager.deleteDatabase(this, "DBUSERS");
    Log.v("", "" + this.databaseList());
  }

  private void createDatabase(String database, String table,
      HashMap<Integer, EditTextRow> hmEditText) {
    dbmanager = new SQLiteManager(this, database);
    db = dbmanager.getWritableDatabase();
    dbmanager.setTableName(table);

    Iterator iterator = hmEditText.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry thisEntry = (Map.Entry) iterator.next();
      EditTextRow value = (EditTextRow) thisEntry.getValue();
      dbmanager.insertColumn(value.getEditTextName(), value.getEditTextType());
    }

    dbmanager.createTable(db);
  }

  public Dialog onCreateDialog(final View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(view)
        // Add action buttons
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            int idEditTextRow = 1;
            if (view.equals(viewCreateDB)) {
              String database = editTextDatabase.getText().toString().trim();
              String table = editTextTable.getText().toString().trim();
              String name = editTextName.getText().toString().trim();
              String type = editTextType.getText().toString().trim();

              EditTextRow editText = new EditTextRow(name, type);
              hashMapEditText.put(idEditTextRow, editText);

              int count = linearLayout.getChildCount();
              ViewGroup v = null;
              EditText editTextNameAux = new EditText(getApplicationContext());
              EditText editTextTypeAux = new EditText(getApplicationContext());
              ;
              View view = null;
              for (int i = 1; i < count; i++) {
                v = (ViewGroup) linearLayout.getChildAt(i);
                int countED = v.getChildCount();

                view = v.getChildAt(0);
                if (view instanceof EditText) {
                  editTextNameAux = (EditText) view;
                }
                view = v.getChildAt(1);
                if (view instanceof EditText) {
                  editTextTypeAux = (EditText) view;
                }
                EditTextRow editTextRowAux = new EditTextRow(editTextNameAux.getText().toString(),
                    editTextTypeAux.getText().toString());
                idEditTextRow++;
                hashMapEditText.put(idEditTextRow, editTextRowAux);
                Log.v("EDITTEXT",
                    "" + editTextNameAux.getText().toString() + " " + editTextTypeAux.getText()
                        .toString());
              }
              if (!TextUtils.isEmpty(database) && !TextUtils.isEmpty(table) && !TextUtils.isEmpty(
                  name) && !TextUtils.isEmpty(type)) {
                createDatabase(database, table, hashMapEditText);
              }
              columnNames = dbmanager.getTableColumnNames(db,table);
              printListTables(columnNames);

            } else if (view.equals(viewCreateTable)) {
              String tablename = editTextTableFromDialogTable.getText().toString().trim();
              if (!TextUtils.isEmpty(tablename)) {
                dbmanager.setTableName(tablename);
                dbmanager.createTable(db);
                toast("CREATED TABLE WITH 1 COLUMN: id-AUTOINCREMENT");
              }
            } else if (view.equals(viewSetDatabase)) {
              String database = editTextSetDatabase.getText().toString().trim();
              dbmanager = new SQLiteManager(getApplicationContext(), database);
              db = dbmanager.getWritableDatabase();
            } else if (view.equals(viewSearchItem)) {
              if (searchItemIsDataBase) {
                String searchItem = editTextSearchItem.getText().toString().trim();
                boolean databaseExists =
                    dbmanagerAux.checkIfDatabaseExists(getApplicationContext(), searchItem);
                toastBool("¿DATABASE EXISTS? ", databaseExists);
              } else {
                String searchItem = editTextSearchItem.getText().toString().trim();
                boolean tableExists = dbmanager.checkIfTableExist(db, searchItem);
                toastBool("¿TABLE EXISTS?", tableExists);
              }
            } else if (view.equals(viewDropItem)) {
              if (dropItemIsDataBase) {
                String dropDatabase = editTextDropItem.getText().toString().trim();
                boolean databaseExists =
                    dbmanagerAux.checkIfDatabaseExists(getApplicationContext(), dropDatabase);
                if (databaseExists) {
                  dbmanagerAux.dropDatabase(getApplicationContext(), dropDatabase);
                } else {
                  toast("DATABASE DOES NOT EXIST");
                }
              } else {
                String dropTable = editTextDropItem.getText().toString().trim();
                boolean tableExists = dbmanager.checkIfTableExist(db, dropTable);
                if (tableExists) {
                  dbmanager.dropTable(db, dropTable);
                  toast("DROPPED TABLE");
                } else {
                  toast("TABLE DOES NOT EXIST");
                }
              }
            } else if (view.equals(viewClearTable)) {
              String tableToClear = editTextClearTableContent.getText().toString().trim();
              boolean tableExists = dbmanager.checkIfTableExist(db, tableToClear);
              boolean tableHasContent = dbmanager.checkIfTableHasContent(db, tableToClear);

              if (tableExists && tableHasContent) {
                userList = dbmanager.load(db, tableToClear);
                printListUsers(userList);
                dbmanager.clearTableContent(db, tableToClear);
                userList = dbmanager.load(db, tableToClear);
                printListUsers(userList);
                toast("CLEARED TABLE");
              } else {
                toast("TABLE DOES NOT EXIST OR HAS NO CONTENT");
              }
            }
          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {

      }
    });
    return builder.create();
  }

  private void toastBool(String content, boolean flag) {
    Toast.makeText(MainActivity.this, content + " " + flag, Toast.LENGTH_SHORT).show();
  }

  private void toast(String content) {
    Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
  }

  private void needDataBase() {
    toast("NEED TO SET UP A DATABASE");
  }

  public void addEditText(View view) {

    LinearLayout linearAux = new LinearLayout(this);
    linearAux.setOrientation(LinearLayout.HORIZONTAL);
    linearAux.setWeightSum(2);

    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    params.weight = 1.0f;

    EditText etName = new EditText(this);
    etName.setLayoutParams(params);
    etName.setId(editTextId);
    etName.setHint("NAME");
    editTextId++;

    EditText etType = new EditText(this);
    etType.setLayoutParams(params);
    etType.setId(editTextId);
    etType.setHint("TYPE");

    editTextId++;
    Log.v("---------------", "" + etName.getId() + etType.getId());

    linearAux.addView(etName);
    linearAux.addView(etType);
    linearLayout.addView(linearAux, params);
  }

  private void printListUsers(ArrayList<ModelUser> userList) {
    Iterator it = userList.iterator();
    ModelUser userAux = new ModelUser();
    while (it.hasNext()) {
      userAux = (ModelUser) it.next();
      Log.v("USERDATA", " ID="
          + userAux.id
          + " NAME="
          + userAux.nombre
          + " AGE="
          + userAux.edad
          + " DATE="
          + userAux.datetime);
    }
  }

  private void printListTables(ArrayList<String> tableList) {
    Iterator it = tableList.iterator();
    while (it.hasNext()) {
      String tableName = (String) it.next();
      Log.v("TABLENAME", " " + tableName);
    }
  }

  private void settingDialogViews() {
    LayoutInflater inflater = getLayoutInflater();
    viewCreateDB = inflater.inflate(R.layout.dialog_create_database, null);
    viewCreateTable = inflater.inflate(R.layout.dialog_create_table, null);
    viewSetDatabase = inflater.inflate(R.layout.dialog_set_database, null);
    viewSearchItem = inflater.inflate(R.layout.dialog_search_item, null);
    viewDropItem = inflater.inflate(R.layout.dialog_drop_item, null);
    viewClearTable = inflater.inflate(R.layout.dialog_clear_table, null);
    ;
  }

  private void settings() {
    editTextId = 0;
    searchItemIsDataBase = true;
    dropItemIsDataBase = true;
    hashMapEditText = new HashMap<>();
    columnNames = new ArrayList<String>();
    linearLayout = (LinearLayout) viewCreateDB.findViewById(R.id.linearParent);
    btnSetDatabase = (Button) findViewById(R.id.btnSetDatabase);
    btnCreateDatabase = (Button) findViewById(R.id.btnCreateDatabase);
    btnExistsDatabase = (Button) findViewById(R.id.btnExistsDatabase);
    btnDropDatabase = (Button) findViewById(R.id.btnDropDatabase);
    btnCreateTable = (Button) findViewById(R.id.btnCreateTable);
    btnExistTable = (Button) findViewById(R.id.btnExistsTable);
    btnDropTable = (Button) findViewById(R.id.btnDropTable);
    btnClearTable = (Button) findViewById(R.id.btnClearTable);
    btnInsertItem = (Button) findViewById(R.id.btnInsertItem);
    btnUpdateItem = (Button) findViewById(R.id.btnUpdateItem);
    btnDeleteItem = (Button) findViewById(R.id.btnDeleteItem);
    btnSelectItemsFromTable = (Button) findViewById(R.id.btnSelectItemsFromTable);
    btnFindItemByName = (Button) findViewById(R.id.btnFindItemByName);
    btnFindItemLikeName = (Button) findViewById(R.id.btnFindItemLikeName);
    dbmanagerAux = new SQLiteManager(getApplicationContext(), null);
  }

  private void settingsFromDialogViewSetDatabase(View view) {
    editTextSetDatabase = (EditText) view.findViewById(R.id.editTextSetDatabase);
  }

  private void settingsFromDialogViewCreateDB(View view) {
    editTextDatabase = (EditText) view.findViewById(R.id.editTextDatabase);
    editTextTable = (EditText) view.findViewById(R.id.editTextTable);
    editTextName = (EditText) view.findViewById(R.id.editTextName);
    editTextType = (EditText) view.findViewById(R.id.editTextType);
    btnAddRowEditText = (Button) view.findViewById(R.id.btnAddRowEditText);
  }

  private void settingsFromDialogViewCreateTable(View view) {
    editTextTableFromDialogTable = (EditText) view.findViewById(R.id.editTextTableFromDialogTable);
  }

  public void settingsFromDialogViewSearchItem(View view) {
    editTextSearchItem = (EditText) view.findViewById(R.id.editTextSearchItem);
  }

  private void settingsFromDialogViewDropItem(View view) {
    editTextDropItem = (EditText) view.findViewById(R.id.editTextDropItem);
  }

  private void settingsFromDialogViewClearTable(View view) {
    editTextClearTableContent = (EditText) view.findViewById(R.id.editTextClearTableContent);
  }
}