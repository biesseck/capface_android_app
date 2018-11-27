package com.example.bernardo.capface;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bernardo.capface.entidades.ControllerCapturedImages;
import com.example.bernardo.capface.entidades.ControllerDisciplinas;
import com.example.bernardo.capface.entidades.ControllerProfessor;
import com.example.bernardo.capface.entidades.ControllerRegistroAula;
import com.example.bernardo.capface.entidades.Disciplina;
import com.example.bernardo.capface.entidades.RegistroAula;
import com.example.bernardo.capface.network.ClienteFTP;

import java.io.File;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class ActivityNovaAula extends AppCompatActivity implements Observer {

    int REQUEST_IMAGE_CAPTURE = 1;

    Spinner spinnerCursos;
    Spinner spinnerDisciplina;
    Spinner spinnerTipoDisciplina;
    EditText editTextBimestre;
    EditText editTextDataAula;
    EditText editTextQtdeAulas;
    EditText editTextHoraInicioAula;
    EditText editTextConteudoAula;
    EditText editTextHoraFimAula;
    Button buttonCamera;
    Button buttonEnviar;
    EditText editTextDiretorioCapface;
    EditText editTextDiretorioAulaAtual;
    ListView listViewImageFilesAulaAtual;
    EditText editTextIPServidor;

    String diretorioPadraoCapfaceAulas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Aulas";
    String diretorioAulaAtual, pathDiretorioAulaAtual;
    private long dataHoraInicial;
    ArrayList<String> arrayListNomesImagensCapturadas = new ArrayList<>();

    ControllerDisciplinas controllerDisciplinas = ControllerDisciplinas.createControllerDisciplinas();
    ControllerCapturedImages controllerCapturedImages = new ControllerCapturedImages(diretorioPadraoCapfaceAulas);
    ControllerRegistroAula controllerRegistroAula = ControllerRegistroAula.createControllerRegistroAula();
    ControllerProfessor controllerProfessor = new ControllerProfessor();

    ClienteFTP clienteFTP;

    ActivityNovaAula activityNovaAula = this;
    ProgressDialog dialog;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_aula);

        inicializarComponentes();
        this.carregarDadosDisciplinas();
        this.atualizarListViewImageFilesAulaAtual(arrayListNomesImagensCapturadas);
        this.criarDiretorio(diretorioPadraoCapfaceAulas);
    }


    public void inicializarComponentes() {
        spinnerCursos = (Spinner) findViewById(R.id.spinnerCursos);
        spinnerDisciplina = (Spinner) findViewById(R.id.spinnerDisciplina);
        spinnerTipoDisciplina = (Spinner) findViewById(R.id.spinnerTipoDisciplina);

        editTextBimestre = (EditText) findViewById(R.id.editTextBimestre);

        editTextDataAula = (EditText) findViewById(R.id.editTextDataAula);
        editTextDataAula.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    aoClicarParaSelecionarDataDaAula();
                }
                return false;
            }
        });

        editTextQtdeAulas = (EditText) findViewById(R.id.editTextQtdeAulas);

        editTextHoraInicioAula = (EditText) findViewById(R.id.editTextHoraInicioAula);
        editTextHoraInicioAula.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    aoClicarParaSelecionarHoraDaAula(editTextHoraInicioAula);
                }
                return false;
            }
        });

        editTextHoraFimAula = (EditText) findViewById(R.id.editTextHoraFimAula);
        editTextHoraFimAula.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    aoClicarParaSelecionarHoraDaAula(editTextHoraFimAula);
                }
                return false;
            }
        });

        editTextConteudoAula = (EditText) findViewById(R.id.editTextConteudoAula);

        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aoClicarNoBotaoCamera();
            }
        });

        buttonEnviar = (Button) findViewById(R.id.buttonEnviar);
        buttonEnviar.setEnabled(false);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aoClicarNoBotaoEnviar();
            }
        });

        editTextDiretorioCapface = (EditText) findViewById(R.id.editTextDiretorioCapface);
        editTextDiretorioCapface.setText(diretorioPadraoCapfaceAulas);

        editTextDiretorioAulaAtual = (EditText) findViewById(R.id.editTextDiretorioAulaAtual);

        listViewImageFilesAulaAtual = (ListView) findViewById(R.id.listViewImageFilesAulaAtual);
        listViewImageFilesAulaAtual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        editTextIPServidor = (EditText) findViewById(R.id.editTextIPServidor);
    }


    public void criarDiretorio(String pathDir) {
        File file = new File(pathDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public String getDirNameAulaAtualFromFormulario() {
        String curso = (String) spinnerCursos.getSelectedItem();
        String disciplina = ((Disciplina) spinnerDisciplina.getSelectedItem()).getNome();
        String tipoDisciplina = (String) spinnerTipoDisciplina.getSelectedItem();
        String dataAula = editTextDataAula.getText().toString().replace('/', '-');
        String qtdeAulas = editTextQtdeAulas.getText().toString();
        String horaInicio = editTextHoraInicioAula.getText().toString().replace(':', '-');
        String horaFim = editTextHoraFimAula.getText().toString().replace(':', '-');

        String dirNameAulaAtual = curso + "_" + disciplina + "_" + tipoDisciplina + "_" + dataAula + "_" + qtdeAulas + "_" + horaInicio + "_" + horaFim;
        return dirNameAulaAtual;
    }


    public boolean formularioAulaEstaValido() {
        if (spinnerDisciplina.getSelectedItem() != null &&
            !editTextBimestre.getText().toString().equals("") &&
            !editTextDataAula.getText().toString().equals("") &&
            !editTextQtdeAulas.getText().toString().equals("") &&
            !editTextHoraInicioAula.getText().toString().equals("") &&
            !editTextHoraFimAula.getText().toString().equals("") &&
            !editTextConteudoAula.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }


    public void aoClicarNoBotaoCamera() {
        if (formularioAulaEstaValido()) {
            //Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);            // SALVA UMA UNICA FOTO QUANDO FECHA A CAMERA
            Intent irParaCamera = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);  // PERMITE SALVAR VARIAS IMAGENS ANTES DE FECHAR A CAMERA
            dataHoraInicial = new Date().getTime(); // Capturar data e hora do sistema (dataHoraInicial)
            startActivityForResult(irParaCamera, REQUEST_IMAGE_CAPTURE);    // inicializa a camera

        } else {
            exibirToastNotification("Preencha todos os dados da aula!", Toast.LENGTH_LONG);
        }
    }


    // Metodo executado automaticamente quando a camera e' finalizada e a execucao retorna
    // para a aplicacao
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {  // se retornou da camera
            aoTerminarCapturaDeImagens();
        }
    }


    public void aoTerminarCapturaDeImagens() {
        if (controllerCapturedImages.existemImagensCapturadasDestaAula(dataHoraInicial)) {  // se o usuario capturou alguma foto
            diretorioAulaAtual = this.getDirNameAulaAtualFromFormulario();
            pathDiretorioAulaAtual = diretorioPadraoCapfaceAulas + "/" + this.getDirNameAulaAtualFromFormulario();
            this.criarDiretorio(pathDiretorioAulaAtual);
            controllerCapturedImages.moverImagensCapturadasParaDiretorioCapface(dataHoraInicial, pathDiretorioAulaAtual);
            arrayListNomesImagensCapturadas = controllerCapturedImages.getFileNamesImagensCapturadas();
            notificarGalleryApp();
            editTextDiretorioAulaAtual.setText(diretorioAulaAtual);
            this.atualizarListViewImageFilesAulaAtual(arrayListNomesImagensCapturadas);
            buttonEnviar.setEnabled(true);

            /*
            for (int i = 0; i < arrayListNomesImagensCapturadas.size(); i++) {
                Log.i("ActivityNovaAula", "onActivityResult() -> FOTO_TIRADA_AGORA - FileName:" + arrayListNomesImagensCapturadas.get(i));
            }
            Log.i("ActivityNovaAula", "onActivityResult() -> Qtde de imagens capturadas agora: " + arrayListNomesImagensCapturadas.size());
            */

        } else {
            buttonEnviar.setEnabled(false);
            //Log.i("ActivityNovaAula", "Nenhuma imagem capturada para esta aula");
            exibirToastNotification("Nenhuma imagem capturada!", Toast.LENGTH_LONG);
        }
    }


    public void aoClicarParaSelecionarDataDaAula() {
        Calendar mCurrentDate = Calendar.getInstance();
        int dia = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int mes = mCurrentDate.get(Calendar.MONTH);
        int ano = mCurrentDate.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dataString = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                    Date dataDate = sdf.parse(dataString);
                    editTextDataAula.setText(sdf.format(dataDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, ano, mes, dia);
        datePickerDialog.show();
    }


    private void aoClicarParaSelecionarHoraDaAula(final EditText editText) {
        TimePickerDialog.OnTimeSetListener  mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String horarioString = hora + ":" + minuto;
                    Date horarioDate = sdf.parse(horarioString);
                    editText.setText(sdf.format(horarioDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Calendar calNow = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, mTimeSetListener, calNow.get(Calendar.HOUR_OF_DAY), calNow.get(Calendar.MINUTE), true);
        dialog.show();
    }


    public void exibirToastNotification(final String text, final int duration){
        new Thread(){
            public void run() {
                ActivityNovaAula.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, text, duration);
                        toast.setDuration(duration);
                        toast.show();
                    }
                });
            }
        }.start();
    }


    public void exibirProgressDialog(final String text){
        new Thread(){
            public void run() {
                ActivityNovaAula.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog = new ProgressDialog(activityNovaAula);
                        dialog.setMessage(text);
                        dialog.show();
                    }
                });
            }
        }.start();
    }


    public void fecharProgressDialog(){
        new Thread(){
            public void run() {
                ActivityNovaAula.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        }.start();
    }


    public void notificarGalleryApp() {
        String diretorioPadraoCamera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera";
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(diretorioPadraoCamera))));
    }


    public RegistroAula getNewRegistroAulaFromFormulario() {
        try {
            RegistroAula novoRegistroAula = new RegistroAula();
            novoRegistroAula.setCurso(spinnerCursos.getSelectedItem().toString());
            novoRegistroAula.setDisciplina(((Disciplina) spinnerDisciplina.getSelectedItem()).getNome());
            novoRegistroAula.setCodigoDisciplina(((Disciplina) spinnerDisciplina.getSelectedItem()).getCodigo());
            novoRegistroAula.setProfessor(new ControllerProfessor().loadProfessor().getNome());
            novoRegistroAula.setTurma(((Disciplina) spinnerDisciplina.getSelectedItem()).getTurma());
            novoRegistroAula.setTipoDisciplina(spinnerTipoDisciplina.getSelectedItem().toString());
            novoRegistroAula.setBimestre(editTextBimestre.getText().toString());
            novoRegistroAula.setData(editTextDataAula.getText().toString());
            novoRegistroAula.setQtdeAulas(editTextQtdeAulas.getText().toString());
            novoRegistroAula.setHoraInicio(editTextHoraInicioAula.getText().toString());
            novoRegistroAula.setHoraFim(editTextHoraFimAula.getText().toString());
            novoRegistroAula.setConteudoAula(editTextConteudoAula.getText().toString());
            novoRegistroAula.setDiretorioAula(getDirNameAulaAtualFromFormulario());
            novoRegistroAula.setArrayListImagens(arrayListNomesImagensCapturadas);
            return novoRegistroAula;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void aoClicarNoBotaoEnviar() {
        try {
            closeKeyboard();

            RegistroAula novoRegistroAula = this.getNewRegistroAulaFromFormulario();
            //Log.i("aoClicarNoBotaoEnviar()", "novoRegistroAula criado");

            controllerRegistroAula.addRegistroAula(novoRegistroAula);
            //Log.i("aoClicarNoBotaoEnviar()", "novoRegistroAula adicionado");

            // zipar diretorio do RegistroAula
            exibirToastNotification("Compactando imagens...", Toast.LENGTH_SHORT);
            controllerRegistroAula.compactarRegistroAula(novoRegistroAula);
            //Log.i("aoClicarNoBotaoEnviar()", "novoRegistroAula adicionado");

            //String ipServidor = "172.16.230.16";
            String ipServidor = editTextIPServidor.getText().toString();
            String user = controllerProfessor.loadProfessor().getUserFTP();
            String password = controllerProfessor.loadProfessor().getSenhaFTP();
            String pathDiretorioArquivoParaEnviar = controllerRegistroAula.getDiretorioDaAplicacaoSalvarRegistroAula();
            String nomeArquivoParaEnviar = novoRegistroAula.getDiretorioAula() + ".zip";

            clienteFTP = new ClienteFTP();
            clienteFTP.addObserver(this);
            clienteFTP.conectarAoServidorEnviarArquivo(ipServidor, user, password, pathDiretorioArquivoParaEnviar, nomeArquivoParaEnviar);

            // setar RegistroAula como "enviado"

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void update(Observable o, Object arg) {
        Log.i("ActivityNovaAula", "conectarAoServidorEnviarArquivo() - update()");
        if (o instanceof ClienteFTP) {  // se recebeu uma notificacao do ClienteFTP
            if (arg instanceof Integer) {
                if ((int) arg == ClienteFTP.SERVIDOR_OFFLINE_OU_IP_ERRADO) {
                    exibirToastNotification("Servidor offline, verifique o Endereço IP", Toast.LENGTH_LONG);

                } else if ((int) arg == ClienteFTP.ENVIANDO_ARQUIVO) {
                    //Log.i("ActivityNovaAula", "conectarAoServidorEnviarArquivo() - update() - enviando arquivo...");
                    exibirProgressDialog("Enviando imagens...");

                } else if ((int) arg == ClienteFTP.UPLOAD_REALIZADO) {
                    fecharProgressDialog();
                    exibirToastNotification("Registro de Aula enviado com sucesso!", Toast.LENGTH_LONG);
                    this.finish();

                } else if ((int) arg == ClienteFTP.UPLOAD_NAO_REALIZADO) {
                    exibirToastNotification("Erro ao enviar Registro de Aula!", Toast.LENGTH_LONG);
                }

            }
        }
    }



    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextConteudoAula.getWindowToken(), 0);
    }



    public void carregarDadosDisciplinas() {
        try {
            ArrayList<Disciplina> arrayListDisciplinas = controllerDisciplinas.loadDisciplinas();
            this.atualizarSpinnerDisciplinasCadastradas(arrayListDisciplinas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void atualizarSpinnerDisciplinasCadastradas(ArrayList<Disciplina> arrayListDisciplinas) {
        ArrayAdapter<Disciplina> adapter = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, arrayListDisciplinas);
        spinnerDisciplina.setAdapter(adapter);
    }


    public void atualizarListViewImageFilesAulaAtual(ArrayList<String> arrayListNomesImagens) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListNomesImagens);
        listViewImageFilesAulaAtual.setAdapter(adapter);
    }

}
