package com.example.bernardo.capface;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bernardo.capface.entidades.ControllerDisciplinas;
import com.example.bernardo.capface.entidades.ControllerProfessor;
import com.example.bernardo.capface.entidades.Disciplina;
import com.example.bernardo.capface.entidades.Professor;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityConfiguracoes extends AppCompatActivity {

    EditText editTextNomeProfessor;
    EditText editTextUserFTP;
    EditText editTextSenhaFTP;
    EditText editTextUserQAcademico;
    EditText editTextSenhaQAcademico;
    Button buttonSalvarEditarProfessor;

    EditText editTextNomeDisciplina;
    EditText editTextCodigoDisciplina;
    EditText editTextCurso;
    EditText editTextTurma;
    EditText editTextTurno;
    ListView listViewDisciplinasCadastradas;
    Button buttonCadastrarDisciplina;

    ControllerProfessor controllerProfessor = new ControllerProfessor();
    ControllerDisciplinas controllerDisciplinas = ControllerDisciplinas.createControllerDisciplinas();

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        inicializarComponentes();
        this.carregarDadosProfessor();
        this.carregarDadosDisciplinas();
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



        editTextNomeDisciplina = (EditText) findViewById(R.id.editTextNomeDisciplina);
        editTextCodigoDisciplina = (EditText) findViewById(R.id.editTextCodigoDisciplina);
        editTextCurso = (EditText) findViewById(R.id.editTextCurso);
        editTextTurma = (EditText) findViewById(R.id.editTextTurma);
        editTextTurno = (EditText) findViewById(R.id.editTextTurno);

        listViewDisciplinasCadastradas = (ListView) findViewById(R.id.listViewDisciplinasCadastradas);
        listViewDisciplinasCadastradas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        buttonCadastrarDisciplina = (Button) findViewById(R.id.buttonCadastrarDisciplina);
        buttonCadastrarDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aoClicarCadastrarDisciplina();
            }
        });

    }


    public void aoClicarCadastrarDisciplina() {
        try {
            Disciplina disciplina = this.getDisciplinaFromFormulario();
            controllerDisciplinas.addDisciplina(disciplina);
            this.carregarDadosDisciplinas();
            this.clearFormularioDisciplina();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean validarFormularioProfessor() {
        if (!editTextNomeProfessor.getText().toString().equals("") &&
            !editTextUserFTP.getText().toString().equals("") &&
            !editTextSenhaFTP.getText().toString().equals("") &&
            !editTextUserQAcademico.getText().toString().equals("") &&
            !editTextSenhaQAcademico.getText().toString().equals("")) {
            return true;
        }
        return false;
    }


    public void exibirToastNotification(final String text, final int duration){
        new Thread(){
            public void run() {
                ActivityConfiguracoes.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, text, duration);
                        toast.setDuration(duration);
                        toast.show();
                    }
                });
            }
        }.start();
    }


    public void aoClicarSalvarEditarProfessor() {
        if (buttonSalvarEditarProfessor.getText().toString().equals("SALVAR")) {
            try {
                if (validarFormularioProfessor() == true) {
                    Professor professor = this.getProfessorFromFormulario();
                    controllerProfessor.saveProfessor(professor);
                    this.disableFormularioProfessor();
                    buttonSalvarEditarProfessor.setText("EDITAR");
                } else {
                    exibirToastNotification("Preencha todos os dados do professor", Toast.LENGTH_LONG);
                }
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


    public void carregarDadosDisciplinas() {
        try {
            ArrayList<Disciplina> arrayListDisciplinas = controllerDisciplinas.loadDisciplinas();
            this.atualizarListViewDisciplinasCadastradas(arrayListDisciplinas);
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


    public void clearFormularioDisciplina() {
        editTextNomeDisciplina.setText("");
        editTextCodigoDisciplina.setText("");
        editTextCurso.setText("");
        editTextTurma.setText("");
        editTextTurno.setText("");
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


    public Disciplina getDisciplinaFromFormulario() {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(editTextNomeDisciplina.getText().toString());
        disciplina.setCodigo(editTextCodigoDisciplina.getText().toString());
        disciplina.setCurso(editTextCurso.getText().toString());
        disciplina.setTurma(editTextTurma.getText().toString());
        disciplina.setTurno(editTextTurno.getText().toString());
        return disciplina;
    }


    public void popularFormularioProfessor(Professor professor) {
        editTextNomeProfessor.setText(professor.getNome());
        editTextUserFTP.setText(professor.getUserFTP());
        editTextSenhaFTP.setText(professor.getSenhaFTP());
        editTextUserQAcademico.setText(professor.getUserQAcademico());
        editTextSenhaQAcademico.setText(professor.getSenhaQAcademico());
    }


    public void atualizarListViewDisciplinasCadastradas(ArrayList<Disciplina> arrayListDisciplinas) {
        ArrayAdapter<Disciplina> adapter = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, arrayListDisciplinas);
        listViewDisciplinasCadastradas.setAdapter(adapter);
    }



}
