package GestionEventos;

import java.util.Date;

public class ReunionGrande extends Evento {
    public ReunionGrande(TipoEvento tipo,Usuario creador, String nombre, Date fecha) {
        super(tipo,creador, nombre,fecha);
        this.limitePersonas=45;
    }
    public ReunionGrande(TipoEvento tipo,Usuario creador, String nombre, Date fecha, String descripcion) {
        super(tipo,creador, nombre,fecha,descripcion);
        this.limitePersonas=45;
    }
    public ReunionGrande(TipoEvento tipo,Usuario creador, String nombre, Date fecha, String descripcion, String ubicacion) {
        super(tipo,creador, nombre,fecha,descripcion,ubicacion);
        this.limitePersonas=45;
    }
}
