package com.anmol.indiantourists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    EditText editaddress,editcontact,editweb,editdes;
    Button addedits;
    String city,place,address,contact,website,description;
    DatabaseReference aref;
    String edadd,edcon,edweb,eddes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editaddress = (EditText)findViewById(R.id.editaddress);
        editcontact = (EditText)findViewById(R.id.edit_phone_number);
        editweb = (EditText)findViewById(R.id.editweb);
        editdes = (EditText)findViewById(R.id.editdes);
        addedits = (Button) findViewById(R.id.addedits);
        city = getIntent().getExtras().getString("city");
        place = getIntent().getExtras().getString("place");
        edadd = getIntent().getExtras().getString("address");
        edcon = getIntent().getExtras().getString("contact");
        edweb = getIntent().getExtras().getString("website");
        eddes = getIntent().getExtras().getString("descript");
        editaddress.setText(edadd);
        editcontact.setText(edcon);
        editweb.setText(edweb);
        editdes.setText(eddes);
        setTitle(place);
        aref = FirebaseDatabase.getInstance().getReference().getRoot().child(city).child(place);
        addedits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = editaddress.getText().toString();
                contact = editcontact.getText().toString();
                website = editweb.getText().toString();
                description = editdes.getText().toString();
                if(!TextUtils.isEmpty(address) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(website) && !TextUtils.isEmpty(description)){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("Address",address);
                    map.put("Contact No",contact);
                    map.put("Website",website);
                    map.put("About",description);
                    aref.updateChildren(map);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else {
                    Toast.makeText(EditActivity.this,"Please fill out all the details",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
    }
}
