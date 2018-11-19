package com.example.bernardo.capface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ActivityNovaAula extends AppCompatActivity {

    ListView listViewImageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_aula);

        inicializarComponentes();
    }


    public void inicializarComponentes() {
        listViewImageFiles = (ListView) findViewById(R.id.listViewImageFiles);
        listViewImageFiles.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
}
