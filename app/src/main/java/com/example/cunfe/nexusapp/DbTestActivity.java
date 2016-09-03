package com.example.cunfe.nexusapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.ExtractEditText;
import android.net.Uri;
import android.preference.DialogPreference;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import Contracts.DataBaseTables;
import Tools.DataBaseHelper;

public class DbTestActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    private List<String> spinnerlist = new ArrayList<String>();
    private String ItemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

//        Button itemsubmit = (Button)findViewById(R.id.itemsubmit);
//        itemsubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddOrDelItem(v);
//            }
//        });
        InitSpinner();
    }

    private void InitSpinner() {
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String query = "SELECT MENUID||'-'||MENUURL as menutext FROM MAINMENU;";
        Cursor c =db.rawQuery(query, null);
        spinnerlist= new ArrayList<String>();
        while (c.moveToNext()){
            int nameColumnIndex = c.getColumnIndex("menutext");
            spinnerlist.add(c.getString(nameColumnIndex));
        }

        Spinner spinner;
        spinner =(Spinner)findViewById(R.id.spinner);
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ItemSelected = adapter.getItem(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ItemSelected = null;
            }
        });
    }
    public void AddOrDelItem(View view)
    {
        try{
        Switch select = (Switch)findViewById(R.id.switchadd);
        String sql;
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(select.isChecked())
        {
            EditText item = (EditText)findViewById(R.id.item);
            //String id =helper.GetNextId("MAINMENU");
            sql="Insert into MAINMENU values(null,'MainMenu"+item.getText()+"','"+item.getText()+"');";
            db.execSQL(sql);
        }else
        {
            if(ItemSelected !=null)
            {
                String[] mennutext = ItemSelected.split("-");
                sql="DELETE FROM MAINMENU WHERE MENUID="+mennutext[0]+";";
                db.execSQL(sql);
            }
        }
        InitSpinner();
        }catch (Exception e)
        {
            EditText item = (EditText)findViewById(R.id.item);
            item.setError(e.getMessage());
            return;
        }
    }

    public void SelectContact(View view)
    {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode!=1)
        {
            return;
        }
        if(resultCode==RESULT_CANCELED)
        {
            EditText item = (EditText)findViewById(R.id.item);
            item.setError("获取联系人失败！");
            return;
        }
        Uri contactUri = data.getData();
        String[] projection ={ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = getContentResolver().query(contactUri, projection, null, null, null);
        c.moveToFirst();

        int column = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String number = c.getString(column);
        EditText item = (EditText)findViewById(R.id.item);
        item.setText(number);
    }
    }
