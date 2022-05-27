package com.noolart.surfaceviewtest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.LinkedList;

public class HelpActivity extends AppCompatActivity {

    ListView helpList;
    Button back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        StorageClass storageClass = new StorageClass();

        int fromWhichActivity = getIntent().getIntExtra("activity",1);
        Log.e("Activity", fromWhichActivity+"");

        helpList = findViewById(R.id.help_list);
        back = findViewById(R.id.backButton);

        LinkedList<HashMap<String,Object>> listForAdapter=new LinkedList<>();

        String[] keyArray={"image", "title", "description"};
        int [] idArray={R.id.image,R.id.title, R.id.description};


        for (int i = 0; i < 64; i++) {

            if (!storageClass.descriptions[i].equals("")){
                Log.e ("help", i + " " + storageClass.descriptions[i]);
                HashMap<String, Object> map = new HashMap<>();
                map.put(keyArray[0],storageClass.images[i]);
                map.put(keyArray[1],storageClass.descriptions[i].split("-")[0]);
                map.put(keyArray[2],storageClass.descriptions[i].split("-")[1]);
                listForAdapter.add(map);
            }
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listForAdapter,R.layout.list_item,keyArray,idArray);

        helpList.setAdapter(simpleAdapter);

        back.setOnClickListener(view -> finish());





    }
}
