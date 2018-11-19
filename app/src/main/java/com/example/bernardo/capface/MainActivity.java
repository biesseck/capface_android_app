package com.example.bernardo.capface;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButtonAddAula;
    AlertDialog.Builder alertDialogBuilder;

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
            this.abrirActivityConfiguracoesProfessor();
            return true;

        } else if (id == R.id.action_aboutCapface) {
            this.showAlertDialog_aboutCapface();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void inicializarComponentes() {
        floatingActionButtonAddAula = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddAula);
        floatingActionButtonAddAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityNovaAula();
            }
        });

        alertDialogBuilder = new AlertDialog.Builder(this);
    }


    public void showAlertDialog_aboutCapface() {
        alertDialogBuilder.setTitle("Sobre o CapFace");
        String aboutCapface = "COORDENADOR\nBernardo Biesseck\n\n";
        aboutCapface += "BOLSISTAS\n";
        aboutCapface += "Leuri\n";
        aboutCapface += "Esdras\n";
        aboutCapface += "Hellen\n";
        aboutCapface += "Matheus Q.\n";
        aboutCapface += "Onofre\n";
        alertDialogBuilder.setMessage(aboutCapface);
        alertDialogBuilder.setPositiveButton("OK", null);
        AlertDialog dialog = alertDialogBuilder.create(); // create and show the alert dialog
        dialog.show();
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