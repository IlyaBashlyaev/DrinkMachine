## Überblick
DrinkMachine ist eine konsolenbasierte Verkaufsautomaten-Simulation mit zwei Automaten-Typen (Snackautomat, Gemischter Automat). Der Nutzer wählt zuerst den Automaten, sieht dessen Produktsortiment, zahlt per Bargeld-Simulation und erhält ggf. Rückgeld.

## Architektur
- Einstieg: `DrinkMachine` (Main-Klasse). Steuert Automatenwahl, Menü, Zahlung und Ausgabe.
- Datenhaltung: `product.ProductCatalog` (je Automat eigener Katalog, In-Memory).
- Domänenobjekte: `Product` (Basisklasse) + spezialisierte `drink.*` und `snack.*`; Nährwerte in `ProductInfo`.
- Zahlung: `payment_system.PaymentSystem` mit Strategie `PaymentMethod` (aktuell `CashPayment`).
- Utilities: Eingabevalidierung (`tryParsePositiveInt`, `tryParseMoney`), Preisformatierung (`fmt`).

## Programmablauf (Kurzfassung)
1. Automaten-Typ wählen (Snack/Gemischt oder Abbruch).
2. Produktmenü anzeigen (Preis, Bestand, kcal/Koffein).
3. Produkt wählen oder abbrechen.
4. Bei Verfügbarkeit: Beträge eingeben, bis Preis erreicht (oder Abbruch).
5. Zahlung versuchen, Produkt ausgeben, Rückgeld anzeigen.
6. Zurück zur Automatenwahl.

## Paket-/Dateistruktur
- `DrinkMachine.java` – Steuerung und UI-Logik.
- `product/` – Basismodelle (`Product`, `ProductInfo`, `ProductCatalog`).
- `product/drink/` – Getränke-Spezialisierungen (`Drink`, `HotDrink`).
- `product/snack/` – Snack-Spezialisierungen (`Snack`, `SnackType`).
- `payment_system/` – Zahlungsschnittstelle und -implementierung.
- `Dokumentation/` – UML-Diagramme, Projektdokumentation.
- `_Ignored_files_/Liste_der_neuen_Produkte/` – Produktliste als Quelle.

## Wichtige Entwurfsentscheidungen
- `BigDecimal` für Geldbeträge (keine Rundungsfehler).
- Getrennte Kataloge pro Automat, damit Bestände unabhängig bleiben.
- Einfache Konsolen-Interaktion mit robusten Abbruchpfaden (`q/quit/abbruch`).
- Preisformatierung manuell (`fmt`), um locale-unabhängig einheitlich `X,YY€` auszugeben.

## Erweiterbarkeit
- Neue Automaten-Typen: `MachineType` erweitern und in `seedCatalogs()` befüllen.
- Weitere Zahlarten: neue `PaymentMethod`-Implementierung und im `PaymentSystem` setzen.
- Neue Produkte: in `seedCatalogs()` registrieren oder Katalog dynamisch laden.
- Zusatzinfos: `ProductInfo` bereits vorbereitet (Beschreibung, Zutaten, Hersteller, Nährwerte).

## Tests / Qualitätssicherung
- Manuelle Smoke-Tests: beide Automaten wählen, ausverkauftes Produkt simulieren (Bestand auf 0 setzen), Zahlung mit Abbruch testen, Rückgeldpfad prüfen.
- Kompilierung: `javac DrinkMachine.java product/*.java product/drink/*.java product/snack/*.java payment_system/*.java`
