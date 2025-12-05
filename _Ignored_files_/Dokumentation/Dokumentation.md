# DrinkMachine - Dokumentation

## Projektübersicht

**Projektbezeichnung:** DrinkMachine  
**Zweck:** Robuster, konsolenbasierter Getränkeautomat für 6 verschiedene Getränke  
**Version:** 1.0  
**Autor:** Student der RBBK Mittelstufe  
**Programmiersprache:** Java  
**Development Environment:** BlueJ  

## Projektbeschreibung

Das DrinkMachine-Projekt ist eine Java-Anwendung, die einen vollständig funktionsfähigen Getränkeautomaten simuliert. Das Programm bietet eine benutzerfreundliche Konsolen-Oberfläche mit robusten Eingabevalidierungen und präziser Geldverwaltung.

### Hauptmerkmale

- **6 verschiedene Getränke** mit individuellen Preisen und Beständen
- **Robuste Eingabevalidierung** für Menüauswahl und Geldbeträge
- **Flexible Abbruchfunktionen** ('q', 'quit', 'abbruch') jederzeit verfügbar
- **Präzise Geldverwaltung** mit BigDecimal (vermeidet Fließkommafehler)
- **Bestandsverwaltung** mit automatischer Ausverkauft-Anzeige
- **Rückgeldberechnung** und -ausgabe
- **Mehrsprachige Unterstützung** (Deutsch mit Euro-Formatierung)

## Technische Details

### Verwendete Technologien

- **Java Standard Edition**
- **BigDecimal** für präzise Geldbeträge (Scale=2, RoundingMode.HALF_UP)
- **NumberFormat** mit deutschem Locale für Währungsformatierung
- **Regular Expressions** für robuste Eingabevalidierung
- **Optional Pattern** für sichere Wertverarbeitung

### Warum BigDecimal?

Das Projekt verwendet `BigDecimal` anstelle von `double` oder `float` für Geldberechnungen, da:
- Binäre Fließkommazahlen zu Rundungsfehlern bei Geldbeträgen führen
- BigDecimal mit Scale=2 und HALF_UP dem Industriestandard für Finanzanwendungen entspricht
- Präzise Berechnungen für Kaufmännisches Runden gewährleistet sind

## Klassenstruktur

### DrinkMachine (öffentliche Hauptklasse)

**Zweck:** Hauptklasse mit Benutzerinteraktion und Programmlogik

**Wichtige Attribute:**
- `List<Drink> drinks` - Liste aller verfügbaren Getränke
- `Scanner scanner` - Eingabe-Scanner (wird NICHT geschlossen!)
- `NumberFormat CURRENCY` - Deutsche Euro-Formatierung

**Hauptmethoden:**

#### `main(String[] args)`
Einstiegspunkt des Programms

#### `run()`
Hauptschleife mit folgenden Schritten:
1. Menü anzeigen
2. Benutzerauswahl validieren
3. Verfügbarkeit prüfen
4. Geld einsammeln
5. Transaktion durchführen
6. Rückgeld ausgeben

#### UI-Methoden
- `printlnHeader()` - Begrüßung und Bedienungshinweise
- `showMenu()` - Aktuelle Getränkeauswahl mit Preisen und Beständen
- `promptSelectionOrQuit()` - Menüauswahl mit Validierung
- `promptMoneyUntilPriceOrQuit(Drink)` - Geldeinsammlung bis Preis erreicht

#### Validierungs-Methoden
- `tryParsePositiveInt(String)` - Parst positive Ganzzahlen
- `tryParseMoney(String)` - Parst Geldbeträge (unterstützt Komma und Punkt)
- `isQuit(String)` - Prüft auf Abbruchbefehle

#### Utility-Methoden
- `fmt(BigDecimal)` - Formatiert Beträge als deutsche Euro-Währung
- `readLine()` - Sichere Zeileneingabe mit Exception-Handling
- `bd(String)` - BigDecimal-Konstruktor mit Standard-Skalierung

### Drink (paketlokale Datenklasse)

**Zweck:** Datencontainer für einzelne Getränke

**Attribute:**
- `String name` - Getränkename (final, nicht leer)
- `BigDecimal price` - Preis in Euro (final, >= 0)
- `int stock` - Aktueller Bestand (>= 0)

**Methoden:**
- `getName()`, `getPrice()`, `getStock()` - Standard-Getter
- `isAvailable()` - Prüft Verfügbarkeit (Bestand > 0)
- `dispenseOne()` - Reduziert Bestand um 1 (mit Schutz vor Unterbestand)

**Konstruktor-Validierung:**
- Name darf nicht null oder leer sein
- Preis muss >= 0 sein
- Bestand muss >= 0 sein

## Vordefinierte Getränke

Das System startet mit 6 vordefinierten Getränken:

| Getränk    | Preis | Anfangsbestand |
|------------|-------|----------------|
| AquaPlus   | 2,00€ | 8              |
| FizzCola   | 3,00€ | 5              |
| OrangePop  | 3,00€ | 5              |
| MateMax    | 3,00€ | 5              |
| EnergyX    | 4,00€ | 4              |
| ColdBrew   | 4,00€ | 3              |

## Eingabevalidierung

### Menüauswahl
- **RegEx:** `\\d+` (nur Ziffern)
- **Bereich:** 1 bis 6
- **Sonderzeichen:** 'q', 'quit', 'abbruch' für Abbruch

### Geldbeträge
- **RegEx:** `\\d+(?:[.,]\\d{1,2})?`
- **Akzeptierte Formate:** 
  - Ganzzahlen: "2", "3"
  - Dezimalzahlen: "1,5", "1.50", "2,00"
- **Dezimaltrenner:** Komma oder Punkt (automatische Normalisierung)
- **Nachkommastellen:** 1-2 Stellen erlaubt
- **Ausschlüsse:** Negative Werte, Null-Beträge

## Installation und Ausführung

### Voraussetzungen
- Java Development Kit (JDK) 8 oder höher
- Optional: BlueJ IDE

### Kompilierung
```bash
javac *.java
```

### Ausführung
```bash
java DrinkMachine
```

### Mit BlueJ
1. Projekt in BlueJ öffnen
2. Rechtsklick auf DrinkMachine-Klasse
3. "void main(String[] args)" auswählen
4. Ausführen

## Benutzungshinweise

### Programmstart
Nach dem Start erscheint eine Begrüßung mit Bedienungshinweisen:
```
====================================
           DRINK MACHINE v1         
====================================

Eingaben:
- Menü: Nummer des Getränks eingeben
- Geld: Betrag in Euro, z. B. 2 oder 1,50 (Komma/Punkt möglich)
- Abbruch jederzeit mit: q | abbruch | quit
```

### Getränkeauswahl
Das Menü zeigt alle verfügbaren Getränke mit Preisen und Beständen:
```
=== Auswahl ===
1) AquaPlus      Preis: 2,00 €  (Bestand: 8)
2) FizzCola      Preis: 3,00 €  (Bestand: 5)
3) OrangePop     Preis: 3,00 €  (AUSVERKAUFT)
...
q) Beenden
```

### Bezahlvorgang
Nach der Getränkeauswahl:
1. System zeigt gewähltes Getränk und Preis
2. Schrittweise Geldeinzahlung möglich
3. System zeigt bisher eingezahlten Betrag
4. Automatische Rückgeldberechnung
5. Bestandsupdate wird angezeigt

### Abbruchfunktionen
Jederzeit verfügbar:
- `q` - Kurzer Abbruch
- `quit` - Standard-Abbruch (englisch)
- `abbruch` - Deutscher Abbruch

## Programm-Features

### Robuste Fehlerbehandlung
- Ungültige Eingaben führen zu Wiederholung der Eingabeaufforderung
- Klare Fehlermeldungen mit Formatvorgaben
- Schutz vor Bestandsunterschreitung
- Exception-Handling bei Eingabeoperationen

### Benutzerfreundlichkeit
- Deutsche Benutzeroberfläche
- Klare Formatierung und Strukturierung
- Fortschrittsanzeigen beim Bezahlen
- Bestätigungen nach erfolgreichem Kauf

### Korrekte Geldverwaltung
- Keine Fließkommafehler durch BigDecimal-Verwendung
- Deutsche Währungsformatierung (1,50 €)
- Präzise Rückgeldberechnung
- Unterstützung für Cent-genaue Beträge

## Projektdateien

```
DrinkMachine/
├── DrinkMachine.java     # Hauptklasse mit Programmlogik
├── Drink.java           # Datenklasse für Getränke
├── DrinkMachine.ctxt    # BlueJ-Kontext (automatisch generiert)
├── Drink.ctxt          # BlueJ-Kontext (automatisch generiert)
├── package.bluej       # BlueJ-Projektdatei
├── README.TXT          # Standard BlueJ README (leer)
└── Dokumentation.md    # Diese Dokumentation
```

## Erweiterungsmöglichkeiten

### Mögliche Verbesserungen
1. **Getränkeverwaltung:** Dynamisches Hinzufügen/Entfernen von Getränken
2. **Persistierung:** Speicherung von Beständen in Dateien
3. **Münzverwaltung:** Simulation von Münzwechsel und -beständen
4. **GUI:** Grafische Benutzeroberfläche mit Swing/JavaFX
5. **Konfiguration:** Externe Konfigurationsdateien für Getränke und Preise
6. **Logging:** Verkaufsstatistiken und Transaktionsprotokoll
7. **Mehrsprachigkeit:** Vollständige Internationalisierung

### Code-Qualität
- Umfassende JavaDoc-Kommentierung
- Klare Trennung von UI-Logik und Geschäftslogik
- Defensive Programmierung mit Eingabevalidierung
- Verwendung von Design Patterns (Optional, Builder)

## Fazit

Das DrinkMachine-Projekt demonstriert solide objektorientierte Programmierprinzipien und robuste Softwareentwicklung. Es zeigt Best Practices für:
- Eingabevalidierung und Fehlerbehandlung
- Präzise Geldverwaltung in Java
- Benutzerfreundliche Konsolenanwendungen
- Saubere Klassenarchitektur und Datenkapselung

Das Projekt eignet sich hervorragend als Lernbeispiel für Programmieranfänger und als Basis für weiterführende Erweiterungen.
