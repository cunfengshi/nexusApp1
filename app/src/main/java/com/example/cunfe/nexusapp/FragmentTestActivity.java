package com.example.cunfe.nexusapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragmentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setArguments(getIntent().getExtras());
       // FragmentManager manager = getSupportFragmentManager();
//        FrameLayout fm =(FrameLayout)findViewById(R.id.fragment_container);
//        ViewGroup p = (ViewGroup)fm.getParent();
//        if (p != null) {
//            p.removeAllViewsInLayout();
//        }
       // manager.beginTransaction().replace(R.id.fragment_container,contentFragment).commit();

}
}
