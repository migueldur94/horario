package gestionhorario.aplicacionmovil.uts.horario.objetos;

/**
 * Created by Miguel on 16/04/2017.
 */

public class Variable {

    private int idVariable;
    private String nombre;
    private String descripcion;
    private String valor;

    public int getIdVariable() {
        return idVariable;
    }

    public void setIdVariable(int idVariable) {
        this.idVariable = idVariable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return  "nombre='" + nombre + '\'' +
                ", valor='" + valor + '\'' ;
    }
}
