package APP;

import GestionEventos.Sistema;
import GestionEventosPantallas.PanelLogin;

import java.text.ParseException;

public class App {
    public static void main(String[] args) throws ParseException {
        Sistema sistema = new Sistema();
        sistema.cargar();
        new PanelLogin(sistema).setVisible(true);
    }
}
