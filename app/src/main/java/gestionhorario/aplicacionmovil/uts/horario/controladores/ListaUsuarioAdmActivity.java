package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

public class ListaUsuarioAdmActivity extends AppCompatActivity {
    UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);
        dao = new UsuarioDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaUsuarioAdmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaUsuario);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Usuarios> adapter = new ArrayAdapter<Usuarios>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllUsuarioRoles());
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuarios usuarios = (Usuarios) lista.getAdapter().getItem(position);
                Intent intent = new Intent(ListaUsuarioAdmActivity.this, ListaRolAdmActivity.class);
                intent.putExtra("idUsuario", usuarios.getCodigo());
                startActivityForResult(intent, 001);
            }
        });
        lista.setTextFilterEnabled(true);
        final EditText filtroUsuario = (EditText) findViewById(R.id.filtroUsuarios);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 001) {
            if (resultCode == RESULT_OK) {
                refrescar();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
