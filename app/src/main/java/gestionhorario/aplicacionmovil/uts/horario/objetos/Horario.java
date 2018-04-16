package gestionhorario.aplicacionmovil.uts.horario.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 10/09/2016.
 */
public class Horario implements Serializable {

    private int idHorario;
    private int idHorarioUbicacion;
    private String diaSemana;
    private int horaInicio;
    private int minutoInicio;
    private int horaFin;
    private int minutoFin;
    private Ubicacion ubicacion;
    private List<Ubicacion> listaUbicacion = new ArrayList<>();

    public Horario() {
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(int minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMinutoFin() {
        return minutoFin;
    }

    public void setMinutoFin(int minutoFin) {
        this.minutoFin = minutoFin;
    }

    public List<Ubicacion> getListaUbicacion() {
        return listaUbicacion;
    }

    public void setListaUbicacion(List<Ubicacion> listaUbicacion) {
        this.listaUbicacion = listaUbicacion;
    }

    public int getIdHorarioUbicacion() {
        return idHorarioUbicacion;
    }

    public void setIdHorarioUbicacion(int idHorarioUbicacion) {
        this.idHorarioUbicacion = idHorarioUbicacion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        String complemento = "";
        if (ubicacion != null) {
            complemento = "-" + ubicacion.getNombre();
        }
        return getDiaSemana()
                + " [ " + getHoraInicio() + ":" + getMinutoInicio() + " -> " + getHoraFin() + ":" + getMinutoFin() + "]"
                + complemento;
    }
}
