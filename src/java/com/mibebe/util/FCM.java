package com.mibebe.util;

import com.mibebe.bean.Consulta;
import com.mibebe.bean.util.ResumenConsulta;
import static com.mibebe.util.Constants.CONFIG_BUNDLE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Sergio Cabrera
 */
public abstract class FCM {
    private static final String TAG = "FCM";
    private static final String URL = "https://fcm.googleapis.com/fcm/send";
    //private static final String KEY = "AIzaSyBcuqjvPoRgJA5EPsg-2FMjRJeNMfDESgM";
    private static final String KEY = ResourceBundle.getBundle(CONFIG_BUNDLE).getString("firebase_key");

    public synchronized static boolean notificarConsulta(Consulta consulta) {
        try {
            JSONObject data = new JSONObject();
            JSONObject subData = new JSONObject();

            data.put("to", consulta.getPediatra().getFCMToken());
            subData.put("opt", "nuevaConsulta");
            subData.put("title", "Nueva consulta");
            subData.put("body", consulta.getUsuario().getNombreCompleto());
            data.put("data", subData);
            data.put("priority", "high");
            if (consulta.getPediatra().isIOS()) {
                HashMap<String, Object> not = new HashMap();
                not.put("title", "Nueva consulta");
                not.put("body", consulta.getUsuario().getNombreCompleto());
                data.put("notification", not);
            }
            return sendData(data);
        } catch (JSONException e) {
            AppLog.Log(TAG, "No se generó la estructura de json para enviar notificacion", e);
            return false;
        }
    }

    public synchronized static boolean notificarConsultaRealizada(ResumenConsulta respuesta) {
        try {
            JSONObject data = new JSONObject();
            JSONObject subData = new JSONObject();

            data.put("to", respuesta.getUsuario().getFCMToken());
            subData.put("opt", "consultaRealizada");
            subData.put("title", "Consulta atendida");
            subData.put("body", "Se ha atendido la consulta");
            data.put("data", subData);
            data.put("priority", "high");
            if (respuesta.getUsuario().isIOS()) {
                HashMap<String, Object> not = new HashMap();
                not.put("title", "Consulta atendida");
                not.put("body", "Se ha atendido la consulta");
                data.put("notification", not);
            }
            return sendData(data);
        } catch (JSONException e) {
            AppLog.Log(TAG, "No se generó la estructura de json para enviar notificacion", e);
            return false;
        }
    }

    private synchronized static boolean sendData(JSONObject data) {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + KEY);
            conn.setDoOutput(true);

            String urlParameters = data.toString();

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(urlParameters);
                writer.flush();
            }

            if (conn.getResponseCode() != 200) {
                AppLog.Log(TAG, "No se puedo enviar la notificación", new Exception("FirebaseNotification#notifyChanges: la conexión respondió con el código de error " + conn.getResponseCode()));
                return false;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String outLine;
            String out = "";
            while ((outLine = br.readLine()) != null) {
                out += outLine;
            }
            JSONObject response = new JSONObject(out);
            if(response.getInt("failure") != 0) {
                AppLog.Log(TAG, "FCM total de envios fallidos: " + response.getInt("failure"), null);
            }            
            return response.getInt("failure") == 0;
        } catch (IOException | JSONException e) {
            AppLog.Log(TAG, "Error al enviar datos por firebase", e);
        }
        return false;
    }

}
