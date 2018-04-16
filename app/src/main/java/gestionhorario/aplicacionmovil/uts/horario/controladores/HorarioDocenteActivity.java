package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.modelos.HorarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.R;

public class HorarioDocenteActivity extends AppCompatActivity {
    private HorarioDAO dao;
    Materia materia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_horario);

        FloatingActionButton cerrar=(FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HorarioDocenteActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        dao = new HorarioDAO(this);
        materia = (Materia) getIntent().getSerializableExtra("m");
        refrescar();
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaHorario);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Horario> adapter = new ArrayAdapter<Horario>(this,
                android.R.layout.simple_list_item_single_choice, materia.getListaHorario());
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

    }


}
