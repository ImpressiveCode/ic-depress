public class EnumTest {
    public enum Kolor implements Runnable {
 
        CZERWONY(false), 
        ZIELONY(true), 
        NIEBIESKI(true);
 
        boolean ladny;
 
        private Kolor(boolean czyLadny) {
            ladny = czyLadny;
        }
 
        @Override
        public String toString() {
            String poprzedniaNazwa = super.toString();
            String nowaNazwa = poprzedniaNazwa.toLowerCase();
            return nowaNazwa;
        }
 
        @Override
        public void run() {
            System.out.println(czyLadny(this));
        }
    }
 
    public static void main(String[] args) {
 
        for(Runnable kolor: Kolor.values()) {
            kolor.run();
        }   
    }
 
    public static String czyLadny(Kolor kolor) {
        String czyLadny = (kolor.ladny) ? "ladny" : "brzydki";
 
        return "Kolor "+kolor.toString()+" jest "+czyLadny;
    }
 
}


//Czytaj wiêcej na: http://javastart.pl/zaawansowane-programowanie/enum/#ixzz2Q6oOd6wP