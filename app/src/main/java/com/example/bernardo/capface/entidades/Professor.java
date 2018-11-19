package com.example.bernardo.capface.entidades;

/**
 * Created by bernardo on 19/11/18.
 */

public class Professor {

    private String nome;
    private String userFTP;
    private String senhaFTP;
    private String userQAcademico;
    private String senhaQAcademico;

    public Professor() {

    }

    public Professor(String nome, String userFTP, String senhaFTP, String userQAcademico, String senhaQAcademico) {
        this.nome = nome;
        this.userFTP = userFTP;
        this.senhaFTP = senhaFTP;
        this.userQAcademico = userQAcademico;
        this.senhaQAcademico = senhaQAcademico;
    }

    @Override
    public String toString() {
        String professorString = nome + "," + userFTP + "," + senhaFTP + "," + userQAcademico + "," + senhaQAcademico;
        return professorString;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUserFTP() {
        return userFTP;
    }

    public void setUserFTP(String userFTP) {
        this.userFTP = userFTP;
    }

    public String getSenhaFTP() {
        return senhaFTP;
    }

    public void setSenhaFTP(String senhaFTP) {
        this.senhaFTP = senhaFTP;
    }

    public String getUserQAcademico() {
        return userQAcademico;
    }

    public void setUserQAcademico(String userQAcademico) {
        this.userQAcademico = userQAcademico;
    }

    public String getSenhaQAcademico() {
        return senhaQAcademico;
    }

    public void setSenhaQAcademico(String senhaQAcademico) {
        this.senhaQAcademico = senhaQAcademico;
    }
}
