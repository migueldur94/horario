package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.modelos.HorarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Materia;
import gestionhorario.aplicacionmovil.uts.horario.modelos.MateriaDAO;
import gestionhorario.aplicacionmovil.uts.horario.R;
import gestionhorario.aplicacionmovil.uts.horario.modelos.UsuarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.objetos.UsuarioMateriaHorario;

public class OpcionesActivity extends AppCompatActivity {
    final int ACTIVITY_CHOOSE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        LinearLayout usuario = (LinearLayout) findViewById(R.id.btnOpcionUsuarios);
        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, UsuariosActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout rol = (LinearLayout) findViewById(R.id.btnOpcionRol);
        rol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, RolActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout materia = (LinearLayout) findViewById(R.id.btnOpcionMateria);
        materia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, MateriasActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout horario = (LinearLayout) findViewById(R.id.btnOpcionHorario);
        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, HorariosActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout relacionur = (LinearLayout) findViewById(R.id.btnOpcionur);
        relacionur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, ListaUsuarioAdmActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout relaiconmh = (LinearLayout) findViewById(R.id.btnOpcionmh);
        relaiconmh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, ListaMateriasAdmActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout relaiconum = (LinearLayout) findViewById(R.id.btnOpcionum);
        relaiconum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, ListaUsuarioMateriaAdmActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnOpcionUbicacion = (LinearLayout) findViewById(R.id.btnOpcioubi);
        btnOpcionUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, UbicacionAdmActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnOpcionHorarioUbicacion = (LinearLayout) findViewById(R.id.btnOpcioubiHor);
        btnOpcionHorarioUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, ListaHorarioUbicacionAdmActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout btnVariable = (LinearLayout) findViewById(R.id.btnVariable);
        btnVariable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesActivity.this, VariablesActivity.class);
                startActivity(intent);
            }
        });
    }


}
