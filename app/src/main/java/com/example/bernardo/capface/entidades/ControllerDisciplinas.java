package com.example.bernardo.capface.entidades;

import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bernardo on 19/11/18.
 */

public class ControllerDisciplinas {

    private static ControllerDisciplinas controllerDisciplinas = null;  // Singleton pattern
    ArrayList<Disciplina> arrayListDisciplinas = new ArrayList<>();
    private String diretorioDaAplicacaoSalvarConfig;
    private String nameFileDisciplinas = "Disciplinas.txt";
    private String caminhoCompletoDoArquivoDisciplinas;

    public static ControllerDisciplinas createControllerDisciplinas() {  // Singleton pattern
        if (controllerDisciplinas == null) {
            controllerDisciplinas = new ControllerDisciplinas();
        }
        return controllerDisciplinas;
    }

    private ControllerDisciplinas() {
        diretorioDaAplicacaoSalvarConfig = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Configs";
        File fileConfig = new File(diretorioDaAplicacaoSalvarConfig);
        if (fileConfig.exists() == false) {
            fileConfig.mkdirs();
        }
        caminhoCompletoDoArquivoDisciplinas = diretorioDaAplicacaoSalvarConfig + "/" + nameFileDisciplinas;
    }

    public void addDisciplina(Disciplina disciplina) throws IOException {
        arrayListDisciplinas.add(disciplina);
        FileOutputStream ArquivoGravar = new FileOutputStream(new File(caminhoCompletoDoArquivoDisciplinas));
        for (int i=0; i<arrayListDisciplinas.size(); i++) {
            ArquivoGravar.write((arrayListDisciplinas.get(i).toStringToSave() + "\r\n").getBytes());
        }
        ArquivoGravar.close();
        Log.e("ControllerDisciplinas", "addDisciplina(): dados das disciplinas salvos com sucesso!");
        Log.e("ControllerDisciplinas", "addDisciplina(): arrayListDisciplinas.size(): " + arrayListDisciplinas.size());
    }

    public ArrayList<Disciplina> loadDisciplinas() throws IOException {
        File fileDadosDisciplinas = new File(caminhoCompletoDoArquivoDisciplinas);
        if (fileDadosDisciplinas.exists()) {
            arrayListDisciplinas.clear();
            BufferedReader leitorDeArquivo = new BufferedReader(new FileReader(fileDadosDisciplinas));
            String dadosDisciplina;
            String[] dadosSeparados;
            Disciplina disciplina;

            while ((dadosDisciplina = leitorDeArquivo.readLine()) != null) {
                dadosSeparados = dadosDisciplina.split(",");
                disciplina = new Disciplina();
                disciplina.setNome(dadosSeparados[0]);
                disciplina.setCodigo(dadosSeparados[1]);
                disciplina.setCurso(dadosSeparados[2]);
                disciplina.setTurma(dadosSeparados[3]);
                disciplina.setTurno(dadosSeparados[4]);
                arrayListDisciplinas.add(disciplina);
                Log.e("ControllerDisciplinas", "loadDisciplinas(): disciplina: " + disciplina.toString());
            }

            leitorDeArquivo.close();
        }
        Log.e("ControllerDisciplinas", "loadDisciplinas(): dados das disciplinas carregados com sucesso!");
        Log.e("ControllerDisciplinas", "loadDisciplinas(): arrayListDisciplinas.size(): " + arrayListDisciplinas.size());
        return arrayListDisciplinas;
    }

}
