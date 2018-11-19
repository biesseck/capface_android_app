package com.example.bernardo.capface;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewHelloWorld;
    FloatingActionButton floatingActionButtonAddAula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializarComponentes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            abrirActivityConfiguracoesProfessor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void inicializarComponentes() {
        textViewHelloWorld = (TextView) findViewById(R.id.textViewHelloWorld);

        floatingActionButtonAddAula = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddAula);
        floatingActionButtonAddAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityNovaAula();
            }
        });
    }


    public void abrirActivityConfiguracoesProfessor() {
        Intent intent = new Intent(this, ActivityConfiguracoes.class);
        //intent.putExtra("arrayListNomeImagensCapturadas", arrayListNomesImagensCapturadas);
        //startActivityForResult(intent,ID_ACTIVITY_CONFIG);
        startActivity(intent);
    }


    public void abrirActivityNovaAula() {
        Intent intent = new Intent(this, ActivityNovaAula.class);
        //intent.putExtra("arrayListNomeImagensCapturadas", arrayListNomesImagensCapturadas);
        //startActivityForResult(intent,ID_ACTIVITY_CONFIG);
        startActivity(intent);
    }
}