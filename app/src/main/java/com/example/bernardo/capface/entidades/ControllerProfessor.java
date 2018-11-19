package com.example.bernardo.capface.entidades;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by bernardo on 19/11/18.
 */

public class ControllerProfessor {

    private Professor professor;
    private String diretorioDaAplicacaoSalvarConfig;
    private String nameFileProfessor = "Professor.txt";
    private String caminhoCompletoDoArquivoProfessor;

    public ControllerProfessor() {
        diretorioDaAplicacaoSalvarConfig = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Configs";
        File fileConfig = new File(diretorioDaAplicacaoSalvarConfig);
        if (fileConfig.exists() == false) {
            fileConfig.mkdirs();
        }
        caminhoCompletoDoArquivoProfessor = diretorioDaAplicacaoSalvarConfig + "/" + nameFileProfessor;
    }


    public Professor loadProfessor() throws IOException {
        File fileDadosProfessor = new File(caminhoCompletoDoArquivoProfessor);
        if (fileDadosProfessor.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fileDadosProfessor));
            String dadosProfessor;
            dadosProfessor = br.readLine();
            br.close();

            String[] dadosSeparados = dadosProfessor.split(",");
            String nomeProfessor = dadosSeparados[0];
            String usuarioFTP = dadosSeparados[1];
            String senhaFTP = dadosSeparados[2];
            String usuarioQAcademico = dadosSeparados[3];
            String senhaQAcademico = dadosSeparados[4];

            Professor professor = new Professor();
            professor.setNome(nomeProfessor);
            professor.setUserFTP(usuarioFTP);
            professor.setSenhaFTP(senhaFTP);
            professor.setUserQAcademico(usuarioQAcademico);
            professor.setSenhaQAcademico(senhaQAcademico);
            this.professor = professor;
            Log.e("ControllerProfessor", "loadProfessor(): dados do professor carregados com sucesso!");
        }

        return professor;
    }

    public void saveProfessor(Professor professor) throws IOException {
        this.professor = professor;
        FileOutputStream ArquivoGravar = new FileOutputStream(new File(caminhoCompletoDoArquivoProfessor));
        ArquivoGravar.write(professor.toString().getBytes());
        //ArquivoGravar.write((nomeProfessor + "," + usuarioFTP + "," + senhaFTP).getBytes());
        ArquivoGravar.close();
        Log.e("ControllerProfessor", "saveProfessor(): dados do professor salvos com sucesso!");
    }

}
