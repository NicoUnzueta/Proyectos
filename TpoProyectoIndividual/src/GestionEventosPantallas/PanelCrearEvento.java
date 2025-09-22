package GestionEventosPantallas;
import GestionEventos.*;
import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class PanelCrearEvento extends JFrame {
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtUbicacion;
    private JTextArea txtDescripcion;
    private JComboBox<TipoEvento> comboTipo;
    private JButton btnCancelar;
    private JButton btnCrear;
    private JFrame previo;
    private Usuario creador;
    private Sistema sistema;
    public PanelCrearEvento(Sistema sistema,JFrame previo,Usuario u) {
        this.sistema=sistema;
        this.creador=u;
        this.previo=previo;
        setLayout(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(0, 2, 10, 10));

        campos.add(new JLabel("Nombre del evento:"));
        txtNombre = new JTextField();
        campos.add(txtNombre);

        campos.add(new JLabel("Fecha (DD-MM-YYYY):"));
        txtFecha = new JTextField();
        campos.add(txtFecha);

        campos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scroll = new JScrollPane(txtDescripcion);
        campos.add(scroll);

        campos.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        campos.add(txtUbicacion);

        campos.add(new JLabel("Tipo de evento:"));
        comboTipo = new JComboBox<>(TipoEvento.values());
        campos.add(comboTipo);



        add(campos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnCancelar);
        botones.add(btnCrear);
        add(botones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> {
            previo.setVisible(true);
            this.dispose();
        });

        btnCrear.addActionListener(e -> {
            Evento evento = null;
            try {
                String nombre = txtNombre.getText();
                String fechaStr = txtFecha.getText();
                String ubicacion = txtUbicacion.getText();
                String descripcion = txtDescripcion.getText();
                TipoEvento tipo = (TipoEvento) comboTipo.getSelectedItem();

                if (nombre.isEmpty() || fechaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mínimo se deben ingresar el nombre y la fecha del evento.");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false);
                Date fecha = sdf.parse(fechaStr);
                LocalDate hoy=LocalDate.now();
                Date fechaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

                if(fecha.before(fechaHoy) ){
                    JOptionPane.showMessageDialog(null,"se debe ingresar una fecha actual o futura");
                    return;
                }

                if (descripcion.isEmpty() && ubicacion.isEmpty()) {
                    evento = sistema.crearEvento(creador.getMail(), tipo, nombre, fecha);
                } else if (ubicacion.isEmpty()) {
                    evento = sistema.crearEvento(creador.getMail(),tipo, nombre, fecha, descripcion);
                } else {
                    evento = sistema.crearEvento(creador.getMail(), tipo, nombre, fecha, descripcion, ubicacion);
                }
                JOptionPane.showMessageDialog(this,"evento creado");

                new PanelInvitar(sistema,evento).setVisible(true);
                this.dispose();

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Formato esperado: dd-MM-yyyy");
            }
        });
        setTitle("Panel del Sistema");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
