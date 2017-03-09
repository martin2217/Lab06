package dam.isi.frsf.utn.edu.ar.lab06;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab06.modelo.Proyecto;


public class ProyectoAdapter extends ArrayAdapter<Proyecto> {
    private LayoutInflater inflador;
    private Context contexto;

    public ProyectoAdapter(Context contexto, int resource, List<Proyecto> listaProyectos) {
        super(contexto, resource, listaProyectos);
        this.contexto =contexto;
    }

    public ProyectoAdapter(Context contexto, int resource) {
        super(contexto, resource);
        this.contexto =contexto;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fila_proyecto, null);
        }

        Proyecto p = getItem(position);

        if (p != null) {
            TextView txtProyectoNombre = (TextView) v.findViewById(R.id.txtProyectoNombre);

            Button btnEliminar = (Button) v.findViewById(R.id.btnProyectoEminar);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                    // Eliminar por ApiRest
                    // Eliminar de la lista
                    // Notify set data changed
                }
            });

            Button btnCambiarNombre = (Button) v.findViewById(R.id.btnProyectoCambiarNombre);
            btnCambiarNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Mostrar JDialog con nombre
                    // Cambiar por ApiRest
                    // Cambiar en la lista
                    // Notify set data changed
                }
            });
        }

        return v;
    }

    // TODO: continuar
}
