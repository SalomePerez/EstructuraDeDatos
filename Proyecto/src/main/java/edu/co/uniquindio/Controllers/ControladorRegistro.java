package edu.co.uniquindio.Controllers;

import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.EstructuraDeDatos.Nodo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class ControladorRegistro {

    @FXML
    private TextField textNombre;

    @FXML
    private TextField textCorreo;

    @FXML
    private TextField textIdentificaion;

    @FXML
    private TextField textContraseniaRegistro;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnVolverLogin;

    private ListaEnlazada<Usuario> usuarios;

    public ControladorRegistro() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();
    }

    @FXML
    private void initialize() {
        // Configurar los eventos de los botones
        btnVolverLogin.setOnAction(event -> volverLogin());
    }

    private void cargarUsuarios() {
        try {
            usuarios = AdministradorArchivos.cargarUsuarios();
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
            usuarios = new ListaEnlazada<>();
        }
    }

    private void registrarUsuario() {
        String nombre = textNombre.getText();
        String correo = textCorreo.getText();
        String celular = textIdentificaion.getText();
        String contrasenia = textContraseniaRegistro.getText();

        if (nombre.isEmpty() || correo.isEmpty() || celular.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos.", AlertType.ERROR);
            return;
        }

        if (existeUsuario(correo)) {
            mostrarAlerta("Error", "Ya existe un usuario con ese correo.", AlertType.ERROR);
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, correo, correo, contrasenia);
        usuarios.insertar(nuevoUsuario);

        // Guardar directamente en XML
        Persistencia.guardarUsuarioEnXML(nuevoUsuario);

        mostrarAlerta("Éxito", "Registro exitoso. Ahora puede iniciar sesión.", AlertType.INFORMATION);
        volverLogin();
    }

    public static boolean existeUsuario(String correo) {
        try {
            File archivo = new File("/edu/co/uniquindio/Application/files/usuarios.xml");

            // Si el archivo no existe, no hay usuarios
            if (!archivo.exists()) {
                return false;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);

            // Crear una lista enlazada para almacenar los correos
            ListaEnlazada<String> listaCorreos = new ListaEnlazada<>();

            // Procesar el archivo XML para extraer los correos
            Element rootElement = doc.getDocumentElement(); // Nodo raíz <Usuarios>
            Element usuarioActual = getFirstChildElement(rootElement);

            while (usuarioActual != null) {
                if (usuarioActual.getTagName().equals("Usuario")) {
                    // Obtener el correo del elemento actual
                    Element correoElement = getFirstChildElement(usuarioActual);
                    if (correoElement != null && correoElement.getTagName().equals("correo")) {
                        String correoGuardado = correoElement.getTextContent();
                        listaCorreos.insertar(correoGuardado); // Insertar en la lista enlazada
                    }
                }
                usuarioActual = getNextSiblingElement(usuarioActual); // Mover al siguiente nodo
            }

            // Recorrer la lista enlazada para verificar si existe el correo
            Nodo<String> nodoActual = listaCorreos.getCabeza(); // Primer nodo de la lista
            while (nodoActual != null) {
                if (nodoActual.getDato().equals(correo)) {
                    return true; // Correo encontrado
                }
                nodoActual = nodoActual.getSiguiente(); // Avanzar al siguiente nodo
            }

            return false; // Correo no encontrado

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }


    private void volverLogin() {
        // Redirigir a la pantalla de login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Application/vista/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Obtén la ventana (Stage) actual y cámbiala por la nueva escena
            Stage stage = (Stage) btnVolverLogin.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio de sesión.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Métodos auxiliares para navegación DOM
    private static Element getFirstChildElement(Element parent) {
        Node node = parent.getFirstChild();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return (Element) node;
    }

    private static Element getNextSiblingElement(Element element) {
        if (element == null) return null;
        Node node = element.getNextSibling();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return (Element) node;
    }

    public void guadar(ActionEvent actionEvent) {
        String nombre = textNombre.getText();
        String correo = textCorreo.getText();
        String celular = textIdentificaion.getText();
        String contrasenia = textContraseniaRegistro.getText();

        if (nombre.isEmpty() || correo.isEmpty() || celular.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos.", AlertType.ERROR);
            return;
        }

        if (existeUsuario(correo)) {
            mostrarAlerta("Error", "Ya existe un usuario con ese correo.", AlertType.ERROR);
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, celular, correo, contrasenia);
        usuarios.insertar(nuevoUsuario);

        // Guardar directamente en XML
        Persistencia.guardarUsuarioEnXML(nuevoUsuario);

        mostrarAlerta("Éxito", "Registro exitoso. Ahora puede iniciar sesión.", AlertType.INFORMATION);
        volverLogin();

    }
}