package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Variable;

public class VariablesActivity extends AppCompatActivity {
    UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variables);
        dao = new UsuarioDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariablesActivity.this, MainActivity.class);
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
                    final Variable obj = (Variable) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(VariablesActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar esta variable?");
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
                    Toast.makeText(VariablesActivity.this, "Debe seleccionar al menos una variable ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button agregar = (Button) findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText idTxt = (EditText) findViewById(R.id.txtId);
                EditText nombreTxt = (EditText) findViewById(R.id.txtNombre);
                EditText descripcionTxt = (EditText) findViewById(R.id.txtDescripcion);
                EditText valorTxt = (EditText) findViewById(R.id.txtValor);

                String id = idTxt.getText().toString();
                String nombre = nombreTxt.getText().toString();
                String descripcion = descripcionTxt.getText().toString();
                String valor = valorTxt.getText().toString();

                StringBuffer error = new StringBuffer();
                if (id == null || id.equals("")) {
                    error.append("- Id \n");
                } else if (Integer.parseInt(id) <= 0) {
                    error.append("- El Id no puede ser menor o igual a cero \n ");
                }
                if (nombre == null || nombre.equals("")) {
                    error.append("- Nombre \n");
                }
                if (descripcion == null || descripcion.equals("")) {
                    error.append("- Descripción \n");
                }
                if (valor == null || valor.equals("")) {
                    error.append("- Valor \n");
                }
                if (error.length() > 0) {
                    Toast.makeText(VariablesActivity.this, "Debe diligenciar (el)los siguiente(s) campo(s) :\n" + error.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Variable obj = new Variable();
                    obj.setIdVariable(Integer.parseInt(id));
                    obj.setNombre(nombre);
                    obj.setDescripcion(descripcion);
                    obj.setValor(valor);
                    if (dao.validacionExisteVariable(obj.getIdVariable())) {
                        if (dao.updateVariable(obj)) {
                            Toast.makeText(VariablesActivity.this, "Variable actualizada satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(VariablesActivity.this, "Variable no se actualizo ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (dao.addVariables(obj)) {
                            Toast.makeText(VariablesActivity.this, "Variable creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(VariablesActivity.this, "Variable no se creado ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        Button actualizar = (Button) findViewById(R.id.btnModificar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idTxt = (EditText) findViewById(R.id.txtId);
                EditText nombreTxt = (EditText) findViewById(R.id.txtNombre);
                EditText descripcionTxt = (EditText) findViewById(R.id.txtDescripcion);
                EditText valorTxt = (EditText) findViewById(R.id.txtValor);

                String id = idTxt.getText().toString();
                String nombre = nombreTxt.getText().toString();
                String descripcion = descripcionTxt.getText().toString();
                String valor = valorTxt.getText().toString();

                ListView lista = (ListView) findViewById(R.id.lista);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {

                    StringBuffer error = new StringBuffer();
                    if (id == null || id.equals("")) {
                        error.append("- Id \n");
                    }
                    if (nombre == null || nombre.equals("")) {
                        error.append("- Nombre \n");
                    }
                    if (descripcion == null || descripcion.equals("")) {
                        error.append("- Descripción \n");
                    }
                    if (valor == null || valor.equals("")) {
                        error.append("- Valor \n");
                    }
                    if (Integer.parseInt(id) <= 0) {
                        error.append("- El Id no puede ser menor o igual a cero \n ");
                    }
                    if (error.length() > 0) {
                        Toast.makeText(VariablesActivity.this, "Debe diligenciar (el)los siguiente(s) campo(s) :\n" + error.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Variable obj = new Variable();
                        obj.setIdVariable(Integer.parseInt(id));
                        obj.setNombre(nombre);
                        obj.setDescripcion(descripcion);
                        obj.setValor(valor);
                        if (dao.updateVariable(obj)) {
                            Toast.makeText(VariablesActivity.this, "Variable actualizada satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(VariablesActivity.this, "Variable no se actualizo ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(VariablesActivity.this, "Debe seleccionar al menos una variable ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void eliminar(Variable obj) {
        if (dao.deleteVariable(obj.getIdVariable())) {
            Toast.makeText(VariablesActivity.this, "Se elimino satisfactoriamente", Toast.LENGTH_SHORT).show();
            reset();
            refrescar();
        } else {
            Toast.makeText(VariablesActivity.this, "No se elimino la variable ", Toast.LENGTH_SHORT).show();
        }
    }


    private void reset() {
        EditText idTxt = (EditText) findViewById(R.id.txtId);
        EditText nombreTxt = (EditText) findViewById(R.id.txtNombre);
        EditText descripcionTxt = (EditText) findViewById(R.id.txtDescripcion);
        EditText valorTxt = (EditText) findViewById(R.id.txtValor);

        idTxt.setText("");
        nombreTxt.setText("");
        descripcionTxt.setText("");
        valorTxt.setText("");
    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.lista);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Variable> adapter = new ArrayAdapter<Variable>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllVariables(-1));
        lista.setAdapter(adapter);
        lista.setTextFilterEnabled(true);
        final EditText filtroVariable = (EditText) findViewById(R.id.filtroVariable);
        filtroVariable.addTextChangedListener(new TextWatcher() {
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
                EditText idTxt = (EditText) findViewById(R.id.txtId);
                EditText nombreTxt = (EditText) findViewById(R.id.txtNombre);
                EditText descripcionTxt = (EditText) findViewById(R.id.txtDescripcion);
                EditText valorTxt = (EditText) findViewById(R.id.txtValor);

                Variable obj = (Variable) lista.getAdapter().getItem(position);

                idTxt.setEnabled(true);

                idTxt.setText("" + obj.getIdVariable());
                nombreTxt.setText("" + obj.getNombre());
                descripcionTxt.setText("" + obj.getDescripcion());
                valorTxt.setText("" + obj.getValor());
            }
        });
    }
}
