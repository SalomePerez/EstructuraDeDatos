
package edu.co.uniquindio.Modelo;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

// Variable para implementar el patrón Singleton
private static whatsapp instance;
// Datos de configuración de la API de WhatsApp
private final String phoneId = "471467826048910";  // Cambia por tu propio ID de teléfono
private final String accessToken = "TU_ACCESS_TOKEN";  // Sustituye por tu token de acceso

private final OkHttpClient httpClient;

// Constructor privado para implementar el patrón Singleton
private WhatsApp() {
    this.httpClient = new OkHttpClient();
}

// Método para obtener la única instancia de la clase
public static whatsapp getInstance() {
    if (instance == null) {
        instance = new whatsapp();
    }
    return instance;
}

// Método principal para enviar el mensaje
public void sendNotification(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
    try {
        JSONObject messageData = buildMessage(recipientPhone, notificationTitle, username, notificationMessage);
        sendHttpRequest(messageData);
    } catch (Exception e) {
        System.err.println("Error al enviar el mensaje de notificación: " + e.getMessage());
        e.printStackTrace();
    }
}

// Método para construir el objeto JSON con el mensaje de WhatsApp
private JSONObject buildMessage(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
    JSONObject message = new JSONObject();
    message.put("messaging_product", "whatsapp");
    message.put("to", recipientPhone);  // Número de teléfono del destinatario
    message.put("type", "template");

    // Crear el objeto del template
    JSONObject template = new JSONObject();
    template.put("name", "notificaciones");  // Nombre del template en WhatsApp
    template.put("language", new JSONObject().put("code", "es"));  // Idioma del mensaje (español)

    // Crear los componentes del mensaje (header y body)
    JSONArray components = new JSONArray();

    // Componente del encabezado
    JSONObject headerComponent = new JSONObject();
    headerComponent.put("type", "header");
    JSONArray headerParams = new JSONArray();
    headerParams.put(new JSONObject()
            .put("type", "text")
            .put("text", notificationTitle));  // Título del mensaje
    headerComponent.put("parameters", headerParams);
    components.put(headerComponent);

    // Componente del cuerpo del mensaje
    JSONObject bodyComponent = new JSONObject();
    bodyComponent.put("type", "body");
    JSONArray bodyParams = new JSONArray();
    bodyParams.put(new JSONObject()
            .put("type", "text")
            .put("text", username));  // Nombre de usuario
    bodyParams.put(new JSONObject()
            .put("type", "text")
            .put("text", notificationMessage));  // Mensaje de notificación
    bodyComponent.put("parameters", bodyParams);
    components.put(bodyComponent);

    // Asociar los componentes al template
    template.put("components", components);
    message.put("template", template);

    return message;
}

// Método para enviar la solicitud HTTP a la API de WhatsApp
private void sendHttpRequest(JSONObject messageData) throws Exception {
    Request request = new Request.Builder()
            .url("https://graph.facebook.com/v17.0/" + numeroId + "/messages")
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(
                    messageData.toString(),
                    MediaType.parse("application/json")
            ))
            .build();

    // Enviar la solicitud y obtener la respuesta
    Response response = httpClient.newCall(request).execute();
    if (!response.isSuccessful()) {
        System.err.println("Error al enviar el mensaje. Código: " + response.code());
        System.err.println("Detalles: " + response.body().string());
    } else {
        System.out.println("Mensaje enviado con éxito.");
    }
}

public class whatsapp {

    // Datos de configuración de la API de WhatsApp
    private final String numeroId = "471467826048910";  // Cambia por tu propio ID de teléfono
    private final String accessToken = "TU_ACCESS_TOKEN";  // Sustituye por tu token de acceso

    private final OkHttpClient httpClient;

    // Variable para implementar el patrón Singleton
    private static whatsapp instance;

    // Constructor privado para implementar el patrón Singleton
    whatsapp() {
        this.httpClient = new OkHttpClient();
    }

    // Método para obtener la única instancia de la clase
    public static whatsapp getInstance() {
        if (instance == null) {
            instance = new whatsapp();
        }
        return instance;
    }

    // Método principal para enviar el mensaje
    public void sendNotification(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        try {
            JSONObject messageData = buildMessage(recipientPhone, notificationTitle, username, notificationMessage);
            sendHttpRequest(messageData);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje de notificación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para construir el objeto JSON con el mensaje de WhatsApp
    private JSONObject buildMessage(String recipientPhone, String notificationTitle, String username, String notificationMessage) {
        JSONObject message = new JSONObject();
        message.put("messaging_product", "whatsapp");
        message.put("to", recipientPhone);  // Número de teléfono del destinatario
        message.put("type", "template");

        // Crear el objeto del template
        JSONObject template = new JSONObject();
        template.put("name", "notificaciones");  // Nombre del template en WhatsApp
        template.put("language", new JSONObject().put("code", "es"));  // Idioma del mensaje (español)

        // Crear los componentes del mensaje (header y body)
        JSONArray components = new JSONArray();

        // Componente del encabezado
        JSONObject headerComponent = new JSONObject();
        headerComponent.put("type", "header");
        JSONArray headerParams = new JSONArray();
        headerParams.put(new JSONObject()
                .put("type", "text")
                .put("text", notificationTitle));  // Título del mensaje
        headerComponent.put("parameters", headerParams);
        components.put(headerComponent);

        // Componente del cuerpo del mensaje
        JSONObject bodyComponent = new JSONObject();
        bodyComponent.put("type", "body");
        JSONArray bodyParams = new JSONArray();
        bodyParams.put(new JSONObject()
                .put("type", "text")
                .put("text", username));  // Nombre de usuario
        bodyParams.put(new JSONObject()
                .put("type", "text")
                .put("text", notificationMessage));  // Mensaje de notificación
        bodyComponent.put("parameters", bodyParams);
        components.put(bodyComponent);

        // Asociar los componentes al template
        template.put("components", components);
        message.put("template", template);

        return message;
    }

    // Método para enviar la solicitud HTTP a la API de WhatsApp
    private void sendHttpRequest(JSONObject messageData) throws Exception {
        Request request = new Request.Builder()
                .url("https://graph.facebook.com/v17.0/" + numeroId + "/messages")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        messageData.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        // Enviar la solicitud y obtener la respuesta
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.err.println("Error al enviar el mensaje. Código: " + response.code());
            System.err.println("Detalles: " + response.body().string());
        } else {
            System.out.println("Mensaje enviado con éxito.");
        }
    }
}
