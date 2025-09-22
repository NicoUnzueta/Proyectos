package GestionEventos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class Evento {
    private static int contadorID = 0;
    private int id;
    private String nombre;
    private Date fecha;
    private String descripcion;
    private String ubicacion;
    protected int limitePersonas;
    private Usuario creador;
    private TipoEvento tipo;
    private List<Usuario> asistencias = new ArrayList<>();

    public Evento(TipoEvento tipo, Usuario creador, String nombre, Date fecha) {
        this.id = contadorID++;
        this.tipo = tipo;
        this.creador = creador;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = "no especificado";
        this.ubicacion = "no especificado";
    }

    public Evento(TipoEvento tipo, Usuario creador, String nombre, Date fecha, String descripcion) {
        this.id = contadorID++;
        this.tipo = tipo;
        this.creador = creador;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.ubicacion = "no especificado";
    }

    public Evento(TipoEvento tipo, Usuario creador, String nombre, Date fecha, String descripcion, String ubicacion) {
        this.id = contadorID++;
        this.tipo = tipo;
        this.creador = creador;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }
    public int cantidadAsistencias(){
        return this.asistencias.size();
    }
    public Boolean compararId(int id) {
        return this.id == id;
    }
    public long calcularDiasRestantes(){
        Date fechaEvento = this.fecha;
        LocalDate fechaEventoLocal = fechaEvento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate hoy=LocalDate.now();
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaEventoLocal);
        return diasRestantes;
    }
    public void actualizarEvento(String nombre,Date fecha,String descripcion,TipoEvento tipo,String ubicacion) {
        setNombre(nombre);
        setFecha(fecha);
        setTipo(tipo);
        setDescripcion(descripcion);
        setUbicacion(ubicacion);
    }
    public boolean compararCreador(String mail){
        return this.getCreador().compararUsuario(mail);
    }
    public boolean compararUsuario(String mail){
        Iterator<Usuario> it=asistencias.iterator();
        while (it.hasNext()){
            Usuario usuario=it.next();
            if(usuario.compararUsuario(mail)){
                return true;
            }
        }
        return false;
    }
    public void asisteUsuario(Usuario u) {
        asistencias.add(u);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }
    public TipoEvento getTipo() {
        return this.tipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public int getId() {
        return this.id;
    }

    public int getLimitePersonas() {
        return this.limitePersonas;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Usuario getCreador() {
        return this.creador;
    }

    public List<Usuario> getAsistencias() {
        return this.asistencias;
    }
}
