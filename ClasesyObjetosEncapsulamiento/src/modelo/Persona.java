package modelo;

public class Persona {
    private String nombre;
    private int edad;
    private double talla;
    public Persona() {
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public int getEdad() {
        return edad;
    }
    public void setTalla(double talla) {
        this.talla = talla;
    }
    public double getTalla() {
        return talla;
    }
}