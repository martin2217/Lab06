package dam.isi.frsf.utn.edu.ar.lab06;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab06.R;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ApiRest;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab06.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horas_estimadas;
    private SeekBar prioridad;
    //private Cursor cursorUsuarios;
    private Cursor cursorProyectos;
    private ProyectoDAO dao;
    //private Spinner responsables;
    private Spinner proyectos;
    private TextView textPrioridad;
    private Button btnSelecionarContacto;
    private TextView txtResponsable;

    private int origen; // 1 para alta, 2 para editar
    public static final int ALTA=1;
    public static final int EDITAR=2;
    private Integer idTarea;

    private Cursor tareaCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);
        dao = new ProyectoDAO(this);

        idTarea=(Integer) getIntent().getExtras().get("ID_TAREA");

        if(idTarea==0){
            origen=ALTA;
        }
        else {
            origen=EDITAR;
            tareaCursor =dao.getTarea(idTarea);
            tareaCursor.moveToFirst();
        }

        descripcion =(EditText) findViewById(R.id.editText);
        horas_estimadas =(EditText) findViewById(R.id.editText2);
        prioridad =(SeekBar) findViewById(R.id.seekBar);
        textPrioridad = (TextView) findViewById(R.id.text_prioridad);
        //responsables = (Spinner) findViewById(R.id.responsablesSpinner);
        proyectos = (Spinner) findViewById(R.id.proyectosSpinner);
        //cursorUsuarios = dao.listarUsuarios();
        cursorProyectos = dao.listarProyectos();
        txtResponsable = (TextView) findViewById(R.id.textViewResponsable);


        // *** Responsables ***
        /*List<String> responsablesArray = new ArrayList<String>();
        if (cursorUsuarios.moveToFirst()) { // si hay registros entra y recorre
            do {
                responsablesArray.add(cursorUsuarios.getString(0));
            } while(cursorUsuarios.moveToNext());
        }*/

        // Adapter
        /*ArrayAdapter<String> responsablesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsablesArray);
        responsablesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsables.setAdapter(responsablesAdapter);*/


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


        final int[] progresoBar = {0};

        if (origen==ALTA) {
            textPrioridad.setText("Prioridad: 0");
        }
        else {
            int prog = tareaCursor.getInt(tareaCursor.getColumnIndex("ID_PRIORIDAD"));
            textPrioridad.setText("Prioridad: " + prog);
            progresoBar[0]=prog;
            prioridad.setProgress(prog);

            descripcion.setText(tareaCursor.getString(tareaCursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
            Integer horasAsignadas = tareaCursor.getInt(tareaCursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
            horas_estimadas.setText(String.valueOf(horasAsignadas));
        }


        // *** Prioridad ***
        prioridad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progreso, boolean fromUser){
                progresoBar[0] = progreso;
                textPrioridad.setText("Prioridad: " + progreso);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        // *** Guardar ***
        final Button btnGuardarAAT = (Button) findViewById(R.id.btnGuardarAAT);
        btnGuardarAAT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtResponsable.getText().toString().trim())) {
                    Toast.makeText(AltaTareaActivity.this, "Debe seleccionar un responsable",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    Tarea nuevaTarea = new Tarea();
                    if (TextUtils.isEmpty(descripcion.getText().toString().trim()))
                        nuevaTarea.setDescripcion("Sin descripcion");
                    else nuevaTarea.setDescripcion(descripcion.getText().toString());
                    if (TextUtils.isEmpty(horas_estimadas.getText().toString().trim()))
                        nuevaTarea.setHorasEstimadas(0);
                    else
                        nuevaTarea.setHorasEstimadas(Integer.parseInt(horas_estimadas.getText().toString()));
                    nuevaTarea.setMinutosTrabajados(0);
                    nuevaTarea.setFinalizada(false);

                    Prioridad auxPrioridad = new Prioridad();
                    auxPrioridad.setId(progresoBar[0]);
                    nuevaTarea.setPrioridad(auxPrioridad);

                    String tituloProyecto = proyectos.getSelectedItem().toString();
                    Cursor cursorProyecto = dao.getProyecto(tituloProyecto);
                    Proyecto auxProyecto = new Proyecto();
                    if (cursorProyecto.moveToFirst()) { // si hay registros entra y recorre
                        do {
                            auxProyecto.setId(Integer.parseInt(cursorProyecto.getString(0)));
                        } while (cursorProyectos.moveToNext());
                    }
                    nuevaTarea.setProyecto(auxProyecto);


                    // Revisar si ya esta ingresado, buscar usuario

                    String nombreUsuario = txtResponsable.getText().toString();
                    Cursor cursorUsuario = dao.getUsuario(nombreUsuario);

                    Boolean ingreso=false;
                    if(cursorUsuario.getCount()<1){ // Ingresarlo

                        Toast.makeText(AltaTareaActivity.this, "Se agregó el usuario",
                                Toast.LENGTH_SHORT).show();
                        ingreso=true;
                        // Agregarlo en la bd local y backend
                        dao.nuevoUsuario(nombreUsuario);

                    }


                    cursorUsuario = dao.getUsuario(nombreUsuario);
                    Usuario auxUsuario = new Usuario();
                    if (cursorUsuario.moveToFirst()) { // si hay registros entra y recorre
                        do {
                            auxUsuario.setId(Integer.parseInt(cursorUsuario.getString(0)));
                        } while (cursorUsuario.moveToNext());
                    }
                    nuevaTarea.setResponsable(auxUsuario);

                    if(ingreso){ // Ingresarlo a backend

                        auxUsuario.setNombre(nombreUsuario);
                        new ApiRest().crearUsuario(auxUsuario);

                    }

                    if (origen == ALTA) {
                        dao.nuevaTarea(nuevaTarea);
                    } else {
                        nuevaTarea.setId(idTarea);
                        dao.actualizarTarea(nuevaTarea); // En realidad no es nueva :p
                    }

                    /*Intent i= new Intent();
                    setResult(MainActivity.RESULT_OK, i);*/
                    finish();
                }
            }
        });

        final Button btnCancelarAAT = (Button) findViewById(R.id.btnCancelarAAT);
        btnCancelarAAT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        btnSelecionarContacto = (Button) findViewById(R.id.btnElegirContacto);
        btnSelecionarContacto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int hasWriteContactsPermission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                        // request permission
                        requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                                1);

                    }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){

                        seleccionarContacto();
                    }
                }
                else
                    seleccionarContacto();
            }
        });

    }
    private void seleccionarContacto(){
        List<String> contactos = buscarContactos();

        // Mostrar contactos
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AltaTareaActivity.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Seeleccione un contacto");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AltaTareaActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(contactos);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                // INTERESANTE
                /*
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(AltaTareaActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
                */

                // Poner el contacto en una lista o textView
                String contacto = arrayAdapter.getItem(i);
                txtResponsable.setText(contacto);

            }
        });
        builderSingle.show();
    }

    public List<String> buscarContactos(){

        List<String> contactos = new ArrayList<>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME ));

                contactos.add(name);

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {


                    /*Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                    }
                    pCur.close();*/

                }
            }
        }
        cur.close();
        return contactos;
    }

    public void buscarContacto(String nombreBuscado){
        JSONArray arr = new JSONArray();
        final StringBuilder resultado = new StringBuilder();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // consulta ejemplo buscando por nombre visualizado en los contactos agregados
        Cursor c =this.getContentResolver().query(uri, null, ContactsContract.Contacts.DISPLAY_NAME+" LIKE '"+nombreBuscado+"%'", null, sortOrder);
        int count = c.getColumnCount();
        int fila = 0;
        String[] columnas= new String[count];
        try {
            while(c.moveToNext()) {
                JSONObject unContacto = new JSONObject();
                for(int i = 0; (i < count );  i++) {
                    if(fila== 0)columnas[i]=c.getColumnName(i);
                    unContacto.put(columnas[i],c.getString(i));
                }
                Log.d("TEST-ARR",unContacto.toString());
                arr.put(fila,unContacto);
                fila++;
                Log.d("TEST-ARR","fila : "+fila);

                // elegir columnas de ejemplo
                resultado.append(unContacto.get("name_raw_contact_id")+" - "+unContacto.get("display_name")+System.getProperty("line.separator"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TEST-ARR",arr.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(1 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarContacto();
            } else {
                Toast.makeText(AltaTareaActivity.this, "No se obtuvo el permiso de contactos",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}