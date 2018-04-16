package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.modelos.HorarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.R;

public class ListaHorarioAdmActivity extends AppCompatActivity {

    HorarioDAO dao;
    int idMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_horario_materia);

        Intent intent = getIntent();
        idMateria = getIntent().getIntExtra("idMateria", -1);

        FloatingActionButton cerrar=(FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ListaHorarioAdmActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        dao = new HorarioDAO(this);
        refrescar();

        Button agregar = (Button) findViewById(R.id.btnAgregarMateriaHorario);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaHorarioRelacion);
                for (int i = 0; i < lista.getCount(); i++) {
                    Horario horario = (Horario) lista.getItemAtPosition(i);
                    if (lista.isItemChecked(i)) {
                        if (dao.validacionMateriaHorario(idMateria, horario.getIdHorarioUbicacion()) == false) {
                            dao.addHorarioMateria(idMateria, horario.getIdHorarioUbicacion());
                        }
                    } else {
                        dao.deleteMateriaHorario(idMateria, horario.getIdHorarioUbicacion());
                    }
                }
                finish();
                setResult(RESULT_OK);
            }
        });
    }


    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaHorarioRelacion);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<Horario> adapter = new ArrayAdapter<Horario>(this,
                android.R.layout.simple_list_item_multiple_choice,
                dao.getHorarioRelacionUbicacion(idMateria));
        lista.setAdapter(adapter);
        lista.setTextFilterEnabled(true);
        final EditText filtroHorario = (EditText) findViewById(R.id.filtroHorario);
        filtroHorario.addTextChangedListener(new TextWatcher() {
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
        MateriaDAO materiaDAO = new MateriaDAO(this);
        Materia materia = materiaDAO.getMateriaHorario(idMateria);
        if (materia != null) {
            for (int i = 0; i < materia.getListaHorario().size(); i++) {
                for (int j = 0; j < lista.getCount(); j++) {
                    Horario horario = (Horario) lista.getItemAtPosition(j);
                    if (materia.getListaHorario().get(i).getIdHorarioUbicacion() == horario.getIdHorarioUbicacion()) {
                        lista.setItemChecked(j, true);
                    }
                }
            }
        }

    }

}

