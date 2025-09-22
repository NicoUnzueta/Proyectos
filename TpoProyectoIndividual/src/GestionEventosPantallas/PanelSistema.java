package GestionEventosPantallas;

import GestionEventos.*;
import javax.swing.*;
import java.awt.*;

public class PanelSistema extends JFrame {
    private JButton btnCambiarDeUsuario = new JButton("Cambiar de usuario");
    private JButton btnCrearEvento = new JButton("Crear Evento");
    private JButton btnVerEventos = new JButton("Ver Eventos actuales");
    private JButton btnHistorialEventos = new JButton("Ver historial de eventos");
    private Usuario usuario;
    private Sistema sistema;

    public PanelSistema(Sistema sistema,String mail) {
        this.sistema=sistema;
        this.usuario =sistema.buscarUsuario(mail) ;
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel("Crea tu evento con nosotros"));
        add(new JLabel());

        add(btnCrearEvento);
        add(btnVerEventos);

        add(btnHistorialEventos);
        add(btnCambiarDeUsuario);

        btnCrearEvento.addActionListener(e -> {
            new PanelCrearEvento(sistema,this,usuario).setVisible(true);
            this.dispose();
            return;
        });
        btnCambiarDeUsuario.addActionListener(e -> {
            new PanelLogin(sistema).setVisible(true);
            this.dispose(); });
        btnHistorialEventos.addActionListener(e -> {
            if(usuario.getHistorialEventos().isEmpty()){
                JOptionPane.showMessageDialog(null,"todavia no se realizo ningun evento en el que haya participado");
                return;
            }
            new PanelHistorialEvento(sistema,usuario).setVisible(true);
            this.dispose();
        });
        btnVerEventos.addActionListener(e -> {
            if(usuario.getEventosFuturos().isEmpty()){
                JOptionPane.showMessageDialog(null,"por el momento no hay eventos ");
                return;
            }
            new PanelVerEventos(sistema,usuario).setVisible(true);
            this.dispose();
        });
        setTitle("Panel del Sistema");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}