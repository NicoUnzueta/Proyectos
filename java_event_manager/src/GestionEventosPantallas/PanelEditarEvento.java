package GestionEventosPantallas;

import GestionEventos.*;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class PanelEditarEvento extends  JFrame{
        private Evento evento;
        private JTextField txtNombre, txtFecha, txtDescripcion, txtUbicacion;
        private JComboBox<TipoEvento> comboTipo;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        private Sistema sistema;
        private JButton btnGuardar;
        private JButton btnCancelar;

        public PanelEditarEvento(Evento evento,Sistema sistema) {
            this.evento = evento;
            this.sistema=sistema;
            setTitle("Editar Evento");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));

            txtNombre = new JTextField(evento.getNombre());
            comboTipo = new JComboBox<>(TipoEvento.values());
            comboTipo.setSelectedItem(evento.getTipo());
            txtFecha = new JTextField(sdf.format(evento.getFecha()));
            txtDescripcion = new JTextField(evento.getDescripcion());
            txtUbicacion = new JTextField(evento.getUbicacion());

            panelCampos.add(new JLabel("Nombre:"));
            panelCampos.add(txtNombre);
            panelCampos.add(new JLabel("Tipo de Evento:"));
            panelCampos.add(comboTipo);
            panelCampos.add(new JLabel("Fecha (dd/MM/yyyy):"));
            panelCampos.add(txtFecha);
            panelCampos.add(new JLabel("Descripción:"));
            panelCampos.add(txtDescripcion);
            panelCampos.add(new JLabel("Ubicación:"));
            panelCampos.add(txtUbicacion);

            JPanel panelBotones = new JPanel();
            btnGuardar = new JButton("Guardar cambios");
            btnCancelar = new JButton("Cancelar");
            btnGuardar.addActionListener(e -> {
                try {
                    Date fecha = sdf.parse(txtFecha.getText());
                    LocalDate hoy=LocalDate.now();
                    Date fechaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    if(fecha.before(fechaHoy)){
                        JOptionPane.showMessageDialog(null,"se debe ingresar una fecha actual o futura");
                        return;
                    }
                    Evento guardado=sistema.buscarEvento(evento.getId());
                    guardado.actualizarEvento(txtNombre.getText(),fecha,txtDescripcion.getText(),(TipoEvento) comboTipo.getSelectedItem(),txtUbicacion.getText());
                    sistema.modificarEventosEnArchivo("eventos.txt");
                    JOptionPane.showMessageDialog(this, "Evento actualizado.");
                    new PanelVerEventos(sistema,evento.getCreador()).setVisible(true);
                    this.dispose();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Fecha inválida.");
                }
            });

            btnCancelar.addActionListener(e -> {
                new PanelVerEventos(sistema,evento.getCreador()).setVisible(true);
                this.dispose();
            });
            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);
            add(panelCampos, BorderLayout.CENTER);
            add(panelBotones, BorderLayout.SOUTH);
        }
    }
