package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.EVariables;
import gestionhorario.aplicacionmovil.uts.horario.EncriptarMD5;
import gestionhorario.aplicacionmovil.uts.horario.Procesos;
import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.HorarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.modelos.RolDAO;
import gestionhorario.aplicacionmovil.uts.horario.modelos.WebService;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Parametro;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Roles;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.UsuarioMateriaHorario;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Usuarios;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Variable;


public class MainActivity extends AppCompatActivity {
    UsuarioDAO usuarioDAO;
    Roles roles = new Roles();
    List<Roles> listaRoles = new ArrayList<Roles>();
    private Integer[] imagenGaleria = {
            R.drawable.fondologin,
            R.drawable.sistemas,
            R.drawable.agroindustrial,
            R.drawable.banca_finanzas,
            R.drawable.contabilidad,
            R.drawable.deportiva,
            R.drawable.gestion_empresarial,
            R.drawable.mercadeo_gestion_comercial,
            R.drawable.turismo,
            R.drawable.fondologin,
            R.drawable.fondologin};
    ProgressDialog dialog;
    List<SoapObject> listaSoapObject = new ArrayList<>();
    List<Parametro> listaParametro = new ArrayList<>();
    String nombreMetodoWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

        txtCodigo.setText("");
        txtPassword.setText("");
        //Calendar now = Calendar.getInstance();
        //Toast.makeText(this,"Hoy es : " + DataInfo.getDiasSemana().get(now.get(Calendar.DAY_OF_WEEK) - 1), Toast.LENGTH_LONG).show();

        cargarGallery();

        usuarioDAO = new UsuarioDAO(this);

        if (usuarioDAO.cantidadUsuarios() <= 0) {
            crearUsuarioPrimeraVez();
            Toast.makeText(this, "Cantidad usuarios " + usuarioDAO.cantidadUsuarios(), Toast.LENGTH_LONG).show();
        }

        Button butonLogin = (Button) findViewById(R.id.btnLogin);
        butonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String codigo;
                String password;

                try {
                    EditText txtCodigo = (EditText) findViewById(R.id.txtCodigo);
                    EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
                    codigo = txtCodigo.getText().toString();
                    password = txtPassword.getText().toString();
                    if (codigo.length() > 0 && password.length() > 0) {
                        Usuarios usuarios = new Usuarios();
                        EncriptarMD5 md5 = new EncriptarMD5();
                        //password = md5.encriptaEnMD5(password);
                        usuarios = usuarioDAO.getUsuario(codigo, password);
                        //Parametros para el servicio web
                        Parametro parametro = new Parametro();
                        parametro.setNombre("idUsuario");
                        parametro.setValor("1");
                        listaParametro.add(parametro);
                        parametro = new Parametro();
                        parametro.setNombre("contrasenia");
                        parametro.setValor("1");
                        listaParametro.add(parametro);
                        parametro = new Parametro();
                        parametro.setNombre("licencia");
                        parametro.setValor("1");
                        listaParametro.add(parametro);
                        CallService service = new CallService();
                        service.execute();
                        usuarios=Procesos.getListaUsuarioSoap(listaSoapObject).get(0);
                        if (usuarios != null) {
                            listaRoles = usuarios.getListaRoles();
                            if (listaRoles.size() > 0) {
                                if (listaRoles.size() == 2) {
                                    ArrayAdapter<Roles> arrayAdapter =
                                            new ArrayAdapter<Roles>(MainActivity.this, android.R.layout.simple_list_item_single_choice, listaRoles);
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                                    alerta.setTitle("!Elejir opción¡");
                                    alerta.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            roles = listaRoles.get(which);
                                            DataInfo.setCodigo(codigo);
                                            validacion();
                                        }
                                    });
                                    alerta.show();
                                } else {
                                    DataInfo.setCodigo(codigo);
                                    roles = listaRoles.get(0);
                                    validacion();
                                }
                                validacionTokenNotificacion(usuarios.getCodigo());
                            } else {
                                Toast.makeText(MainActivity.this, "Señor(a) " + usuarios.getNombre() + " " + usuarios.getApellido() + " dirijase al departamento de sistemas no tiene rol.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(MainActivity.this, "Bienvenid@ " + usuarios.getNombre() + " " + usuarios.getApellido(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(MainActivity.this, "El codigo ò contraseña no validos.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Debe diligenciar el campo codigo y contraseña", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Ha ocurrido un problema informar al administrador.", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout btnGenerarPdf = (LinearLayout) findViewById(R.id.btnGenerarPdf);
        btnGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF();
            }
        });
    }

    public void validacion() {
        DataInfo.setIdRol(roles.getIdRol());
        if (usuarioDAO.getAllVariables(-1).size() > 0) {
            if (roles.getIdRol() == Integer.parseInt(usuarioDAO.getAllVariables(Integer.parseInt(EVariables.ID_VARIABLE_ROL_ADMIN)).get(0).getValor())) {
                Intent intent = new Intent(MainActivity.this, OpcionesActivity.class);
                startActivity(intent);
            } else if (roles.getIdRol() == Integer.parseInt(usuarioDAO.getAllVariables(Integer.parseInt(EVariables.ID_VARIABLE_ROL_DOCENTE)).get(0).getValor())) {
                Intent intent = new Intent(MainActivity.this, ListaMateriasDocenteActivity.class);
                startActivity(intent);
            } else if (roles.getIdRol() == Integer.parseInt(usuarioDAO.getAllVariables(Integer.parseInt(EVariables.ID_VARIABLE_ROL_ESTUDIANTE)).get(0).getValor())) {
                Intent intent = new Intent(MainActivity.this, ListaMateriasEstudianteActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(MainActivity.this, "Ha ocurrido un error por favor informar al administrador del sistema: 'ERROR CONFIGURACION VARIABLES'.", Toast.LENGTH_LONG).show();
        }
    }

    public void validacionTokenNotificacion(String codigo) {
        usuarioDAO = new UsuarioDAO(this);

        //String recent_token = FirebaseInstanceId.getInstance().getToken();
        // usuarioDAO.updateToken(codigo, recent_token);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        int imageBackground;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imagenGaleria.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ImageView imageView = new ImageView(context);
            if (arg0 == imagenGaleria.length - 1) {
                cargarGallery();
            }
            imageView.setImageResource(imagenGaleria[arg0]);
            return imageView;
        }
    }

    public void crearUsuarioPrimeraVez() {
        usuarioDAO = new UsuarioDAO(this);
        Usuarios usuarios = new Usuarios();
        usuarios.setCodigo("1");
        usuarios.setNombre("miguel ");
        usuarios.setApellido("duran ");
        usuarios.setPassword("1234");
        usuarios.setCorreo("administrador@hotmail.com");
        usuarios.setActivo(true);

        usuarioDAO.addUsuarios(usuarios);

        RolDAO rolDAO = new RolDAO(this);
        Roles roles = new Roles();
        roles.setNombre("admin");
        rolDAO.addRoles(roles);
        roles = new Roles();
        roles.setNombre("docente");
        rolDAO.addRoles(roles);
        roles = new Roles();
        roles.setNombre("estudiante");
        rolDAO.addRoles(roles);

        rolDAO.addRolesUsuario(usuarios.getCodigo(), 1);

        Variable variable = new Variable();
        variable.setIdVariable(1);
        variable.setNombre("ID_ROL_ADMIN");
        variable.setDescripcion("Se ingresa el id del rol administrador");
        variable.setValor("1");
        usuarioDAO.addVariables(variable);
        variable = new Variable();
        variable.setIdVariable(2);
        variable.setNombre("ID_ROL_DOCENTE");
        variable.setDescripcion("Se ingresa el id del rol docente");
        variable.setValor("3");
        usuarioDAO.addVariables(variable);
        variable = new Variable();
        variable.setIdVariable(3);
        variable.setNombre("ID_ROL_ESTUDIANTE");
        variable.setDescripcion("Se ingresa el id del rol estudiante");
        variable.setValor("2");
        usuarioDAO.addVariables(variable);
    }

    public void cargarGallery() {
        Gallery gallery = (Gallery) findViewById(R.id.galeriaLogin);
        gallery.setAdapter(new ImageAdapter(this));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Uri uriUrl = null;
                if (arg2 == 0) {
                    uriUrl = Uri.parse("http://www.uts.edu.co/portal/");
                } else if (arg2 == 1) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=5&cat=1&key=fc8a7a2f3e27dda3644f86b2fa3955d9");
                } else if (arg2 == 2) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=9&cat=1&key=3eb4b7ef08d2da996078fbd5ee55b262");
                } else if (arg2 == 3) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=8&cat=1&key=54bd395fd7c467640ac19d08e8cd8930");
                } else if (arg2 == 4) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=1&cat=1&key=5e0e1c4d71d83d88775d08499311232d");
                } else if (arg2 == 5) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=7&cat=1&key=8f82d790eeb939a5a7db4966498bed9e");
                } else if (arg2 == 6) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=2&cat=1&key=1682382b43c41ff5f9d9e6fc0252b2ed");
                } else if (arg2 == 7) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=6&cat=1&key=2d271a7cd308fb719f9e4fb66f5dc29b");
                } else if (arg2 == 8) {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/views/prgdetalle.php?id=52&cat=1&key=6f7f981d38f57ce61ac0a879028b20ee");
                } else {
                    uriUrl = Uri.parse("http://uts.edu.co/portal/");
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent);
            }
        });
    }

    public void createPDF() {

        Document doc = new Document();

        String outpath = Environment.getExternalStorageDirectory() + "/horario.pdf";

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();
            Drawable d = getResources().getDrawable(R.drawable.logo);

            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            doc.add(image);

            List<Materia> listaMateria = new ArrayList<>();
            MateriaDAO materiaDAO = new MateriaDAO(this);
            listaMateria = materiaDAO.getAllMaterias();
            for (int i = 0; i < listaMateria.size(); i++) {
                doc.add(createFirstTable(listaMateria.get(i)));
                doc.add(new Paragraph("\n"));
            }
            doc.close();

            abrirPDF(outpath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirPDF(String ruta) {
        File targetFile = new File(ruta);
        Uri targetUri = Uri.fromFile(targetFile);
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(targetUri, "application/pdf");
        startActivity(intent);

    }

    public PdfPTable createFirstTable(Materia materia) {
        PdfPTable table = new PdfPTable(DataInfo.getDiasSemana().size() + 1);
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph(materia.getNombreMateria().toUpperCase()));
        cell.setColspan(DataInfo.getDiasSemana().size() + 1);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
        table.addCell("Hora");
        for (int i = 0; i < DataInfo.getDiasSemana().size(); i++) {
            table.addCell(DataInfo.getDiasSemana().get(i));
        }
        List<Horario> listaHorario = new ArrayList<>();
        HorarioDAO horarioDAO = new HorarioDAO(this);
        listaHorario = horarioDAO.getHorarioRelacionUbicacionPDf(materia.getIdMateria());
        for (int j = 0; j < listaHorario.size(); j++) {
            table.addCell(listaHorario.get(j).getHoraInicio() + ":" + listaHorario.get(j).getMinutoInicio() + "-" + listaHorario.get(j).getHoraFin() + ":" + listaHorario.get(j).getMinutoFin());
            UsuarioDAO usuarioDAO = new UsuarioDAO(this);
            for (int k = 0; k < DataInfo.getDiasSemana().size(); k++) {
                List<UsuarioMateriaHorario> listaUsuario = new ArrayList<>();
                String usuario = "";
                listaUsuario = usuarioDAO.getUsuarioMateriaHorarioAll(listaHorario.get(j).getIdHorario(), materia.getIdMateria(), DataInfo.getDiasSemana().get(k));
                for (int h = 0; h < listaUsuario.size(); h++) {
                    usuario = usuario + listaUsuario.get(h).getUsuarios().getNombre() + " " + listaUsuario.get(h).getUsuarios().getApellido() + "\n" + listaUsuario.get(h).getUbicacion().getNombre() + "\n";
                    if (h != 0) {
                        usuario = usuario + "------------\n";
                    }
                }
                table.addCell(usuario);
            }

        }

        return table;
    }

    private class CallService extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listaSoapObject = WebService.getConexion(nombreMetodoWS, listaParametro);

            return null;
        }

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setIndeterminate(false);
            dialog.setMessage("Loding...");
            dialog.setCancelable(false);
            dialog.show();

        }

        protected void onProgressUpdate(String... params) {
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void resultado) {

            dialog.dismiss();
        }
    }
}
