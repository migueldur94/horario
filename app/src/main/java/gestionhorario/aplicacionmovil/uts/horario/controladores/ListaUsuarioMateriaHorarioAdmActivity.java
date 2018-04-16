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

import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.MateriaHorario;
import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;

public class ListaUsuarioMateriaHorarioAdmActivity extends AppCompatActivity {

    MateriaDAO dao;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario_materia_horario_adm);
        Intent intent = getIntent();
        idUsuario = getIntent().getStringExtra("idUsuario");

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaUsuarioMateriaHorarioAdmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dao = new MateriaDAO(this);
        refrescar();


        Button agregar = (Button) findViewById(R.id.btnAgregarUsuarioMateriaHorario);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaUsuarioMateriaHorario);
                dao.deleteUsuarioMateriaHorario(idUsuario);

                for (int i = 0; i < lista.getCount(); i++) {
                    if (lista.isItemChecked(i)) {
                        MateriaHorario materiaHorario = (MateriaHorario) lista.getItemAtPosition(i);
                        dao.addUsuarioMateriaHorario(idUsuario, materiaHorario.getIdMateriaHorario());
                    }
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public boolean validacionSeleccion(ListView lista) {
        for (int i = 0; i < lista.getCount(); i++) {
            if (lista.isItemChecked(i)) {
                return true;
            }
        }
        return false;
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaUsuarioMateriaHorario);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<MateriaHorario> adapter = new ArrayAdapter<MateriaHorario>(this,
                android.R.layout.simple_list_item_multiple_choice,
                dao.getAllMateriasHorarios(idUsuario));
        lista.setAdapter(adapter);
        lista.setTextFilterEnabled(true);
        final EditText filtroUsuario = (EditText) findViewById(R.id.filtroMateria);
        filtroUsuario.addTextChangedListener(new TextWatcher() {
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
        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        List<MateriaHorario> listaMH = usuarioDAO.getUsuarioMaterias(idUsuario);
        for (int i = 0; i < listaMH.size(); i++) {
            for (int j = 0; j < lista.getCount(); j++) {
                MateriaHorario materiaHorario = (MateriaHorario) lista.getItemAtPosition(j);
                if (listaMH.get(i).getIdMateriaHorario() == materiaHorario.getIdMateriaHorario()) {
                    lista.setItemChecked(j, true);
                }
            }
        }
    }
}
