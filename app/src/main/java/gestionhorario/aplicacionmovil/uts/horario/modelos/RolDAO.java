package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

/**
 * Created by Miguel on 21/09/2016.
 */
public class RolDAO {
    private TutoriasDB dbHelper;
    private SQLiteDatabase db;

    public RolDAO(Context context) {
        dbHelper = new TutoriasDB(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addRoles(Roles roles) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", roles.getNombre());
        if (db.insert("roles", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addRolesUsuario(String idUsuario, int idRol) {
        ContentValues valores = new ContentValues();
        valores.put("idUsuario", idUsuario);
        valores.put("idRol", idRol);
        if (db.insert("usuarios_roles", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteRolesUsuario(String idUsuario) {
        if (db.delete("usuarios_roles", "idUsuario = '" + idUsuario + "'", null) != -1) {
            return true;
        } else {
            return false;
        }
    }


    public List<Usuarios> getAllUsuarioRoles() {
        List<Usuarios> lista = new ArrayList<>();
        Cursor cursor = db.query("usuarios u inner join usuarios_roles ur on u.codigo\n" +
                "= ur.idUsuario left join roles r on ur.idRol = r.idRol ", new String[]{
                "u.codigo ", "u.password ", "u.nombre as nombreUsuario", "u.apellido", "u.activo", "u.correo",
                "r.idRol", "r.nombre as nombreRol"}, null, null, null, null, null);
        Usuarios u = null;
        Map<String, Usuarios> mapa = new HashMap<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String idUsuario = cursor.getString(cursor.getColumnIndex("codigo"));
                if (mapa.get(idUsuario) == null) {
                    u = new Usuarios();
                    u.setCodigo(idUsuario);
                    u.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                    u.setNombre(cursor.getString(cursor.getColumnIndex("nombreUsuario")));
                    u.setApellido(cursor.getString(cursor.getColumnIndex("apellido")));
                    u.setActivo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("activo"))));
                    u.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                    mapa.put(idUsuario, u);
                }
                Roles roles = new Roles();
                roles.setIdRol(cursor.getInt(cursor.getColumnIndex("idRol")));
                roles.setNombre(cursor.getString(cursor.getColumnIndex("nombreRol")));
                mapa.get(idUsuario).getListaRoles().add(roles);

            } while (cursor.moveToNext());
            for (Usuarios itObjeto : mapa.values()) {
                lista.add(itObjeto);
            }
        }
        return lista;
    }


    public boolean updateRoles(Roles roles) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", roles.getNombre());
        if (db.update("roles", valores, "idRol=" + roles.getIdRol(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteRoles(Roles roles) {
        if (db.delete("roles", "idRol=" + roles.getIdRol(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Roles> getAllRoles() {
        ArrayList<Roles> lista = new ArrayList<>();
        Cursor cursor = db.query("roles", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Roles m = new Roles();
                m.setIdRol(cursor.getInt(cursor.getColumnIndex("idRol")));
                m.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                lista.add(m);
            } while (cursor.moveToNext());
        }
        return lista;
    }

}

