package GestionEventos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Usuario {
    private String nombre;
    private String mail;
    private List<Evento> eventos=new ArrayList<>();


    public Usuario(String nombre,String mail){
        this.nombre=nombre;
        this.mail=mail;
    }

    public void invitacionAEvento(Evento e){
        this.eventos.add(e);
    }
    public boolean buscarEvento(int id){
        Iterator<Evento> it=eventos.iterator();
        while(it.hasNext()){
            Evento evento =it.next();
            if(evento.compararId(id)){
                return true;
            }
        }
        return false;
    }
    public boolean compararUsuario(String mail){
        return this.mail.equals(mail);
    }

    public String getMail() {
        return this.mail;
    }

    public String getNombre() {
        return this.nombre;
    }
    public List<Evento> getEventosInvitados() {
        List<Evento> invitaciones=new ArrayList<>();
        Iterator<Evento> it=eventos.iterator();
        while(it.hasNext()){
            Evento e=it.next();
            if(e.getCreador().equals(this.mail)){
                continue;
            }
            invitaciones.add(e);
        }
        return invitaciones;
    }
    public List<Evento> getEventosCreados() {
        List<Evento> eventosPropios=new ArrayList<>();
        Iterator<Evento> it=eventos.iterator();
        while(it.hasNext()){
            Evento e=it.next();
            if(e.getCreador().equals(this.mail)){
                eventosPropios.add(e);
            }
        }
        return eventosPropios;
    }
    public List<Evento> getEventos() {
        return eventos;
    }
    public List<Evento> getHistorialEventos(){
        List<Evento> historial=new ArrayList<>();
        LocalDate hoy=LocalDate.now();
        Date fechaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Iterator<Evento> it=eventos.iterator();
        while(it.hasNext()){
            Evento evento=it.next();
            if(evento.getFecha().before(fechaHoy)){
                historial.add(evento);
            }
        }
        return historial;
    }
    public List<Evento> getEventosFuturos(){
        List<Evento> futuros=new ArrayList<>();
        LocalDate hoy=LocalDate.now();
        Date fechaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Iterator<Evento> it=eventos.iterator();
        while(it.hasNext()){
            Evento evento=it.next();
            if(evento.getFecha().after(fechaHoy) || evento.getFecha().equals(fechaHoy) ){
                futuros.add(evento);
            }
        }
        return futuros;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
