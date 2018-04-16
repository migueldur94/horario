package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.RolDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;

public class RolActivity extends AppCompatActivity {
    RolDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol);

        dao = new RolDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RolActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

        Button eliminar = (Button) findViewById(R.id.btnEliminarRol);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaRoles);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    final Roles obj = (Roles) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(RolActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar esta rol?");
                    alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminar(obj);
                            reset();
                        }
                    });
                    alerta.setNegativeButton("No", null);
                    alerta.show();
                } else {
                    Toast.makeText(RolActivity.this, "Debe seleccionar al menos una rol ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button agregar = (Button) findViewById(R.id.btnAgregarRol);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nombreRoles = (EditText) findViewById(R.id.txtNombreRol);
                Roles obj = new Roles();
                String nombre = nombreRoles.getText().toString();

                if (!(nombre.equals(""))) {
                    obj.setNombre(nombre);
                    if (dao.addRoles(obj)) {
                        Toast.makeText(RolActivity.this, "Rol creado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        reset();
                        refrescar();
                    } else {
                        Toast.makeText(RolActivity.this, "Rol no se creado ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RolActivity.this, "Debe diligenciar el campo: \n -nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button actualizar = (Button) findViewById(R.id.btnModificarRol);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreRoles = (EditText) findViewById(R.id.txtNombreRol);

                String nombre = nombreRoles.getText().toString();

                ListView lista = (ListView) findViewById(R.id.listaRoles);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    Roles obj = (Roles) lista.getAdapter().getItem(pos);
                    if (!(nombre.equals(""))) {
                        obj.setNombre(nombre);

                        if (dao.updateRoles(obj)) {
                            Toast.makeText(RolActivity.this, "Rol actualizado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(RolActivity.this, "Rol no actualizado ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RolActivity.this, "Debe diligenciar el campo: \n -nombre", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RolActivity.this, "Debe seleccionar al menos una rol ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void eliminar(Roles obj) {
        if (dao.deleteRoles(obj)) {
            Toast.makeText(RolActivity.this, "Se elimino satisfactoriamente", Toast.LENGTH_SHORT).show();
            reset();
            refrescar();
        } else {
            Toast.makeText(RolActivity.this, "No se elimino el rol ", Toast.LENGTH_SHORT).show();
        }
    }


    private void reset() {
        EditText nombreRoles = (EditText) findViewById(R.id.txtNombreRol);

        nombreRoles.setText("");
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaRoles);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<Roles> adapter = new ArrayAdapter<Roles>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllRoles());
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText nombre = (EditText) findViewById(R.id.txtNombreRol);

                Roles obj = (Roles) lista.getAdapter().getItem(position);

                nombre.setText("" + obj.getNombre());
            }
        });
    }
}
