package gestionhorario.aplicacionmovil.uts.horario.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 21/03/2017.
 */

public class Ubicacion implements Serializable {
    private int idUbicacion;
    private String nombre;
    private List<Horario> listaHorario = new ArrayList<>();

    public Ubicacion() {
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Horario> getListaHorario() {
        return listaHorario;
    }

    public void setListaHorario(List<Horario> listaHorario) {
        this.listaHorario = listaHorario;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
