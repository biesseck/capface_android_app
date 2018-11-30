package com.example.bernardo.capface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
    Disciplina disciplinaSendoEditada;

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
        listViewDisciplinasCadastradas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                aoClicarLongoEmUmaDisciplinaCadastrada(parent, view, position, id);
                return true;
            }
        });
        listViewDisciplinasCadastradas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aoClicarEmUmaDisciplinaCadastrada(parent, view, position, id);
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


    public void aoClicarEmUmaDisciplinaCadastrada(AdapterView<?> parent, View view, int position, long id) {
        Disciplina disciplina = controllerDisciplinas.getDisciplina(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disciplina");
        builder.setMessage(disciplina.toStringToShowInAlertDialog());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }


    /*
    public void aoClicarLongoEmUmaDisciplinaCadastrada(AdapterView<?> parent, View view, int position, long id) {
        this.disciplinaSendoEditada = controllerDisciplinas.getDisciplina(position);
        popularFormularioDisciplina(disciplinaSendoEditada);
        buttonCadastrarDisciplina.setText("SALVAR");
        listViewDisciplinasCadastradas.setEnabled(false);
    }
    */


    public void aoClicarLongoEmUmaDisciplinaCadastrada(AdapterView<?> parent, View view, final int position, long id) {
        disciplinaSendoEditada = controllerDisciplinas.getDisciplina(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disciplina");
        builder.setMessage(disciplinaSendoEditada.toStringToShowInAlertDialog());
        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                disciplinaSendoEditada = controllerDisciplinas.getDisciplina(position);
                popularFormularioDisciplina(disciplinaSendoEditada);
                buttonCadastrarDisciplina.setText("SALVAR");
                listViewDisciplinasCadastradas.setEnabled(false);
            }
        });
        builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    controllerDisciplinas.excluirDisciplina(position);
                    carregarDadosDisciplinas();
                    exibirToastNotification("Disciplina excluída com sucesso", Toast.LENGTH_LONG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }


    public void aoClicarCadastrarDisciplina() {
        if (buttonCadastrarDisciplina.getText().toString().equals("CADASTRAR")) {
            if (validarFormularioDisciplina() == true) {
                try {
                    Disciplina disciplina = this.getDisciplinaFromFormulario();
                    controllerDisciplinas.addDisciplina(disciplina);
                    this.carregarDadosDisciplinas();
                    this.clearFormularioDisciplina();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                exibirToastNotification("Preencha todos os dados da disciplina", Toast.LENGTH_LONG);
            }

        } else if (buttonCadastrarDisciplina.getText().toString().equals("SALVAR")) {
            if (validarFormularioDisciplina() == true) {
                try {
                    disciplinaSendoEditada.setNome(editTextNomeDisciplina.getText().toString());
                    disciplinaSendoEditada.setCodigo(editTextCodigoDisciplina.getText().toString());
                    disciplinaSendoEditada.setCurso(editTextCurso.getText().toString());
                    disciplinaSendoEditada.setTurma(editTextTurma.getText().toString());
                    disciplinaSendoEditada.setTurno(editTextTurno.getText().toString());
                    controllerDisciplinas.atualizarArquivoDeDisciplinas();
                    this.carregarDadosDisciplinas();
                    this.clearFormularioDisciplina();
                    listViewDisciplinasCadastradas.setEnabled(true);
                    buttonCadastrarDisciplina.setText("CADASTRAR");
                    exibirToastNotification("Disciplina atualizada com sucesso", Toast.LENGTH_LONG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                exibirToastNotification("Não é possível atualizar disciplina com dados vazios", Toast.LENGTH_LONG);
            }
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


    public boolean validarFormularioDisciplina() {
        if (!editTextNomeDisciplina.getText().toString().equals("") &&
            !editTextCodigoDisciplina.getText().toString().equals("") &&
            !editTextCurso.getText().toString().equals("") &&
            !editTextTurma.getText().toString().equals("") &&
            !editTextTurno.getText().toString().equals("")) {
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


    public void popularFormularioDisciplina(Disciplina disciplina) {
        editTextNomeDisciplina.setText(disciplina.getNome());
        editTextCodigoDisciplina.setText(disciplina.getCodigo());
        editTextCurso.setText(disciplina.getCurso());
        editTextTurma.setText(disciplina.getTurma());
        editTextTurno.setText(disciplina.getTurno());
    }


    public void atualizarListViewDisciplinasCadastradas(ArrayList<Disciplina> arrayListDisciplinas) {
        ArrayAdapter<Disciplina> adapter = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, arrayListDisciplinas);
        listViewDisciplinasCadastradas.setAdapter(adapter);
    }



}
