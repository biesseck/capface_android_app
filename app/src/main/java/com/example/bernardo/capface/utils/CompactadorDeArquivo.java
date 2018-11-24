package com.example.bernardo.capface.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by bernardo on 22/11/18.
 */

public class CompactadorDeArquivo {

    public CompactadorDeArquivo() {

    }

    public static void compactarArquivos_zip(String pathDiretorioOrigem, ArrayList<String> arrayListNomesArquivosParaZipar, String pathArquivoZip) throws IOException {
        //String caminhoCompletoArquivoZip = diretorio + "/" + NomeArquivoZip;
        byte[] buffer = new byte[1024];
        ZipOutputStream Saida = new ZipOutputStream(new FileOutputStream((pathArquivoZip)));
        Log.i("CompactadorDeArquivo", "compactarArquivos_zip() - Arquivo '" + pathArquivoZip + "' criado");
        Saida.setLevel(Deflater.DEFAULT_COMPRESSION);

        for (int i=0;i<arrayListNomesArquivosParaZipar.size();i++){
            FileInputStream Entrada = new FileInputStream(pathDiretorioOrigem + "/" + arrayListNomesArquivosParaZipar.get(i));
            Saida.putNextEntry(new ZipEntry(arrayListNomesArquivosParaZipar.get(i)));
            Log.i("CompactadorDeArquivo", "compactarArquivos_zip() - Arquivo '" + arrayListNomesArquivosParaZipar.get(i) + "' adicionado ao ZIP");
            int  len;

            while ((len = Entrada.read(buffer))>0) {
                Saida.write (buffer,  0 , len);
            }

            Saida.closeEntry();
            Entrada.close();
        }
        Saida.close();
    }

}
