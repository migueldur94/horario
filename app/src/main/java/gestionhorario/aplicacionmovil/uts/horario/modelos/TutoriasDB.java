package gestionhorario.aplicacionmovil.uts.horario.modelos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Miguel on 10/09/2016.
 */
public class TutoriasDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "tutorias.db";
    public static final int version = 2;

    public TutoriasDB(Context context) {
        super(context, DB_NAME, null, version);
    }

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE \"usuarios\" (\"codigo\" varchar(50) PRIMARY KEY  NOT NULL, " +
                "\"nombre\" varchar(200) NOT NULL ,\"apellido\" varchar(200) NOT NULL ," +
                "\"password\" varchar(200) NOT NULL , \"activo\" boolean NOT NULL , " +
                "\"correo\" varchar(100) NOT NULL  UNIQUE ) ";
        db.execSQL(sql);

        String sql2 = "CREATE TABLE \"roles\" (\"idRol\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"nombre\" varchar(100))";
        db.execSQL(sql2);

        String sql3 = "CREATE  TABLE \"materias\" (\"idMateria\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, " +
                "\"nombre\" varchar(100) NOT NULL , \"cantidadEstudiante\" INTEGER NOT NULL );";
        db.execSQL(sql3);

        String sql4 = " CREATE  TABLE \"horarios\" (\"idHorario\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"diaSemana\" varchar(20) NOT NULL , \"horaInicio\" INTEGER NOT NULL , \"minutoInicio\" INTEGER NOT NULL , " +
                "\"horaFin\" INTEGER NOT NULL , \"minutoFin\" INTEGER NOT NULL )";
        db.execSQL(sql4);

        String sql5 = "CREATE TABLE \"usuarios_materias_horarios\" (\"idUsuarioMateria\" INTEGER PRIMARY KEY  NOT NULL " +
                ",\"idMateriaHorario\" INTEGER NOT NULL,\"idUsuario\"  varchar(50) NOT NULL ," +
                "FOREIGN KEY (\"idMateriaHorario\") REFERENCES \"materias_horarios\" (\"idMateriaHorario\")," +
                "FOREIGN KEY (\"idUsuario\") REFERENCES \"usuarios\" (\"codigo\"))";
        db.execSQL(sql5);

        String sql6 = "CREATE TABLE \"materias_horarios\" (\"idMateriaHorario\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ," +
                " \"idMateria\" INTEGER NOT NULL , \"idHorarioUbicacion\" INTEGER NOT NULL ," +
                "FOREIGN KEY (\"idMateria\") REFERENCES \"materias\" (\"idMateria\"), " +
                "FOREIGN KEY (\"idHorarioUbicacion\") REFERENCES \"horarios_ubicacion\" (\"idHorarioUbicacion\"))";
        db.execSQL(sql6);

        String sql7 = "CREATE TABLE \"usuarios_roles\" (\"idUsuarioRol\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"idUsuario\" varchar(50) NOT NULL , \"idRol\" INTEGER NOT NULL ," +
                "FOREIGN KEY (\"idUsuario\") REFERENCES \"usuarios\" (\"codigo\"), " +
                "FOREIGN KEY (\"idRol\") REFERENCES \"roles\" (\"idRol\"))";
        db.execSQL(sql7);

        String sql8 = "CREATE TABLE \"variables\" (\"idVariable\" INTEGER PRIMARY KEY NOT NULL , " +
                "\"nombre\" varchar(50) NOT NULL ," +
                "\"descripcion\" varchar(250) NOT NULL ," +
                "\"valor\" varchar(50) NOT NULL )";
        db.execSQL(sql8);

        String sql9 = "CREATE TABLE \"usuarios_usuarios_materias_horarios\" (" +
                "\"idEstudiante\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"idUsuarioMateria\" INTEGER NOT NULL ," +
                "\"idUsuario\" varchar(50) NOT NULL ,"+
                "\"fecha\" date NOT NULL ,"+
                "FOREIGN KEY (\"idUsuario\") REFERENCES \"usuarios\" (\"codigo\"), " +
                "FOREIGN KEY (\"idUsuarioMateria\") REFERENCES \"usuarios_materias_horarios\" (\"idUsuarioMateria\"))";
        db.execSQL(sql9);

        String sql10 = "CREATE TABLE \"horarios_ubicacion\" (" +
                "\"idHorarioUbicacion\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"idHorario\" INTEGER NOT NULL , " +
                "\"idUbicacion\" INTEGER NOT NULL , " +
                "FOREIGN KEY (\"idHorario\") REFERENCES \"horarios\" (\"idHorario\"), " +
                "FOREIGN KEY (\"idUbicacion\") REFERENCES \"ubicacion\" (\"idUbicacion\"))";
        db.execSQL(sql10);

        String sql11 = "CREATE TABLE \"ubicacion\" (" +
                "\"idUbicacion\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"nombre\" varchar(100) NOT NULL)";
        db.execSQL(sql11);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE usuarios";
        db.execSQL(sql);

        String sql2 = "DROP TABLE roles";
        db.execSQL(sql2);

        String sql3 = "DROP TABLE materias";
        db.execSQL(sql3);

        String sql4 = "DROP TABLE horarios";
        db.execSQL(sql4);

        String sql5 = "DROP TABLE usuarios_materias_horarios";
        db.execSQL(sql5);

        String sql6 = "DROP TABLE materias_horarios";
        db.execSQL(sql6);

        String sql7 = "DROP TABLE usuarios_roles";
        db.execSQL(sql7);

        String sql8 = "DROP TABLE variables";
        db.execSQL(sql8);

        String sql9 = "DROP TABLE usuarios_usuarios_materias_horarios";
        db.execSQL(sql9);

        String sql10 = "DROP TABLE horarios_ubicacion";
        db.execSQL(sql10);

        String sql11 = "DROP TABLE ubicacion";
        db.execSQL(sql11);

        onCreate(db);

    }
}
