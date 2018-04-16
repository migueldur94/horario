package gestionhorario.aplicacionmovil.uts.horario.objetos;

import java.util.Date;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

/**
 * Created by Miguel on 12/02/2017.
 */

public class UsuarioMateriaHorario {
    private int idUsuarioMateriaHorario;
    private Usuarios usuarios = new Usuarios();
    private Materia materia = new Materia();
    private Horario horario = new Horario();
    private Ubicacion ubicacion = new Ubicacion();
    private Date fecha;

    public UsuarioMateriaHorario() {
    }

    public int getIdUsuarioMateriaHorario() {
        return idUsuarioMateriaHorario;
    }

    public void setIdUsuarioMateriaHorario(int idUsuarioMateriaHorario) {
        this.idUsuarioMateriaHorario = idUsuarioMateriaHorario;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
