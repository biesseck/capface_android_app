package com.example.bernardo.capface.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bernardo on 21/11/18.
 */

public class RegistroAula implements Serializable {

    private static final long serialVersionUID = 9125271018717732648L;

    private String curso;
    private String disciplina;
    private String codigoDisciplina;
    private String professor;
    private String turma;
    private String tipoDisciplina;
    private String bimestre;
    private String data;
    private String qtdeAulas;
    private String horaInicio;
    private String horaFim;
    private String conteudoAula;
    private boolean enviado = false;
    private String diretorioAula;
    private ArrayList<String> arrayListImagens = new ArrayList<>();
    private String conteudoJSON;
    private String fileNameJSON = "inicial.json";

    public RegistroAula() {

    }

    public RegistroAula(String curso, String disciplina, String tipoDisciplina, String data, String qtdeAulas, String horaInicio, String horaFim, String conteudoAula, String diretorioAula, ArrayList<String> arrayListImagens) {
        this.curso = curso;
        this.disciplina = disciplina;
        this.tipoDisciplina = tipoDisciplina;
        this.data = data;
        this.qtdeAulas = qtdeAulas;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.conteudoAula = conteudoAula;
        this.diretorioAula = diretorioAula;
        this.arrayListImagens = arrayListImagens;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
        this.makeConteudoJSON();
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
        this.makeConteudoJSON();
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
        this.makeConteudoJSON();
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
        this.makeConteudoJSON();
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
        this.makeConteudoJSON();
    }

    public String getTipoDisciplina() {
        return tipoDisciplina;
    }

    public void setTipoDisciplina(String tipoDisciplina) {
        this.tipoDisciplina = tipoDisciplina;
        this.makeConteudoJSON();
    }

    public String getBimestre() {
        return bimestre;
    }

    public void setBimestre(String bimestre) {
        this.bimestre = bimestre;
        this.makeConteudoJSON();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.makeConteudoJSON();
    }

    public String getQtdeAulas() {
        return qtdeAulas;
    }

    public void setQtdeAulas(String qtdeAulas) {
        this.qtdeAulas = qtdeAulas;
        this.makeConteudoJSON();
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
        this.makeConteudoJSON();
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
        this.makeConteudoJSON();
    }

    public String getConteudoAula() {
        return conteudoAula;
    }

    public void setConteudoAula(String conteudoAula) {
        this.conteudoAula = conteudoAula;
        this.makeConteudoJSON();
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public String getDiretorioAula() {
        return diretorioAula;
    }

    public void setDiretorioAula(String diretorioAula) {
        this.diretorioAula = diretorioAula;
        this.makeConteudoJSON();
    }

    public ArrayList<String> getArrayListImagens() {
        return arrayListImagens;
    }

    public void setArrayListImagens(ArrayList<String> arrayListImagens) {
        this.arrayListImagens = arrayListImagens;
        this.makeConteudoJSON();
    }

    public void addImagem(String fileNameImagem) {
        this.arrayListImagens.add(fileNameImagem);
        this.makeConteudoJSON();
    }

    public String getConteudoJSON() {
        return conteudoJSON;
    }

    private void makeConteudoJSON() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("Curso", this.curso);
            obj.put("Disciplina", this.disciplina);
            obj.put("Codigo", this.codigoDisciplina);
            obj.put("Professor", this.professor);
            obj.put("Turma", this.turma);
            obj.put("data da aula", this.data);
            obj.put("Horario de inicio", this.horaInicio);
            obj.put("Horario de fim", this.horaFim);
            obj.put("quantidade de aulas", this.qtdeAulas);
            obj.put("Pergunta", this.tipoDisciplina);
            obj.put("Bimestre", this.bimestre);
            obj.put("Conteudo", this.conteudoAula);
            obj.put("Diretorio aula", diretorioAula);

            JSONArray jsonArrayImgFiles = new JSONArray();
            for (int i=0; i<arrayListImagens.size(); i++) {
                jsonArrayImgFiles.put(arrayListImagens.get(i));
            }
            obj.put("imgFiles", jsonArrayImgFiles);

            this.conteudoJSON = obj.toString(3).replace("\\/", "/");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveJsonFile(String pathDir) throws IOException {
        FileWriter file = new FileWriter(pathDir + "/" + fileNameJSON);
        file.write(this.conteudoJSON);
        file.flush();
        file.close();
    }

    public String getFileNameJSON() {
        return fileNameJSON;
    }

    public void setFileNameJSON(String fileNameJSON) {
        this.fileNameJSON = fileNameJSON;
    }

    public String toString() {
        String registroAulaString = curso;
        registroAulaString += ", " + disciplina;
        registroAulaString += ", " + codigoDisciplina;
        registroAulaString += ", " + professor;
        registroAulaString += ", " + turma;
        registroAulaString += ", " + tipoDisciplina;
        registroAulaString += ", " + bimestre;
        registroAulaString += ", " + data;
        registroAulaString += ", " + qtdeAulas;
        registroAulaString += ", " + horaInicio;
        registroAulaString += ", " + horaFim;
        registroAulaString += ", " + conteudoAula;

        return registroAulaString;
    }

    public String toStringForListView() {
        String registroAulaString = "Disc: " + disciplina + "  -  " + codigoDisciplina;
        registroAulaString += "\r\n" + "Bimestre: " + bimestre + "   Data: " + data;
        registroAulaString += "\r\n" + qtdeAulas + " aulas   Inicio: " + horaInicio + "   Fim: " + horaFim;

        return registroAulaString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroAula that = (RegistroAula) o;

        if (curso != null ? !curso.equals(that.curso) : that.curso != null) return false;
        if (disciplina != null ? !disciplina.equals(that.disciplina) : that.disciplina != null)
            return false;
        if (tipoDisciplina != null ? !tipoDisciplina.equals(that.tipoDisciplina) : that.tipoDisciplina != null)
            return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (qtdeAulas != null ? !qtdeAulas.equals(that.qtdeAulas) : that.qtdeAulas != null)
            return false;
        if (horaInicio != null ? !horaInicio.equals(that.horaInicio) : that.horaInicio != null)
            return false;
        return horaFim != null ? horaFim.equals(that.horaFim) : that.horaFim == null;
    }

}
