package com.example.bernardo.capface;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.bernardo.capface.entidades.Disciplina;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityNovaAula extends AppCompatActivity {

    int REQUEST_IMAGE_CAPTURE = 1;

    Spinner spinnerCursos;
    Spinner spinnerDisciplina;
    Spinner spinnerTipoDisciplina;
    EditText editTextDataAula;
    EditText editTextQtdeAulas;
    EditText editTextHoraInicioAula;
    EditText editTextHoraFimAula;
    Button buttonCamera;
    Button buttonEnviar;
    EditText editTextDiretorioCapface;
    EditText editTextDiretorioAulaAtual;
    ListView listViewImageFilesAulaAtual;
    EditText editTextJSONfile;
    EditText editTextIPServidor;

    String diretorioPadraoCapfaceAulas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/CapFace/Aulas";
    String diretorioAulaAtual, pathDiretorioAulaAtual;
    private long dataHoraInicial;
    ArrayList<String> arrayListNomesImagensCapturadas = new ArrayList<>();


    ControllerDisciplinas controllerDisciplinas = ControllerDisciplinas.createControllerDisciplinas();
    ControllerCapturedImages controllerCapturedImages = new ControllerCapturedImages(diretorioPadraoCapfaceAulas);


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
            !editTextDataAula.getText().toString().equals("") &&
            !editTextQtdeAulas.getText().toString().equals("") &&
            !editTextHoraInicioAula.getText().toString().equals("") &&
            !editTextHoraFimAula.getText().toString().equals("")) {
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
            exibirToastNotification("Preencha os dados da aula!", Toast.LENGTH_LONG);
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


    public void exibirToastNotification(String text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void notificarGalleryApp() {
        String diretorioPadraoCamera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera";
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(diretorioPadraoCamera))));
    }


    public void aoClicarNoBotaoEnviar() {

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
