import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import payment_system.CashPayment;
import payment_system.PaymentSystem;
import product.Product;
import product.ProductCatalog;
import product.ProductInfo;

/**
 * Konsolenbasierter Automat mit Snacks und Drinks in einem Modus.
 */
public class DrinkMachine {
    private final Map<MachineType, ProductCatalog> machineCatalogs = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentSystem paymentSystem = new PaymentSystem(new CashPayment());

    public DrinkMachine() {
        seedCatalogs();
    }

    /** Einstiegspunkt des Programms. */
    public static void main(String[] args) {
        new DrinkMachine().run();
    }

    private void seedCatalogs() {
        machineCatalogs.put(MachineType.SNACKAUTOMAT, new ProductCatalog());
        machineCatalogs.put(MachineType.GEMISCHTER_AUTOMAT, new ProductCatalog());

        ProductCatalog snackCatalog = machineCatalogs.get(MachineType.SNACKAUTOMAT);
        snackCatalog.registerProduct(new Product("Merkur Riegel", bd("2.00"), 20, bd("50"), "g", 250, bd("23.5"), bd("12.5"), bd("7.5"), 0));
        snackCatalog.registerProduct(new Product("Saturn Ringe", bd("5.00"), 15, bd("75"), "g", 255, bd("34.5"), bd("0.15"), bd("0.08"), 0));
        snackCatalog.registerProduct(new Product("Lakritzstangen", bd("2.30"), 25, bd("25"), "g", 88, bd("8.8"), bd("0.13"), bd("0.03"), 0));
        snackCatalog.registerProduct(new Product("DoupleChoc Riegel", bd("3.00"), 20, bd("55"), "g", 286, bd("26.4"), bd("15.4"), bd("9.4"), 0));
        snackCatalog.registerProduct(new Product("Silberbaeren Tuete", bd("7.20"), 15, bd("100"), "g", 343, bd("47.0"), bd("0.5"), bd("0.1"), 0));

        ProductCatalog mixedCatalog = machineCatalogs.get(MachineType.GEMISCHTER_AUTOMAT);
        mixedCatalog.registerProduct(new Product("NeptunDrink", bd("2.50"), 20, bd("330"), "ml", 139, bd("35.0"), bd("0.0"), bd("0.0"), 0));
        mixedCatalog.registerProduct(new Product("Saturn Ringe", bd("5.00"), 15, bd("75"), "g", 255, bd("34.5"), bd("0.15"), bd("0.08"), 0));
        mixedCatalog.registerProduct(new Product("Lakritzstangen", bd("2.30"), 25, bd("25"), "g", 88, bd("8.8"), bd("0.13"), bd("0.03"), 0));
        mixedCatalog.registerProduct(new Product("AquaPlus", bd("2.00"), 10, bd("500"), "ml", 0, bd("0.0"), bd("0.0"), bd("0.0"), 0));
        mixedCatalog.registerProduct(new Product("EnergyX", bd("4.00"), 15, bd("250"), "ml", 113, bd("27.5"), bd("0.0"), bd("0.0"), 80));
        mixedCatalog.registerProduct(new Product("Kaffee (schwarz)", bd("2.50"), 30, bd("200"), "ml", 2, bd("0.0"), bd("0.0"), bd("0.0"), 95));
        mixedCatalog.registerProduct(new Product("Espresso", bd("3.50"), 15, bd("30"), "ml", 3, bd("0.0"), bd("0.0"), bd("0.0"), 63));
        mixedCatalog.registerProduct(new Product("Cappuccino", bd("5.00"), 15, bd("200"), "ml", 120, bd("6.0"), bd("5.0"), bd("3.4"), 75));
    }

    /**
     * Hauptprogramm-Schleife:
     * 1) Automaten-Typ waehlen
     * 2) Menue des Automaten anzeigen
     * 3) Auswahl einlesen (mit Wiederholung/Validierung/Abbruch)
     * 4) Falls verfuegbar: Geld einnehmen (bis Preis erreicht oder Abbruch)
     * 5) Ware ausgeben, Rueckgeld berechnen, Bestand reduzieren
     * 6) Zurueck zur Automatenauswahl
     */
    private void run() {
        printlnHeader();

        while (true) {
            MachineType machine = promptMachineSelection();
            if (machine == null) {
                System.out.println("\nAuf Wiedersehen!");
                break;
            }

            ProductCatalog productCatalog = machineCatalogs.get(machine);
            showMenu(productCatalog, machine);

            Integer choice = promptSelectionOrQuit(productCatalog);
            if (choice == null) {
                System.out.println("\nAuf Wiedersehen!");
                break;
            }

            int idx = choice - 1;
            Product product = productCatalog.allProducts().get(idx);

            if (!product.isAvailable()) {
                System.out.println("Hinweis: \"" + product.getName() + "\" ist leider ausverkauft.");
                continue;
            }

            printNutrition(product);

            BigDecimal totalPaid = promptMoneyUntilPriceOrQuit(product);
            if (totalPaid == null) {
                System.out.println("Vorgang abgebrochen.\n");
                continue;
            }

            boolean success = paymentSystem.pay(totalPaid);
            if (!success) {
                System.out.println("Zahlung fehlgeschlagen.\n");
                continue;
            }

            product.dispenseOne();
            BigDecimal change = totalPaid.subtract(product.getPrice());
            System.out.println("\nDanke! Bitte entnehmen Sie: " + product.getName());
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Rueckgeld: " + fmt(change));
            }
            System.out.println("Verbleibender Bestand von \"" + product.getName() + "\": " + product.getStock());
            System.out.println();
        }
    }

    private void printlnHeader() {
        System.out.println("====================================");
        System.out.println("           DRINK MACHINE v2         ");
        System.out.println("====================================\n");
        System.out.println("Eingaben:");
        System.out.println("- Zuerst Automaten-Typ waehlen");
        System.out.println("- Menue: Nummer des Produkts eingeben");
        System.out.println("- Geld: Betrag in Euro, z. B. 2 oder 1,50 (Komma/Punkt moeglich)");
        System.out.println("- Abbruch jederzeit mit: q | abbruch | quit\n");
    }

    /** Aktuelles Menue mit allen Produkten, Preisen und Bestaenden anzeigen. */
    private void showMenu(ProductCatalog productCatalog, MachineType machine) {
        List<Product> products = productCatalog.allProducts();
        System.out.println("\n=== " + machine.getDisplayName() + " ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            String line = String.format(
                    "%d) %-18s Preis: %s  %s%s",
                    i + 1,
                    product.getName(),
                    fmt(product.getPrice()),
                    product.isAvailable() ? "(Bestand: " + product.getStock() + ")" : "(AUSVERKAUFT)",
                    nutritionSummary(product.getInfo())
            );
            System.out.println(line);
        }
        System.out.println("q) Beenden");
    }

    /**
     * Fragt eine Menueauswahl ab.
     * Rueckgabe:
     *  - Zahl zwischen 1 und Anzahl der Produkte
     *  - null, wenn Nutzer 'q'/'quit'/'abbruch' eingibt (aktiver Abbruch)
     */
    private Integer promptSelectionOrQuit(ProductCatalog productCatalog) {
        int productCount = productCatalog.allProducts().size();

        while (true) {
            System.out.print("Bitte Auswahl eingeben (1-" + productCount + ", oder 'q'): ");
            String in = readLine();

            if (isQuit(in)) return null;

            Optional<Integer> maybeInt = tryParsePositiveInt(in);
            if (!maybeInt.isPresent()) {
                System.out.println("Ungueltige Eingabe. Bitte eine Zahl zwischen 1 und " + productCount + " eingeben.");
                continue;
            }

            int value = maybeInt.get();
            if (value < 1 || value > productCount) {
                System.out.println("Ungueltige Auswahl. Bitte 1-" + productCount + " waehlen.");
                continue;
            }

            return value;
        }
    }

    /**
     * Fragt so lange Geldbetraege ab, bis der Preis des gewaehlten Produkts erreicht ist,
     * oder der Nutzer aktiv abbricht.
     *
     * Rueckgabe:
     *  - Summe der eingezahlten Betraege (>= Preis)
     *  - null bei aktivem Abbruch ('q'/'quit'/'abbruch')
     */
    private BigDecimal promptMoneyUntilPriceOrQuit(Product product) {
        System.out.println("\nAusgewaehlt: " + product.getName() + " (" + fmt(product.getPrice()) + ")");
        BigDecimal total = BigDecimal.ZERO;

        while (total.compareTo(product.getPrice()) < 0) {
            BigDecimal missing = product.getPrice().subtract(total);
            System.out.print("Bitte Betrag eingeben (fehlend: " + fmt(missing) + ", 'q' fuer Abbruch): ");
            String in = readLine();

            if (isQuit(in)) return null;

            Optional<BigDecimal> maybe = tryParseMoney(in);
            if (!maybe.isPresent()) {
                System.out.println("Ungueltige Eingabe. Bitte eine Zahl im Format z. B. 2, 2.00 oder 1,50 eingeben.");
                continue;
            }

            BigDecimal value = maybe.get();
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Bitte einen Betrag > 0 eingeben.");
                continue;
            }

            total = total.add(value);
            System.out.println("Bisher eingezahlt: " + fmt(total));
        }

        return total;
    }

    private Optional<Integer> tryParsePositiveInt(String in) {
        String s = in.trim();

        if (!s.matches("\\d+")) return Optional.empty();

        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> tryParseMoney(String in) {
        String s = in.trim();

        if (!s.matches("\\d+(?:[.,]\\d{1,2})?")) return Optional.empty();

        s = s.replace(',', '.');

        try {
            BigDecimal val = new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
            if (val.compareTo(BigDecimal.ZERO) < 0) return Optional.empty();
            return Optional.of(val);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String nutritionSummary(ProductInfo info) {
        if (info == null) return "";
        return " | kcal/Portion: " + info.getKcalPerPortion() + ", Koffein: " + info.getCaffeineMgPerPortion() + "mg";
    }

    private void printNutrition(Product product) {
        ProductInfo info = product.getInfo();
        if (info == null) return;

        System.out.println("\nProdukt: " + product.getName());
        System.out.println("Naehrwerte pro Portion (" + strip(info.getPortionSize()) + info.getPortionUnit() + "):");
        System.out.println("  kcal: " + info.getKcalPerPortion());
        System.out.println("  Zucker: " + strip(info.getSugarPerPortion()) + " g");
        System.out.println("  Fett: " + strip(info.getFatPerPortion()) + " g");
        System.out.println("  ges. Fett: " + strip(info.getSaturatedFatPerPortion()) + " g");
        System.out.println("  Koffein: " + info.getCaffeineMgPerPortion() + " mg");
    }

    private String fmt(BigDecimal amount) {
        BigDecimal scaled = amount.setScale(2, RoundingMode.HALF_UP);
        String plain = scaled.toPlainString().replace('.', ',');
        return plain + " EUR";
    }

    private MachineType promptMachineSelection() {
        List<MachineType> machines = new ArrayList<>(machineCatalogs.keySet());

        System.out.println("Bitte Automaten auswaehlen:");
        for (int i = 0; i < machines.size(); i++) {
            System.out.println((i + 1) + ") " + machines.get(i).getDisplayName());
        }
        System.out.println("q) Beenden");

        while (true) {
            System.out.print("Bitte Auswahl eingeben (1-" + machines.size() + ", oder 'q'): ");
            String input = readLine();

            if (isQuit(input)) return null;

            Optional<Integer> maybe = tryParsePositiveInt(input);
            if (!maybe.isPresent()) {
                System.out.println("Ungueltige Eingabe. Bitte erneut versuchen.");
                continue;
            }

            int choice = maybe.get();
            if (choice < 1 || choice > machines.size()) {
                System.out.println("Ungueltige Auswahl. Bitte 1-" + machines.size() + " waehlen.");
                continue;
            }

            return machines.get(choice - 1);
        }
    }

    private boolean isQuit(String in) {
        String s = in.trim().toLowerCase(Locale.ROOT);
        return s.equals("q") || s.equals("quit") || s.equals("abbruch");
    }

    private String readLine() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
    }

    private static BigDecimal bd(String s) {
        return new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
    }

    private static String strip(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
