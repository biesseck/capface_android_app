package com.example.bernardo.capface.network;

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

        /*
        } catch (Exception e) {

            Log.e(TAG, "Erro: nÃ£o foi possivel se conectar");
        }
        */
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
                            if (Conectado) {
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - conectado ao servidor");

                                //arquivoEnviado = clienteFTP.Upload(pathArquivoParaEnviar);
                                arquivoEnviado = clienteFTP.Upload(pathDiretorioArquivoParaEnviar, nomeArquivoParaEnviar);
                                if (arquivoEnviado) {
                                    Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - arquivo enviado: '" + nomeArquivoParaEnviar + "'");
                                } else {
                                    Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - nao foi possivel enviar o arquivo '" + nomeArquivoParaEnviar);
                                }

                                clienteFTP.Desconectar();
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - desconectado do servidor");

                            } else {
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - nao foi possivel conectar");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        threadConectarEnviarArquivo.start();
    }


}