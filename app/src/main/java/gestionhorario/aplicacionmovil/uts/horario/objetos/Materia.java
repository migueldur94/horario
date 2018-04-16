package gestionhorario.aplicacionmovil.uts.horario.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 10/09/2016.
 */
public class Materia implements Serializable {

    int serial=123456789;
    private int idMateria;
    private String nombreMateria;
    private int cantidadEstudiante;
    private Horario horario;
    private List<Horario> listaHorario = new ArrayList<>();

    public Materia() {
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public int getCantidadEstudiante() {
        return cantidadEstudiante;
    }

    public void setCantidadEstudiante(int cantidadEstudiante) {
        this.cantidadEstudiante = cantidadEstudiante;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public List<Horario> getListaHorario() {
        return listaHorario;
    }

    public void setListaHorario(List<Horario> listaHorario) {
        this.listaHorario = listaHorario;
    }

    @Override
    public String toString() {

        return nombreMateria + " ";
    }
}
