package dam.isi.frsf.utn.edu.ar.lab06.dao;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RestClient {

    private final String PC = "192.168.0.12";
    private final String EMULADOR = "10.0.2.2"; // IP Emulador
    private final String IP_SERVER = PC;

    private final String PORT_SERVER = "4000";
    private final String TAG_LOG = "LAB06";

    public JSONObject getById(final Integer id, final String path) {
        final JSONObject[] resultado = new JSONObject[1];
        class asyncGet extends AsyncTask<Void, Void, JSONObject> {
            @Override
            protected JSONObject doInBackground(Void... urls) {
                try {
                    JSONObject result= getById2(id, path);
                    return result;
                } catch (Exception e) {
                    Log.d("Error en asyncPost: ",e.toString());
                    return null;
                }
            }

            protected void onPostExecute(JSONObject objeto){
                resultado[0] =objeto;
            }
        }
        return resultado[0];
    }

    private JSONObject getById2(Integer id, String path) {
        JSONObject resultado = null;
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path+"/"+id);
            Log.d("TAG_LOG",url.getPath()+ " --> "+url.toString());
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            Log.d("TAG_LOG",url.getPath()+ " --> "+sb.toString());
            resultado = new JSONObject(sb.toString());
        }
        catch (IOException e) {
            Log.e("TEST-ARR",e.getMessage(),e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return resultado;
    }

    public JSONArray getByAll(Integer id,String path) {
        JSONArray resultado = null;
        return resultado;
    }


    //TODO: Creería que esto esta mal....... Pero bueno jajajaja
    public void crear(final JSONObject objeto, final String path) {
        class asyncCrear extends AsyncTask<Void, Void, Void> {
            protected Void doInBackground(Void... urls) {
                try {
                    post(objeto, path);
                    return null;
                } catch (Exception e) {
                    Log.d("Error en asyncPost: ",e.toString());
                    return null;
                }
            }
        }
    }
    private void post(JSONObject objeto,String path){
        HttpURLConnection urlConnection=null;
        try {
            String str= objeto.toString();
            byte[] data=str.getBytes("UTF-8");
            Log.d("EjemploPost","str---> "+str);

            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path+"/");

            // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");

            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(data);
            printout.flush ();
            printout.close ();

            Log.d("TEST-POST","FIN!!! "+urlConnection.getResponseMessage());

        }  catch (IOException e1) {
            Log.e("TEST-POST",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
    }

    public void actualizar(JSONObject objeto,String path) {
    }

    public void borrar(Integer id,String path) {
    }
}