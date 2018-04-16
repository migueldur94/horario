package gestionhorario.aplicacionmovil.uts.horario.controladores;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Horario;
import gestionhorario.aplicacionmovil.uts.horario.modelos.HorarioDAO;
import gestionhorario.aplicacionmovil.uts.horario.R;

public class HorariosActivity extends AppCompatActivity {
    private HorarioDAO dao;
    private Calendar calendar = Calendar.getInstance();
    private boolean hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        dao = new HorarioDAO(this);
        refrescar();
        ejecutarClicksHoras();

        FloatingActionButton cerrar=(FloatingActionButton) findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HorariosActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button eliminar = (Button) findViewById(R.id.btnEliminarHorario);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista = (ListView) findViewById(R.id.listaHorario);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    final Horario obj = (Horario) lista.getAdapter().getItem(pos);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(HorariosActivity.this);
                    alerta.setTitle("!Advertencia¡");
                    alerta.setMessage("Esta seguro que desea eliminar este horario?");
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
                    Toast.makeText(HorariosActivity.this, "Debe seleccionar al menos un horario ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button agregar = (Button) findViewById(R.id.btnAgregarHorario);
        agregar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Spinner txtDiaSemana = (Spinner) findViewById(R.id.spinerDiaSemana);
                EditText txtHoraInicio = (EditText) findViewById(R.id.txtHoraInicio);
                EditText txtMinutoInicio = (EditText) findViewById(R.id.txtMinutoInicio);
                EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
                EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);


                String diaSemana = txtDiaSemana.getItemAtPosition(txtDiaSemana.getSelectedItemPosition()).toString();
                String hora = txtHoraInicio.getText().toString();
                String minuto = txtMinutoInicio.getText().toString();
                String horaFin = txtHoraFin.getText().toString();
                String minutoFin = txtMinutoFin.getText().toString();

                Horario obj = new Horario();
                StringBuffer error = new StringBuffer();

                if (diaSemana == null || diaSemana.equals("")) {
                    error.append("- Dia semana \n");
                }
                if (hora == null || hora.equals("")) {
                    error.append("- Hora Inicio \n");
                }
                if (minuto == null || minuto.equals("")) {
                    error.append("- Minuto Inicio \n");
                }
                if (horaFin == null || horaFin.equals("")) {
                    error.append("- Hora Fin \n");
                }
                if (minutoFin == null || minutoFin.equals("")) {
                    error.append("- Minuto Fin \n");
                }

                if (error.length() > 0) {
                    Toast.makeText(HorariosActivity.this, "Debe seleccionar el(los) siguiente(s) campo(s): \n" + error.toString(), Toast.LENGTH_LONG).show();
                } else {

                    if ((Integer.parseInt(hora) > 23 || Integer.parseInt(minuto) > 59) || (Integer.parseInt(horaFin) > 23 || Integer.parseInt(minutoFin) > 59)) {
                        Toast.makeText(HorariosActivity.this, "La Hora Inicio y Fin debe ser infeior a 24 horas y los minutos inferior a 60.", Toast.LENGTH_LONG).show();
                    } else if ((Integer.parseInt(hora)) > (Integer.parseInt(horaFin))) {
                        Toast.makeText(HorariosActivity.this, "La Hora Inicio es mayor a la Hora Fin .", Toast.LENGTH_LONG).show();
                    } else if ((Integer.parseInt(hora)) == (Integer.parseInt(horaFin)) && (Integer.parseInt(minuto)) > (Integer.parseInt(minutoFin))) {
                        Toast.makeText(HorariosActivity.this, "La Hora Inicio es mayor a la Hora Fin .", Toast.LENGTH_LONG).show();
                    } else if ((Integer.parseInt(hora)) == (Integer.parseInt(horaFin)) && (Integer.parseInt(minuto)) == (Integer.parseInt(minutoFin))) {
                        Toast.makeText(HorariosActivity.this, "La Hora Inicio debe ser mayor a la Hora Fin .", Toast.LENGTH_LONG).show();
                    } else {
                        obj.setDiaSemana(diaSemana);
                        obj.setHoraInicio(Integer.parseInt(hora));
                        obj.setMinutoInicio(Integer.parseInt(minuto));
                        obj.setHoraFin(Integer.parseInt(horaFin));
                        obj.setMinutoFin(Integer.parseInt(minutoFin));

                        if (dao.addHorario(obj)) {
                            Toast.makeText(HorariosActivity.this, "Horario creado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            reset();
                            refrescar();
                        } else {
                            Toast.makeText(HorariosActivity.this, "Horario no se ha creado ", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        Button actualizar = (Button) findViewById(R.id.btnModificarHorario);
        actualizar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Spinner txtDiaSemana = (Spinner) findViewById(R.id.spinerDiaSemana);
                EditText txtHora = (EditText) findViewById(R.id.txtHoraInicio);
                EditText txtMinuto = (EditText) findViewById(R.id.txtMinutoInicio);
                EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
                EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);

                String diaSemana = txtDiaSemana.getItemAtPosition(txtDiaSemana.getSelectedItemPosition()).toString();
                String hora = txtHora.getText().toString();
                String minuto = txtMinuto.getText().toString();
                String horaFin = txtHoraFin.getText().toString();
                String minutoFin = txtMinutoFin.getText().toString();

                ListView lista = (ListView) findViewById(R.id.listaHorario);
                int pos = lista.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    Horario obj = (Horario) lista.getAdapter().getItem(pos);
                    StringBuffer error = new StringBuffer();
                    if (diaSemana == null || diaSemana.equals("")) {
                        error.append("- Dia semana \n");
                    }
                    if (hora == null || hora.equals("")) {
                        error.append("- Hora Inicio \n");
                    }
                    if (minuto == null || minuto.equals("")) {
                        error.append("- Minuto Inicio \n");
                    }
                    if (horaFin == null || horaFin.equals("")) {
                        error.append("- Hora Fin \n");
                    }
                    if (minutoFin == null || minutoFin.equals("")) {
                        error.append("- Minuto Fin \n");
                    }
                    if (error.length() > 0) {
                        Toast.makeText(HorariosActivity.this, "Debe seleccionar el(los) siguiente(s) campo(s): \n" + error.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        if ((Integer.parseInt(hora) > 23 || Integer.parseInt(minuto) > 59) || (Integer.parseInt(horaFin) > 23 || Integer.parseInt(minutoFin) > 59)) {
                            Toast.makeText(HorariosActivity.this, "La hora Inicio y Fin debe ser infeior a 24 horas y los minutos inferior a 60.", Toast.LENGTH_LONG).show();
                        } else {
                            obj.setDiaSemana(diaSemana);
                            obj.setHoraInicio(Integer.parseInt(hora));
                            obj.setMinutoInicio(Integer.parseInt(minuto));
                            obj.setHoraFin(Integer.parseInt(horaFin));
                            obj.setMinutoFin(Integer.parseInt(minutoFin));

                            if (dao.updateHorario(obj)) {
                                Toast.makeText(HorariosActivity.this, "Horario actualizado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                reset();
                                refrescar();
                            } else {
                                Toast.makeText(HorariosActivity.this, "Horario no actualizado ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(HorariosActivity.this, "Debe seleccionar al menos un horario ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void eliminar(Horario obj) {
        if (dao.deleteHorario(obj)) {
            Toast.makeText(HorariosActivity.this, "Se elimino satisfactoriamente", Toast.LENGTH_SHORT).show();
            reset();
            refrescar();
        } else {
            Toast.makeText(HorariosActivity.this, "No se elimino el horario ", Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarClicksHoras() {
        EditText txtHora = (EditText) findViewById(R.id.txtHoraInicio);
        EditText txtMinuto = (EditText) findViewById(R.id.txtMinutoInicio);
        final EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
        EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);
        txtHora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TimePickerDialog objeto =
                        new TimePickerDialog(HorariosActivity.this, t, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                true);
                objeto.setTitle("Hora Inicio");
                objeto.show();
                hora = true;
            }

        });
        txtMinuto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TimePickerDialog objeto =
                        new TimePickerDialog(HorariosActivity.this, t, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                true);
                objeto.setTitle("Hora Inicio");
                objeto.show();
                hora = true;
            }

        });
        txtHoraFin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TimePickerDialog objeto =
                        new TimePickerDialog(HorariosActivity.this, t, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                true);
                objeto.setTitle("Hora Fin");
                objeto.show();
                hora = false;
            }

        });
        txtMinutoFin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TimePickerDialog objeto =
                        new TimePickerDialog(HorariosActivity.this, t, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                true);
                objeto.setTitle("Hora Fin");
                objeto.show();
                hora = false;
            }

        });
    }


    private void reset() {
        Spinner txtDiaSemana = (Spinner) findViewById(R.id.spinerDiaSemana);
        EditText txtHora = (EditText) findViewById(R.id.txtHoraInicio);
        EditText txtMinuto = (EditText) findViewById(R.id.txtMinutoInicio);
        EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
        EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);

        txtDiaSemana.setSelection(0);
        txtHora.setText("");
        txtMinuto.setText("");
        txtHoraFin.setText("");
        txtMinutoFin.setText("");
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            if (hora) {
                EditText txtHora = (EditText) findViewById(R.id.txtHoraInicio);
                txtHora.setText(hourOfDay + "");
                EditText txtMinuto = (EditText) findViewById(R.id.txtMinutoInicio);
                txtMinuto.setText(minute + "");
            } else {
                EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
                txtHoraFin.setText(hourOfDay + "");
                EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);
                txtMinutoFin.setText(minute + "");
            }
        }
    };

    private void refrescar() {
        final ListView lista = (ListView) findViewById(R.id.listaHorario);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final Spinner diaSemana = (Spinner) findViewById(R.id.spinerDiaSemana);
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DataInfo.getDiasSemana());
        diaSemana.setAdapter(adapterSpinner);

        final ArrayAdapter<Horario> adapter = new ArrayAdapter<Horario>(this,
                android.R.layout.simple_list_item_single_choice,
                dao.getAllHorarios());
        lista.setAdapter(adapter);
        lista.setTextFilterEnabled(true);
        final EditText filtroHorario = (EditText) findViewById(R.id.filtroHorario);
        filtroHorario.addTextChangedListener(new TextWatcher() {
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

                EditText txtHora = (EditText) findViewById(R.id.txtHoraInicio);
                EditText txtMinuto = (EditText) findViewById(R.id.txtMinutoInicio);
                EditText txtHoraFin = (EditText) findViewById(R.id.txtHoraFin);
                EditText txtMinutoFin = (EditText) findViewById(R.id.txtMinutoFin);

                Horario obj = (Horario) lista.getAdapter().getItem(position);
                diaSemana.setSelection(posicionDiaSemana(obj.getDiaSemana()));
                txtHora.setText("" + obj.getHoraInicio());
                txtMinuto.setText("" + obj.getMinutoInicio());
                txtHoraFin.setText("" + obj.getHoraFin());
                txtMinutoFin.setText("" + obj.getMinutoFin());
            }
        });
    }

    public int posicionDiaSemana(String diaSemana) {
        int numero = 0;
        for (int i = 0; i < DataInfo.getDiasSemana().size(); i++) {
            if (DataInfo.getDiasSemana().get(i).equals(diaSemana)) {
                numero = i;
                break;
            }
        }
        return numero;
    }
}
