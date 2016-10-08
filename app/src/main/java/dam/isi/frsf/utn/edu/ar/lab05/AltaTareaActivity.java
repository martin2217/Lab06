package dam.isi.frsf.utn.edu.ar.lab05;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horas_estimadas;
    private SeekBar prioridad;
    private Cursor cursor;
    private ProyectoDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);
        dao= new ProyectoDAO(this);

        descripcion=(EditText) findViewById(R.id.editText);

        horas_estimadas=(EditText) findViewById(R.id.editText2);

        prioridad=(SeekBar) findViewById(R.id.seekBar);

        cursor = dao.listarUsuarios();
        // TODO Revisar como implementar el SImple cursor adapter con el Cursor... Revisar el CONTEXTO QUE SE PASA POR AHI
        //SimpleCursorAdapter adapterUsuario = new SimpleCursorAdapter()
    }
}
