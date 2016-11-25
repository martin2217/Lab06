package dam.isi.frsf.utn.edu.ar.lab05;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horas_estimadas;
    private SeekBar prioridad;
    private Cursor cursor;
    private ProyectoDAO dao;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);
        dao= new ProyectoDAO(this);

        descripcion=(EditText) findViewById(R.id.editText);

        horas_estimadas=(EditText) findViewById(R.id.editText2);

        prioridad=(SeekBar) findViewById(R.id.seekBar);

        spinner= (Spinner) findViewById(R.id.spinner);

        cursor = dao.listarUsuarios();
        // TODO Revisar como implementar el Simple cursor adapter con el Cursor..
        if (cursor.getCount() > 0) {
            String[] from = new String[]{"NOMBRE"};
            int[] to = new int[]{android.R.id.text1};
            SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_spinner_item,
                    cursor,
                    from,
                    to);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mAdapter);
        }
    }
}
