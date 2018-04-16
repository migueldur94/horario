package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.UsuarioMateriaHorario;

public class DetalleMateriaActivity extends AppCompatActivity {
    UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_materia);
        dao = new UsuarioDAO(this);

        int idMateria = getIntent().getIntExtra("idMateria", -1);
        int idHorario = getIntent().getIntExtra("idHorarioUbicacion", -1);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleMateriaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button agregar = (Button) findViewById(R.id.btnInscripcion);

        Calendar now = Calendar.getInstance();
        int horaConsulta = now.get(Calendar.HOUR_OF_DAY);
        int minutos = now.get(Calendar.MINUTE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date();
        final String fechaCadena = sdf.format(fecha);

        final UsuarioMateriaHorario umh = dao.getUsuarioMateriaHorario(idMateria, idHorario);

        if (dao.validacionInscrito(DataInfo.getCodigo(), umh.getIdUsuarioMateriaHorario(), fechaCadena, horaConsulta, minutos)) {
            agregar = (Button) findViewById(R.id.btnInscripcion);
            agregar.setText("Cancelar");
            agregar.setBackground(getResources().getDrawable(R.drawable.border_cancelar_inscripcion));
        } else {

            if (dao.cantidadInscritos(umh.getIdUsuarioMateriaHorario(), fechaCadena, horaConsulta, minutos) >= umh.getMateria().getCantidadEstudiante() || dao.validacionExisteInscripcionPorHorario(umh.getHorario().getIdHorario(), fechaCadena, horaConsulta, minutos) > 0) {
                agregar = (Button) findViewById(R.id.btnInscripcion);
                agregar.setVisibility(View.GONE);
            }
        }
        TextView nombreDocente = (TextView) findViewById(R.id.nombreDocente);
        TextView correoDocente = (TextView) findViewById(R.id.correoDocente);
        TextView nombreMateria = (TextView) findViewById(R.id.nombreMateria);
        TextView diaSemana = (TextView) findViewById(R.id.diaSemana);
        TextView hora = (TextView) findViewById(R.id.hora);
        TextView horaAlterna = (TextView) findViewById(R.id.horaAlterna);
        TextView ubicacion = (TextView) findViewById(R.id.ubicacion);

        nombreDocente.setText(umh.getUsuarios().getNombre() + " " + umh.getUsuarios().getApellido());
        correoDocente.setText(umh.getUsuarios().getCorreo());
        nombreMateria.setText(umh.getMateria().getNombreMateria());
        diaSemana.setText(umh.getHorario().getDiaSemana());
        hora.setText("[" + umh.getHorario().getHoraInicio() + ":" + umh.getHorario().getMinutoInicio() + " Hasta " + umh.getHorario().getHoraFin() + ":" + umh.getHorario().getMinutoFin() + "]");
        horaAlterna.setText("");
        ubicacion.setText(umh.getUbicacion().getNombre());

        final Button inscripcion = (Button) findViewById(R.id.btnInscripcion);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inscripcion.getText().equals("Inscribir")) {
                    dao.addUsuariosMateriaHorario(DataInfo.getCodigo(), umh.getIdUsuarioMateriaHorario(), fechaCadena);
                } else {
                    dao.deleteUsuariosMateriaHorario(DataInfo.getCodigo(), umh.getIdUsuarioMateriaHorario());
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
