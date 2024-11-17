package edu.co.uniquindio.Model.Notificacion;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase para enviar notificaciones vía WhatsApp
 */
public class WhatsApp {

    private static WhatsApp instance;
    private final String accessToken = System.getenv("WHATSAPP_ACCESS_TOKEN");  // Token seguro desde variables de entorno
    private final String numeroCelular = System.getenv("WHATSAPP_PHONE_ID");   // ID de teléfono de la API
    private final OkHttpClient httpClient;

    private WhatsApp() {
        this.httpClient = new OkHttpClient();
    }

    public static WhatsApp getInstance() {
        if (instance == null) {
            instance = new WhatsApp();
        }
        return instance;
    }

    public void sendNotification(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        try {
            if (!isPhoneNumberValid(recipientPhone)) {
                throw new IllegalArgumentException("El número de teléfono no es válido: " + recipientPhone);
            }
            JSONObject messageData = buildMessage(recipientPhone, notificationTitle, username, notificationMessage);
            sendHttpRequest(messageData);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    private boolean isPhoneNumberValid(String phone) {
        return phone != null && phone.matches("\\+\\d{10,15}");
    }

    private JSONObject buildMessage(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        JSONObject message = new JSONObject();
        message.put("messaging_product", "whatsapp");
        message.put("to", recipientPhone);
        message.put("type", "template");

        JSONObject template = new JSONObject();
        template.put("name", "notificaciones"); // Cambia esto al nombre exacto de tu plantilla configurada en WhatsApp API.
        template.put("language", new JSONObject().put("code", "es"));

        JSONArray components = new JSONArray();

        // Componente del cuerpo del mensaje
        JSONObject bodyComponent = new JSONObject();
        bodyComponent.put("type", "body");
        JSONArray bodyParams = new JSONArray();
        bodyParams.put(new JSONObject().put("type", "text").put("text", notificationTitle));
        bodyParams.put(new JSONObject().put("type", "text").put("text", username));
        bodyParams.put(new JSONObject().put("type", "text").put("text", notificationMessage));
        bodyComponent.put("parameters", bodyParams);
        components.put(bodyComponent);

        template.put("components", components);
        message.put("template", template);

        return message;
    }

    private void sendHttpRequest(JSONObject messageData) throws Exception {
        Request request = new Request.Builder()
                .url("https://graph.facebook.com/v17.0/" + numeroCelular + "/messages")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(messageData.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error al enviar el mensaje. Código: " + response.code() +
                        ". Respuesta: " + response.body().string());
            } else {
                System.out.println("Mensaje enviado con éxito: " + response.body().string());
            }
        }
    }
}