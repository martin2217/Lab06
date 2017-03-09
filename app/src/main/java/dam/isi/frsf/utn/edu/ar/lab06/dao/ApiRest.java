package dam.isi.frsf.utn.edu.ar.lab06.dao;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab06.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab06.modelo.Usuario;


public class ApiRest {

    private final String PROYECTOS = "proyectos";
    private final String USUARIOS = "usuarios";

    RestClient cliRest;
    JSONObject t;

    public void crearProyecto(Proyecto p){

    }
    public void borrarProyecto(Integer id){

    }
    public void actualizarProyecto(Proyecto p){

    }
    public List<Proyecto> listarProyectos(){
        return null;
    }

    public Proyecto buscarProyecto(Integer id){
        cliRest = new RestClient();
        t = cliRest.getById(1,PROYECTOS);
        // transformar el objeto JSON a proyecto y retornarlo
        return null;
    }

    public void crearUsuario(Usuario u){
        cliRest = new RestClient();
        t = new JSONObject();
        try{
            t.put("id", u.getId());
            t.put("nombre", u.getNombre());
            t.put("correoElectronico", "example@mail.com"); // HARDCODEADO
        } catch (JSONException e) {
            Log.e("TEST-CrearUser",e.getMessage(),e);
            e.printStackTrace();
        }

        cliRest.crear(t, USUARIOS);
    }
}