package com.anmol.indiantourists;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Boolean check = false;
    ListView list;
    ArrayList<String> citylist;
    ArrayAdapter<String> cityadapter;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.CHANGE_NETWORK_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.MODIFY_PHONE_STATE,
            android.Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS

    };
    private int permissionRecord;
    private DatabaseReference databaseReference;
    Button addcity;
    String cityname;
    private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, 100);
        list = (ListView)findViewById(R.id.list);
        addcity = (Button)findViewById(R.id.addcity);
        citylist = new ArrayList<String>();
        cityadapter = new ArrayAdapter<String>(this,R.layout.list_item,citylist);
        list.setAdapter(cityadapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalertdialog();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();
                while (iterator.hasNext()){
                    set.add((String)((DataSnapshot)iterator.next()).getKey());
                }
                citylist.clear();
                citylist.addAll(set);
                cityadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((TextView)view).getText().toString();
                Bundle b = new Bundle();
                b.putString("city",s);
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

    }

    private void showalertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter City Name");
        final EditText edittext = new EditText(this);
        builder.setView(edittext);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityname = edittext.getText().toString();
                if(!TextUtils.isEmpty(cityname)){

                    for(int i = 0;i<list.getAdapter().getCount();i++){
                        if(cityname.equals((String)list.getAdapter().getItem(i))){
                            check = true;
                        }
                    }
                    if(check.equals(true)){
                        Toast.makeText(MainActivity.this,"City already exists",Toast.LENGTH_SHORT).show();
                        check = false;
                    }else {
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put(cityname,"");
                        databaseReference.updateChildren(map);
                    }
                }

                else {
                    showalertdialog();
                    Toast.makeText(MainActivity.this,"Please Enter a Valid City Name",Toast.LENGTH_LONG).show();
                }


            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            permissionRecord = grantResults[0] = PackageManager.PERMISSION_GRANTED;
            int length = grantResults.length;
        }
    }
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){

            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
            //FragmentManager fm = getFragmentManager();
            //fm.beginTransaction().replace(R.id.content, new main()).commit();
        }
    }

}
