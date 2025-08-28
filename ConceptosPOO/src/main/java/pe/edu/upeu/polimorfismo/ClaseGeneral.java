package pe.edu.upeu.polimorfismo;

import pe.edu.upeu.claseinterface.gato;

public class ClaseGeneral {
    public static void main(String[] args) {
        Gato g=new Gato();
        g.sonidoAnimal();

        Loro l=new Loro();
        l.sonidoAnimal();
    }
}
