# DrinkMachine - Code-Verlaufsanalyse

## Übersicht

Diese Dokumentation zeigt den detaillierten Ablauf des DrinkMachine-Programms mit realitätsnahen Beispieleingaben und -ausgaben sowie Erklärungen der Code-Schnittstellen.

## Programmstart und Initialisierung

### 1. Hauptmethode (`main`)

```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=55
public static void main(String[] args) {
    new DrinkMachine().run();
}
```

**Ablauf:**
1. Erstellt eine neue Instanz der `DrinkMachine`-Klasse
2. Ruft sofort die `run()`-Methode auf

### 2. Konstruktor-Aufruf

```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=45
public DrinkMachine() {
    drinks.add(new Product("AquaPlus",  bd("2.00"), 8)); // 1× 2 €
    drinks.add(new Product("FizzCola",  bd("3.00"), 5)); // 3× 3 €
    drinks.add(new Product("OrangePop", bd("3.00"), 5));
    drinks.add(new Product("MateMax",   bd("3.00"), 5));
    drinks.add(new Product("EnergyX",   bd("4.00"), 4)); // 2× 4 €
    drinks.add(new Product("ColdBrew",  bd("4.00"), 3));
}
```

**Ablauf:**
1. Initialisiert die `drinks`-Liste
2. Erstellt 6 `Drink`-Objekte mit verschiedenen Preisen und Beständen
3. Jeder `Drink`-Konstruktor validiert die Eingaben:
   - Name nicht null oder leer
   - Preis >= 0
   - Bestand >= 0

## Programmausführung - Beispielszenarien

### Szenario 1: Erfolgreicher Kauf (AquaPlus)

#### Programmstart - Header ausgeben

**Methodenaufruf:** `printlnHeader()`

```
Ausgabe:
====================================
           DRINK MACHINE v1         
====================================

Eingaben:
- Menü: Nummer des Getränks eingeben
- Geld: Betrag in Euro, z. B. 2 oder 1,50 (Komma/Punkt möglich)
- Abbruch jederzeit mit: q | abbruch | quit

```

#### Menü anzeigen

**Methodenaufruf:** `showMenu()`

```
Ausgabe:
=== Auswahl ===
1) AquaPlus      Preis: 2,00 €  (Bestand: 8)
2) FizzCola      Preis: 3,00 €  (Bestand: 5)
3) OrangePop     Preis: 3,00 €  (Bestand: 5)
4) MateMax       Preis: 3,00 €  (Bestand: 5)
5) EnergyX       Preis: 4,00 €  (Bestand: 4)
6) ColdBrew      Preis: 4,00 €  (Bestand: 3)
q) Beenden
```

**Code-Erklärung:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=132
private void showMenu() {
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
```

#### Benutzerauswahl validieren

**Methodenaufruf:** `promptSelectionOrQuit()`

```
Ausgabe: Bitte Auswahl eingeben (1-6, oder 'q'): 
Eingabe: 1
```

**Code-Analyse:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=159
private Integer promptSelectionOrQuit() {
    while (true) {
        System.out.print("\nBitte Auswahl eingeben (1-" + drinks.size() + ", oder 'q'): ");
        String in = readLine();
        
        // Aktiver Abbruch
        if (isQuit(in)) return null;
        
        // Versuch, einen positiven Integer zu parsen
        Optional<Integer> maybeInt = tryParsePositiveInt(in);
        if (maybeInt.isEmpty()) {
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
```

**Validierungsprozess:**
1. `readLine()` - Sichere Eingabe
2. `isQuit(in)` - Prüfung auf Abbruchbefehle
3. `tryParsePositiveInt(in)` - Integer-Parsing mit RegEx `\\d+`
4. Bereichsprüfung (1-6)

#### Verfügbarkeitsprüfung

```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=86
if (!d.isAvailable()) {
    System.out.println("Hinweis: \"" + d.getName() + "\" ist leider ausverkauft.");
    continue; // Zurück zum Menü
}
```

**Drink.isAvailable() Methode:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/Drink.java start=41
public boolean isAvailable() { return stock > 0; }
```

#### Geld einsammeln

**Methodenaufruf:** `promptMoneyUntilPriceOrQuit(d)`

```
Ausgabe:
Ausgewählt: AquaPlus (2,00 €)
Bitte Betrag eingeben (fehlend: 2,00 €, 'q' für Abbruch): 
Eingabe: 2
Ausgabe: Bisher eingezahlt: 2,00 €
```

**Code-Analyse:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=200
private BigDecimal promptMoneyUntilPriceOrQuit(Drink d) {
    System.out.println("\nAusgewählt: " + d.getName() + " (" + fmt(d.getPrice()) + ")");
    BigDecimal total = BigDecimal.ZERO;
    
    // Solange weiter einwerfen, bis total >= Preis
    while (total.compareTo(d.getPrice()) < 0) {
        BigDecimal missing = d.getPrice().subtract(total);
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
```

**Geld-Parsing mit `tryParseMoney()`:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=271
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
```

**RegEx-Erklärung:** `\\d+(?:[.,]\\d{1,2})?`
- `\\d+` - Mindestens eine Ziffer (Ganzzahlteil)
- `(?:[.,]\\d{1,2})?` - Optional: Komma/Punkt + 1-2 Ziffern

#### Transaktion durchführen

```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=98
// Kauf durchführen: Bestand mindern, Rückgeld berechnen, Ausgabe
d.dispenseOne();
BigDecimal change = totalPaid.subtract(d.getPrice());
System.out.println("\nDanke! Bitte entnehmen Sie: " + d.getName());

// Nur ausgeben, wenn tatsächlich Rückgeld > 0
if (change.compareTo(BigDecimal.ZERO) > 0) {
    System.out.println("Rückgeld: " + fmt(change));
}

System.out.println("Verbleibender Bestand von \"" + d.getName() + "\": " + d.getStock());
```

**Ausgabe:**
```
Danke! Bitte entnehmen Sie: AquaPlus
Verbleibender Bestand von "AquaPlus": 7

```

**`dispenseOne()` Methode:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/Drink.java start=47
public void dispenseOne() {
    if (!isAvailable()) throw new IllegalStateException("Ausverkauft.");
    stock--;
}
```

### Szenario 2: Ungültige Eingaben und Validierung

#### Ungültige Menüauswahl

```
Ausgabe: Bitte Auswahl eingeben (1-6, oder 'q'): 
Eingabe: abc
Ausgabe: Ungültige Eingabe. Bitte eine Zahl zwischen 1 und 6 eingeben.

Ausgabe: Bitte Auswahl eingeben (1-6, oder 'q'): 
Eingabe: 9
Ausgabe: Ungültige Auswahl. Bitte 1-6 wählen.

Ausgabe: Bitte Auswahl eingeben (1-6, oder 'q'): 
Eingabe: 3
```

#### Ungültige Geldbeträge

```
Ausgabe: Bitte Betrag eingeben (fehlend: 3,00 €, 'q' für Abbruch): 
Eingabe: abc
Ausgabe: Ungültige Eingabe. Bitte eine Zahl im Format z. B. 2, 2.00 oder 1,50 eingeben.

Ausgabe: Bitte Betrag eingeben (fehlend: 3,00 €, 'q' für Abbruch): 
Eingabe: -1
Ausgabe: Ungültige Eingabe. Bitte eine Zahl im Format z. B. 2, 2.00 oder 1,50 eingeben.

Ausgabe: Bitte Betrag eingeben (fehlend: 3,00 €, 'q' für Abbruch): 
Eingabe: 0
Ausgabe: Bitte einen Betrag > 0 eingeben.

Ausgabe: Bitte Betrag eingeben (fehlend: 3,00 €, 'q' für Abbruch): 
Eingabe: 1,50
Ausgabe: Bisher eingezahlt: 1,50 €
```

### Szenario 3: Schrittweise Bezahlung mit Rückgeld

```
=== Auswahl ===
1) AquaPlus      Preis: 2,00 €  (Bestand: 7)
...
5) EnergyX       Preis: 4,00 €  (Bestand: 4)
...

Bitte Auswahl eingeben (1-6, oder 'q'): 5

Ausgewählt: EnergyX (4,00 €)
Bitte Betrag eingeben (fehlend: 4,00 €, 'q' für Abbruch): 2
Bisher eingezahlt: 2,00 €
Bitte Betrag eingeben (fehlend: 2,00 €, 'q' für Abbruch): 1.50
Bisher eingezahlt: 3,50 €
Bitte Betrag eingeben (fehlend: 0,50 €, 'q' für Abbruch): 1
Bisher eingezahlt: 4,50 €

Danke! Bitte entnehmen Sie: EnergyX
Rückgeld: 0,50 €
Verbleibender Bestand von "EnergyX": 3
```

### Szenario 4: Ausverkauftes Getränk

Nach mehreren Käufen:

```
=== Auswahl ===
1) AquaPlus      Preis: 2,00 €  (Bestand: 0)
...

Bitte Auswahl eingeben (1-6, oder 'q'): 1
Hinweis: "AquaPlus" ist leider ausverkauft.

=== Auswahl ===
1) AquaPlus      Preis: 2,00 €  (AUSVERKAUFT)
...
```

### Szenario 5: Abbruch-Funktionen

#### Abbruch bei Menüauswahl

```
Bitte Auswahl eingeben (1-6, oder 'q'): q

Auf Wiedersehen!
```

#### Abbruch bei Geldeingabe

```
Ausgewählt: FizzCola (3,00 €)
Bitte Betrag eingeben (fehlend: 3,00 €, 'q' für Abbruch): quit
Vorgang abgebrochen.
```

#### Verschiedene Abbruchbefehle

**`isQuit()` Methode:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=303
private boolean isQuit(String in) {
    String s = in.trim().toLowerCase(Locale.ROOT);
    return s.equals("q") || s.equals("quit") || s.equals("abbruch");
}
```

Akzeptierte Abbruchbefehle (case-insensitive):
- `q` / `Q`
- `quit` / `QUIT` / `Quit`
- `abbruch` / `ABBRUCH` / `Abbruch`

## Code-Schnittstellen Analyse

### Öffentliche Schnittstellen

#### DrinkMachine Klasse

**Konstruktor:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=45
public DrinkMachine()
```
- Initialisiert den Automaten mit 6 vordefinierten Getränken
- Keine Parameter erforderlich
- Erstellt vollständig einsatzbereiten Zustand

**Hauptmethode:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=55
public static void main(String[] args)
```
- Einstiegspunkt der Anwendung
- Erstellt DrinkMachine-Instanz und startet Hauptschleife

#### Drink Klasse

**Konstruktor:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/Drink.java start=26
public Product(String name, BigDecimal price, int stock)
```
- **Parameter:**
  - `name`: Getränkename (nicht null/leer)
  - `price`: Preis in Euro (>= 0)
  - `stock`: Anfangsbestand (>= 0)
- **Validierung:** Wirft `IllegalArgumentException` bei ungültigen Parametern

**Getter-Methoden:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/Drink.java start=36
public String getName()
public BigDecimal getPrice()
public int getStock()
```

**Geschäftslogik-Methoden:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/Drink.java start=41
public boolean isAvailable()
public void dispenseOne()
```

### Private Utility-Methoden

#### Eingabevalidierung
- `tryParsePositiveInt(String)` - Parst positive Ganzzahlen
- `tryParseMoney(String)` - Parst Geldbeträge mit flexibler Syntax
- `isQuit(String)` - Erkennt Abbruchbefehle

#### Benutzerinteraktion
- `promptSelectionOrQuit()` - Menüauswahl mit Validierung
- `promptMoneyUntilPriceOrQuit(Drink)` - Geldsammlung bis Ziel erreicht
- `showMenu()` - Formatierte Menüanzeige
- `printlnHeader()` - Willkommensnachricht

#### Datenformatierung
- `fmt(BigDecimal)` - Deutsche Euro-Formatierung
- `bd(String)` - BigDecimal-Factory mit Standard-Skalierung
- `readLine()` - Sichere Scanner-Eingabe

## Datenfluss und Zustandsübergänge

### Programmzustand-Automaton

1. **INIT** → Header anzeigen → **MENU**
2. **MENU** → Menü anzeigen → **SELECTION**
3. **SELECTION** → Auswahl validieren → **AVAILABILITY** oder **MENU**
4. **AVAILABILITY** → Verfügbarkeit prüfen → **PAYMENT** oder **MENU**
5. **PAYMENT** → Geld sammeln → **TRANSACTION** oder **MENU**
6. **TRANSACTION** → Kauf durchführen → **MENU**
7. **EXIT** → Programm beenden

### Fehlerbehandlung

**Prinzip:** Defensive Programmierung
- Ungültige Eingaben führen nie zum Programmabsturz
- Klare Fehlermeldungen mit Handlungsanweisungen
- Automatische Wiederholung von Eingabeaufforderungen
- Graceful Degradation bei unerwarteten Zuständen

### Speicherverwaltung

**Scanner-Handling:**
```java path=/Users/ilyabashlyaev/RBBK/Mittelstufe/STD/OOP/DrinkMachine/DrinkMachine.java start=35
private final Scanner scanner = new Scanner(System.in);
```
- Scanner wird NICHT geschlossen (System.in darf nicht geschlossen werden)
- Eine Scanner-Instanz für die gesamte Programmausführung
- Exception-Handling in `readLine()` für Robustheit

**BigDecimal-Operationen:**
- Immutable Objekte - keine Seiteneffekte
- Scale=2 für konsistente Cent-Genauigkeit
- HALF_UP Rundungsmodus für kaufmännisches Runden

## Performance-Charakteristika

### Zeitkomplexität
- Menüanzeige: O(n) - Linear in Anzahl Getränke
- Eingabevalidierung: O(1) - Konstant durch RegEx
- Geldberechnung: O(1) - BigDecimal-Operationen

### Speicherkomplexität
- Getränkeliste: O(n) - Linear in Anzahl Getränke
- Eingabepuffer: O(1) - Einzelne String-Eingaben
- BigDecimal-Objekte: O(1) - Pro Transaktion

Dieses Design gewährleistet auch bei einer größeren Anzahl von Getränken eine responsive Benutzerinteraktion.
