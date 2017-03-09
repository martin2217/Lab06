package dam.isi.frsf.utn.edu.ar.lab06;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import dam.isi.frsf.utn.edu.ar.lab06.R;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ProyectoDAO;

public class MainActivity extends AppCompatActivity {

    private ListView lvTareas;
    private ProyectoDAO proyectoDAO;
    private Cursor cursor;
    private TareaCursorAdapter tca;

    private final int CODIGO_ORIGEN_ALTA=1;
    private final int CODIGO_ORIGEN_EDITAR=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intActAlta= new Intent(MainActivity.this,AltaTareaActivity.class);
                intActAlta.putExtra("ID_TAREA", 0);
                startActivity(intActAlta);
            }
        });
        lvTareas = (ListView) findViewById(R.id.listaTareas);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LAB06-MAIN","en resume");
        proyectoDAO = new ProyectoDAO(MainActivity.this);
        proyectoDAO.open();
        cursor = proyectoDAO.listaTareas(1);
        Log.d("LAB06-MAIN","mediol "+cursor.getCount());

        tca = new TareaCursorAdapter(MainActivity.this,cursor,proyectoDAO);
        lvTareas.setAdapter(tca);
        Log.d("LAB06-MAIN","fin resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LAB06-MAIN","on pausa");

        if(cursor!=null) cursor.close();
        if(proyectoDAO!=null) proyectoDAO.close();
        Log.d("LAB06-MAIN","fin on pausa");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.desvios) {
            Intent intActAlta= new Intent(MainActivity.this, DesviosActivity.class);
            intActAlta.putExtra("ID_TAREA", 0);
            startActivity(intActAlta);
        }
        else if (id == R.id.proyectos){
            Intent intActAlta= new Intent(MainActivity.this, ProyectosActivity.class);
            startActivity(intActAlta);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CODIGO_ORIGEN_EDITAR:
            case CODIGO_ORIGEN_ALTA:
                // Resultado
                if (resultCode == Activity.RESULT_OK) {
                    tca.notifyDataSetChanged();
                }
                break;
        }
    }
}
