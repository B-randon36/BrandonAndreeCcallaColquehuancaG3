package pe.edu.upeu.claseinterface;

public class Clasegeneral {
    public static void main(String[] args) {
        Animal a= new Loro();
        a.emitirSonido();
        a.dormir();

        a=new gato();
        a.emitirSonido();
        a.dormir();

    }
}
