package com.example.bernardo.capface.network;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

public class ClienteFTP extends Observable {

    public static int DESCONECTADO_DO_SERVIDOR = 0;
    public static int CONECTADO_AO_SERVIDOR = 1;
    public static int ENVIANDO_ARQUIVO = 2;
    public static int UPLOAD_REALIZADO = 3;
    public static int UPLOAD_NAO_REALIZADO = 4;
    public static int SERVIDOR_OFFLINE_OU_IP_ERRADO = 5;

    FTPClient mFtp = new FTPClient();
    ClienteFTP clienteFTP = this;
    boolean Conectado, arquivoEnviado;

    String TAG = "classeFTP";

    Thread threadConectarEnviarArquivo;


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

        return false;
    }


    public boolean Upload (String Diretorio, String NomeArquivo) throws FileNotFoundException, IOException {
            //FileInputStream arpEnviar = new FileInputStream(Environment.getExternalStorageDirectory() + Diretorio);
            FileInputStream arpEnviar = new FileInputStream(Diretorio + "/" + NomeArquivo);
            mFtp.storeFile(NomeArquivo, arpEnviar);
            //Desconectar();
            return true;
    }


    public void conectarAoServidorEnviarArquivo (final String ipServidor, final String user, final String password, final String pathDiretorioArquivoParaEnviar, final String nomeArquivoParaEnviar) {
        threadConectarEnviarArquivo = new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - conectando...");
                        try {
                            Conectado = clienteFTP.Conectar(user, password, ipServidor);
                            Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - conectado ao servidor");

                            setChanged();
                            notifyObservers(ENVIANDO_ARQUIVO);
                            Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - enviando arquivo...");
                            arquivoEnviado = clienteFTP.Upload(pathDiretorioArquivoParaEnviar, nomeArquivoParaEnviar);

                            if (arquivoEnviado) {
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - arquivo enviado: '" + nomeArquivoParaEnviar + "'");
                                setChanged();
                                notifyObservers(UPLOAD_REALIZADO);
                            } else {
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - nao foi possivel enviar o arquivo '" + nomeArquivoParaEnviar);
                                setChanged();
                                notifyObservers(UPLOAD_NAO_REALIZADO);
                            }

                            clienteFTP.Desconectar();
                            Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - desconectado do servidor");

                        } catch (IOException e) {
                            setChanged();
                            notifyObservers(SERVIDOR_OFFLINE_OU_IP_ERRADO);

                            //Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - " + e.getMessage());
                            //e.printStackTrace();
                        }

                    }
                }
        );
        threadConectarEnviarArquivo.start();
    }


}