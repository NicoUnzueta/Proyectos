package GestionEventosPantallas;

import GestionEventos.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class PanelInvitar extends JFrame {
    private Evento evento;
    private Sistema sistema;

    public PanelInvitar(Sistema sistema,Evento evento) {
        setTitle("Invitar Personas");
        this.sistema = sistema;
        this.evento = evento;

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        add(scrollPane, BorderLayout.CENTER);

        Iterator<Usuario> it = sistema.getUsuarios().iterator();
        while (it.hasNext()) {
            Usuario u = it.next();
            if (u.compararUsuario(evento.getCreador().getMail())) {
                continue;
            }

            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel etiqueta = new JLabel(u.getNombre());
            JButton boton = new JButton("Invitar");

            boton.addActionListener(e -> {
                    if(evento.cantidadAsistencias()<=evento.getLimitePersonas()) {
                        if(u.buscarEvento(evento.getId())){
                            JOptionPane.showMessageDialog(null,"ya invitaste a este usuario");
                            return;
                        }
                        u.invitacionAEvento(evento);
                        sistema.guardarInvitacionesEnArchivo("invitaciones.txt", evento, u);
                        JOptionPane.showMessageDialog(null, "Invitaste a " + u.getNombre());
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"se invito al maximo nro de personas para este evento");
                        return;
                    }
            });

            fila.add(etiqueta);
            fila.add(boton);
            panelPrincipal.add(fila);
        }

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
                new PanelSistema(sistema,evento.getCreador().getMail()).setVisible(true);
                this.dispose();
        });

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}