package com.example.bernardo.capface.network;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

/**
 * Created by bernardo on 28/11/18.
 */

public class ClienteSocket extends Observable {

    public static int DESCONECTADO_DO_SERVIDOR = 0;
    public static int CONECTADO_AO_SERVIDOR = 1;
    public static int ENVIANDO_ARQUIVO = 2;
    public static int UPLOAD_REALIZADO = 3;
    public static int UPLOAD_NAO_REALIZADO = 4;
    public static int SERVIDOR_OFFLINE_OU_IP_ERRADO = 5;

    Socket socket;
    Thread threadConectarEnviarArquivo;
    ClienteSocket clienteSocket = this;

    boolean Conectado, arquivoEnviado;

    public ClienteSocket() {

    }


    public void conectar(final String enderecoIP, final int porta) throws IOException {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket(enderecoIP, porta);
                            Conectado = true;
                            setChanged();
                            notifyObservers(CONECTADO_AO_SERVIDOR);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Conectado = false;
                        }

                    }
                }
        ).start();
    }


    public void enviarString(DataOutputStream dos, String dado) throws IOException {
        dos.writeUTF(dado);
        dos.flush();
    }


    public boolean enviarArquivo(String pathFile) throws IOException {
        setChanged();
        notifyObservers(ENVIANDO_ARQUIVO);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream  fis = new FileInputStream(pathFile);
        byte[] buffer = new byte[4096];

        String userName = "bernardo";
        Log.i("ClienteSocket", "enviarArquivo() - userName: " + userName);
        enviarString(dos, userName);

        String pathFileSplit[] = pathFile.split("/");
        String fileName = pathFileSplit[pathFileSplit.length-1];
        Log.i("ClienteSocket", "enviarArquivo() - fileName: " + fileName);
        enviarString(dos, fileName);

        String zipFileSize = Long.toString(new File(pathFile).length());
        Log.i("ClienteSocket", "enviarArquivo() - zipFileSize: " + zipFileSize);
        enviarString(dos, zipFileSize);

        int read = 0;
        int totalLido = 0;
        int restante = (int) new File(pathFile).length();
        int iter = 0;
        while ((read = fis.read(buffer, 0, Math.min(buffer.length, restante))) > 0) {
            totalLido += read;
            restante -= read;
            iter++;
            //Log.i("ClienteSocket", "enviarArquivo() - iter: " + iter + "   read: " + read + "   totalLido: " + totalLido + "   restante: " + restante);
            dos.write(buffer, 0, read);
            dos.flush();
        }

        fis.close();
        dos.close();

        return true;
    }





    public void conectarAoServidorEnviarArquivo (final String ipServidor, final int porta, final String pathDiretorioArquivoParaEnviar, final String nomeArquivoParaEnviar) {
        threadConectarEnviarArquivo = new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - conectando...");
                        try {
                            conectar(ipServidor, porta);
                            Thread.sleep(1000);
                            Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - conectado ao servidor");

                            if (Conectado) {

                                setChanged();
                                notifyObservers(ENVIANDO_ARQUIVO);
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - enviando arquivo...");
                                arquivoEnviado = enviarArquivo(pathDiretorioArquivoParaEnviar + "/" + nomeArquivoParaEnviar);
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - arquivoEnviado: " + arquivoEnviado);

                                if (arquivoEnviado) {
                                    Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - arquivo enviado: '" + nomeArquivoParaEnviar + "'");
                                    setChanged();
                                    notifyObservers(UPLOAD_REALIZADO);
                                } else {
                                    Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - nao foi possivel enviar o arquivo '" + nomeArquivoParaEnviar);
                                    setChanged();
                                    notifyObservers(UPLOAD_NAO_REALIZADO);
                                }

                                //clienteFTP.Desconectar();
                                Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - desconectado do servidor");

                            } else {
                                setChanged();
                                notifyObservers(SERVIDOR_OFFLINE_OU_IP_ERRADO);
                            }

                        } catch (IOException e) {
                            setChanged();
                            notifyObservers(SERVIDOR_OFFLINE_OU_IP_ERRADO);

                            //Log.i("ClienteFTP", "conectarAoServidorEnviarArquivo() - " + e.getMessage());
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        threadConectarEnviarArquivo.start();
    }

}