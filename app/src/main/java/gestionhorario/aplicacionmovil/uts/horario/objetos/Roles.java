package gestionhorario.aplicacionmovil.uts.horario.objetos;

/**
 * Created by Miguel on 25/09/2016.
 */
public class Roles {
    private int idRol;
    private String nombre;

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return idRol + "-" + nombre;
    }
}
