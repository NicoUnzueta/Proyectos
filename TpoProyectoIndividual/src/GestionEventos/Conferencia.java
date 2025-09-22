package GestionEventos;

import java.util.Date;

public class Conferencia extends Evento {
    public Conferencia(TipoEvento tipo,Usuario creador, String nombre, Date fecha) {
        super(tipo,creador, nombre,fecha);
        this.limitePersonas=20;
    }
    public Conferencia(TipoEvento tipo,Usuario creador, String nombre, Date fecha, String descripcion) {
        super(tipo,creador, nombre,fecha,descripcion);
        this.limitePersonas=20;
    }
    public Conferencia(TipoEvento tipo,Usuario creador, String nombre, Date fecha, String descripcion, String ubicacion) {
        super(tipo, creador, nombre, fecha, descripcion, ubicacion);
        this.limitePersonas = 20;
    }
}
