class Pies { }

//-----------------------------------------------------

public class MojaListaPsow {
  private Pies[] psy = new Pies[5];
  private int nastepnyIndx = 0;

  public void dodaj(Pies p) {
    if (nastepnyIndx < psy.length) {
      psy[nastepnyIndx] = p;
      System.out.println("Pies dodany na pozycji nr. " + nastepnyIndx);
      nastepnyIndx++;
    }
  } 
}