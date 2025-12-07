# DrinkMachine

Die konsolenbasierte Simulation zweier Verkaufsautomaten (Snackautomat, Gemischter Automat). Nutzer wählen den Automaten, sehen die verfügbaren Produkte mit Nährwerten, zahlen per Bargeld-Simulation und erhalten Rückgeld.

## Features
- Zwei getrennte Automaten mit eigenem Bestand und Sortiment.
- 11 vordefinierte Produkte inkl. Nährwert- und Koffeinangaben.
- Robuste Eingabevalidierung (Menü, Geldbeträge) und jederzeitiger Abbruch (`q`, `quit`, `abbruch`).
- Preisformatierung ohne Locale-Probleme (`X,YY EUR`).
- Erweiterbar über zusätzliche Automaten, Produkte oder Zahlungsarten.

## Projektstruktur
- `DrinkMachine.java` – Einstiegspunkt, Automaten- und UI-Logik.
- `product/` – Basismodelle (`Product`, `ProductInfo`, `ProductCatalog`).
- `product/drink/` – Getränke-Spezialisierungen (`Drink`, `HotDrink`).
- `product/snack/` – Snack-Spezialisierungen (`Snack`, `SnackType`).
- `payment_system/` – Zahlungsschnittstelle (`PaymentMethod`) und Implementierung (`CashPayment`, `PaymentSystem`).
- `Dokumentation/` – UML-Diagramme und Projektdokumentation.
- `_Ignored_files_/Liste_der_neuen_Produkte/` – Produktliste als Datenquelle.

## Build & Run
1. Kompilieren:
   ```bash
   javac DrinkMachine.java
   ```
2. Starten:
   ```bash
   java DrinkMachine
   ```

## Nutzung
- Schritt 1: Automaten wählen (Snackautomat oder Gemischter Automat).
- Schritt 2: Produktnummer eingeben oder mit `q/quit/abbruch` zurück/enden.
- Schritt 3: Geldbeträge eingeben, bis der Preis erreicht ist; Abbruch jederzeit möglich.
- Ausgabe: Produkt wird ausgegeben, Bestand reduziert, Rückgeld angezeigt.

## Dokumentation
- Klassendiagramm: `Dokumentation/UML_Klassendiagramm.md`
- Aktivitätsdiagramm: `Dokumentation/UML_Aktivitaetsdiagramm.md`
- Projektdoku: `Dokumentation/Dokumentation.md`
