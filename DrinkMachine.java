import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 * =============================================================
 *  DRINK MACHINE
 * =============================================================
 * - Zweck: Robuster, konsolenbasierter Getränkeautomat für 6 Drinks
 * - Features:
 *    • Saubere Eingabeprüfung (Menüauswahl, Geldbeträge)
 *    • Abbruch jederzeit via 'q', 'quit' oder 'abbruch'
 *    • BigDecimal für Preise/Geld (vermeidet Fließkommafehler)
 *    • Ausführliche Nutzerhinweise & Fehlermeldungen
 *
 *  Warum BigDecimal?
 *   - double/float sind binär und führen bei Geld zu Rundungsfehlern.
 *   - BigDecimal mit Scale=2 und RoundingMode.HALF_UP ist Standardpraxis.
 *
 *  Datei-Layout:
 *   - Öffentliche Klasse DrinkMachine (Einstiegspunkt mit main)
 *   - Paketlokale Klasse Drink (Datencontainer + einfache Logik)
 */
public class DrinkMachine {

    // Katalog aller verfügbaren Produkte
    private final ProductCatalog productCatalog = new ProductCatalog();

    // Ein Scanner für alle Konsoleneingaben (System.in NICHT schließen!)
    private final Scanner scanner = new Scanner(System.in);

    // Landesformat für Währung (Deutsch, Euro mit Komma als Dezimaltrenner)
    private final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    /**
     * Konstruktor: Befüllt den Automaten mit 6 Dummy-Getränken.
     * Preisvorgaben: 1× 2€, 3× 3€, 2× 4€.
     * Zusätzlich je ein Beispiel-Bestand.
     */
    public DrinkMachine() {
        productCatalog.registerProduct(new Product("AquaPlus",  bd("2.00"), 8)); // 1× 2 €
        productCatalog.registerProduct(new Product("FizzCola",  bd("3.00"), 5)); // 3× 3 €
        productCatalog.registerProduct(new Product("OrangePop", bd("3.00"), 5));
        productCatalog.registerProduct(new Product("MateMax",   bd("3.00"), 5));
        productCatalog.registerProduct(new Product("EnergyX",   bd("4.00"), 4)); // 2× 4 €
        productCatalog.registerProduct(new Product("ColdBrew",  bd("4.00"), 3));
    }

    /** Einstiegspunkt des Programms. */
    public static void main(String[] args) {
        new DrinkMachine().run();
    }

    /**
     * Hauptprogramm-Schleife:
     * 1) Menü anzeigen
     * 2) Auswahl einlesen (mit Wiederholung/Validierung/Abbruch)
     * 3) Falls verfügbar: Geld einnehmen (bis Preis erreicht oder Abbruch)
     * 4) Ware ausgeben, Rückgeld berechnen, Bestand reduzieren
     * 5) Zurück zum Menü
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
            Product product = productCatalog.allProducts().get(idx);

            // Verfügbarkeitsprüfung (Bestand > 0)
            if (!product.isAvailable()) {
                System.out.println("Hinweis: \"" + product.getName() + "\" ist leider ausverkauft.");
                continue; // Zurück zum Menü
            }

            // Geld sammeln; null -> Vorgang abgebrochen
            BigDecimal totalPaid = promptMoneyUntilPriceOrQuit(product);
            if (totalPaid == null) {
                System.out.println("Vorgang abgebrochen.\n");
                continue;
            }

            // Kauf durchführen: Bestand mindern, Rückgeld berechnen, Ausgabe
            product.dispenseOne();
            BigDecimal change = totalPaid.subtract(product.getPrice());
            System.out.println("\nDanke! Bitte entnehmen Sie: " + product.getName());

            // Nur ausgeben, wenn tatsächlich Rückgeld > 0
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Rückgeld: " + fmt(change));
            }

            System.out.println("Verbleibender Bestand von \"" + product.getName() + "\": " + product.getStock());
            System.out.println(); // Leerzeile für Lesbarkeit
        }
    }

    // ------------------------------------------------------------------------
    // UI / Konsoleninteraktion
    // ------------------------------------------------------------------------

    /** Begrüßung und Eingabehinweise einmalig ausgeben. */
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
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            String line = String.format(
                    "%d) %-12s  Preis: %s  %s",
                    i + 1,
                    product.getName(),
                    fmt(product.getPrice()),
                    product.isAvailable() ? "(Bestand: " + product.getStock() + ")" : "(AUSVERKAUFT)"
            );
            System.out.println(line);
        }
        System.out.println("q) Beenden");
    }

    /**
     * Fragt eine Menüauswahl ab.
     * Rückgabe:
     *  - Zahl zwischen 1 und Anzahl der Produkte
     *  - null, wenn Nutzer 'q'/'quit'/'abbruch' eingibt (aktiver Abbruch)
     *
     * Validierung:
     *  - Nur Ziffern erlaubt (RegEx "\\d+")
     *  - Bereichsprüfung
     *  - Fehler führen zu erneuter Eingabeaufforderung
     */
    private Integer promptSelectionOrQuit() {
        int productCount = productCatalog.allProducts().size();

        while (true) {
            System.out.print("\nBitte Auswahl eingeben (1-" + productCount + ", oder 'q'): ");
            String in = readLine();

            // Aktiver Abbruch
            if (isQuit(in)) return null;

            // Versuch, einen positiven Integer zu parsen
            Optional<Integer> maybeInt = tryParsePositiveInt(in);
            if (maybeInt.isEmpty()) {
                System.out.println("Ungültige Eingabe. Bitte eine Zahl zwischen 1 und " + productCount + " eingeben.");
                continue; // Nochmal probieren
            }

            int value = maybeInt.get();

            // Bereichsprüfung (Menü beginnt bei 1)
            if (value < 1 || value > productCount) {
                System.out.println("Ungültige Auswahl. Bitte 1-" + productCount + " wählen.");
                continue;
            }

            return value; // gültige Auswahl
        }
    }

    /**
     * Fragt so lange Geldbeträge ab, bis der Preis des gewählten Getränks erreicht ist,
     * oder der Nutzer aktiv abbricht.
     *
     * Rückgabe:
     *  - Summe der eingezahlten Beträge (>= Preis)
     *  - null bei aktivem Abbruch ('q'/'quit'/'abbruch')
     *
     * Validierung:
     *  - Erlaubt sind Ganzzahlen (z. B. "2") oder Dezimalzahlen mit 1–2 Nachkommastellen
     *    (z. B. "1,5" oder "1.50"). RegEx: "\\d+(?:[.,]\\d{1,2})?"
     *  - Komma oder Punkt als Dezimaltrenner (wird einheitlich in '.' umgewandelt)
     *  - Negative oder 0-Beträge werden abgelehnt
     */
    private BigDecimal promptMoneyUntilPriceOrQuit(Product product) {
        System.out.println("\nAusgewählt: " + product.getName() + " (" + fmt(product.getPrice()) + ")");
        BigDecimal total = BigDecimal.ZERO;

        // Solange weiter einwerfen, bis total >= Preis
        while (total.compareTo(product.getPrice()) < 0) {
            BigDecimal missing = product.getPrice().subtract(total);
            System.out.print("Bitte Betrag eingeben (fehlend: " + fmt(missing) + ", 'q' für Abbruch): ");
            String in = readLine();

            // Aktiver Abbruch (z. B. Tippfehler oder Meinungsänderung)
            if (isQuit(in)) return null;

            // Versuch, einen gültigen Geldbetrag zu parsen
            Optional<BigDecimal> maybe = tryParseMoney(in);
            if (maybe.isEmpty()) {
                System.out.println("Ungültige Eingabe. Bitte eine Zahl im Format z. B. 2, 2.00 oder 1,50 eingeben.");
                continue;
            }

            BigDecimal value = maybe.get();

            // Keine Null- oder Negativbeträge akzeptieren
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Bitte einen Betrag > 0 eingeben.");
                continue;
            }

            // Betrag akzeptieren und zur Gesamtsumme addieren
            total = total.add(value);
            System.out.println("Bisher eingezahlt: " + fmt(total));
        }

        // Hier ist total >= Preis
        return total;
    }

    // ------------------------------------------------------------------------
    // Parsing / Validierungs-Hilfen
    // ------------------------------------------------------------------------

    /**
     * Parst einen positiven Integer (nur Ziffern, kein Vorzeichen).
     * @param in Rohstring aus der Konsole
     * @return Optional.of(Integer) bei Erfolg, sonst Optional.empty()
     */
    private Optional<Integer> tryParsePositiveInt(String in) {
        String s = in.trim();

        // RegEx: eine oder mehr Ziffern (keine Minuszeichen, keine Leerzeichen)
        if (!s.matches("\\d+")) return Optional.empty();

        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            // Sehr große Zahlen würden hier landen
            return Optional.empty();
        }
    }

    /**
     * Parst einen Geldbetrag mit optionalen Nachkommastellen (1–2),
     * akzeptiert Komma ODER Punkt als Dezimaltrenner.
     *
     * RegEx-Erklärung:
     *   \\d+                  -> mind. eine Ziffer (Ganzzahlteil)
     *   (?:[.,]\\d{1,2})?    -> optional: Komma/Punkt + 1..2 Ziffern (Nachkomma)
     *
     * Beispiele gültig: "2", "2.0", "2.00", "1,5", "1,50"
     * Beispiele ungültig: "2.", "1,234", "-1", "abc", "1,2,3"
     */
    private Optional<BigDecimal> tryParseMoney(String in) {
        String s = in.trim();

        if (!s.matches("\\d+(?:[.,]\\d{1,2})?")) return Optional.empty();

        // Einheitlicher Dezimaltrenner: Punkt
        s = s.replace(',', '.');

        try {
            // Scale=2 erzwingen; HALF_UP = kaufmännisches Runden
            BigDecimal val = new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);

            // negative Werte ausschließen
            if (val.compareTo(BigDecimal.ZERO) < 0) return Optional.empty();

            return Optional.of(val);
        } catch (NumberFormatException e) {
            // Sollte bei obiger RegEx selten passieren, aber sicher ist sicher
            return Optional.empty();
        }
    }

    // ------------------------------------------------------------------------
    // Kleinere Utility-Methoden
    // ------------------------------------------------------------------------

    /** Formatiert BigDecimal als Euro im deutschen Locale (z. B. 1,50 €). */
    private String fmt(BigDecimal amount) {
        return CURRENCY.format(amount);
    }

    /** Prüft auf Abbruchbefehle (klein/ groß egal). */
    private boolean isQuit(String in) {
        String s = in.trim().toLowerCase(Locale.ROOT);
        return s.equals("q") || s.equals("quit") || s.equals("abbruch");
    }

    /**
     * Sicheres Einlesen einer Zeile.
     * Scanner.nextLine() kann in seltenen Fällen Exceptions werfen,
     * daher hier mit try/catch abgesichert. Rückgabe dann leere Zeichenkette.
     */
    private String readLine() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Komfortfunktion: Erzeugt BigDecimal mit Scale=2 und HALF_UP.
     * Erwartet einen String im Punkt-Format ("2.00", "3.00", ...).
     */
    private static BigDecimal bd(String s) {
        return new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
    }
}
