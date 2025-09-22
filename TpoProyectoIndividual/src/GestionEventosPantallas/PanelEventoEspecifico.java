package GestionEventosPantallas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import GestionEventos.*;
import GestionEventos.TipoEvento;

public class PanelEventoEspecifico extends JFrame {
    private Evento evento;
    private Sistema sistema;
    private Usuario usuario;
    private JButton btnVolver;
    private JButton btnAsistir;
    private JButton btnVerAsistencias;
    private JButton btnEditarEvento;
    public PanelEventoEspecifico(Sistema sistema,Evento evento,Usuario u) {
        this.evento = evento;
        this.sistema=sistema;
        this.usuario=u;
        LocalDate hoy=LocalDate.now();
        Date fechaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
        setTitle("Detalles del Evento");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridLayout(5, 1));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        panelDetalles.add(new JLabel("Nombre: " + evento.getNombre()));
        panelDetalles.add(new JLabel("Fecha: " + sdf.format(evento.getFecha())));
        panelDetalles.add(new JLabel("Nombre: " + evento.getCreador().getNombre()));
        panelDetalles.add(new JLabel("Tipo: " + evento.getTipo().toString()));
        panelDetalles.add(new JLabel("Descripción: " + evento.getDescripcion()));
        panelDetalles.add(new JLabel("Ubicación: " + evento.getUbicacion()));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

         btnAsistir = new JButton("Asistir");
         btnVolver = new JButton("volver");
        btnVerAsistencias=new JButton("asistencias");
        btnEditarEvento=new JButton("editar evento");
        btnAsistir.addActionListener(e -> {
            if(evento.compararUsuario(usuario.getMail())){
                JOptionPane.showMessageDialog(null,"usted ya confirmo su asistencia al evento");
                return;
            }
            JOptionPane.showMessageDialog(this, "Has confirmado tu asistencia.");
            Iterator<Evento> it=sistema.getEventos().iterator();
            while(it.hasNext()){
                Evento guardado=it.next();
                if(guardado.compararId(evento.getId())){
                    guardado.asisteUsuario(usuario);
                    sistema.guardarAsistenciasEnArchivo("asistencias.txt",evento,usuario);
                   new PanelVerEventos(sistema,usuario).setVisible(true);
                    this.dispose();
                }
            }
        });
        btnVolver.addActionListener(e -> {
            if(evento.getFecha().before(fechaHoy)){
                new PanelHistorialEvento(sistema,u);
            }
            else {
                new PanelVerEventos(sistema, usuario).setVisible(true);
            }
            this.dispose();
        });
        btnVerAsistencias.addActionListener(e -> {
            JDialog dialogoAsistencias = new JDialog(this, "Asistencias", true);
            dialogoAsistencias.setSize(300, 200);
            dialogoAsistencias.setLocationRelativeTo(this);

            JPanel panelAsistencias = new JPanel();
            panelAsistencias.setLayout(new BoxLayout(panelAsistencias, BoxLayout.Y_AXIS));

            List<Usuario> asistentes = evento.getAsistencias();
            if (asistentes.isEmpty()) {
                if(evento.getFecha().before(fechaHoy)){
                    JOptionPane.showMessageDialog(null,"no asistio nadie al vento");
                }
                JOptionPane.showMessageDialog(null,"actualmentenadie al evento");
                return;
            } else {
                if(evento.compararUsuario(usuario.getMail())){
                    JOptionPane.showMessageDialog(null,"esta informacion es solo para el creador del evento");
                    return;
                }
                if(evento.getFecha().before(fechaHoy)){
                    JOptionPane.showMessageDialog(null,"asistieron "+asistentes.size()+" al evento");
                }
                else {
                    JOptionPane.showMessageDialog(null, "estan asistiendo " + asistentes.size() + " personas");
                }
                for (Usuario usuario : asistentes) {
                    panelAsistencias.add(new JLabel("- " + usuario.getNombre()));
                }
            }
            JScrollPane scroll = new JScrollPane(panelAsistencias);
            dialogoAsistencias.add(scroll);

            dialogoAsistencias.setVisible(true);
        });
        btnEditarEvento.addActionListener(e -> {
                new PanelEditarEvento(evento, sistema).setVisible(true);
                this.dispose();
        });

        if(evento.compararCreador(usuario.getMail()) ){
            if((evento.getFecha().after(fechaHoy) )) {
                panelBotones.add(btnEditarEvento);
            }
            panelBotones.add(btnVerAsistencias);
        }
        else if(evento.getFecha().after(fechaHoy) || evento.getFecha().equals(fechaHoy)) {
            panelBotones.add(btnAsistir);
        }
        panelBotones.add(btnVolver);

        add(panelDetalles, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}