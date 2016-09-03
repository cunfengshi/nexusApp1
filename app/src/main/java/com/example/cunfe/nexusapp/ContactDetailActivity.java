package com.example.cunfe.nexusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cunfe.nexusapp.Models.ContactModel;

public class ContactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Intent intent = this.getIntent();
        ContactModel model = (ContactModel) intent.getSerializableExtra("contact");

        ((TextView)findViewById(R.id.textContactName)).setText(model.getDisplayName());
        ((TextView)findViewById(R.id.textPhoneNum)).setText(model.getPhoneNum());
    }
}
