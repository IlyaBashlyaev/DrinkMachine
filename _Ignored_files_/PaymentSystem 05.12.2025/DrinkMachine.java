
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 * ============================================================= DRINK MACHINE
 * ============================================================= - Zweck:
 * Robuster, konsolenbasierter Getränkeautomat für 6 Drinks - Features: •
 * Saubere Eingabeprüfung (Menüauswahl, Geldbeträge) • Abbruch jederzeit via
 * 'q', 'quit' oder 'abbruch' • BigDecimal für Preise/Geld (vermeidet
 * Fließkommafehler) • Ausführliche Nutzerhinweise & Fehlermeldungen
 *
 * Warum BigDecimal? - double/float sind binär und führen bei Geld zu
 * Rundungsfehlern. - BigDecimal mit Scale=2 und RoundingMode.HALF_UP ist
 * Standardpraxis.
 *
 * Datei-Layout: - Öffentliche Klasse DrinkMachine (Einstiegspunkt mit main) -
 * Paketlokale Klasse Drink (Datencontainer + einfache Logik)
 */
public class DrinkMachine {

    // Liste aller verfügbaren Getränke
    private final List<Drink> drinks = new ArrayList<>();

    // Ein Scanner für alle Konsoleneingaben (System.in NICHT schließen!)
    private final Scanner scanner = new Scanner(System.in);

    // Landesformat für Währung (Deutsch, Euro mit Komma als Dezimaltrenner)
    private final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    //new Payment System
    private final PaymentSystem paymentSystem = new PaymentSystem(new CashPayment());

    /**
     * Konstruktor: Befüllt den Automaten mit 6 Dummy-Getränken. Preisvorgaben:
     * 1× 2€, 3× 3€, 2× 4€. Zusätzlich je ein Beispiel-Bestand.
     */
    public DrinkMachine() {
        drinks.add(new Drink("AquaPlus", bd("2.00"), 8)); // 1× 2 €
        drinks.add(new Drink("FizzCola", bd("3.00"), 5)); // 3× 3 €
        drinks.add(new Drink("OrangePop", bd("3.00"), 5));
        drinks.add(new Drink("MateMax", bd("3.00"), 5));
        drinks.add(new Drink("EnergyX", bd("4.00"), 4)); // 2× 4 €
        drinks.add(new Drink("ColdBrew", bd("4.00"), 3));
    }

    /**
     * Einstiegspunkt des Programms.
     */
    public static void main(String[] args) {
        new DrinkMachine().run();
    }

    /**
     * Hauptprogramm-Schleife: 1) Menü anzeigen 2) Auswahl einlesen (mit
     * Wiederholung/Validierung/Abbruch) 3) Falls verfügbar: Geld einnehmen (bis
     * Preis erreicht oder Abbruch) 4) Ware ausgeben, Rückgeld berechnen,
     * Bestand reduzieren 5) Zurück zum Menü
     */
    private void run() {
        printlnHeader();

        // Endlosschleife – wird nur durch Nutzerabbruch ('q') verlassen
        while (true) {
            showMenu();

            // Auswahl lesen; null bedeutet: Benutzer will beenden
            Integer choice = promptSelectionOrQuit();
            if (choice == null) {
                System.out.println("\nAuf Wiedersehen!");
                break;
            }

            // Index in der Liste bestimmen (Menü beginnt bei 1)
            int idx = choice - 1;
            Drink d = drinks.get(idx);

            // Verfügbarkeitsprüfung (Bestand > 0)
            if (!d.isAvailable()) {
                System.out.println("Hinweis: \"" + d.getName() + "\" ist leider ausverkauft.");
                continue; // Zurück zum Menü
            }

            boolean success = paymentSystem.pay(d.getPrice());

            if (!success) {
                System.out.println("Zahlung fehlgeschlagen.\n");
            }

        }
    }

    // ------------------------------------------------------------------------
    // UI / Konsoleninteraktion
    // ------------------------------------------------------------------------
    /**
     * Begrüßung und Eingabehinweise einmalig ausgeben.
     */
    private void printlnHeader() {
        System.out.println("====================================");
        System.out.println("           DRINK MACHINE v1         ");
        System.out.println("====================================\n");
        System.out.println("Eingaben:");
        System.out.println("- Menü: Nummer des Getränks eingeben");
        System.out.println("- Geld: Betrag in Euro, z. B. 2 oder 1,50 (Komma/Punkt möglich)");
        System.out.println("- Abbruch jederzeit mit: q | abbruch | quit\n");
    }

    /**
     * Aktuelles Menü mit allen Getränken, Preisen und Beständen anzeigen.
     * AUSVERKAUFT wird deutlich gekennzeichnet.
     */
    private void showMenu() {
        List<Product> products = productCatalog.allProducts();
        System.out.println("=== Auswahl ===");
        for (int i = 0; i < drinks.size(); i++) {
            Drink d = drinks.get(i);
            String line = String.format(
                    "%d) %-12s  Preis: %s  %s",
                    i + 1,
                    d.getName(),
                    fmt(d.getPrice()),
                    d.isAvailable() ? "(Bestand: " + d.getStock() + ")" : "(AUSVERKAUFT)"
            );
            System.out.println(line);
        }
        System.out.println("q) Beenden");
    }

    /**
     * Fragt eine Menüauswahl ab. Rückgabe: - Zahl zwischen 1 und drinks.size()
     * - null, wenn Nutzer 'q'/'quit'/'abbruch' eingibt (aktiver Abbruch)
     *
     * Validierung: - Nur Ziffern erlaubt (RegEx "\\d+") - Bereichsprüfung -
     * Fehler führen zu erneuter Eingabeaufforderung
     */
    private Integer promptSelectionOrQuit() {
        int productCount = productCatalog.allProducts().size();

        while (true) {
            System.out.print("\nBitte Auswahl eingeben (1-" + drinks.size() + ", oder 'q'): ");
            String in = readLine();

            // Aktiver Abbruch
            if (isQuit(in)) {
                return null;
            }

            // Versuch, einen positiven Integer zu parsen
            Optional<Integer> maybeInt = tryParsePositiveInt(in);
            if (!maybeInt.isPresent()) {
                System.out.println("Ungültige Eingabe. Bitte eine Zahl zwischen 1 und " + drinks.size() + " eingeben.");
                continue; // Nochmal probieren
            }

            int value = maybeInt.get();

            // Bereichsprüfung (Menü beginnt bei 1)
            if (value < 1 || value > drinks.size()) {
                System.out.println("Ungültige Auswahl. Bitte 1-" + drinks.size() + " wählen.");
                continue;
            }

            return value; // gültige Auswahl
        }
    }
    /**
     * Fragt so lange Geldbeträge ab, bis der Preis des gewählten Getränks
     * erreicht ist, oder der Nutzer aktiv abbricht.
     *
     * Rückgabe: - Summe der eingezahlten Beträge (>= Preis) - null bei aktivem
     * Abbruch ('q'/'quit'/'abbruch')
     *
     * Validierung: - Erlaubt sind Ganzzahlen (z. B. "2") oder Dezimalzahlen mit
     * 1–2 Nachkommastellen (z. B. "1,5" oder "1.50"). RegEx:
     * "\\d+(?:[.,]\\d{1,2})?" - Komma oder Punkt als Dezimaltrenner (wird
     * einheitlich in '.' umgewandelt) - Negative oder 0-Beträge werden
     * abgelehnt
     */

    // ------------------------------------------------------------------------
    // Parsing / Validierungs-Hilfen
    // ------------------------------------------------------------------------
    /**
     * Parst einen positiven Integer (nur Ziffern, kein Vorzeichen).
     *
     * @param in Rohstring aus der Konsole
     * @return Optional.of(Integer) bei Erfolg, sonst Optional.empty()
     */
    private Optional<Integer> tryParsePositiveInt(String in) {
        String s = in.trim();

        // RegEx: eine oder mehr Ziffern (keine Minuszeichen, keine Leerzeichen)
        if (!s.matches("\\d+")) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.valueOf(s));
        } catch (NumberFormatException e) {
            // Sehr große Zahlen würden hier landen
            return Optional.empty();
        }
    }
    /**
     * Parst einen Geldbetrag mit optionalen Nachkommastellen (1–2), akzeptiert
     * Komma ODER Punkt als Dezimaltrenner.
     *
     * RegEx-Erklärung: \\d+ -> mind. eine Ziffer (Ganzzahlteil)
     * (?:[.,]\\d{1,2})? -> optional: Komma/Punkt + 1..2 Ziffern (Nachkomma)
     *
     * Beispiele gültig: "2", "2.0", "2.00", "1,5", "1,50" Beispiele ungültig:
     * "2.", "1,234", "-1", "abc", "1,2,3"
     */

    // ------------------------------------------------------------------------
    // Kleinere Utility-Methoden
    // ------------------------------------------------------------------------
    /**
     * Formatiert BigDecimal als Euro im deutschen Locale (z. B. 1,50 €).
     */
    private String fmt(BigDecimal amount) {
        return CURRENCY.format(amount);
    }

    /**
     * Prüft auf Abbruchbefehle (klein/ groß egal).
     */
    private boolean isQuit(String in) {
        String s = in.trim().toLowerCase(Locale.ROOT);
        return s.equals("q") || s.equals("quit") || s.equals("abbruch");
    }

    /**
     * Sicheres Einlesen einer Zeile. Scanner.nextLine() kann in seltenen Fällen
     * Exceptions werfen, daher hier mit try/catch abgesichert. Rückgabe dann
     * leere Zeichenkette.
     */
    private String readLine() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Komfortfunktion: Erzeugt BigDecimal mit Scale=2 und HALF_UP. Erwartet
     * einen String im Punkt-Format ("2.00", "3.00", ...).
     */
    private static BigDecimal bd(String s) {
        return new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
    }
}
