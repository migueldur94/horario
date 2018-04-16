package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.R;

public class MateriasActivity extends AppCompatActivity {
    MateriaDAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);
        dao = new MateriaDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriasActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

        Button eliminar = (Button) findViewById(R.id.btnEliminarMateria);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaMaterias);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    final Materia obj = (Materia) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(MateriasActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar esta materia?");
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
                    Toast.makeText(MateriasActivity.this, "Debe seleccionar al menos una materia ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button agregar = (Button) findViewById(R.id.btnAgregarMateria);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nombreMateria = (EditText) findViewById(R.id.txtNombreMateria);
                EditText cantidadEstudiante = (EditText) findViewById(R.id.txtCantidadEstudiante);

                Materia obj = new Materia();

                String nombre = nombreMateria.getText().toString();
                String cantidad = cantidadEstudiante.getText().toString();
                StringBuffer error = new StringBuffer();
                if (nombre == null || nombre.equals("")) {
                    error.append("- Nombre \n");
                }
                if (cantidad == null || cantidad.equals("")) {
                    error.append("- Cantidad estudiantes \n");
                }
                if (error.length() > 0) {
                    Toast.makeText(MateriasActivity.this, "Debe seleccionar el(los) siguiente(s) campo(s): \n" + error.toString(), Toast.LENGTH_LONG).show();
                } else {
                    obj.setNombreMateria(nombre);
                    obj.setCantidadEstudiante(Integer.parseInt(cantidad));
                    if (dao.addMateria(obj)) {
                        Toast.makeText(MateriasActivity.this, "Materia creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                        reset();
                        refrescar();
                    } else {
                        Toast.makeText(MateriasActivity.this, "Materia no se creado ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        Button actualizar = (Button) findViewById(R.id.btnModificarMateria);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreMateria = (EditText) findViewById(R.id.txtNombreMateria);
                EditText cantidadEstudiante = (EditText) findViewById(R.id.txtCantidadEstudiante);

                String nombre = nombreMateria.getText().toString();
                String cantidad = cantidadEstudiante.getText().toString();

                ListView lista = (ListView) findViewById(R.id.listaMaterias);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    Materia obj = (Materia) lista.getAdapter().getItem(pos);
                    if (!(nombre.equals("") && cantidad.equals(""))) {
                        obj.setNombreMateria(nombre);
                        obj.setCantidadEstudiante(Integer.parseInt(cantidad));

                        if (dao.updateMateria(obj)) {
                            Toast.makeText(MateriasActivity.this, "Materia actualizado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(MateriasActivity.this, "Materia no actualizado ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MateriasActivity.this, "Datos vacios", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MateriasActivity.this, "Debe seleccionar al menos una materia ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void eliminar(Materia obj) {
        if (dao.deleteMateria(obj)) {
            Toast.makeText(MateriasActivity.this, "Se elimino satisfactoriamente", Toast.LENGTH_SHORT).show();
            reset();
            refrescar();
        } else {
            Toast.makeText(MateriasActivity.this, "No se elimino la materia ", Toast.LENGTH_SHORT).show();
        }
    }


    private void reset() {
        EditText nombreMateria = (EditText) findViewById(R.id.txtNombreMateria);
        EditText cantidadEstudiante = (EditText) findViewById(R.id.txtCantidadEstudiante);

        nombreMateria.setText("");
        cantidadEstudiante.setText("");
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaMaterias);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Materia> adapter = new ArrayAdapter<Materia>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllMaterias());
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
                EditText codigo = (EditText) findViewById(R.id.txtNombreMateria);
                EditText nombre = (EditText) findViewById(R.id.txtCantidadEstudiante);

                Materia obj = (Materia) lista.getAdapter().getItem(position);

                codigo.setText("" + obj.getNombreMateria());
                nombre.setText("" + obj.getCantidadEstudiante());
            }
        });
    }
}
