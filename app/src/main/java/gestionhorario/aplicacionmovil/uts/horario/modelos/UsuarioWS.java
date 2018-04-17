package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.objetos.MateriaHorario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;
import gestionhorario.aplicacionmovil.uts.horario.objetos.UsuarioMateriaHorario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Variable;

/**
 * Created by Miguel on 21/09/2016.
 */
public class UsuarioWS {
    private TutoriasDB dbHelper;
    private SQLiteDatabase db;

    public UsuarioWS(Context context) {
        dbHelper = new TutoriasDB(context);
        db = dbHelper.getWritableDatabase();
    }


    public boolean addUsuarios(Usuarios usuario) {
        ContentValues valores = new ContentValues();
        valores.put("codigo", usuario.getCodigo());
        valores.put("nombre", usuario.getNombre());
        valores.put("apellido", usuario.getApellido());
        valores.put("password", usuario.getPassword());
        valores.put("activo", usuario.isActivo());
        valores.put("correo", usuario.getCorreo());
        if (db.insert("usuarios", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean addUsuariosMateriaHorario(String codigo, int idUsuarioMateriaHorario, String fechaCadena) {
        ContentValues valores = new ContentValues();
        valores.put("idUsuario", codigo);
        valores.put("idUsuarioMateria", idUsuarioMateriaHorario);
        valores.put("fecha", fechaCadena);
        if (db.insert("usuarios_usuarios_materias_horarios", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUsuariosMateriaHorario(String codigo, int id) {
        if (db.delete("usuarios_usuarios_materias_horarios", "idUsuario='" + codigo + "' and idUsuarioMateria=" + id, null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUsuarios(Usuarios usuario) {
        ContentValues valores = new ContentValues();
        valores.put("codigo", usuario.getCodigo());
        valores.put("nombre", usuario.getNombre());
        valores.put("apellido", usuario.getApellido());
        valores.put("password", usuario.getPassword());
        valores.put("activo", usuario.isActivo());
        valores.put("correo", usuario.getCorreo());
        if (db.update("usuarios", valores, "codigo='" + usuario.getCodigo() + "'", null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUsuarios(Usuarios usuario) {
        if (db.delete("usuarios", "codigo='" + usuario.getCodigo() + "'", null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    /* public boolean ajustarproducto(Producto producto) {
         ContentValues valores = new ContentValues();
         valores.put("cantidad", producto.get_cantidad());
         if (db.update("productos", valores, "_id=" + producto.getId(), null) != -1) {
             return true;
         } else {
             return false;
         }
     }
*/

    public ArrayList<Usuarios> getAllUsuarios() {
        ArrayList<Usuarios> lista = new ArrayList<>();
        Cursor cursor = db.query("usuarios", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Usuarios u = new Usuarios();
                u.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
                u.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                u.setApellido(cursor.getString(cursor.getColumnIndex("apellido")));
                u.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                u.setActivo(cursor.getInt(cursor.getColumnIndex("activo")) == 1 ? true : false);
                u.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                lista.add(u);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public int cantidadUsuarios() {
        int cantidad = -1;
        Cursor cursor = db.query("usuarios", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cantidad = cursor.getCount();
        }
        return cantidad;
    }

    public Usuarios getUsuario(String codigo, String password) {
        Cursor cursor = db.query("usuarios u inner join usuarios_roles ur on u.codigo\n" +
                "= ur.idUsuario inner join roles r on ur.idRol = r.idRol ", new String[]{
                "u.codigo ", "u.password ", "u.nombre as nombreUsuario", "u.apellido", "u.activo", "u.correo",
                "r.idRol", "r.nombre as nombreRol"}, "u.codigo='" + codigo.trim() + "' and u.password='" + password + "'", null, null, null, null);
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
                u = itObjeto;
            }
        }
        return u;
    }

    public Usuarios getUsuarioRol(String codigo) {
        Cursor cursor = db.query("usuarios u left join usuarios_roles ur on u.codigo\n" +
                "= ur.idUsuario left join roles r on ur.idRol = r.idRol ", new String[]{
                "u.codigo ", "u.password ", "u.nombre as nombreUsuario", "u.apellido", "u.activo", "u.correo",
                "r.idRol", "r.nombre as nombreRol"}, "u.codigo='" + codigo + "'", null, null, null, null);
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
                u = itObjeto;
            }
        }
        return u;
    }

    public List<Usuarios> getAllUsuarioRoles() {
        List<Usuarios> lista = new ArrayList<>();
        Cursor cursor = db.query("usuarios u left join usuarios_roles ur on u.codigo\n" +
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

    public int validacionToken(String codigo, String token) {
        int cantidad = -1;
        Cursor cursor = db.query("usuarios", new String[]{"count(codigo) AS cantidad"}, "codigo='" + codigo + "' and token='" + token + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cantidad = cursor.getCount();
        }
        return cantidad;
    }

    public boolean updateToken(String codigo, String token) {
        ContentValues valores = new ContentValues();
        valores.put("token", token);
        if (db.update("usuarios", valores, "codigo='" + codigo + "'", null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Usuarios> getAllUsuariosDocentes() {
        ArrayList<Usuarios> lista = new ArrayList<>();
        Cursor cursor = db.query("usuarios u inner join usuarios_roles ur on u.codigo=ur.idUsuario", null, "idRol=(SELECT valor FROM variables WHERE idVariable=3)", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Usuarios u = new Usuarios();
                u.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
                u.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                u.setApellido(cursor.getString(cursor.getColumnIndex("apellido")));
                u.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                u.setActivo(cursor.getInt(cursor.getColumnIndex("activo")) == 1 ? true : false);
                u.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                lista.add(u);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public List<MateriaHorario> getUsuarioMaterias(String codigo) {
        List<MateriaHorario> lista = new ArrayList<>();
        Cursor cursor = db.query("materias_horarios mh left join usuarios_materias_horarios um on mh.idMateriaHorario = um.idMateriaHorario"
                , null, "um.idUsuario='" + codigo + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MateriaHorario materiaHorario = new MateriaHorario();
                materiaHorario.setIdMateriaHorario(cursor.getInt(cursor.getColumnIndex("idMateriaHorario")));
                lista.add(materiaHorario);
            } while (cursor.moveToNext());

        }
        return lista;
    }


    public UsuarioMateriaHorario getUsuarioMateriaHorario(int idMateria, int idHorario) {
        Cursor cursor = db.query("  usuarios_materias_horarios umh " +
                        " inner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                        "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                        "\tinner join horarios h on hu.idHorario=h.idHorario " +
                        "\tinner join usuarios u on umh.idUsuario = u.codigo" +
                        "\tinner join ubicacion ub on hu.idUbicacion = ub.idUbicacion",
                new String[]{
                        "\tumh.idUsuarioMateria \n",
                        "\tm.idMateria\n",
                        "\tm.nombre as nombreMateria\n",
                        "\tm.cantidadEstudiante \n",
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin\n",
                        "\tu.codigo \n",
                        "\tu.password \n",
                        "\tu.nombre as nombreUsuario\n",
                        "\tu.apellido\n",
                        "\tu.activo\n",
                        "\tu.correo",
                        "\tub.idUbicacion\n",
                        "\tub.nombre as nombreUbicacion\n"}, " m.idMateria=" + idMateria + " and hu.idHorarioUbicacion=" + idHorario, null, null, null, null);
        UsuarioMateriaHorario umh = new UsuarioMateriaHorario();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                umh.setIdUsuarioMateriaHorario(cursor.getInt(cursor.getColumnIndex("idUsuarioMateria")));
                Usuarios u = new Usuarios();
                u.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
                u.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                u.setNombre(cursor.getString(cursor.getColumnIndex("nombreUsuario")));
                u.setApellido(cursor.getString(cursor.getColumnIndex("apellido")));
                u.setActivo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("activo"))));
                u.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                Materia m = new Materia();
                m.setIdMateria(cursor.getInt(cursor.getColumnIndex("idMateria")));
                m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                Horario h = new Horario();
                h.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                h.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                h.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                h.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                h.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                h.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                Ubicacion ub = new Ubicacion();
                ub.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                ub.setNombre(cursor.getString(cursor.getColumnIndex("nombreUbicacion")));
                umh.setUsuarios(u);
                umh.setMateria(m);
                umh.setHorario(h);
                umh.setUbicacion(ub);
            } while (cursor.moveToNext());
        }
        return umh;
    }

    public boolean validacionInscrito(String codigo, int id, String fecha, int hora, int minuto) {
        Cursor cursor = db.query("  usuarios_usuarios_materias_horarios uumh " +
                "\tinner join usuarios_materias_horarios umh on umh.idUsuarioMateria=uumh.idUsuarioMateria\n" +
                "\tinner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion\n" +
                "\tinner join horarios h on hu.idHorario=h.idHorario", new String[]{"count(1) cantidad"}
                , " uumh.idUsuario='" + codigo + "' and uumh.idUsuarioMateria=" + id + " and uumh.fecha >= '" + fecha + "' and h.horaInicio >=" + hora + " and h.minutoInicio >= " + minuto, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")) > 0 ? true : false);
            } while (cursor.moveToNext());
        }

        return false;
    }

    public int cantidadInscritos(int id, String fecha, int hora, int minuto) {
        Cursor cursor = db.query("  usuarios_usuarios_materias_horarios uumh" +
                "\tinner join usuarios_materias_horarios umh on umh.idUsuarioMateria=uumh.idUsuarioMateria\n" +
                "\tinner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion\n" +
                "\tinner join horarios h on hu.idHorario=h.idHorario", new String[]{
                "count(1) cantidad"}, " uumh.idUsuarioMateria=" + id + " and uumh.fecha >= '" + fecha + "' and h.horaInicio >=" + hora + " and h.minutoInicio >= " + minuto, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")));
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public int validarExisteUsuario(String codigo) {
        Cursor cursor = db.query("  usuarios ", new String[]{
                "count(1) cantidad"}, " codigo='" + codigo + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")));
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public List<UsuarioMateriaHorario> getUsuarioMateriaHorarioAll(int idHorario, int idMateria, String diaSemana) {
        Cursor cursor = db.query("  usuarios_materias_horarios umh " +
                " inner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                "\tinner join horarios h on hu.idHorario=h.idHorario " +
                "\tinner join usuarios u on umh.idUsuario = u.codigo" +
                "\tinner join ubicacion ub on hu.idUbicacion = ub.idUbicacion", new String[]{
                "\tumh.idUsuarioMateria \n",
                "\tm.idMateria\n",
                "\tm.nombre as nombreMateria\n",
                "\tm.cantidadEstudiante \n",
                "\th.idHorario\n",
                "\th.diaSemana\n",
                "\th.horaInicio\n",
                "\th.minutoInicio\n",
                "\th.horaFin\n",
                "\th.minutoFin\n",
                "\tu.codigo \n",
                "\tu.password \n",
                "\tu.nombre as nombreUsuario\n",
                "\tu.apellido\n",
                "\tu.activo\n",
                "\tu.correo",
                "\tub.idUbicacion\n",
                "\tub.nombre as nombreUbicacion\n"}, " h.idHorario=" + idHorario + " and m.idMateria=" + idMateria + " and diaSemana='" + diaSemana + "'", null, null, null, null);
        List<UsuarioMateriaHorario> lista = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UsuarioMateriaHorario umh = new UsuarioMateriaHorario();
                umh.setIdUsuarioMateriaHorario(cursor.getInt(cursor.getColumnIndex("idUsuarioMateria")));
                Usuarios u = new Usuarios();
                u.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
                u.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                u.setNombre(cursor.getString(cursor.getColumnIndex("nombreUsuario")));
                u.setApellido(cursor.getString(cursor.getColumnIndex("apellido")));
                u.setActivo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("activo"))));
                u.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                Materia m = new Materia();
                m.setIdMateria(cursor.getInt(cursor.getColumnIndex("idMateria")));
                m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                Horario h = new Horario();
                h.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                h.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                h.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                h.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                h.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                h.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                Ubicacion ub = new Ubicacion();
                ub.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                ub.setNombre(cursor.getString(cursor.getColumnIndex("nombreUbicacion")));
                umh.setUsuarios(u);
                umh.setMateria(m);
                umh.setHorario(h);
                umh.setUbicacion(ub);
                lista.add(umh);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public int validacionExisteInscripcionPorHorario(int idHorario, String fecha, int hora, int minuto) {
        int cantidad = 0;
        Cursor cursor = db.query("  usuarios_usuarios_materias_horarios uumh" +
                " inner join usuarios_materias_horarios umh on umh.idUsuarioMateria=uumh.idUsuarioMateria" +
                " inner join materias_horarios mh on mh.idMateriaHorario=umh.idMateriaHorario" +
                " inner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                "\tinner join horarios h on hu.idHorario=h.idHorario ", null, "uumh.idUsuario='" + DataInfo.getCodigo() + "' and hu.idHorario=" + idHorario + " and uumh.fecha >= '" + fecha + "' and h.horaInicio >=" + hora + " and h.minutoInicio >= " + minuto, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cantidad = cursor.getCount();
        }
        return cantidad;
    }

    //--------------------------------------------VARIABLES-----------------------------------------
    public boolean deleteVariable(int idVariable) {
        if (db.delete("variables", "idVariable=" + idVariable, null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addVariables(Variable variable) {
        ContentValues valores = new ContentValues();
        valores.put("idVariable", variable.getIdVariable());
        valores.put("nombre", variable.getNombre());
        valores.put("descripcion", variable.getDescripcion());
        valores.put("valor", variable.getValor());
        if (db.insert("variables", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateVariable(Variable variable) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", variable.getNombre());
        valores.put("descripcion", variable.getDescripcion());
        valores.put("valor", variable.getValor());
        if (db.update("variables", valores, "idVariable=" + variable.getIdVariable(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Variable> getAllVariables(int idVariable) {
        String validacion = "";
        if (idVariable <= 0) {
            validacion = "";
        } else {
            validacion = " idVariable='" + idVariable + "'";
        }
        ArrayList<Variable> lista = new ArrayList<>();
        Cursor cursor = db.query("variables", null, validacion, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Variable v = new Variable();
                v.setIdVariable(cursor.getInt(cursor.getColumnIndex("idVariable")));
                v.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                v.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));
                v.setValor(cursor.getString(cursor.getColumnIndex("valor")));

                lista.add(v);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public boolean validacionExisteVariable(int idVariable) {
        Variable v = new Variable();
        Cursor cursor = db.query("variables", null, "idVariable=" + idVariable, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }


}

