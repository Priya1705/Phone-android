package com.example.priya_000.phone;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener{
        private ListView listView;
        private List<ContactBean> list = new ArrayList<ContactBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(this);




        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactBean objContact = new ContactBean();
            objContact.setName(name);
            objContact.setPhoneNo(phoneNumber);
            list.add(objContact);

        }
        phones.close();

        ContactAdapter objAdapter = new ContactAdapter(
                MainActivity.this, R.layout.alluser_row, list);
        listView.setAdapter(objAdapter);

        if (null != list && list.size() != 0) {
            Collections.sort(list, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });


            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("");

            alert.setMessage(list.size() + " Contact Found!!!");

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();


        } else {
            showToast("No Contact Found!!!");
        }
    }

    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> listview, View v, int position,
                            long id) {
        ContactBean bean = (ContactBean) listview.getItemAtPosition(position);
        showCallDialog(bean.getName(), bean.getPhoneNo());
    }

    private void showCallDialog(String name, final String phoneNo) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Call?");

        alert.setMessage("Are you sure want to call " + name + " ?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = "tel:" + phoneNo;
                Intent i = new Intent(Intent.ACTION_DIAL, Uri
                        .parse(phoneNumber));
                startActivity(i);
            }
        });

      alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });




        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }


}





