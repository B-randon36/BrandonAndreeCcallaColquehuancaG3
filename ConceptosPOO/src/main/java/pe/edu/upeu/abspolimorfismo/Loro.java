package pe.edu.upeu.abspolimorfismo;

public class Loro  extends Animal{
    @Override
    void emitirSonido() {
        System.out.println("cuak");
    }

    @Override
    void dormir() {
        //super.dormir();
        System.out.println("ZZzZzZzZZ");

    }

}
