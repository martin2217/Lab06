package dam.isi.frsf.utn.edu.ar.lab06;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab06.dao.ApiRest;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Proyecto;

public class ProyectosActivity extends AppCompatActivity{

    private FloatingActionButton btnNuevoProyecto;
    private ListView listViewProyectos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);

        btnNuevoProyecto = (FloatingActionButton) findViewById(R.id.btnNuevoProyecto);
        btnNuevoProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: nuevo proyecto
                // Jdialog pidiendo el nombre
                /*Intent intActAlta= new Intent(MainActivity.this,AltaTareaActivity.class);
                intActAlta.putExtra("ID_TAREA", 0);
                startActivity(intActAlta);*/
            }
        });

        listViewProyectos = (ListView) findViewById(R.id.listaProyectos);

        // TODO: obtener los proyectos mediante apiRest
        //List<Proyecto> listaProyectos = new ApiRest().listarProyectos();
        //ProyectoCAdapter pa = new ProyectoAdapter(ProyectosActivity.this, R.layout.fila_proyect, listaProyectos);
        //listViewProyectos.setAdapter(pca);

        // Con cada cambio:
        // 1) Se enviará al backend el cambio
        // 2) Se modificará la lista
        // 3) Se notificará el cambio con notifySetDataChanged()
    }


}
