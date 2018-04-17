package gestionhorario.aplicacionmovil.uts.horario;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

/**
 * Created by Miguel Duran on 15/04/2018.
 */
public class Procesos {

    public static List<Usuarios> getListaUsuarioSoap(List<SoapObject> listaSoapObject) {
        Usuarios usuarios;
        List<Usuarios> lista = new ArrayList<>();
        for (int i = 0; i < listaSoapObject.size(); i++) {
            SoapObject soapObject = listaSoapObject.get(i);
            usuarios = new Usuarios();
            usuarios.setNombre(soapObject.getProperty("nombre").toString());
            usuarios.setApellido(soapObject.getProperty("apellido").toString());
            usuarios.setCodigo(soapObject.getProperty("codigo").toString());
            usuarios.setCorreo(soapObject.getProperty("correo").toString());
            SoapObject soapObejectRol = (SoapObject) soapObject.getProperty("listaRoles");

            lista.add(usuarios);
        }
        return lista;
    }
}
