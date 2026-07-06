package GestionEventos;

import java.util.Date;

public class ReunionPequeña extends Evento {
    public ReunionPequeña(TipoEvento tipo, Usuario creador, String nombre, Date fecha) {
        super(tipo, creador, nombre, fecha);
        this.limitePersonas = 15;
    }

    public ReunionPequeña(TipoEvento tipo, Usuario creador, String nombre, Date fecha, String descripcion) {
        super(tipo, creador, nombre, fecha, descripcion);
        this.limitePersonas = 15;
    }

    public ReunionPequeña(TipoEvento tipo, Usuario creador, String nombre, Date fecha, String descripcion, String ubicacion) {
        super(tipo, creador, nombre, fecha, descripcion, ubicacion);
        this.limitePersonas = 15;
    }
}

