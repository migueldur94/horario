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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.R;

public class ListaMateriasDocenteActivity extends AppCompatActivity {
    MateriaDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materias);
        dao = new MateriaDAO(this);
        refrescar();

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaMateriasDocenteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaMateria);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Materia> adapter = new ArrayAdapter<Materia>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getMateriasPorUsuario(DataInfo.getCodigo(), DataInfo.getIdRol()));
        lista.setAdapter(adapter);
        lista.setTextFilterEnabled(true);
        final EditText filtroMateria = (EditText) findViewById(R.id.filtroMateria);
        filtroMateria.addTextChangedListener(new TextWatcher() {
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
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Materia materia = (Materia) lista.getAdapter().getItem(position);
                Intent intent = new Intent(ListaMateriasDocenteActivity.this, HorarioDocenteActivity.class);
                intent.putExtra("m", materia);
                startActivityForResult(intent, 001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refrescar();

        super.onActivityResult(requestCode, resultCode, data);
    }
}
