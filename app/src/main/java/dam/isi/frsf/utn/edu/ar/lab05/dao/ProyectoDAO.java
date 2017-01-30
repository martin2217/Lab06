package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;

    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        cursorPry.close();
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public void nuevaTarea(Tarea tarea){
        db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA, tarea.getDescripcion());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS, tarea.getHorasEstimadas());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS, tarea.getMinutosTrabajados());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD, tarea.getPrioridad().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE, tarea.getResponsable().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO, tarea.getProyecto().getId());

        db.insert(ProyectoDBMetadata.TABLA_TAREAS, null, valores);
    }

    public void actualizarTarea(Tarea t){

    }

    public void borrarTarea(Tarea t){
        (dbHelper.getWritableDatabase()).delete(ProyectoDBMetadata.TABLA_TAREAS,"_id=?",new String[]{t.getId().toString()});
    }

    public Cursor getProyecto(Integer id){
        open();
        String[] campos = new String[]{
                ProyectoDBMetadata.TablaProyectoMetadata.TITULO
        };

        Cursor cursor = db.query(ProyectoDBMetadata.TABLA_PROYECTO, campos, "_id=" + id, null, null, null, null);

        return cursor;
    }

    public Cursor getProyecto(String titulo){
        open();
        String[] campos = new String[]{
                ProyectoDBMetadata.TablaProyectoMetadata._ID
        };

        Cursor cursor = db.query(ProyectoDBMetadata.TABLA_PROYECTO, campos, "titulo=" + titulo, null, null, null, null);

        return cursor;
    }

    public Cursor getTarea(Integer id){
        open();
        String[] campos = new String[]{
                ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,
                ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD,
                ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE,
                ProyectoDBMetadata.TablaTareasMetadata.PROYECTO
        };

        Cursor cursor = db.query(ProyectoDBMetadata.TABLA_TAREAS, campos, "_id=" + id, null, null, null, null);

        return cursor;
    }

    public Cursor getUsuario(Integer id){
        open();
        String[] campos = new String[]{
                ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO
        };

        Cursor cursor = db.query(ProyectoDBMetadata.TABLA_USUARIOS, campos, "_id=" + id, null, null, null, null);

        return cursor;
    }

    public Cursor getUsuario(String usuario){
        open();
        String[] campos = new String[]{
                ProyectoDBMetadata.TablaUsuariosMetadata._ID
        };

        Cursor cursor = db.query(ProyectoDBMetadata.TABLA_USUARIOS, campos, "usuario=" + usuario, null, null, null, null);

        return cursor;
    }

    public void actualizarTiempo(String tiempo, Integer id){
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,tiempo);
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{id.toString()});
    }

    public List<Prioridad> listarPrioridades(){
        return null;
    }

    public Cursor listarUsuarios(){
        open();
        Cursor cursorUsuario= db.rawQuery("SELECT "+ ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +
                " FROM "+ProyectoDBMetadata.TABLA_USUARIOS, null);
        return cursorUsuario;
    }

    public Cursor listarProyectos(){
        open();
        Cursor cursorAux = db.rawQuery("SELECT "+ ProyectoDBMetadata.TablaProyectoMetadata.TITULO +
                " FROM "+ProyectoDBMetadata.TABLA_PROYECTO, null);
        return cursorAux;
    }

    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});
    }

    public List<Tarea> listarDesviosPlanificacion(Boolean soloTerminadas,Integer desvioMaximoMinutos){
        // retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
        // que el tiempo planificado.
        // si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.
        return null;
    }


}
