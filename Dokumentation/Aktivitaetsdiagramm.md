# DrinkMachine - Aktivitätsdiagramm

## Übersicht

Dieses Aktivitätsdiagramm zeigt den vollständigen Ablauf des DrinkMachine-Programms vom Programmstart bis zum Beenden. Es visualisiert alle Entscheidungspunkte, Schleifen und parallele Aktivitäten.

## Haupt-Aktivitätsdiagramm

```mermaid
flowchart TD
    Start([Programmstart]) --> Init[DrinkMachine erstellen]
    Init --> FillDrinks[6 Getränke initialisieren]
    FillDrinks --> Header[Header und Bedienungshinweise anzeigen]
    
    Header --> MainLoop{Hauptschleife}
    MainLoop --> ShowMenu[Menü mit Getränken anzeigen]
    
    ShowMenu --> PromptSelection[Benutzeraufforderung: Auswahl eingeben]
    PromptSelection --> ReadInput[Eingabe lesen]
    
    ReadInput --> CheckQuit{Abbruchbefehl?}
    CheckQuit -->|Ja: q/quit/abbruch| Goodbye[Auf Wiedersehen ausgeben]
    Goodbye --> End([Programmende])
    
    CheckQuit -->|Nein| ValidateInput{Eingabe gültig?}
    ValidateInput -->|Nein: nicht numerisch| ErrorMessage1[Fehlermeldung: Ungültige Eingabe]
    ErrorMessage1 --> PromptSelection
    
    ValidateInput -->|Ja, aber außerhalb 1-6| ErrorMessage2[Fehlermeldung: Ungültige Auswahl]
    ErrorMessage2 --> PromptSelection
    
    ValidateInput -->|Ja: 1-6| GetDrink[Gewähltes Getränk abrufen]
    GetDrink --> CheckAvailability{Getränk verfügbar?}
    
    CheckAvailability -->|Nein: stock = 0| SoldOut[Ausverkauft-Meldung anzeigen]
    SoldOut --> MainLoop
    
    CheckAvailability -->|Ja: stock > 0| StartPayment[Bezahlvorgang starten]
    StartPayment --> ShowSelected[Gewähltes Getränk und Preis anzeigen]
    
    ShowSelected --> PaymentLoop{Bezahlschleife}
    PaymentLoop --> CalcMissing[Fehlenden Betrag berechnen]
    CalcMissing --> PromptMoney[Geld-Eingabeaufforderung]
    
    PromptMoney --> ReadMoney[Geldbetrag lesen]
    ReadMoney --> CheckQuitPayment{Abbruch beim Bezahlen?}
    
    CheckQuitPayment -->|Ja| CancelPayment[Vorgang abgebrochen]
    CancelPayment --> MainLoop
    
    CheckQuitPayment -->|Nein| ValidateMoney{Geld-Eingabe gültig?}
    
    ValidateMoney -->|Nein: Format falsch| MoneyErrorFormat[Fehlermeldung: Ungültiges Format]
    MoneyErrorFormat --> PromptMoney
    
    ValidateMoney -->|Nein: Betrag ≤ 0| MoneyErrorAmount[Fehlermeldung: Betrag > 0]
    MoneyErrorAmount --> PromptMoney
    
    ValidateMoney -->|Ja| AddMoney[Betrag zur Summe addieren]
    AddMoney --> ShowTotal[Bisher eingezahlt anzeigen]
    
    ShowTotal --> CheckSufficient{Genug bezahlt?}
    CheckSufficient -->|Nein: total < Preis| PaymentLoop
    CheckSufficient -->|Ja: total ≥ Preis| ProcessTransaction[Transaktion durchführen]
    
    ProcessTransaction --> DispenseOne[Bestand um 1 reduzieren]
    DispenseOne --> CalcChange[Rückgeld berechnen]
    CalcChange --> ShowThankYou[Danke-Nachricht anzeigen]
    
    ShowThankYou --> CheckChange{Rückgeld > 0?}
    CheckChange -->|Ja| ShowChange[Rückgeld anzeigen]
    CheckChange -->|Nein| ShowStock[Verbleibenden Bestand anzeigen]
    ShowChange --> ShowStock
    
    ShowStock --> MainLoop
    
    style Start fill:#90EE90
    style End fill:#FFB6C1
    style MainLoop fill:#87CEEB
    style PaymentLoop fill:#87CEEB
    style CheckQuit fill:#FFD700
    style CheckAvailability fill:#FFD700
    style ValidateInput fill:#FFD700
    style ValidateMoney fill:#FFD700
    style CheckSufficient fill:#FFD700
    style CheckChange fill:#FFD700
    style ErrorMessage1 fill:#FF6347
    style ErrorMessage2 fill:#FF6347
    style MoneyErrorFormat fill:#FF6347
    style MoneyErrorAmount fill:#FF6347
    style SoldOut fill:#FFA500
```

## Detaillierte Teilprozesse

### 1. Eingabe-Validierungsprozess (Menüauswahl)

```mermaid
flowchart TD
    ReadInput[Eingabe lesen: readLine()] --> TrimInput[Eingabe trimmen]
    TrimInput --> CheckQuitCmd{isQuit(input)?}
    
    CheckQuitCmd -->|Ja| ReturnNull[return null]
    CheckQuitCmd -->|Nein| ParseInt[tryParsePositiveInt(input)]
    
    ParseInt --> RegexCheck{Regex \\d+ erfüllt?}
    RegexCheck -->|Nein| ReturnEmpty1[Optional.empty()]
    RegexCheck -->|Ja| TryParse[Integer.parseInt()]
    
    TryParse -->|Exception| ReturnEmpty2[Optional.empty()]
    TryParse -->|Erfolg| CheckRange{1 ≤ value ≤ 6?}
    
    CheckRange -->|Nein| ShowRangeError[Bereichsfehler anzeigen]
    ShowRangeError --> ReadInput
    CheckRange -->|Ja| ReturnValue[Gültigen Wert zurückgeben]
    
    ReturnEmpty1 --> ShowFormatError[Formatfehler anzeigen]
    ReturnEmpty2 --> ShowFormatError
    ShowFormatError --> ReadInput
    
    style RegexCheck fill:#FFD700
    style CheckRange fill:#FFD700
    style TryParse fill:#DDA0DD
```

### 2. Geld-Validierungsprozess

```mermaid
flowchart TD
    ReadMoney[Geldbetrag lesen] --> TrimMoney[String trimmen]
    TrimMoney --> MoneyRegex{Regex \\d+(?:[.,]\\d{1,2})? erfüllt?}
    
    MoneyRegex -->|Nein| MoneyFormatError[Format-Fehlermeldung]
    MoneyFormatError --> ReadMoney
    
    MoneyRegex -->|Ja| NormalizeDecimal[Komma zu Punkt konvertieren]
    NormalizeDecimal --> CreateBigDecimal[BigDecimal erstellen mit Scale=2]
    
    CreateBigDecimal -->|Exception| MoneyParseError[Parse-Fehlermeldung]
    MoneyParseError --> ReadMoney
    
    CreateBigDecimal -->|Erfolg| CheckPositive{Betrag > 0?}
    CheckPositive -->|Nein| PositiveError[Positiv-Fehlermeldung]
    PositiveError --> ReadMoney
    
    CheckPositive -->|Ja| ReturnAmount[Gültigen Betrag zurückgeben]
    
    style MoneyRegex fill:#FFD700
    style CheckPositive fill:#FFD700
    style CreateBigDecimal fill:#DDA0DD
```

### 3. Transaktionsprozess

```mermaid
flowchart TD
    StartTransaction[Transaktion starten] --> CheckStock{stock > 0?}
    CheckStock -->|Nein| ThrowException[IllegalStateException werfen]
    CheckStock -->|Ja| DecrementStock[stock--]
    
    DecrementStock --> CalcChange[change = totalPaid - price]
    CalcChange --> ShowDrink[Getränkename ausgeben]
    ShowDrink --> CheckChangeAmount{change > 0?}
    
    CheckChangeAmount -->|Ja| DisplayChange[Rückgeld formatiert ausgeben]
    CheckChangeAmount -->|Nein| SkipChange[Kein Rückgeld]
    
    DisplayChange --> ShowRemaining[Verbleibenden Bestand anzeigen]
    SkipChange --> ShowRemaining
    ShowRemaining --> TransactionComplete[Transaktion abgeschlossen]
    
    style CheckStock fill:#FFD700
    style CheckChangeAmount fill:#FFD700
    style ThrowException fill:#FF6347
```

## Parallelitäts- und Zustandsdiagramm

### Objektzustände während der Ausführung

```mermaid
stateDiagram-v2
    [*] --> Initialisierung
    Initialisierung --> BereitFuerMenu : Getränke geladen
    
    BereitFuerMenu --> MenuAngezeigt : showMenu()
    MenuAngezeigt --> WarteAufAuswahl : promptSelection()
    
    WarteAufAuswahl --> ValidiereAuswahl : Eingabe erhalten
    ValidiereAuswahl --> WarteAufAuswahl : Ungültige Eingabe
    ValidiereAuswahl --> PruefVerfuegbarkeit : Gültige Auswahl
    ValidiereAuswahl --> Beenden : Abbruchbefehl
    
    PruefVerfuegbarkeit --> BereitFuerMenu : Ausverkauft
    PruefVerfuegbarkeit --> Bezahlung : Verfügbar
    
    Bezahlung --> WarteAufGeld : Preis angezeigt
    WarteAufGeld --> ValidiereGeld : Geldeingabe
    ValidiereGeld --> WarteAufGeld : Ungültiger Betrag
    ValidiereGeld --> SammleGeld : Gültiger Betrag
    ValidiereGeld --> BereitFuerMenu : Abbruch
    
    SammleGeld --> WarteAufGeld : Noch nicht genug
    SammleGeld --> Transaktion : Ausreichend bezahlt
    
    Transaktion --> BereitFuerMenu : Abgeschlossen
    
    Beenden --> [*]
    
    note right of Bezahlung
        Schleife bis
        total >= Preis
        erreicht ist
    end note
```

## Fehlerbehandlungs-Aktivitäten

```mermaid
flowchart TD
    Error[Fehler aufgetreten] --> CheckType{Fehlertyp?}
    
    CheckType -->|Eingabevalidierung| InputError[Eingabefehler]
    CheckType -->|Geldvalidierung| MoneyError[Geldfehler]
    CheckType -->|Verfügbarkeit| StockError[Bestandsfehler]
    CheckType -->|System| SystemError[Systemfehler]
    
    InputError --> ShowInputMessage[Klare Fehlermeldung + Format-Hinweis]
    MoneyError --> ShowMoneyMessage[Geld-Formathinweis anzeigen]
    StockError --> ShowStockMessage[Ausverkauft-Hinweis]
    SystemError --> SafeDefault[Sichere Standard-Antwort]
    
    ShowInputMessage --> RetryInput[Eingabe wiederholen]
    ShowMoneyMessage --> RetryMoney[Geldeingabe wiederholen]
    ShowStockMessage --> BackToMenu[Zurück zum Menü]
    SafeDefault --> BackToMenu
    
    RetryInput --> MainFlow[Zurück zum Hauptablauf]
    RetryMoney --> MainFlow
    BackToMenu --> MainFlow
    
    style Error fill:#FF6347
    style SystemError fill:#DC143C
    style SafeDefault fill:#FFA500
```

## Performance-Aktivitäten

### Speicher- und Ressourcenmanagement

```mermaid
flowchart TD
    ProgramStart[Programmstart] --> InitScanner[Scanner initialisieren]
    InitScanner --> Note1[Scanner NICHT schließen!]
    Note1 --> InitNumberFormat[NumberFormat erstellen]
    
    InitNumberFormat --> InitDrinks[ArrayList für Drinks]
    InitDrinks --> CreateDrinks[6 Drink-Objekte erstellen]
    
    CreateDrinks --> MainExecution[Hauptausführung]
    MainExecution --> ReuseObjects[Objekte wiederverwenden]
    
    ReuseObjects --> Note2[Keine neuen Scanner pro Eingabe]
    Note2 --> Note3[BigDecimal immutable - neue Objekte bei Operationen]
    Note3 --> Note4[String-Operationen minimiert]
    
    Note4 --> ProgramEnd[Programmende]
    ProgramEnd --> AutoGC[Automatische Garbage Collection]
    
    style Note1 fill:#90EE90
    style Note2 fill:#90EE90
    style Note3 fill:#FFE4B5
    style Note4 fill:#FFE4B5
```

## Datenfluss-Aktivitäten

```mermaid
flowchart LR
    subgraph Input["Eingabe-Schicht"]
        UserInput[Benutzereingabe]
        Scanner[Scanner.nextLine]
        RawString[Roher String]
    end
    
    subgraph Validation["Validierungs-Schicht"]
        RegexCheck[RegEx-Prüfung]
        TypeConversion[Typ-Konvertierung]
        RangeCheck[Bereichsprüfung]
    end
    
    subgraph Business["Geschäftslogik-Schicht"]
        DrinkSelection[Getränkeauswahl]
        AvailabilityCheck[Verfügbarkeitsprüfung]
        PaymentProcessing[Bezahlungsverarbeitung]
        StockUpdate[Bestandsupdate]
    end
    
    subgraph Output["Ausgabe-Schicht"]
        Formatting[Deutsche Formatierung]
        DisplayMessage[Nachrichtenanzeige]
        UserFeedback[Benutzer-Rückmeldung]
    end
    
    UserInput --> Scanner
    Scanner --> RawString
    RawString --> RegexCheck
    RegexCheck --> TypeConversion
    TypeConversion --> RangeCheck
    RangeCheck --> DrinkSelection
    DrinkSelection --> AvailabilityCheck
    AvailabilityCheck --> PaymentProcessing
    PaymentProcessing --> StockUpdate
    StockUpdate --> Formatting
    Formatting --> DisplayMessage
    DisplayMessage --> UserFeedback
    
    style Input fill:#E6F3FF
    style Validation fill:#FFE6CC
    style Business fill:#E6FFE6
    style Output fill:#FFE6F3
```

## Zeitliche Abläufe (Timing-Diagramm)

```mermaid
sequenceDiagram
    participant U as Benutzer
    participant DM as DrinkMachine
    participant D as Drink
    participant V as Validierung
    
    Note over U,V: Programminitialisierung (einmalig)
    DM->>DM: Konstruktor ausführen
    DM->>D: 6 Drink-Objekte erstellen
    D-->>DM: Objekte bereit
    
    Note over U,V: Hauptschleife (wiederholend)
    loop Bis Benutzer beendet
        DM->>U: Menü anzeigen
        U->>DM: Auswahl eingeben
        DM->>V: Eingabe validieren
        alt Gültige Auswahl
            V-->>DM: Validierung OK
            DM->>D: Verfügbarkeit prüfen
            alt Verfügbar
                D-->>DM: Verfügbar
                loop Bis ausreichend Geld
                    DM->>U: Geld anfordern
                    U->>DM: Betrag eingeben
                    DM->>V: Geld validieren
                    V-->>DM: Geld OK/Fehler
                end
                DM->>D: Getränk ausgeben
                D->>D: Bestand reduzieren
                DM->>U: Bestätigung + Rückgeld
            else Nicht verfügbar
                D-->>DM: Ausverkauft
                DM->>U: Ausverkauft-Meldung
            end
        else Ungültige Auswahl
            V-->>DM: Validierungsfehler
            DM->>U: Fehlermeldung
        end
    end
```

## Komplexitäts-Kennzahlen

### Aktivitäts-Metriken

| Kategorie | Anzahl | Komplexität | Beschreibung |
|-----------|--------|-------------|--------------|
| **Entscheidungspunkte** | 12 | Hoch | Viele Validierungs- und Prüfschritte |
| **Schleifen** | 2 | Mittel | Hauptschleife + Bezahlschleife |
| **Fehlerbehandlungen** | 8 | Mittel | Umfassende Eingabevalidierung |
| **Zustandsübergänge** | 15 | Hoch | Viele verschiedene Programmzustände |
| **Parallele Aktivitäten** | 0 | Niedrig | Sequenzieller Ablauf |

### Optimierungspotentiale

```mermaid
flowchart TD
    Current[Aktueller Ablauf] --> Analyze[Analyse durchführen]
    Analyze --> Bottleneck1[Engpass: Wiederholte Validierung]
    Analyze --> Bottleneck2[Engpass: String-Erstellung bei Formatierung]
    
    Bottleneck1 --> Solution1[Lösung: Validator-Klassen]
    Bottleneck2 --> Solution2[Lösung: StringBuilder verwenden]
    
    Solution1 --> Improved[Verbesserter Ablauf]
    Solution2 --> Improved
    
    style Bottleneck1 fill:#FF6347
    style Bottleneck2 fill:#FF6347
    style Solution1 fill:#90EE90
    style Solution2 fill:#90EE90
```

Dieses umfassende Aktivitätsdiagramm zeigt alle Aspekte des DrinkMachine-Programms: vom einfachen linearen Ablauf über komplexe Verzweigungen bis hin zu Fehlerbehandlung und Performance-Überlegungen. Es dient als vollständige Referenz für das Verständnis des Programmverhaltens und kann für Debugging, Optimierung oder Weiterentwicklung verwendet werden.
