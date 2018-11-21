package com.example.bernardo.capface.entidades;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bernardo on 20/11/18.
 */

public class ControllerCapturedImages {

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private long dataHoraInicial;
    private long dataHoraFinal;
    String diretorioPadraoCamera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera";
    String diretorioPadraoCapface;

    ArrayList<String> arrayListNomesImagensCapturadas = new ArrayList<>();

    public ControllerCapturedImages(String diretorioPadraoCapface) {
        this.diretorioPadraoCapface = diretorioPadraoCapface;
    }

    public boolean existemImagensCapturadasDestaAula(long dataHoraInicial) {
        long dataHoraFinal = new Date().getTime();
        File diretorio = new File(diretorioPadraoCamera);
        File[] files = diretorio.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].lastModified() >= dataHoraInicial && files[i].lastModified() <= dataHoraFinal){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getFileNamesImagensCapturadas() {
        return arrayListNomesImagensCapturadas;
    }

    public void moverImagensCapturadasParaDiretorioCapface(long dataHoraInicial, String diretorioPadraoCapface) {
        this.dataHoraInicial = dataHoraInicial;
        dataHoraFinal = new Date().getTime();

        File diretorio = new File(diretorioPadraoCamera);
        File[] files = diretorio.listFiles();

        arrayListNomesImagensCapturadas.clear();
        for (int i = 0; i < files.length; i++) {
            if ( files[i].lastModified() >= this.dataHoraInicial && files[i].lastModified() <= this.dataHoraFinal){
                //Log.i("ControllerCapturedImage", "onActivityResult() -> FOTOS_TIRADAS_AGORA - FileName:" + files[i].getName() + "   Data: " + files[i].lastModified() +  "   Diretorio: " + files[i].getPath());
                moverArquivo(diretorioPadraoCamera+"/"+files[i].getName(), diretorioPadraoCapface+"/"+files[i].getName());
                arrayListNomesImagensCapturadas.add(files[i].getName());
            }
        }
        //Log.i("ControllerCapturedImage", "onActivityResult() -> Qtde de imagens capturadas agora: "+ arrayListNomesImagensCapturadas.size());
    }


    public void moverArquivo(String arquivoOriginal, String arquivoDestino) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try{
            File afile =new File(arquivoOriginal);
            File bfile =new File(arquivoDestino);
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);  //copy the file content in bytes
            }

            inStream.close();
            outStream.close();
            afile.delete();  //delete the original file

        }catch(IOException e){
            e.printStackTrace();
        }
    }


} // end public class ControllerCapturedImages
