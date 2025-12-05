# DrinkMachine – erweitertes UML-Klassendiagramm

Erweiterte Struktur zur Aufnahme von Snacks und Heissgetraenken, Pflege von Produktinformationen sowie dynamischer Bestandsaenderung. Die bisherige Klasse `Drink` wird durch die abstrakte Basisklasse `Produkt` ersetzt.

```mermaid
classDiagram
    class DrinkMachine {
        - List~Drink~ drinks
        - Scanner scanner
        - NumberFormat CURRENCY
        
        + DrinkMachine()
        + static main(String[] args) void
        
        - run() void
        - printlnHeader() void
        - showMenu() void
        - promptSelectionOrQuit() Integer
        - promptMoneyUntilPriceOrQuit(Drink d) BigDecimal
        - tryParsePositiveInt(String in) Optional~Integer~
        - tryParseMoney(String in) Optional~BigDecimal~
        - isQuit(String in) boolean
        - fmt(BigDecimal amount) String
        - readLine() String
        - static bd(String s) BigDecimal
    }

    class Produkt {
        <<abstract>>
        -String id
        -String name
        -BigDecimal preis
        -int bestand
        -ProduktInfo info
        +Produkt(String id, String name, BigDecimal preis, int bestand, ProduktInfo info)
        +isAvailable() boolean
        +verkaufeEinheit() void
        +erhoeheBestand(int menge) void
        +reduziereBestand(int menge) void
        +formatierteInfo() String
    }

    class Getraenk {
        -boolean gekuehlt
        +Getraenk(String id, String name, BigDecimal preis, int bestand, ProduktInfo info, boolean gekuehlt)
    }

    class Heissgetraenk {
        -int zielTemperaturC
        +Heissgetraenk(String id, String name, BigDecimal preis, int bestand, ProduktInfo info, int zielTemperaturC)
    }

    class Snack {
        -SnackTyp typ
        -int gewichtGramm
        +Snack(String id, String name, BigDecimal preis, int bestand, ProduktInfo info, SnackTyp typ, int gewichtGramm)
    }

    class ProduktInfo {
        -String beschreibung
        -String zutaten
        -String hersteller
        +ProduktInfo(String beschreibung, String zutaten, String hersteller)
        +toDisplayText() String
    }

    class ProduktKatalog {
        -List~Produkt~ produkte
        +ProduktKatalog()
        +registriereProdukt(Produkt p) void
        +alleProdukte() List~Produkt~
        +alleVerfuegbaren() List~Produkt~
        +findeNachId(String id) Optional~Produkt~
        +erhoeheBestand(String id, int menge) void
    }

    class SnackTyp {
        <<enumeration>>
        SUESS
        SALZIG
        HEALTHY
    }

    DrinkMachine "1" *-- "1" ProduktKatalog : verwaltet
    ProduktKatalog "1" o-- "*" Produkt : haelt
    Produkt "1" *-- "1" ProduktInfo : enthaelt
    Produkt <|-- Getraenk
    Getraenk <|-- Heissgetraenk
    Produkt <|-- Snack
    Snack --> SnackTyp
```
