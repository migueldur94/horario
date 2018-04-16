package gestionhorario.aplicacionmovil.uts.horario.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 21/09/2016.
 */
public class Usuarios implements Serializable {
    private String codigo;
    private String nombre;
    private String apellido;
    private String password;
    private boolean activo;
    private String correo;
    private List<Roles> listaRoles = new ArrayList<>();
    private List<MateriaHorario> listaMateria = new ArrayList<>();


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<Roles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Roles> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<MateriaHorario> getListaMateria() {
        return listaMateria;
    }

    public void setListaMateria(List<MateriaHorario> listaMateria) {
        this.listaMateria = listaMateria;
    }

    @Override
    public String toString() {
        String nombreRol = "";
        if (listaRoles.size() > 0) {
            nombreRol = "\nRol :";
            String nombreTmp = listaRoles.get(0).getNombre();
            if (nombreTmp == null) {
                nombreTmp = "";
            }
            nombreRol += nombreTmp;
            if (listaRoles.size() > 1) {
                nombreRol += "...";
            }
        }
        return "Nombres : " + nombre + " " + apellido + nombreRol;
    }
}
