package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;

/**
 * Created by Miguel on 21/09/2016.
 */
public class HorarioDAO {
    private TutoriasDB dbHelper;
    private SQLiteDatabase db;

    public HorarioDAO(Context context) {
        dbHelper = new TutoriasDB(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addHorario(Horario horario) {
        ContentValues valores = new ContentValues();
        valores.put("diaSemana", horario.getDiaSemana());
        valores.put("horaInicio", horario.getHoraInicio());
        valores.put("minutoInicio", horario.getMinutoInicio());
        valores.put("horaFin", horario.getHoraFin());
        valores.put("minutoFin", horario.getMinutoFin());
        if (db.insert("horarios", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateHorario(Horario horario) {
        ContentValues valores = new ContentValues();
        valores.put("diaSemana", horario.getDiaSemana());
        valores.put("horaInicio", horario.getHoraInicio());
        valores.put("minutoInicio", horario.getMinutoInicio());
        valores.put("horaFin", horario.getHoraFin());
        valores.put("minutoFin", horario.getMinutoFin());
        if (db.update("horarios", valores, "idHorario=" + horario.getIdHorario(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteHorario(Horario horario) {
        if (db.delete("horarios", "idHorario=" + horario.getIdHorario(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteMateriaHorario(int idMateria, int idHorario) {
        if (db.delete("materias_horarios", "idMateria=" + idMateria + " and idHorarioUbicacion=" + idHorario, null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Horario> getAllHorarios() {
        ArrayList<Horario> lista = new ArrayList<>();
        Cursor cursor = db.query("horarios ", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Horario h = new Horario();
                h.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                h.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                h.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                h.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                h.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                h.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                lista.add(h);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public boolean addHorarioMateria(int idMateria, int idHorario) {
        ContentValues valores = new ContentValues();
        valores.put("idMateria", idMateria);
        valores.put("idHorarioUbicacion", idHorario);
        if (db.insert("materias_horarios", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean validacionMateriaHorario(int idMateria, int idHorario) {
        ArrayList<Horario> lista = new ArrayList<>();
        Cursor cursor = db.query("materias_horarios", new String[]{"count(1) AS cantidad"}, "idMateria=" + idMateria + " and idHorarioUbicacion=" + idHorario, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")) >= 1 ? true : false);
            } while (cursor.moveToNext());
        }
        return false;
    }

    public List<Horario> getHorariosPorEstudiante(String codigo, int idMateria) {
        Cursor cursor = db.query(" usuarios_usuarios_materias_horarios uumh " +
                        "inner join usuarios_materias_horarios umh on uumh.idUsuarioMateria=umh.idUsuarioMateria" +
                        " inner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario " +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                        "\tinner join horarios h on hu.idHorario=h.idHorario "
                , new String[]{
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin\n",
                }, "uumh.idUsuario='" + codigo + "' and mh.idMateria=" + idMateria, null, null, null, null);
        List<Horario> lista = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Horario horario = new Horario();
                horario.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                horario.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                lista.add(horario);

            } while (cursor.moveToNext());
        }
        return lista;
    }

    public List<Horario> getHorarioUbicacion() {
        Cursor cursor = db.query("horarios h \n" +
                "\tleft join horarios_ubicacion hu on h.idHorario=hu.idHorario\n" +
                "\tleft join ubicacion u on hu.idUbicacion=u.idUbicacion ", new String[]{
                "u.idUbicacion\n",
                "\tu.nombre\n",
                "\th.idHorario\n",
                "\th.diaSemana\n",
                "\th.horaInicio\n",
                "\th.minutoInicio\n",
                "\th.horaFin\n",
                "\th.minutoFin"}, null, null, null, null, null);
        List<Horario> listaHorario = new ArrayList<>();
        Map<Integer, Horario> mapa = new HashMap<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Integer idHorario = cursor.getInt(cursor.getColumnIndex("idHorario"));
                if (mapa.get(idHorario) == null) {
                    Horario horario = new Horario();
                    horario.setIdHorario(idHorario);
                    horario.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                    horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                    horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                    horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                    horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                    mapa.put(idHorario, horario);
                }
                if (cursor.getInt(cursor.getColumnIndex("idUbicacion")) > 0) {
                    Ubicacion ubicacion = new Ubicacion();
                    ubicacion.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                    ubicacion.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                    mapa.get(idHorario).getListaUbicacion().add(ubicacion);
                }

            } while (cursor.moveToNext());
            for (Horario itObjeto : mapa.values()) {
                listaHorario.add(itObjeto);
            }
        }
        return listaHorario;
    }

    public List<Horario> getHorarioRelacionUbicacion(int idMateria) {
        Cursor cursor = db.query("horarios h \n" +
                "\tinner join horarios_ubicacion hu on h.idHorario=hu.idHorario\n" +
                "\tinner join ubicacion u on hu.idUbicacion=u.idUbicacion " +
                "\tleft join materias_horarios mh on hu.idHorarioUbicacion = mh.idHorarioUbicacion", new String[]{
                "u.idUbicacion\n",
                "\tu.nombre\n",
                "\th.idHorario\n",
                "\th.diaSemana\n",
                "\th.horaInicio\n",
                "\th.minutoInicio\n",
                "\th.horaFin\n",
                "\th.minutoFin\n",
                "\thu.idHorarioUbicacion\n"}, "mh.idHorarioUbicacion IS NULL or mh.idMateria=" + idMateria, null, null, null, null);
        List<Horario> listaHorario = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Horario horario = new Horario();
                horario.setIdHorarioUbicacion(cursor.getInt(cursor.getColumnIndex("idHorarioUbicacion")));
                horario.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                horario.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                ubicacion.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                horario.setUbicacion(ubicacion);
                listaHorario.add(horario);
            } while (cursor.moveToNext());
        }
        return listaHorario;
    }

    public List<Horario> getHorarioRelacionUbicacionPDf(int idMateria) {
        Cursor cursor = db.query("horarios h \n" +
                "\tinner join horarios_ubicacion hu on h.idHorario=hu.idHorario\n" +
                "\tleft join materias_horarios mh on hu.idHorarioUbicacion = mh.idHorarioUbicacion", new String[]{
                " distinct h.idHorario\n",
                "\th.diaSemana\n",
                "\th.horaInicio\n",
                "\th.minutoInicio\n",
                "\th.horaFin\n",
                "\th.minutoFin\n"}, "mh.idHorarioUbicacion IS NULL or mh.idMateria=" + idMateria, null, null, null, null);
        List<Horario> listaHorario = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Horario horario = new Horario();
                horario.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                horario.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                listaHorario.add(horario);
            } while (cursor.moveToNext());
        }
        return listaHorario;
    }
}

