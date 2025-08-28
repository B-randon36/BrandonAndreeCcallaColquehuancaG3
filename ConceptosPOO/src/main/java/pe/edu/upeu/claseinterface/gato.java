package pe.edu.upeu.claseinterface;

public class gato implements Animal {
    @Override
    public void emitirSonido() {
        System.out.println("tripi tropi");

    }

    @Override
    public void dormir() {
        System.out.println("gato waton");

    }
}
