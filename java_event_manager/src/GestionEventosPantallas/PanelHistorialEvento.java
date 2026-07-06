package GestionEventosPantallas;

import GestionEventos.Evento;
import GestionEventos.Sistema;
import GestionEventos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PanelHistorialEvento extends JFrame {
    private Usuario usuario;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Sistema sistema;
    private JButton btnVolver;
    public PanelHistorialEvento(Sistema sistema,Usuario u) {
        setTitle("Historial de eventos");
        this.sistema = sistema;
        this.usuario = u;
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        List<Evento> historial=u.getEventos();
        historial.sort(Comparator.comparing(Evento::getFecha).reversed());
        Iterator<Evento> it = usuario.getHistorialEventos().iterator();
        while (it.hasNext()) {
            Evento evento = it.next();
                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel etiqueta = new JLabel(evento.getNombre() + ",de: " + evento.getCreador().getNombre() + " el : " + sdf.format(evento.getFecha()));
                JButton boton = new JButton("elegir");
                boton.addActionListener(e -> {
                        new PanelEventoEspecifico(sistema, evento, u).setVisible(true);
                        this.dispose();
                });
                fila.add(etiqueta);
                fila.add(boton);
                panelPrincipal.add(fila);
        }
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            new PanelSistema(sistema,usuario.getMail()).setVisible(true);
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
