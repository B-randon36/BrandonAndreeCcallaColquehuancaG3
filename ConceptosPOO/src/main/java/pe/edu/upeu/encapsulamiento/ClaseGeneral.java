package pe.edu.upeu.encapsulamiento;

public class ClaseGeneral {
    public static void main (String[] args){
        Persona p = new Persona();
        p.setNombre ("Brandon");//encapsulamiento
        p.setEdad (17); // encapsulamiento
        p.apellido ="Ccalla";
        //no es esta aplicando encapsulamiento

        p.saluda();

        Trabajador t = new Trabajador(); //t=objeto
        t.setNombre ("Brandon");
        t.setEdad (17);
        t.setApellido("Ccalla");
        t.setArea("Ingenieria de sistemas");
        t.setGenero('M');
        System.out.println(t);

    }
}
