package com.example.bernardo.capface.entidades;

import android.os.Environment;
import android.util.Log;

import com.example.bernardo.capface.utils.CompactadorDeArquivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by bernardo on 21/11/18.
 */

public class ControllerRegistroAula {

    private static ControllerRegistroAula controllerRegistroAula = null;  // Singleton pattern
    private String diretorioDaAplicacaoSalvarConfig, diretorioDaAplicacaoSalvarRegistroAula;
    private String nameFileRegistroAula = "RegistroAula.dat";
    private String caminhoCompletoDoArquivoRegistroAula;
    private ArrayList<RegistroAula> arrayListRegistroAula = new ArrayList<>();

    public static ControllerRegistroAula createControllerRegistroAula() {  // Singleton pattern
        if (controllerRegistroAula == null) {
            controllerRegistroAula = new ControllerRegistroAula();
        }
        return controllerRegistroAula;
    }

    private ControllerRegistroAula() {
        diretorioDaAplicacaoSalvarConfig = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Configs";
        diretorioDaAplicacaoSalvarRegistroAula = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Aulas";
        File fileConfig = new File(diretorioDaAplicacaoSalvarConfig);
        if (fileConfig.exists() == false) {
            fileConfig.mkdirs();
        }
        caminhoCompletoDoArquivoRegistroAula = diretorioDaAplicacaoSalvarConfig + "/" + nameFileRegistroAula;
    }

    public void addRegistroAula(RegistroAula novoRegistroAula) throws IOException {
        arrayListRegistroAula.add(novoRegistroAula);
        String pathToSaveJSONFile = diretorioDaAplicacaoSalvarRegistroAula + "/" + novoRegistroAula.getDiretorioAula();
        novoRegistroAula.saveJsonFile(pathToSaveJSONFile);
        this.saveTodosRegistrosAula();
    }

    public void deleteRegistroAula(RegistroAula registroAula) throws IOException {
        arrayListRegistroAula.remove(registroAula);
        this.saveTodosRegistrosAula();
    }

    private void saveTodosRegistrosAula() throws IOException {
        FileOutputStream f = new FileOutputStream(new File(caminhoCompletoDoArquivoRegistroAula));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(arrayListRegistroAula);
        o.flush();
        o.close();
        f.close();
    }

    public ArrayList<RegistroAula> loadTodosRegistrosAula() throws IOException, ClassNotFoundException {
        File file = new File(caminhoCompletoDoArquivoRegistroAula);
        if (file.exists()) {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            this.arrayListRegistroAula = (ArrayList<RegistroAula>) oi.readObject();
            oi.close();
            fi.close();
        }
        return this.arrayListRegistroAula;
    }

    public void compactarRegistroAula(RegistroAula registroAula) throws IOException {
        String pathDiretorioOrigem = diretorioDaAplicacaoSalvarRegistroAula + "/" + registroAula.getDiretorioAula();
        String nomeArquivoZip = registroAula.getDiretorioAula() + ".zip";
        //String pathArquivoZip = diretorioDaAplicacaoSalvarRegistroAula + "/" + registroAula.getDiretorioAula() + "/" + nomeArquivoZip;
        String pathArquivoZip = diretorioDaAplicacaoSalvarRegistroAula + "/" + nomeArquivoZip;

        ArrayList<String> arrayListArquivosParaCompactar = (ArrayList<String>) registroAula.getArrayListImagens().clone();
        arrayListArquivosParaCompactar.add(registroAula.getFileNameJSON());

        CompactadorDeArquivo.compactarArquivos_zip(pathDiretorioOrigem, arrayListArquivosParaCompactar, pathArquivoZip);
    }

    public String getDiretorioDaAplicacaoSalvarRegistroAula() {
        return diretorioDaAplicacaoSalvarRegistroAula;
    }

}
