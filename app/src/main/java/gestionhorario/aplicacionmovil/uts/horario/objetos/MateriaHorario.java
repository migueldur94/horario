package gestionhorario.aplicacionmovil.uts.horario.objetos;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;

/**
 * Created by Miguel on 5/02/2017.
 */

public class MateriaHorario {
    private int idMateriaHorario;
    private Materia materia;
    private Horario horario;

    public MateriaHorario() {
    }

    public int getIdMateriaHorario() {
        return idMateriaHorario;
    }

    public void setIdMateriaHorario(int idMateriaHorario) {
        this.idMateriaHorario = idMateriaHorario;
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

    @Override
    public String toString() {
        String nombreRetorno = "";
        if (materia != null) {
            nombreRetorno = materia.getNombreMateria();
        }
        if (horario != null) {
            nombreRetorno = nombreRetorno + " \n" + horario.toString();
        }
        return nombreRetorno;
    }
}
