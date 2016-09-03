package com.example.cunfe.nexusapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.cunfe.nexusapp.Models.ContactModel;
import com.example.cunfe.nexusapp.widgets.RoundImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //存放首字母缩写
    private Map<String,Integer> mSortKeyMap;
    //多选框是否可见
    private int checkBoxVisible=View.INVISIBLE;
    //存放已选中的checkbox的TAG 以便于复用时更新状态
    private List<Integer> checkedList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化联系人列表
        this.InitContactList();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_ID,ContactsContract.CommonDataKinds.Phone.CONTACT_ID,"phonebook_label"};

    private void InitContactList() {
        /* 取得ContentResolver */
        ContentResolver resolver = getContentResolver();
        /* 取得通讯录的Phones表的cursor */
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION,null,null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);

        ContactModel[] models = getContactModelFromCursor(cursor);

        mRecyclerView = (RecyclerView) findViewById(R.id.contactList);
        mRecyclerView.setHasFixedSize(true);
        checkedList = new ArrayList<Integer>();
        mSortKeyMap = new HashMap<String, Integer>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyContactAdapter(models);
        mRecyclerView.setAdapter(mAdapter);
    }


    private ContactModel[] getContactModelFromCursor(Cursor cursor) {
        ContactModel[] models=new ContactModel[cursor.getCount()];
        for (int i=0;i<cursor.getCount();i++)
        {
            ContactModel model = new ContactModel();
            cursor.moveToPosition(i);
            model.setContactId(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
            model.setDisplayName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            model.setPhoneNum(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            model.setPhotoId(cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
            model.setSortKey(cursor.getString(cursor.getColumnIndex("phonebook_label")));
            models[i]=model;
        }
        return models;
    }

    public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.myViewHolder>
    {
        private ContactModel[] mdata;


        public MyContactAdapter(ContactModel[] data)
        {
            mdata=data;
        }
        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 给ViewHolder设置布局文件
            View v = LayoutInflater.from(ContactListActivity.this).inflate(R.layout.contact_item, parent, false);
            return new myViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final myViewHolder holder, final int position) {

            String displayname=mdata[position].getDisplayName();
            if(displayname!=null&&!displayname.equals(""))
            {
                holder.mNameTextView.setText(displayname);
            }else
            {
                holder.mNameTextView.setText("无名称");
            }
            holder.mNameTextView.setGravity(Gravity.CENTER_VERTICAL);
            holder.mNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return changeAllCheckBoxVisible();
                }
            });
            holder.mNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(),ContactDetailActivity.class);
                    ContactModel model = mdata[position];
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contact",model);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent);
                }
            });
            if(mdata[position].getPhotoId()>0)
            {
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,mdata[position].getContactId());
                ContentResolver resolver = getBaseContext().getContentResolver();
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                Bitmap bitmap =  BitmapFactory.decodeStream(input);
                holder.mPicImageView.setImageBitmap(bitmap);
            }else
            {
                holder.mPicImageView.setImageResource(R.drawable.contacts);
            }
            holder.mcheckBox.setTag(position);
            holder.mcheckBox.setVisibility(checkBoxVisible);
            if(checkedList!=null&&checkedList.size()>0)
            {
                holder.mcheckBox.setChecked(checkedList.contains(position)?true:false);
            }else
            {
                holder.mcheckBox.setChecked(false);
            }
            holder.mcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked&&!checkedList.contains((Integer) holder.mcheckBox.getTag()))
                    {
                        checkedList.add((Integer) holder.mcheckBox.getTag());
                    }
                    if(!isChecked&&checkedList.contains((Integer) holder.mcheckBox.getTag()))
                    {
                        checkedList.remove(checkedList.indexOf((Integer) holder.mcheckBox.getTag()));
                    }
                }
            });

            Log.i("ContactListActivity","Position "+position+"'s SortKey is "+mdata[position].getSortKey());
            InitSortKey(holder,mdata[position].getSortKey(),position);
        }

        private void InitSortKey(myViewHolder holder, String sortKey, int position) {

            if(!mSortKeyMap.containsKey(mdata[position].getSortKey()))
            {
                holder.mSortKeyTextView.setText(mdata[position].getSortKey());
                mSortKeyMap.put(sortKey,position);
            }else if(mSortKeyMap.get(mdata[position].getSortKey())==position)
            {
                holder.mSortKeyTextView.setText(mdata[position].getSortKey());
            }else
            {
                holder.mSortKeyTextView.setText("");
            }
        }

        @Override
        public int getItemCount() {
            int count =mdata.length;
            return count;
        }
        class myViewHolder extends RecyclerView.ViewHolder
        {

            public CheckBox mcheckBox;
            public TextView mNameTextView;
            public TextView mSortKeyTextView;
            public RoundImageView mPicImageView;
            public myViewHolder(View itemView) {
                super(itemView);
                mcheckBox=(CheckBox) itemView.findViewById(R.id.letterIndex);
                mNameTextView=(TextView) itemView.findViewById(R.id.contactName);
                mSortKeyTextView=(TextView) itemView.findViewById(R.id.sortKeyTextView);
                mPicImageView=(RoundImageView) itemView.findViewById(R.id.contactPic);
            }
        }
    }



    private boolean changeAllCheckBoxVisible() {
        RecyclerView view = (RecyclerView) findViewById(R.id.contactList);
        checkBoxVisible = checkBoxVisible==View.VISIBLE?View.INVISIBLE: View.VISIBLE;
        Map<String,Integer> mdsad = mSortKeyMap;
        try{
            changeAllCheckBoxVisible(view,R.id.letterIndex);
            return true;
        }catch (Exception e)
        {
            String message = e.getMessage();
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            return  false;
        }
    }

    private void changeAllCheckBoxVisible(ViewGroup view,int id) {
        for(int i = 0; i<view.getChildCount();i++)
        {
            View child = view.getChildAt(i);
            if(child.getId()== id)
            {
                child.setVisibility(checkBoxVisible);
            }else
            {
                if(child instanceof ViewGroup)
                {
                    changeAllCheckBoxVisible((ViewGroup) child,id);
                }
            }
        }
    }
}
