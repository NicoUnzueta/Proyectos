package GestionEventosPantallas;
import GestionEventos.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PanelVerEventos extends JFrame{
    private Usuario usuario;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Sistema sistema;
    public PanelVerEventos(Sistema sistema,Usuario u) {
        setTitle("Eventos");
        this.sistema = sistema;
        this.usuario = u;
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        List<Evento> eventosUsuario=u.getEventosFuturos();
        eventosUsuario.sort(Comparator.comparing(Evento::getFecha));
        Iterator<Evento> it = eventosUsuario.iterator();
        JLabel etiqueta;
        while (it.hasNext()) {
            Evento evento = it.next();
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                if(evento.calcularDiasRestantes()<30 && evento.calcularDiasRestantes()!=0) {
                    etiqueta = new JLabel(evento.getNombre() + ",de: " + evento.getCreador().getNombre() + " en " + evento.calcularDiasRestantes() + " dias");
                }
                else if(evento.calcularDiasRestantes()!=0){
                    etiqueta = new JLabel(evento.getNombre() + ",de: " + evento.getCreador().getNombre() + " el "+ sdf.format(evento.getFecha()));
                }
                else {
                    etiqueta = new JLabel(evento.getNombre() + ",de: " + evento.getCreador().getNombre() + " es hoy");
                }
            JButton boton = new JButton("elegir");

            boton.addActionListener(e -> {
                    new PanelEventoEspecifico(sistema,evento,u).setVisible(true);
                    this.dispose();
            });
            fila.add(etiqueta);
            fila.add(boton);
            panelPrincipal.add(fila);
        }
        JButton btnVolver = new JButton("Volver");
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
