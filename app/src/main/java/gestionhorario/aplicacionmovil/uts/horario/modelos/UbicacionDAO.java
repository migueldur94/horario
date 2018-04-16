package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;

/**
 * Created by Miguel on 21/09/2016.
 */
public class UbicacionDAO {
    private TutoriasDB dbHelper;
    private SQLiteDatabase db;

    public UbicacionDAO(Context context) {
        dbHelper = new TutoriasDB(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addUbicacion(Ubicacion ubicacion) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", ubicacion.getNombre());
        if (db.insert("ubicacion", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUbicacion(int idUbicacion) {
        if (db.delete("ubicacion", "idUbicacion = " + idUbicacion + "", null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUbicacion(Ubicacion ubicacion) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", ubicacion.getNombre());
        if (db.update("ubicacion", valores, "idUbicacion=" + ubicacion.getIdUbicacion(), null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Ubicacion> getAllUbicaciones() {
        ArrayList<Ubicacion> lista = new ArrayList<>();
        Cursor cursor = db.query("ubicacion", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Ubicacion u = new Ubicacion();
                u.setIdUbicacion(cursor.getInt(cursor.getColumnIndex("idUbicacion")));
                u.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                lista.add(u);
            } while (cursor.moveToNext());
        }
        return lista;
    }


    public boolean addHorarioUbicacion(Horario horario, Ubicacion ubicacion) {
        ContentValues valores = new ContentValues();
        valores.put("idHorario", horario.getIdHorario());
        valores.put("idUbicacion", ubicacion.getIdUbicacion());
        if (db.insert("horarios_ubicacion", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteHorarioUbicacion(int idHorario) {
        if (db.delete("horarios_ubicacion", "idHorario = " + idHorario, null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addUbicacionHorario(int idUbicacion, int idHorario) {
        ContentValues valores = new ContentValues();
        valores.put("idUbicacion", idUbicacion);
        valores.put("idHorario", idHorario);
        if (db.insert("horarios_ubicacion", null, valores) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validacionUbicacionHorario(int idUbicacion, int idHorario) {
        ArrayList<Horario> lista = new ArrayList<>();
        Cursor cursor = db.query("horarios_ubicacion", new String[]{"count(1) AS cantidad"}, "idUbicacion=" + idUbicacion + " and idHorario=" + idHorario, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")) >= 1 ? true : false);
            } while (cursor.moveToNext());
        }
        return false;
    }

    public boolean deleteUbicacionHorario(int idUbicacion, int idHorario) {
        if (db.delete("horarios_ubicacion", "idUbicacion=" + idUbicacion + " and idHorario=" + idHorario, null) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validacionExisteHorarioUbicacion(String diaSemana, int horaInicio, int horaFin, int minutoInicio, int minutoFin, int idUbicacion) {
        ArrayList<Horario> lista = new ArrayList<>();
        Cursor cursor = db.query("horarios_ubicacion hu inner join horarios h on hu.idHorario = h.idHorario",
                new String[]{"count(1) AS cantidad"},
                "diaSemana= '" + diaSemana + "' "
                        + " and ((horaInicio between " + horaInicio + " and " + horaFin
                        + " and minutoInicio between " + minutoInicio + " and " + minutoFin
                        + ") or (horaFin between " + horaInicio + " and " + horaFin
                        + " or minutoFin between " + minutoInicio + " and " + minutoFin
                        + ")) and idUbicacion=" + idUbicacion, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return (cursor.getInt(cursor.getColumnIndex("cantidad")) >= 1 ? true : false);
            } while (cursor.moveToNext());
        }
        return false;
    }

}

