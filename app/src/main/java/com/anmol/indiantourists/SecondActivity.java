package com.anmol.indiantourists;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {
    Boolean check = false;
    Button addplace;
    ListView place;
    ArrayList<String> placelist;
    ArrayAdapter<String> placeadapter;
    DatabaseReference placedatabase;
    String city;
    String placename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        city = getIntent().getExtras().getString("city");
        setTitle(city);
        place = (ListView)findViewById(R.id.list);
        addplace = (Button)findViewById(R.id.addplace); 
        placelist = new ArrayList<String>();
        placeadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,placelist);
        place.setAdapter(placeadapter);
        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddplace();
            }
        });
        placedatabase = FirebaseDatabase.getInstance().getReference().getRoot().child(city);
        placedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();
                while (iterator.hasNext()){
                    set.add((String)((DataSnapshot)iterator.next()).getKey());
                }
                placelist.clear();
                placelist.addAll(set);
                placeadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((TextView)view).getText().toString();
                Bundle b = new Bundle();
                b.putString("place",s);
                b.putString("city",city);
                Intent intent = new Intent(SecondActivity.this,FirstActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void showaddplace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Place Name");
        final EditText edittext = new EditText(this);
        builder.setView(edittext);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placename = edittext.getText().toString();
                if(!TextUtils.isEmpty(placename)){

                    for(int i = 0;i<place.getAdapter().getCount();i++){
                        if(placename.equals((String)place.getAdapter().getItem(i))){
                            check = true;
                        }
                    }
                    if(check.equals(true)){
                        Toast.makeText(SecondActivity.this,"Place already exists",Toast.LENGTH_SHORT).show();
                        check = false;
                    }else {
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put(placename,"");
                        placedatabase.updateChildren(map);
                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().getRoot().child(city).child(placename);
                        Map<String,Object> map1 = new HashMap<String, Object>();
                        map1.put("Address","Add Address");
                        map1.put("Contact No","Add phone");
                        map1.put("Website","Add Website");
                        map1.put("About"," Add Description");
                        mref.updateChildren(map1);
                    }
                }

                else {
                    showaddplace();
                    Toast.makeText(SecondActivity.this,"Please Enter a Valid Place Name",Toast.LENGTH_LONG).show();
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
}
