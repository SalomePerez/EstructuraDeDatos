package estructura.model;

import java.io.Serializable;

public class Usuario  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String user;
    private String password;
    private String rol;

    public Usuario() {
    }

    public Usuario(String user, String password) {
        this.user = user;
        this.password = password;
        this.rol = "Usuario";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
