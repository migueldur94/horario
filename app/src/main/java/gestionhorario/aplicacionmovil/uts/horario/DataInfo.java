package gestionhorario.aplicacionmovil.uts.horario;

import java.util.ArrayList;
import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;

/**
 * Created by Red Dan on 16/04/2016.
 */
public class DataInfo {

    private static ArrayList<String> diasSemana = new ArrayList<>();
    private static List<Roles> listaRoles = new ArrayList<>();
    private static String codigo;
    private static int idRol;

    //Datos conexion web service
    public static String NAMESPACE = "http://tutoriaWS/";
    public static String URL = "http://192.168.1.39:8080/tutoriaUTS/tutoriaWS?wsdl";

    public static ArrayList<String> getDiasSemana() {
        if (diasSemana.size() == 0) {
            diasSemana.add("Domingo");
            diasSemana.add("Lunes");
            diasSemana.add("Martes");
            diasSemana.add("Miércoles");
            diasSemana.add("Jueves");
            diasSemana.add("Viernes");
            diasSemana.add("Sábado");
        }
        return diasSemana;
    }

    public static List<Roles> getListaRoles() {
        return listaRoles;
    }

    public static void setListaRoles(List<Roles> listaParam) {
        listaRoles = listaParam;
    }

    public static String getCodigo() {
        return codigo;
    }

    public static void setCodigo(String codigo) {
        DataInfo.codigo = codigo;
    }

    public static int getIdRol() {
        return idRol;
    }

    public static void setIdRol(int idRol) {
        DataInfo.idRol = idRol;
    }
}
