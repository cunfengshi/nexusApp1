package com.example.cunfe.nexusapp;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;

public class MainContentActivity extends AppCompatActivity implements ContentFragment.OnTextChange{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);


        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ContentFragment mWeixin = new ContentFragment();
        transaction.add(R.id.id_content,mWeixin);
         transaction.commit();
        // ContentFragment contentFragment= new ContentFragment();
        //FragmentManager manager=getSupportFragmentManager();
       // manager.beginTransaction().add(R.id.fragment_contain, contentFragment).commit();
    }

    @Override
    public String GetTextName() {
        return "";
    }
}
