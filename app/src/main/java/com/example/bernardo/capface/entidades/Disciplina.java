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
        return "Disciplina{" +
                "nome='" + nome + '\'' +
                ", codigo='" + codigo + '\'' +
                ", curso='" + curso + '\'' +
                ", turma='" + turma + '\'' +
                ", turno='" + turno + '\'' +
                '}';
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
