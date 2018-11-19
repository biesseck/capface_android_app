package com.example.bernardo.capface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bernardo.capface.entidades.ControllerProfessor;
import com.example.bernardo.capface.entidades.Professor;

import java.io.IOException;

public class ActivityConfiguracoes extends AppCompatActivity {

    EditText editTextNomeProfessor;
    EditText editTextUserFTP;
    EditText editTextSenhaFTP;
    EditText editTextUserQAcademico;
    EditText editTextSenhaQAcademico;
    Button buttonSalvarEditarProfessor;

    ListView listViewDisciplinasCadastradas;

    ControllerProfessor controllerProfessor = new ControllerProfessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        inicializarComponentes();
        this.carregarDadosProfessor();
    }


    public void inicializarComponentes() {
        editTextNomeProfessor = (EditText) findViewById(R.id.editTextNomeProfessor);
        editTextUserFTP = (EditText) findViewById(R.id.editTextUserFTP);
        editTextSenhaFTP = (EditText) findViewById(R.id.editTextSenhaFTP);
        editTextUserQAcademico = (EditText) findViewById(R.id.editTextUserQAcademico);
        editTextSenhaQAcademico = (EditText) findViewById(R.id.editTextSenhaQAcademico);

        buttonSalvarEditarProfessor = (Button) findViewById(R.id.buttonSalvarEditarProfessor);
        buttonSalvarEditarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aoClicarSalvarEditarProfessor();
            }
        });

        listViewDisciplinasCadastradas = (ListView) findViewById(R.id.listViewDisciplinasCadastradas);
        listViewDisciplinasCadastradas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    public void aoClicarSalvarEditarProfessor() {
        if (buttonSalvarEditarProfessor.getText().toString().equals("SALVAR")) {
            try {
                Professor professor = this.getProfessorFromFormulario();
                controllerProfessor.saveProfessor(professor);
                this.disableFormularioProfessor();
                buttonSalvarEditarProfessor.setText("EDITAR");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (buttonSalvarEditarProfessor.getText().toString().equals("EDITAR")) {
            try {
                Professor professor = controllerProfessor.loadProfessor();
                this.popularFormularioProfessor(professor);
                this.enableFormularioProfessor();
                buttonSalvarEditarProfessor.setText("SALVAR");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void carregarDadosProfessor() {
        try {
            Professor professor = controllerProfessor.loadProfessor();
            if (professor != null) {
                this.popularFormularioProfessor(professor);
                this.disableFormularioProfessor();
                buttonSalvarEditarProfessor.setText("EDITAR");
            } else {
                this.enableFormularioProfessor();
                buttonSalvarEditarProfessor.setText("SALVAR");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disableFormularioProfessor() {
        editTextNomeProfessor.setEnabled(false);
        editTextUserFTP.setEnabled(false);
        editTextSenhaFTP.setEnabled(false);
        editTextUserQAcademico.setEnabled(false);
        editTextSenhaQAcademico.setEnabled(false);
    }


    public void enableFormularioProfessor() {
        editTextNomeProfessor.setEnabled(true);
        editTextUserFTP.setEnabled(true);
        editTextSenhaFTP.setEnabled(true);
        editTextUserQAcademico.setEnabled(true);
        editTextSenhaQAcademico.setEnabled(true);
    }


    public Professor getProfessorFromFormulario() {
        Professor professor = new Professor();
        professor.setNome(editTextNomeProfessor.getText().toString());
        professor.setUserFTP(editTextUserFTP.getText().toString());
        professor.setSenhaFTP(editTextSenhaFTP.getText().toString());
        professor.setUserQAcademico(editTextUserQAcademico.getText().toString());
        professor.setSenhaQAcademico(editTextSenhaQAcademico.getText().toString());
        return professor;
    }


    public void popularFormularioProfessor(Professor professor) {
        editTextNomeProfessor.setText(professor.getNome());
        editTextUserFTP.setText(professor.getUserFTP());
        editTextSenhaFTP.setText(professor.getSenhaFTP());
        editTextUserQAcademico.setText(professor.getUserQAcademico());
        editTextSenhaQAcademico.setText(professor.getSenhaQAcademico());
    }




}
