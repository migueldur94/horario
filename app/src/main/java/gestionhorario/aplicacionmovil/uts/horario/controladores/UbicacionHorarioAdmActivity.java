package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UbicacionDAO;

public class UbicacionHorarioAdmActivity extends AppCompatActivity {
    UbicacionDAO dao;
    Horario horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_horario_adm);
        dao = new UbicacionDAO(this);
        horario = (Horario) getIntent().getSerializableExtra("horario");

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UbicacionHorarioAdmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

        Button agregar = (Button) findViewById(R.id.btnAgregarUbicacion);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaUbicacion);
                for (int i = 0; i < lista.getCount(); i++) {
                    Ubicacion ubicacion = (Ubicacion) lista.getItemAtPosition(i);
                    if (lista.isItemChecked(i)) {
                        if (dao.validacionUbicacionHorario(ubicacion.getIdUbicacion(), horario.getIdHorario()) == false) {
                            dao.addUbicacionHorario(ubicacion.getIdUbicacion(), horario.getIdHorario());
                        }
                    } else {
                        dao.deleteUbicacionHorario(ubicacion.getIdUbicacion(), horario.getIdHorario());
                    }
                }
                finish();
                setResult(RESULT_OK);
            }
        });
    }


    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaUbicacion);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<Ubicacion> adapter = new ArrayAdapter<Ubicacion>(this,
                android.R.layout.simple_list_item_multiple_choice, dao.getAllUbicaciones());
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ubicacion ubicacion = (Ubicacion) lista.getAdapter().getItem(position);
                if (dao.validacionExisteHorarioUbicacion(horario.getDiaSemana(), horario.getHoraInicio(), horario.getHoraFin(), horario.getMinutoInicio(), horario.getMinutoFin(), ubicacion.getIdUbicacion())) {
                    Toast.makeText(UbicacionHorarioAdmActivity.this, "El " + ubicacion.getNombre() + ", de " + horario.getHoraInicio() + " hasta " + horario.getHoraFin() + " ya tiene un horario", Toast.LENGTH_LONG).show();
                    lista.setItemChecked(position, false);
                }
            }
        });
        lista.setTextFilterEnabled(true);
        final EditText filtro = (EditText) findViewById(R.id.filtro);
        filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        for (int i = 0; i < horario.getListaUbicacion().size(); i++) {
            for (int j = 0; j < lista.getCount(); j++) {
                Ubicacion h = (Ubicacion) lista.getItemAtPosition(j);
                if (horario.getListaUbicacion().get(i).getIdUbicacion() == h.getIdUbicacion()) {
                    lista.setItemChecked(j, true);
                    break;
                }
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refrescar();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
