package dam.isi.frsf.utn.edu.ar.lab05;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horas_estimadas;
    private SeekBar prioridad;
    private Cursor cursorUsuarios;
    private Cursor cursorProyectos;
    private ProyectoDAO dao;
    private Spinner responsables;
    private Spinner proyectos;
    private int progresoSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);
        dao = new ProyectoDAO(this);

        descripcion =(EditText) findViewById(R.id.editText);
        horas_estimadas =(EditText) findViewById(R.id.editText2);
        prioridad =(SeekBar) findViewById(R.id.seekBar);
        responsables = (Spinner) findViewById(R.id.responsablesSpinner);
        proyectos = (Spinner) findViewById(R.id.proyectosSpinner);
        cursorUsuarios = dao.listarUsuarios();
        cursorProyectos = dao.listarProyectos();


        // *** Responsables ***
        List<String> responsablesArray = new ArrayList<String>();
        if (cursorUsuarios.moveToFirst()) { // si hay registros entra y recorre
            do {
                responsablesArray.add(cursorUsuarios.getString(0));
            } while(cursorUsuarios.moveToNext());
        }

        // Adapter
        ArrayAdapter<String> responsablesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsablesArray);
        responsablesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsables.setAdapter(responsablesAdapter);


        // *** Proyectos ***
        List<String> proyectosArray = new ArrayList<String>();
        if (cursorProyectos.moveToFirst()) { // si hay registros entra y recorre
            do {
                proyectosArray.add(cursorProyectos.getString(0));
            } while(cursorProyectos.moveToNext());
        }

        // Adapter
        ArrayAdapter<String> proyectosAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proyectosArray);
        proyectosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proyectos.setAdapter(proyectosAdapter);


        prioridad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progresoSeekBar = progresValue;
                Toast.makeText(getApplicationContext(), progresoSeekBar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progresoSeekBar=0;
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });

        final Button btnGuardarAAT = (Button) findViewById(R.id.btnGuardarAAT);
        btnGuardarAAT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Tarea nuevaTarea = new Tarea();
                nuevaTarea.setDescripcion(descripcion.getText().toString());
                nuevaTarea.setHorasEstimadas(Integer.parseInt(horas_estimadas.getText().toString()));
                nuevaTarea.setMinutosTrabajados(0);
                nuevaTarea.setFinalizada(false);

                Prioridad auxPrioridad = new Prioridad();
                auxPrioridad.setId(progresoSeekBar);
                nuevaTarea.setPrioridad(auxPrioridad);

                String tituloProyecto = proyectos.getSelectedItem().toString();
                Cursor cursorProyecto = dao.getProyecto(tituloProyecto);
                Proyecto auxProyecto = new Proyecto();
                if (cursorProyecto.moveToFirst()) { // si hay registros entra y recorre
                    do {
                        auxProyecto.setId(Integer.parseInt(cursorProyecto.getString(0)));
                    } while(cursorProyectos.moveToNext());
                }
                nuevaTarea.setProyecto(auxProyecto);

                String nombreUsuario = responsables.getSelectedItem().toString();
                Cursor cursorUsuario = dao.getUsuario(nombreUsuario);
                Usuario auxUsuario = new Usuario();
                if (cursorUsuario.moveToFirst()) { // si hay registros entra y recorre
                    do {
                        auxUsuario.setId(Integer.parseInt(cursorUsuario.getString(0)));
                    } while(cursorUsuario.moveToNext());
                }
                nuevaTarea.setResponsable(auxUsuario);

                finish();
            }
        });

        final Button btnCancelarAAT = (Button) findViewById(R.id.btnCancelarAAT);
        btnCancelarAAT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}