package GestionEventosPantallas;
import GestionEventos.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelLogin extends JFrame {
    private JTextField txtNombreUsuario;
    private JTextField txtMailUsuario;
    private JButton btnCrearUsuario;
    private JButton btnIngresar;
    private Sistema sistema;
    public PanelLogin(Sistema sistema){
        this.sistema=sistema;

        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel("Nombre Usuario:"));
        txtNombreUsuario = new JTextField();
        add(txtNombreUsuario);

        add(new JLabel("Mail Usuario:"));
        txtMailUsuario = new JTextField();
        add(txtMailUsuario);

        btnCrearUsuario = new JButton("Crear Usuario");
        add(btnCrearUsuario);
        add(new JLabel());

        btnIngresar= new JButton("ingresar");
        add(btnIngresar);

        btnCrearUsuario.addActionListener(e -> {
            String nombre = txtNombreUsuario.getText();
            String mail = txtMailUsuario.getText();
            if(nombre.equals("") || mail.equals("")){
                JOptionPane.showMessageDialog(null,"No se ingreso el usuario o mail, por favor ingrese algo");
                return;
            }
            Usuario u=sistema.buscarUsuario(mail);
            if(u!=null){
                JOptionPane.showMessageDialog(null, "ese usuario ya existe");
                return;
            }
            sistema.crearUsuario(nombre, mail);
            JOptionPane.showMessageDialog(null, "Usuario creado");
            new PanelSistema(sistema,mail).setVisible(true);
            this.dispose();
        });
        btnIngresar.addActionListener(e -> {
            try {
                String nombre = txtNombreUsuario.getText();
                String mail = txtMailUsuario.getText();
                if(nombre.equals("") || mail.equals("")){
                    JOptionPane.showMessageDialog(null,"No se ingreso el usuario o mail, por favor ingrese algo");
                    return;
                }
                Usuario u=sistema.buscarUsuario(mail);
                if ( u!=null && sistema.buscarUsuario(mail).getNombre().equals(nombre)) {
                    JOptionPane.showMessageDialog(null, "Bienvenido "+u.getNombre());
                    new PanelSistema(sistema,mail).setVisible(true);
                    this.dispose();
                }
                else if(u!=null){
                    JOptionPane.showMessageDialog(null, "nombre ingresado erroneo");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Ese usuario no existe");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ocurrió un error: " + ex.getMessage());
            }
        });
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar
        setVisible(true);
    }
}

