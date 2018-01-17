package de.kintel.ki.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Behandelt Ein- und Ausgaben.
 * @author czoeller, atrogisch
 * Erstellt am: 02.12.2014
 * Erstell von: czoeller, atrogisch
 * Verändert am:
 * Verändert von:
 */
public class IOUtil {
    
    /** The reader for console inputs  */ 
    private BufferedReader br = null;
    
    /** Initialisierungskonstruktor */
    public IOUtil() {
        br = new BufferedReader( new InputStreamReader( System.in ) );
    }

    /**
     * Erstellt am: 03.11.2014
     * Erstellt von: czoeller, atrogisch
     * Verändert am:
     * Verändert von:
     * Reads in a line from the console.
     * @return String input
     */
    private String read() {
        String input = "";
        try {
            input = br.readLine();
        } catch (IOException ex) {
            ausgabe( ex );
        }
        return input;
    }
    
    private String read(String beschreibung) {
        ausgabe(beschreibung);
        return read();
    }
    
    private String read(String beschreibung, String current) {
        ausgabe(beschreibung + "[" + current + "]");
        String read = read();
        if( read.isEmpty() ) {
            return current;
        }
        return read;
    }
    
    /**
     * Liest eine Zahl ein.
     * Erstellt am: 02.12.2014
     * Erstellt von: czoeller, atrogisch
     * Verändert am:
     * Verändert von:
     * @param beschreibung Beschreibung für die einzugebende Zahl
     * @return zahl
     */
    private double readNumber( String beschreibung ) {
        boolean valid = false;
        double input = 0.0;
        do {
            try {
                ausgabe(beschreibung);
                input = Integer.parseInt( read() );
                valid = true;
            } catch (NumberFormatException e) {
                ausgabe("Fehlerhafte Eingabe! Bitte nur Zahlen eingeben!");
            } catch (IllegalArgumentException ia) {
                ausgabe( ia.getMessage() );
            }
        } while ( !valid );
        return input;
    }
    
    /**
     * Liest eine Zahl in einem bestimmten Wertebereich ein.
     * Erstellt am: 02.12.2014
     * Erstellt von: czoeller, atrogisch
     * Verändert am:
     * Verändert von:
     * @param beschreibung Beschreibung für die einzugebende Zahl
     * @param min Minimum
     * @param max Maximum
     * @return zahl
     */
    public double readNumberBetween( String beschreibung, int min, int max ) {
        boolean valid = false;
        double input;
        do {
            input = readNumber( beschreibung );
            if( input < min ) {
                ausgabe("Fehlerhafte Eingabe! Bitte mindestens " + min + " eingeben!");
            } else if( input > max ) {
                ausgabe("Fehlerhafte Eingabe! Bitte höchstens " + max + " eingeben!");
            } else {
                valid = true;
            }
        } while ( !valid );
        return input;
    }
    
    /**
     * Checks if user wants to abort.
     * Erstellt am: 03.11.2014
     * Erstellt von: czoeller, atrogisch
     * Verändert am:
     * Verändert von:
     * Returns a Bruch.
     * @return true if user wants to abort
     */
    public boolean eingabe_abbruch() {
        ausgabe("Weiteren Eingaben? [y oder Abbruch mit Return]");
        String input = read();
        return input.isEmpty() || !input.equalsIgnoreCase("y");
    }
    

     /**
     * Prints a message.
     * Erstellt am: 03.11.2014
     * Erstellt von: czoeller, atrogisch
     * Verändert am:
     * Verändert von:
     * @param message a message
     */
    public <T> void ausgabe(T message) {
        System.out.println(message.toString());
    }
    
    public int readMainMenu() {
        printMainMenu("KI spielt auf Farbe: ");
        return (int) readNumberBetween("", 1, 2);
    }
    
    private void printMainMenu(String beschreibung) {
        ausgabe(beschreibung);
        ausgabe("1) schwarz");
        ausgabe("2) weiß");
    }
    
}
