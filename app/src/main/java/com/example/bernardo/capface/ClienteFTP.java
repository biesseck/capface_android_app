package com.example.bernardo.capface;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClienteFTP {

    FTPClient mFtp = new FTPClient();
    String TAG = "classeFTP";


    public ClienteFTP(){

    }


    public FTPFile[] Dir(String Diretorio) {

        try {
            FTPFile[] ftpfiles = mFtp.listFiles(Diretorio);
            return ftpfiles;
        } catch (Exception e) {
            Log.e(TAG, "Erro nao foi possivel listar os arquivos e pastas do diretorio " +
                    Diretorio + e.getMessage());
        }
        return null;
    }


    public boolean Desconectar () {
        try {
            mFtp.disconnect();
            mFtp = null;
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Erro: ao desconectar" + e.getMessage());
        }
        return false;
    }


    public boolean Conectar (String Usuario, String Senha, String ip) throws IOException {
        //try {

            mFtp = new FTPClient();
            mFtp.connect(ip, 21);
            if (FTPReply.isPositiveCompletion(mFtp.getReplyCode())){
                mFtp.login(Usuario, Senha);
                mFtp.setFileType(FTP.BINARY_FILE_TYPE);
                mFtp.enterLocalPassiveMode();

                return true;
            }

        /*
        } catch (Exception e) {

            Log.e(TAG, "Erro: nÃ£o foi possivel se conectar");
        }
        */
        return false;
    }


    public boolean Upload (String Diretorio, String NomeArquivo) throws FileNotFoundException, IOException {
        //try {
            //FileInputStream arpEnviar = new FileInputStream(Environment.getExternalStorageDirectory() + Diretorio);
            FileInputStream arpEnviar = new FileInputStream(Diretorio + "/" + NomeArquivo);
            mFtp.storeFile(NomeArquivo, arpEnviar);
            //Desconectar();
            return true;
        /*
        } catch (Exception e) {
            Log.e("Upload()", "e.class: " + e.getClass());
            Log.e("Upload()", "e.message: " + e.getMessage());
            return false;
        }
        */


    }



}