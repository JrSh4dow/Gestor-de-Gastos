package utils;

import java.io.IOException;
import javafx.scene.control.TextField;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 *
 * @author
 */
public class Utils {

    public static Boolean checkEditEmail(TextField Email) {
        return User.checkEmail(Email.getText());
    }

    public static Boolean checkEditPass(TextField Pass) {
        return User.checkPassword(Pass.getText());
    }

    public static int checkEquals(TextField Pass, TextField Rpass) {
        return Pass.getText().compareTo(Rpass.getText());
    }

    public static boolean validarNickname(TextField t) throws IOException, AcountDAOException {
        Acount ac = Acount.getInstance();
        boolean existe = ac.existsLogin(t.getText());
        return User.checkNickName(t.getText()) && !existe;
    }

    public static boolean validarDatos(TextField t) {
        String s = t.getText();
        return (!s.isEmpty()) && (s.trim().length() != 0);
    }

}
