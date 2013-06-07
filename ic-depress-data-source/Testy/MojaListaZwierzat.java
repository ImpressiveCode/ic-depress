public class MojaListaZwierzat { 
  private Zwierze[] zwierzeta = new Zwierze[5];
  private int nastepnyIndx = 0;

  public void dodaj(Zwierze z) {
    if (nastepnyIndx < zwierzeta.length) {
      zwierzeta[nastepnyIndx] = z;
      System.out.println("Zwierze dodano na pozycji nr. " + nastepnyIndx);
      nastepnyIndx++;
    }
  }
}

