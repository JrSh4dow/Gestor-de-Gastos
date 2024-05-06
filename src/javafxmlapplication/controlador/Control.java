package javafxmlapplication.controlador;

import model.User;
/**
 *
 * @author MEG
 */
public class Control {
    
    private static User user;
    
    private static boolean loggedIn = false;

    public static void cambiarLoggedIn(boolean  b) {
        loggedIn = b;
    }

    public static boolean isLogged() {
        return loggedIn;
    }
    
    public static void setUsuario(User m) {
        Control.user = m;
    }
    
    public static User getUsuario() {
        return user;
    }
}