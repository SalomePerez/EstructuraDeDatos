package edu.co.uniquindio.Model.Notificacion;

import jakarta.mail.MessagingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncEmailService {
    private final ExecutorService threadPool;
    private final Email email = Email.getInstance();
    private static AsyncEmailService instance;

    // Constructor
    public AsyncEmailService() {
        this.threadPool = Executors.newFixedThreadPool(2);  // Usamos un pool con 2 hilos
    }

    // Método Singleton para obtener la instancia única
    public static AsyncEmailService getInstance() {
        if (instance == null) {
            instance = new AsyncEmailService();
        }
        return instance;
    }

    // Método para enviar correos de forma asincrónica
    public void sendEmailAsync(String subject, String body) {
        threadPool.submit(() -> {
            try {
                String userEmail = "";
                FileReader fileReader = new FileReader("src/main/resources/Login_Archivo/UsuarioActual");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                userEmail = bufferedReader.readLine(); // Leer el correo del usuario
                email.enviarCorreo(userEmail, subject, body); // Enviar el correo
                bufferedReader.close();
                fileReader.close();
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    // Método para detener el ExecutorService cuando no se necesite más
    public void shutdown() {
        threadPool.shutdown();
    }

}
