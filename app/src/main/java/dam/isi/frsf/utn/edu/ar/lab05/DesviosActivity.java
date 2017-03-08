package dam.isi.frsf.utn.edu.ar.lab05;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class DesviosActivity extends AppCompatActivity {

    private Button btnBuscar;
    private EditText txtMinutosDesvio;
    private CheckBox checkBoxTerminada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desvios);

        txtMinutosDesvio= (EditText) findViewById(R.id.editTextDesviosValor);

        checkBoxTerminada= (CheckBox) findViewById(R.id.checkBoxDesviosTerminada);

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
                    // checkBoxTerminada.isChecked();
                    // txtMinutosDesvio.getText();
                    /*proyectoDAO = new ProyectoDAO(MainActivity.this);
                    proyectoDAO.open();
                    cursor = proyectoDAO.listaTareas(1);

                    tca = new TareaCursorAdapter(MainActivity.this,cursor,proyectoDAO);
                    lvTareas.setAdapter(tca);*/
                }
            }
        });


    }
}
