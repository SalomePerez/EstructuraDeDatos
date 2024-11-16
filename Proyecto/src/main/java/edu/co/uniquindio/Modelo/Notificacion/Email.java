package edu.co.uniquindio.Modelo.Notificacion;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.AddressException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Esta clase proporciona funcionalidades para enviar correos electrónicos de manera segura utilizando el protocolo SMTP.
 * Se utiliza una configuración para la autenticación con el servidor SMTP de Gmail y valida las direcciones de correo antes de enviar los mensajes.
 * Se implementa el patrón Singleton, lo que garantiza que solo haya una instancia de esta clase.
 */

public class Email {

    // Nombre de usuario de la cuenta de correo
    private final String correoUsuario = "danielhisaza16@gmail.com";

    // Contraseña de la cuenta de correo
    private final String contrasena = "ylkybslrxbjksrfs";

    // Propiedades de configuración para la conexión con el servidor SMTP
    private final Properties configuraciones;

    // Instancia única de la clase (Singleton)
    private static Email instancia;

    // Patrón de expresión regular para validar el formato del correo electrónico
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Constructor privado que inicializa las configuraciones del servidor SMTP.
     */
    public Email() {
        configuraciones = new Properties();
        configuraciones.put("mail.smtp.host", "smtp.gmail.com");
        configuraciones.put("mail.smtp.port", "587");
        configuraciones.put("mail.smtp.auth", "true");
        configuraciones.put("mail.smtp.starttls.enable", "true");
    }

    /**
     * Método para obtener la única instancia de la clase (patrón Singleton).
     *
     * @return la instancia de la clase Email
     */
    public static Email getInstance() {
        if (instancia == null) {
            instancia = new Email();
        }
        return instancia;
    }

    /**
     * Método que valida el formato de un correo electrónico utilizando una expresión regular.
     *
     * @param correo el correo electrónico a validar
     * @return verdadero si el correo tiene un formato válido, falso en caso contrario
     */
    private boolean esCorreoValido(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo).matches();
    }

    /**
     * Método para enviar un correo electrónico.
     *
     * @param destinatario el correo electrónico del destinatario
     * @param asunto el asunto del correo
     * @param mensaje el cuerpo del correo
     * @throws MessagingException si ocurre un error al intentar enviar el correo
     */
    public void enviarCorreo(String destinatario, String asunto, String mensaje) throws MessagingException {
        // Validar el correo del remitente
        if (!esCorreoValido(correoUsuario)) {
            throw new MessagingException("La dirección del remitente no es válida: " + correoUsuario);
        }

        // Validar el correo del destinatario
        if (!esCorreoValido(destinatario)) {
            throw new MessagingException("La dirección del destinatario no es válida: " + destinatario);
        }

        try {
            // Crear la sesión de correo con autenticación
            Session session = Session.getInstance(configuraciones, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoUsuario, contrasena);
                }
            });

            // Crear el mensaje MIME
            Message mensajeCorreo = new MimeMessage(session);

            // Configurar el remitente
            try {
                mensajeCorreo.setFrom(new InternetAddress(correoUsuario, true));
            } catch (AddressException e) {
                throw new MessagingException("Error al configurar el remitente: " + e.getMessage());
            }

            // Configurar el destinatario
            try {
                mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, true));
            } catch (AddressException e) {
                throw new MessagingException("Error al configurar el destinatario: " + e.getMessage());
            }

            // Establecer el asunto y el cuerpo del mensaje
            mensajeCorreo.setSubject(asunto);
            mensajeCorreo.setText(mensaje);

            // Enviar el mensaje
            Transport.send(mensajeCorreo);
        } catch (MessagingException e) {
            throw new MessagingException("Error al enviar el correo: " + e.getMessage());
        }
    }
}
