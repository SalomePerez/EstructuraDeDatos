package edu.co.uniquindio.Modelo;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase para enviar notificaciones vía WhatsApp.
 */
public class WhatsApp {

    // Instancia Singleton
    private static WhatsApp instance;
    // Datos de configuración de la API
    private final String phoneId = "471467826048910";  // ID de teléfono
    private final String accessToken = "TU_ACCESS_TOKEN";  // Token de acceso
    private final OkHttpClient httpClient;

    // Constructor privado
    private WhatsApp() {
        this.httpClient = new OkHttpClient();
    }

    // Método para obtener la instancia única
    public static WhatsApp getInstance() {
        if (instance == null) {
            instance = new WhatsApp();
        }
        return instance;
    }

    // Método para enviar la notificación
    public void sendNotification(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        try {
            JSONObject messageData = buildMessage(recipientPhone, notificationTitle, username, notificationMessage);
            sendHttpRequest(messageData);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    // Construye el mensaje en formato JSON
    private JSONObject buildMessage(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        JSONObject message = new JSONObject();
        message.put("messaging_product", "whatsapp");
        message.put("to", recipientPhone);
        message.put("type", "template");

        JSONObject template = new JSONObject();
        template.put("name", "notificaciones");
        template.put("language", new JSONObject().put("code", "es"));

        JSONArray components = new JSONArray();

        // Componente del encabezado
        JSONObject headerComponent = new JSONObject();
        headerComponent.put("type", "header");
        JSONArray headerParams = new JSONArray();
        headerParams.put(new JSONObject().put("type", "text").put("text", notificationTitle));
        headerComponent.put("parameters", headerParams);
        components.put(headerComponent);

        // Componente del cuerpo
        JSONObject bodyComponent = new JSONObject();
        bodyComponent.put("type", "body");
        JSONArray bodyParams = new JSONArray();
        bodyParams.put(new JSONObject().put("type", "text").put("text", username));
        bodyParams.put(new JSONObject().put("type", "text").put("text", notificationMessage));
        bodyComponent.put("parameters", bodyParams);
        components.put(bodyComponent);

        template.put("components", components);
        message.put("template", template);

        return message;
    }

    // Envía la solicitud HTTP
    private void sendHttpRequest(JSONObject messageData) throws Exception {
        Request request = new Request.Builder()
                .url("https://graph.facebook.com/v17.0/" + phoneId + "/messages")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(messageData.toString(), MediaType.parse("application/json")))
                .build();

        // Realiza la solicitud
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.err.println("Error al enviar el mensaje. Código: " + response.code());
        } else {
            System.out.println("Mensaje enviado con éxito.");
        }
    }
}