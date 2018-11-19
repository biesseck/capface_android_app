package com.example.bernardo.capface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ActivityConfiguracoes extends AppCompatActivity {

    ListView listViewDisciplinasCadastradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        inicializarComponentes();
    }


    public void inicializarComponentes() {
        listViewDisciplinasCadastradas = (ListView) findViewById(R.id.listViewDisciplinasCadastradas);
        listViewDisciplinasCadastradas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
}
