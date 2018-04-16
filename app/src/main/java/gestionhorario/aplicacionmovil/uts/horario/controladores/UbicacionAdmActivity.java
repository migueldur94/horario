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
import gestionhorario.aplicacionmovil.uts.horario.objetos.Ubicacion;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UbicacionDAO;

public class UbicacionAdmActivity extends AppCompatActivity {
    UbicacionDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_adm);
        dao = new UbicacionDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UbicacionAdmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

        Button eliminar = (Button) findViewById(R.id.btnEliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.lista);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    final Ubicacion obj = (Ubicacion) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(UbicacionAdmActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar la ubicación?");
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
                    Toast.makeText(UbicacionAdmActivity.this, "Debe seleccionar al menos una ubicación ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button agregar = (Button) findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nombreUbicacion = (EditText) findViewById(R.id.txtNombreUbicacion);
                Ubicacion obj = new Ubicacion();
                String nombre = nombreUbicacion.getText().toString();

                if (!(nombre.equals(""))) {
                    obj.setNombre(nombre);
                    if (dao.addUbicacion(obj)) {
                        Toast.makeText(UbicacionAdmActivity.this, "Ubicación creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                        reset();
                        refrescar();
                    } else {
                        Toast.makeText(UbicacionAdmActivity.this, "Ubicación no se ha creado ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UbicacionAdmActivity.this, "Debe diligenciar el campo: \n -nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button actualizar = (Button) findViewById(R.id.btnModificar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreUbicacion = (EditText) findViewById(R.id.txtNombreUbicacion);

                String nombre = nombreUbicacion.getText().toString();

                ListView lista = (ListView) findViewById(R.id.lista);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    Ubicacion obj = (Ubicacion) lista.getAdapter().getItem(pos);
                    if (!(nombre.equals(""))) {
                        obj.setNombre(nombre);

                        if (dao.updateUbicacion(obj)) {
                            Toast.makeText(UbicacionAdmActivity.this, "Ubicación actualizado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(UbicacionAdmActivity.this, "Ubicacion no actualizado ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UbicacionAdmActivity.this, "Debe diligenciar el campo: \n -nombre", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UbicacionAdmActivity.this, "Debe seleccionar al menos una ubicación ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void eliminar(Ubicacion obj) {
        if (dao.deleteUbicacion(obj.getIdUbicacion())) {
            Toast.makeText(UbicacionAdmActivity.this, "Se elimino satisfactoriamente", Toast.LENGTH_SHORT).show();
            reset();
            refrescar();
        } else {
            Toast.makeText(UbicacionAdmActivity.this, "No se elimino la ubicación ", Toast.LENGTH_SHORT).show();
        }
    }


    private void reset() {
        EditText nombre = (EditText) findViewById(R.id.txtNombreUbicacion);

        nombre.setText("");
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.lista);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<Ubicacion> adapter = new ArrayAdapter<Ubicacion>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllUbicaciones());
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText nombre = (EditText) findViewById(R.id.txtNombreUbicacion);

                Ubicacion obj = (Ubicacion) lista.getAdapter().getItem(position);

                nombre.setText("" + obj.getNombre());
            }
        });
    }
}
