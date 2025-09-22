package GestionEventos;

import javax.swing.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Sistema {
    private List<Usuario> usuarios=new ArrayList<>();
    private List<Evento> eventos=new ArrayList<>();

        public void crearUsuario(String nombre, String mail) {
            for(Usuario u : usuarios) {
                if(u.getMail().equalsIgnoreCase(mail)) {
                    return;
                }
            }
            Usuario nuevo = new Usuario(nombre, mail);
            usuarios.add(nuevo);
            guardarUsuariosEnArchivo("usuarios.txt", nuevo);
        }
        public Evento buscarEvento(int id){
            Iterator<Evento> it=eventos.iterator();
            while(it.hasNext()){
                Evento e=it.next();
                if(e.compararId(id)){
                        return e;
                }
            }
            return null;
        }

        public Usuario buscarUsuario(String mail) {
            Iterator<Usuario> it=usuarios.iterator();
            while (it.hasNext()) {
                Usuario u = it.next();
                if (u.compararUsuario(mail)) {
                 return u;
                }
            }
            return null;
        }
        public void cargar(){
            cargarUsuariosDesdeArchivo("usuarios.txt");
            cargarEventosDesdeArchivo("eventos.txt");
            cargarInvitacionesEnArchivo("invitaciones.txt");
            cargarAsistenciasEnArchivo("asistencias.txt");
        }

    public Evento crearEvento(String mail, TipoEvento tipo, String nombre, Date fecha) {
        return crearEventoCompleto(mail, tipo, nombre, fecha, "no especificado", "no especificado");
    }

    public Evento crearEvento(String mail, TipoEvento tipo, String nombre, Date fecha, String descripcion) {
        return crearEventoCompleto(mail, tipo, nombre, fecha, descripcion, "no especificado");
    }

    public Evento crearEvento(String mail, TipoEvento tipo, String nombre, Date fecha, String descripcion, String ubicacion) {
        return crearEventoCompleto(mail, tipo, nombre, fecha, descripcion, ubicacion);
    }

    private Evento crearEventoCompleto(String mail, TipoEvento tipo, String nombre, Date fecha, String descripcion, String ubicacion) {
        Usuario u = buscarUsuario(mail);
        Evento evento;

        switch (tipo) {
            case REUNION_PEQUEÑA:
                evento = new ReunionPequeña(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case REUNION_MEDIANA:
                evento = new ReunionMediana(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case REUNION_GRANDE:
                evento = new ReunionGrande(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case CONFERENCIA:
                evento = new Conferencia(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            default:
                System.out.println("se envio un termino no valido");
                return null;
        }

        eventos.add(evento);
        u.getEventos().add(evento);
        guardarEventosEnArchivo("eventos.txt", evento);
        return evento;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    private void cargarUsuario(String nombre, String mail){
            Usuario guardado=new Usuario(nombre,mail);
            usuarios.add(guardado);
        }

        private void guardarUsuariosEnArchivo(String nombreArchivo, Usuario u) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
                writer.println(u.getNombre() + ";" + u.getMail());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar usuario: " + e.getMessage());
            }
        }
        private void cargarUsuariosDesdeArchivo(String nombreArchivo) {
            usuarios.clear(); // limpiar lista antes de cargar
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 2) {
                        String nombre = partes[0];
                        String mail = partes[1];
                        cargarUsuario(nombre, mail);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void guardarEventosEnArchivo(String nombreArchivo,Evento evento){
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaTexto = formato.format(evento.getFecha());
                writer.println(evento.getTipo()+ ";" +evento.getNombre() + ";" + fechaTexto+ ";" + evento.getCreador().getMail()+ ";" +evento.getDescripcion()+";"+evento.getUbicacion());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar evento: " + e.getMessage());
            }
        }
    public void modificarEventosEnArchivo(String nombreArchivo){
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            Iterator<Evento> it=eventos.iterator();
            while(it.hasNext()) {
                Evento evento=it.next();
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaTexto = formato.format(evento.getFecha());
                writer.println(evento.getTipo() + ";" + evento.getNombre() + ";" + fechaTexto + ";" + evento.getCreador().getMail() + ";" + evento.getDescripcion() + ";" + evento.getUbicacion());
            }
            } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar evento: " + e.getMessage());
        }
    }
    private void cargarEvento( String mail, TipoEvento tipo, String nombre, Date fecha, String descripcion, String ubicacion){
        Usuario u = buscarUsuario(mail);
        Evento e;
        Iterator<Evento> it=eventos.iterator();
        int cont=1;
        while(it.hasNext()){
            Evento muestra=it.next();
        }
        switch (tipo) {
            case REUNION_PEQUEÑA:
                e = new ReunionPequeña(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case REUNION_MEDIANA:
                e = new ReunionMediana(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case REUNION_GRANDE:
                e = new ReunionGrande(tipo, u, nombre, fecha, descripcion, ubicacion);
                break;
            case CONFERENCIA:
                e = new Conferencia(tipo, u, nombre, fecha, descripcion, ubicacion);break;
            default:
                return;
        }
        eventos.add(e);
        u.getEventos().add(e);
    }
        private void cargarEventosDesdeArchivo(String nombreArchivo){
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 6) {
                        TipoEvento tipo = TipoEvento.valueOf(partes[0]);
                        String nombre = partes[1];
                        Date fecha=sdf.parse(partes[2]);
                        String mailCreador=partes[3];
                        String descripcion=partes[4];
                        String ubicacion=partes[5];
                        cargarEvento(mailCreador,tipo,nombre,fecha,descripcion,ubicacion);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        public void guardarInvitacionesEnArchivo(String nombreArchivo,Evento evento, Usuario u){
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
                writer.println(evento.getId() + ";" + u.getMail());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void cargarInvitacionesEnArchivo(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    String idEvento = partes[0];
                    int id = Integer.parseInt(idEvento);
                    String mailInvitado = partes[1];
                    Evento evento = buscarEvento(id);
                    Usuario usuario = buscarUsuario(mailInvitado);
                    if (evento != null && usuario != null) {
                        usuario.invitacionAEvento(evento);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void guardarAsistenciasEnArchivo(String nombreArchivo,Evento evento, Usuario u){
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
            writer.println(evento.getId() + ";" + u.getMail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cargarAsistenciasEnArchivo(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    String idEvento = partes[0];
                    int id = Integer.parseInt(idEvento);
                    String mailInvitado = partes[1];
                    Evento evento = buscarEvento(id);
                    Usuario usuario = buscarUsuario(mailInvitado);
                    if (evento != null && usuario != null) {
                        evento.asisteUsuario(usuario);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
