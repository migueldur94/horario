package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gestionhorario.aplicacionmovil.uts.horario.objetos.MateriaHorario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;

/**
 * Created by Miguel on 21/09/2016.
 */
public class MateriaDAO {
    private TutoriasDB dbHelper;
    private SQLiteDatabase db;

    public MateriaDAO(Context context) {
        dbHelper = new TutoriasDB(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addMateria(Materia materia) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", materia.getNombreMateria());
        valores.put("cantidadEstudiante", materia.getCantidadEstudiante());
        if (db.insert("materias", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateMateria(Materia materia) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", materia.getNombreMateria());
        valores.put("cantidadEstudiante", materia.getCantidadEstudiante());
        if (db.update("materias", valores, "idMateria=" + materia.getIdMateria(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteMateria(Materia materia) {
        if (db.delete("materias", "idMateria=" + materia.getIdMateria(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Materia> getAllMaterias() {
        ArrayList<Materia> lista = new ArrayList<>();
        Cursor cursor = db.query("materias", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Materia m = new Materia();
                m.setIdMateria(cursor.getInt(cursor.getColumnIndex("idMateria")));
                m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombre")));
                m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                lista.add(m);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public Materia getMateriaHorario(int idMateriaParam) {
        Cursor cursor = db.query("materias m \n" +
                        "\tinner join materias_horarios mh on m.idMateria=mh.idMateria\n" +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion" +
                        "\tinner join horarios h on h.idHorario=hu.idHorario " +
                        "\tinner join ubicacion u on u.idUbicacion=hu.idUbicacion"
                , new String[]{
                        "m.idMateria\n",
                        "\tm.nombre as nombreMateria\n",
                        "\tm.cantidadEstudiante \n",
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin\n",
                        "\tu.idUbicacion\n",
                        "\tu.nombre AS nombreUbicacion\n",
                        "\thu.idHorarioUbicacion\n"}, "m.idMateria=" + idMateriaParam, null, null, null, null);
        Materia m = null;
        Map<Integer, Materia> mapa = new HashMap<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Integer idMateria = cursor.getInt(cursor.getColumnIndex("idMateria"));
                if (mapa.get(idMateria) == null) {
                    m = new Materia();
                    m.setIdMateria(idMateria);
                    m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                    m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                    mapa.put(idMateria, m);
                }
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
                ubicacion.setNombre(cursor.getString(cursor.getColumnIndex("nombreUbicacion")));
                horario.setUbicacion(ubicacion);
                mapa.get(idMateria).getListaHorario().add(horario);
            } while (cursor.moveToNext());
            for (Materia itObjeto : mapa.values()) {
                m = itObjeto;
            }
        }
        return m;
    }

    public boolean addUsuarioMateriaHorario(String idUsuario, int idMateria) {
        ContentValues valores = new ContentValues();
        valores.put("idUsuario", idUsuario);
        valores.put("idMateriaHorario", idMateria);
        if (db.insert("usuarios_materias_horarios", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUsuarioMateriaHorario(String idUsuario) {
        if (db.delete("usuarios_materias_horarios", "idUsuario='" + idUsuario + "'", null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<MateriaHorario> getAllMateriasHorarios(String codigo) {
        ArrayList<MateriaHorario> lista = new ArrayList<>();
        Cursor cursor = db.query("materias m \n" +
                        "\tinner join materias_horarios mh on m.idMateria=mh.idMateria\n " +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                        "\tinner join horarios h on h.idHorario=hu.idHorario " +
                        "\tinner join ubicacion u on u.idUbicacion=hu.idUbicacion " +
                        "\t left join usuarios_materias_horarios umh on umh.idMateriaHorario=mh.idMateriaHorario"
                , new String[]{
                        "m.idMateria\n",
                        "\tm.nombre as nombreMateria\n",
                        "\tm.cantidadEstudiante \n",
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin\n",
                        "\tu.idUbicacion\n",
                        "\tu.nombre AS nombreUbicacion\n",
                        "\thu.idHorarioUbicacion\n",
                        "\tmh.idMateriaHorario\n"}, "umh.idMateriaHorario IS NULL or umh.idUsuario='" + codigo + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MateriaHorario materiaHorario = new MateriaHorario();
                materiaHorario.setIdMateriaHorario(cursor.getInt(cursor.getColumnIndex("idMateriaHorario")));
                Materia m = new Materia();
                m.setIdMateria(cursor.getInt(cursor.getColumnIndex("idMateria")));
                m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                materiaHorario.setMateria(m);
                Horario h = new Horario();
                h.setIdHorarioUbicacion(cursor.getInt(cursor.getColumnIndex("idHorarioUbicacion")));
                h.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                h.setDiaSemana(cursor.getString(cursor.getColumnIndex("diaSemana")));
                h.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                h.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                h.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                h.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                ubicacion.setNombre(cursor.getString(cursor.getColumnIndex("nombreUbicacion")));
                h.setUbicacion(ubicacion);
                materiaHorario.setHorario(h);
                lista.add(materiaHorario);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public List<Materia> getMateriasPorUsuario(String codigo, int idRol) {
        Cursor cursor = db.query("  usuarios_materias_horarios umh " +
                        " inner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                        "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                        "\tinner join horarios h on hu.idHorario=h.idHorario"
                , new String[]{
                        "m.idMateria\n",
                        "\tm.nombre as nombreMateria\n",
                        "\tm.cantidadEstudiante \n",
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin"}, "umh.idUsuario='" + codigo + "'", null, null, null, null);
        List<Materia> lista = new ArrayList<>();
        Materia m = null;
        Map<Integer, Materia> mapa = new HashMap<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Integer idMateria = cursor.getInt(cursor.getColumnIndex("idMateria"));
                if (idMateria != 0 && mapa.get(idMateria) == null) {
                    m = new Materia();
                    m.setIdMateria(idMateria);
                    m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                    m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                    mapa.put(idMateria, m);
                }
                String semana = cursor.getString(cursor.getColumnIndex("diaSemana"));
                if (semana != null && !semana.equals("")) {
                    Horario horario = new Horario();
                    horario.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                    horario.setDiaSemana(semana);
                    horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                    horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                    horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                    horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                    mapa.get(idMateria).getListaHorario().add(horario);
                }
            } while (cursor.moveToNext());
            for (Materia itObjeto : mapa.values()) {
                lista.add(itObjeto);
            }
        }
        return lista;
    }

    public List<Materia> getMateriasHorariosConDocente() {
        Cursor cursor = db.query("  usuarios_materias_horarios umh " +
                        "\tinner join materias_horarios mh on umh.idMateriaHorario=mh.idMateriaHorario \n" +
                        "\tinner join materias m on m.idMateria=mh.idMateria\n" +
                        "\tinner join horarios_ubicacion hu on hu.idHorarioUbicacion=mh.idHorarioUbicacion " +
                        "\tinner join horarios h on hu.idHorario=h.idHorario " +
                        "\tinner join ubicacion u on u.idUbicacion=hu.idUbicacion " , new String[]{
                        "m.idMateria\n",
                        "\tm.nombre as nombreMateria\n",
                        "\tm.cantidadEstudiante \n",
                        "\th.idHorario\n",
                        "\th.diaSemana\n",
                        "\th.horaInicio\n",
                        "\th.minutoInicio\n",
                        "\th.horaFin\n",
                        "\th.minutoFin\n",
                        "\tu.idUbicacion\n",
                        "\tu.nombre AS nombreUbicacion\n",
                        "\thu.idHorarioUbicacion\n"}, null, null, null, null, null);
        List<Materia> lista = new ArrayList<>();
        Materia m = null;
        Map<Integer, Materia> mapa = new HashMap<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Integer idMateria = cursor.getInt(cursor.getColumnIndex("idMateria"));
                if (idMateria != 0 && mapa.get(idMateria) == null) {
                    m = new Materia();
                    m.setIdMateria(idMateria);
                    m.setNombreMateria(cursor.getString(cursor.getColumnIndex("nombreMateria")));
                    m.setCantidadEstudiante(cursor.getInt(cursor.getColumnIndex("cantidadEstudiante")));
                    mapa.put(idMateria, m);
                }
                String semana = cursor.getString(cursor.getColumnIndex("diaSemana"));
                if (semana != null && !semana.equals("")) {
                    Horario horario = new Horario();
                    horario.setIdHorarioUbicacion(cursor.getInt(cursor.getColumnIndex("idHorarioUbicacion")));
                    horario.setIdHorario(cursor.getInt(cursor.getColumnIndex("idHorario")));
                    horario.setDiaSemana(semana);
                    horario.setHoraInicio(cursor.getInt(cursor.getColumnIndex("horaInicio")));
                    horario.setMinutoInicio(cursor.getInt(cursor.getColumnIndex("minutoInicio")));
                    horario.setHoraFin(cursor.getInt(cursor.getColumnIndex("horaFin")));
                    horario.setMinutoFin(cursor.getInt(cursor.getColumnIndex("minutoFin")));
                    Ubicacion ubicacion = new Ubicacion();
                    ubicacion.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                    ubicacion.setNombre(cursor.getString(cursor.getColumnIndex("nombreUbicacion")));
                    horario.setUbicacion(ubicacion);
                    mapa.get(idMateria).getListaHorario().add(horario);
                }
            } while (cursor.moveToNext());
            for (Materia itObjeto : mapa.values()) {
                lista.add(itObjeto);
            }
        }
        return lista;
    }
}

