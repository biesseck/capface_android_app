package com.example.bernardo.capface;


public class ManipuladorArquivos {

    private static ManipuladorArquivos manipuladorArquivos = null;
    private String professorFileName = "Professor.txt";
    private String disciplinasFileName = "Disciplinas.txt";
    private String aulasRegistradasFileName = "AulasRegistradas.txt";

    public static ManipuladorArquivos createManipuladorArquivos() {
        if (manipuladorArquivos == null) {
            manipuladorArquivos = new ManipuladorArquivos();
        }
        return manipuladorArquivos;
    }


    private ManipuladorArquivos() {

    }


    public void salvarDadosProfessor() {

    }

    public void carregarDadosProfessor() {

    }


    public void addDisciplina() {

    }

    public void carregarDisciplinas() {

    }


    public void addAula() {

    }

    public void carregarAulas() {

    }


    public String getAulasRegistradasFileName() {
        return aulasRegistradasFileName;
    }

    public void setAulasRegistradasFileName(String aulasRegistradasFileName) {
        this.aulasRegistradasFileName = aulasRegistradasFileName;
    }

    public String getProfessorFileName() {
        return professorFileName;
    }

    public void setProfessorFileName(String professorFileName) {
        this.professorFileName = professorFileName;
    }

    public String getDisciplinasFileName() {
        return disciplinasFileName;
    }

    public void setDisciplinasFileName(String disciplinasFileName) {
        this.disciplinasFileName = disciplinasFileName;
    }
}
