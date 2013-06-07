public class MojaListaZwierzatTester { 
  public static void main(String[] args) {
    MojaListaZwierzat lista = new MojaListaZwierzat();
    Pies p = new Pies();
    Kot k = new Kot();
    lista.dodaj(p);
    lista.dodaj(k);
  }
}