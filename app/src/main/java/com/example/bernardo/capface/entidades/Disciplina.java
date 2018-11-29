package com.example.bernardo.capface.entidades;

/**
 * Created by bernardo on 19/11/18.
 */

public class Disciplina {

    private String nome;
    private String codigo;
    private String curso;
    private String turma;
    private String turno;

    public Disciplina() {

    }

    public Disciplina(String nome, String codigo, String curso, String turma, String turno) {
        this.nome = nome;
        this.codigo = codigo;
        this.curso = curso;
        this.turma = turma;
        this.turno = turno;
    }

    @Override
    public String toString() {
        String disciplinaString = nome;
        //disciplinaString += "\nCod.: " + codigo;
        disciplinaString += "\nCurso: " + curso;
        return disciplinaString;
    }

    public String toStringToSave() {
        String disciplinaString = nome + "," + codigo + "," + curso + "," + turma + "," + turno;
        return disciplinaString;
    }

    public String toStringToShowInAlertDialog() {
        String disciplinaString = "  NOME: " + nome
                                  + "\r\n\r\n  CÓDIGO: " + codigo
                                  + "\r\n\r\n  CURSO: " + curso
                                  + "\r\n\r\n  TURMA: " + turma
                                  + "\r\n\r\n  TURNO: " + turno;
        return disciplinaString;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
