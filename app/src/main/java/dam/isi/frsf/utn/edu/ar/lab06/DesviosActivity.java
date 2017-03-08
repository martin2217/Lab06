package dam.isi.frsf.utn.edu.ar.lab06;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab06.R;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ProyectoDBMetadata;

public class DesviosActivity extends AppCompatActivity {

    private Button btnBuscar;
    private EditText txtMinutosDesvio;
    private CheckBox checkBoxTerminada;
    private ListView listView;

    private ProyectoDAO proyectoDAO;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desvios);

        txtMinutosDesvio= (EditText) findViewById(R.id.editTextDesviosValor);

        checkBoxTerminada= (CheckBox) findViewById(R.id.checkBoxDesviosTerminada);

        listView = (ListView) findViewById(R.id.listViewDesvios) ;

        btnBuscar= (Button) findViewById(R.id.btnDesviosBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(txtMinutosDesvio.getText().toString().trim())){
                    Toast.makeText(DesviosActivity.this, "No se ingresó un valor",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // Buscar

                    // Obtención de las tareas
                    proyectoDAO = new ProyectoDAO(DesviosActivity.this);
                    proyectoDAO.open();
                    cursor = proyectoDAO.listaTareas(1);


                    Integer horasAsigandas;
                    Integer minutosAsigandos;
                    int diferencia;
                    List<String> resultado= new ArrayList<>();

                    // Iteración sobre las tareas
                    try {
                        while (cursor.moveToNext()) {

                            horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
                            minutosAsigandos = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
                            diferencia = horasAsigandas.intValue()*60 - minutosAsigandos.intValue();

                            // Condiciones para ser agregada (diferencia válida y si está o no finalizada)
                            if((cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA)) == 1) == checkBoxTerminada.isChecked()
                                    && Math.abs(diferencia)>=Double.valueOf(txtMinutosDesvio.getText().toString())){
                                cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA));
                                StringBuilder string = new StringBuilder();
                                string.append("Tarea: ").append(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
                                string.append(".\nUsuario: ").append(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));
                                string.append(".\nDesvío: ").append(Math.abs(diferencia));
                                if (diferencia<0) string.append(" minutos excedida.");
                                else string.append(" minutos de sobra.");

                                resultado.add(string.toString());
                            }
                        }
                    } finally {
                        cursor.close();
                    }


                    // Desaparecer el teclado
                    txtMinutosDesvio.clearFocus();;
                    View vieww = DesviosActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    ArrayAdapter adapter = new ArrayAdapter<String>(DesviosActivity.this, android.R.layout.simple_list_item_1, resultado);
                    listView.setAdapter(adapter);

                    if(resultado.size()==0)
                        Toast.makeText(DesviosActivity.this, "No se obtuvieron resultados",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
