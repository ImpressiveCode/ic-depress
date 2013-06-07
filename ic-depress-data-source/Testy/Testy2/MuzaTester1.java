import javax.sound.midi.*;

public class MuzaTester1 {
  public void graj()  {
    try {
    	Sequencer sekwenser = MidiSystem.getSequencer();
    } catch (Exception e) {}
    System.out.println("Mamy sekwenser!");
  } // koniec graj

  public static void main(String[] args) {
    MuzaTester1 mt = new MuzaTester1();
    
    mt.graj();
  } // koniec main
} // koniec klasy
 