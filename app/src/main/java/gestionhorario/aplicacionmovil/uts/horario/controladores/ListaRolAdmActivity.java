package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.RolDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

public class ListaRolAdmActivity extends AppCompatActivity {

    RolDAO dao;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rol);

        Intent intent = getIntent();
        idUsuario = getIntent().getStringExtra("idUsuario");

        FloatingActionButton cerrar=(FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ListaRolAdmActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        dao = new RolDAO(this);
        refrescar();

        Button agregar = (Button) findViewById(R.id.btnAgregarRolUsuario);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaRolRelacion);
                if (validacionSeleccion(lista)) {
                    dao.deleteRolesUsuario(idUsuario);

                    for (int i = 0; i < lista.getCount(); i++) {
                        if (lista.isItemChecked(i)) {
                            Roles roles = (Roles) lista.getItemAtPosition(i);
                            dao.addRolesUsuario(idUsuario, roles.getIdRol());
                        }
                    }
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(ListaRolAdmActivity.this,"Debe selecionar minimo un rol",Toast.LENGTH_LONG).show();
                }
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
        final ListView lista = (ListView) findViewById(R.id.listaRolRelacion);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<Roles> adapter = new ArrayAdapter<Roles>(this,
                android.R.layout.simple_list_item_multiple_choice,
                dao.getAllRoles());
        lista.setAdapter(adapter);

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        Usuarios usuarios = usuarioDAO.getUsuarioRol(idUsuario);
        for (int i = 0; i < usuarios.getListaRoles().size(); i++) {
            for (int j = 0; j < lista.getCount(); j++) {
                Roles roles = (Roles) lista.getItemAtPosition(j);
                if (usuarios.getListaRoles().get(i).getIdRol() == roles.getIdRol()) {
                    lista.setItemChecked(j, true);
                }
            }
        }
    }

}
