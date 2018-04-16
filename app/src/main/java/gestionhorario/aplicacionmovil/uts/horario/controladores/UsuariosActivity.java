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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;

public class UsuariosActivity extends AppCompatActivity {
    UsuarioDAO usuarioDAO;


    Usuarios objeto = new Usuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        usuarioDAO = new UsuarioDAO(this);

        FloatingActionButton cerrar = (FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuariosActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refrescar();

        Button agregar = (Button) findViewById(R.id.btnAgregarUsuario);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtCodigoUsuario;
                EditText txtNombreUsuario;
                EditText txtApellidoUsuario;
                EditText txtPasswordUsuario;
                RadioGroup grBtnActivoUsuario;
                EditText txtCorreoUsuario;

                txtCodigoUsuario = (EditText) findViewById(R.id.txtCodigoUsuario);
                txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
                txtApellidoUsuario = (EditText) findViewById(R.id.txtApellidoUsuario);
                txtPasswordUsuario = (EditText) findViewById(R.id.txtPasswordUsuario);
                grBtnActivoUsuario = (RadioGroup) findViewById(R.id.grBtnActivoUsuario);
                txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoUsuario);

                String codigo = txtCodigoUsuario.getText().toString();
                String nombre = txtNombreUsuario.getText().toString();
                String apellido = txtApellidoUsuario.getText().toString();
                String password = txtPasswordUsuario.getText().toString();
                boolean activo = false;
                StringBuffer error = new StringBuffer();
                if (grBtnActivoUsuario.getCheckedRadioButtonId() == R.id.rBtnSi) {
                    activo = true;
                } else if (grBtnActivoUsuario.getCheckedRadioButtonId() == R.id.rBtnNo) {
                    activo = false;
                } else {
                    error.append("-Activo \n");
                }
                String correo = txtCorreoUsuario.getText().toString();

                //Validaciones de los datos del usuario

                if (codigo == null || codigo.equals("")) {
                    error.append("-Codigo \n");
                }
                if (nombre == null || nombre.equals("")) {
                    error.append("-Nombre \n");
                }
                if (apellido == null || apellido.equals("")) {
                    error.append("-Apellido \n");
                }
                if (password == null || password.equals("")) {
                    error.append("-Password \n");
                }
                if (correo == null || correo.equals("")) {
                    error.append("-Correo \n");
                } else if (correo.indexOf("@") == -1 || correo.indexOf(".") == -1) {
                    error.append("-El correo debe de tener un simbolo '@' y '.'");
                }

                if (error.length() > 0) {
                    Toast.makeText(UsuariosActivity.this, "Debe seleccionar el(los) siguiente(s) campo(s): \n" + error.toString(), Toast.LENGTH_LONG).show();
                } else {
                    if (usuarioDAO.validarExisteUsuario(codigo) == 0) {
                        //Seteo de datos del objeto usuario
                        objeto.setCodigo(codigo);
                        objeto.setNombre(nombre);
                        objeto.setApellido(apellido);
                        objeto.setPassword(password);
                        objeto.setActivo(activo);
                        objeto.setCorreo(correo);

                        txtCodigoUsuario.setText("");
                        txtNombreUsuario.setText("");
                        txtApellidoUsuario.setText("");
                        txtPasswordUsuario.setText("");
                        txtCorreoUsuario.setText("");

                        if (usuarioDAO.addUsuarios(objeto)) {
                            Toast.makeText(UsuariosActivity.this, "Usuario creado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            refrescar();
                        } else {
                            Toast.makeText(UsuariosActivity.this, "Codigo del usuario ya existe ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UsuariosActivity.this, "El codigo ya existe ", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        Button actualizar = (Button) findViewById(R.id.btnModificarUsuario);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtCodigoUsuario;
                EditText txtNombreUsuario;
                EditText txtApellidoUsuario;
                EditText txtPasswordUsuario;
                RadioGroup grBtnActivoUsuario;
                EditText txtCorreoUsuario;

                txtCodigoUsuario = (EditText) findViewById(R.id.txtCodigoUsuario);
                txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
                txtApellidoUsuario = (EditText) findViewById(R.id.txtApellidoUsuario);
                txtPasswordUsuario = (EditText) findViewById(R.id.txtPasswordUsuario);
                grBtnActivoUsuario = (RadioGroup) findViewById(R.id.grBtnActivoUsuario);
                txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoUsuario);

                String codigo = txtCodigoUsuario.getText().toString();
                String nombre = txtNombreUsuario.getText().toString();
                String apellido = txtApellidoUsuario.getText().toString();
                String password = txtPasswordUsuario.getText().toString();
                boolean activo = false;
                StringBuffer error = new StringBuffer();
                if (grBtnActivoUsuario.getCheckedRadioButtonId() == R.id.rBtnSi) {
                    activo = true;
                } else if (grBtnActivoUsuario.getCheckedRadioButtonId() == R.id.rBtnNo) {
                    activo = false;
                } else {
                    error.append("-Activo \n");
                }
                String correo = txtCorreoUsuario.getText().toString();

                //Validaciones de los datos del usuario

                if (nombre == null || nombre.equals("")) {
                    error.append("-Nombre \n");
                }
                if (apellido == null || apellido.equals("")) {
                    error.append("-Apellido \n");
                }
                if (password == null || password.equals("")) {
                    error.append("-Password \n");
                }
                if (correo == null || correo.equals("")) {
                    error.append("-Correo \n");
                } else if (correo.indexOf("@") == -1 || correo.indexOf(".") == -1) {
                    error.append("-El correo debe de tener un simbolo '@' y '.'");
                }

                if (error.length() > 0) {
                    Toast.makeText(UsuariosActivity.this, "Debe seleccionar el(los) siguiente(s) campo(s): \n" + error.toString(), Toast.LENGTH_LONG).show();
                } else {

                    //Seteo de datos del objeto usuario
                    objeto.setCodigo(codigo);
                    objeto.setNombre(nombre);
                    objeto.setApellido(apellido);
                    objeto.setPassword(password);
                    objeto.setActivo(activo);
                    objeto.setCorreo(correo);

                    txtCodigoUsuario.setText("");
                    txtNombreUsuario.setText("");
                    txtApellidoUsuario.setText("");
                    txtPasswordUsuario.setText("");
                    txtCorreoUsuario.setText("");

                    if (usuarioDAO.updateUsuarios(objeto)) {
                        Toast.makeText(UsuariosActivity.this, "Usuario actualizado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        refrescar();
                    } else {
                        Toast.makeText(UsuariosActivity.this, "Error al actualizar el usuario ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        Button eliminar = (Button) findViewById(R.id.btnEliminarUsuario);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaUsuarios);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    final Usuarios obj = (Usuarios) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(UsuariosActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar este usuario?");
                    alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminar(obj);
                            EditText txtCodigoUsuario;
                            EditText txtNombreUsuario;
                            EditText txtApellidoUsuario;
                            EditText txtPasswordUsuario;
                            RadioGroup grBtnActivoUsuario;
                            EditText txtCorreoUsuario;

                            txtCodigoUsuario = (EditText) findViewById(R.id.txtCodigoUsuario);
                            txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
                            txtApellidoUsuario = (EditText) findViewById(R.id.txtApellidoUsuario);
                            txtPasswordUsuario = (EditText) findViewById(R.id.txtPasswordUsuario);
                            grBtnActivoUsuario = (RadioGroup) findViewById(R.id.grBtnActivoUsuario);
                            txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoUsuario);

                            txtCodigoUsuario.setText("");
                            txtNombreUsuario.setText("");
                            txtApellidoUsuario.setText("");
                            txtPasswordUsuario.setText("");
                            txtCorreoUsuario.setText("");

                        }
                    });
                    alerta.setNegativeButton("No", null);
                    alerta.show();
                } else {
                    Toast.makeText(UsuariosActivity.this, "Seleccione un usuario", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaUsuarios);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<Usuarios> adapter =
                new ArrayAdapter<Usuarios>(this, android.R.layout.simple_list_item_single_choice,
                        usuarioDAO.getAllUsuarios());
        lista.setAdapter(adapter);
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
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Usuarios obj = (Usuarios) lista.getAdapter().getItem(position);
                EditText txtCodigoUsuario;
                EditText txtNombreUsuario;
                EditText txtApellidoUsuario;
                EditText txtPasswordUsuario;
                RadioGroup grBtnActivoUsuario;
                EditText txtCorreoUsuario;

                txtCodigoUsuario = (EditText) findViewById(R.id.txtCodigoUsuario);
                txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
                txtApellidoUsuario = (EditText) findViewById(R.id.txtApellidoUsuario);
                txtPasswordUsuario = (EditText) findViewById(R.id.txtPasswordUsuario);
                grBtnActivoUsuario = (RadioGroup) findViewById(R.id.grBtnActivoUsuario);
                txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoUsuario);

                if (obj.isActivo()) {
                    ((RadioButton) grBtnActivoUsuario.findViewById(R.id.rBtnSi)).setChecked(true);
                } else {
                    ((RadioButton) grBtnActivoUsuario.findViewById(R.id.rBtnNo)).setChecked(true);
                }
                txtCodigoUsuario.setText("" + obj.getCodigo().toString());
                txtNombreUsuario.setText("" + obj.getNombre());
                txtApellidoUsuario.setText("" + obj.getApellido());
                txtPasswordUsuario.setText("" + obj.getPassword());
                txtCorreoUsuario.setText("" + obj.getCorreo());
                txtCodigoUsuario.setEnabled(false);

            }
        });
    }


    public void eliminar(Usuarios obj) {
        if (usuarioDAO.deleteUsuarios(obj)) {
            Toast.makeText(UsuariosActivity.this, "Se elimino el usuario satisfactoriamente", Toast.LENGTH_SHORT).show();
            refrescar();
        } else {
            Toast.makeText(UsuariosActivity.this, "No se elimino el usuario ", Toast.LENGTH_SHORT).show();
        }
    }


}
