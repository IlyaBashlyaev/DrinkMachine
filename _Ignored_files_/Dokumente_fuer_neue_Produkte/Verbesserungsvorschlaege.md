🎯 Wie soll das Produkt-System in eure DrinkMachine integriert werden?
Ihr benötigt ein Datenmodell, das alle nötigen Infos für jedes Produkt speichert:
Produktname
Preis
Maximalbestand
Aktueller Bestand
Portionsgröße (g oder ml)
kcal pro 100g / 100ml
kcal pro Portion
Zucker pro Portion
Fett pro Portion
Gesättigte Fette pro Portion
(bei Getränken: Koffein)
Diese Daten sind in der Datei auf Seite 2 genau aufgelistet.

⭐ Schritt 1 — Wir benötigen eine neue Produktklasse
Euer aktuelles Projekt hat Drink, aber die neue Aufgabe verlangt ein allgemeineres Produktmodell:
Lösung:
Wir machen eine Product-Klasse, die ALLE Informationen halten kann:
public class Product { private String name; private BigDecimal price; private int maxStock; private int currentStock; // Nährwerte private int portion; // g oder ml private double kcalPer100; private double kcalPerPortion; private double sugar; private double fat; private double saturatedFat; private double caffeine; // nur für Getränke // Konstruktor, Getter, Methoden etc. }
Damit könnt ihr ALLE Produkte aus dem PDF abbilden.
⭐ Schritt 2 — Produkte aus der Datei in Code übertragen
Beispiel Snackautomat aus PDF:

Merkur Riegel
Preis: 2 €
Maximal: 20 Stück
Portion: 50g
kcal/Portion: 250
Zucker: 23.5g
Fett: 12.5g
Ges. Fett: 7.5g
So würde das als Java-Objekt aussehen:
Product merkur = new Product( "Merkur Riegel", bd("2.00"), 20, 20, 50, 500, 250, 23.5, 12.5, 7.5, 0 );
Dasselbe machen wir für:
Saturn Ringe
Lakritzstangen
DoubleChoc Riegel
Silberbären Tüte
Und für den gemischten Automaten:
NeptunDrink
Saturn Ringe
Lakritzstangen
AquaPlus
EnergyX
Kaffee
Espresso
Cappuccino
Daten stehen alle auf Seite 2 im PDF.

⭐ Schritt 3 — Automaten unterscheiden
Ihr habt laut Aufgabe zwei Gerätetypen:
🔹 1. Snackautomat
Nur Snacks → nur Festprodukte (g)
🔹 2. Gemischter Automat
Snacks + Getränke → verschiedene Portionseinheiten (g & ml)
Dafür braucht ihr:
public enum MachineType { SNACK, MIXED }
Und euer Automat bekommt:
private MachineType type; private List<Product> products;
⭐ Schritt 4 — DrinkMachine erweitern
Statt der alten festen Liste:
private final List<Drink> drinks = ...
verwendet ihr:
private final List<Product> products = new ArrayList<>();
Und ladet Produkte abhängig vom Automatentyp:
if (type == MachineType.SNACK) { loadSnackProducts(); } else { loadMixedProducts(); }
⭐ Schritt 5 — Produktinformationen anzeigen
In der Aufgabe steht:
„Kunden sollen am Automaten die Möglichkeit haben, sich Produktdaten anzeigen zu lassen.“
Das heißt:
☑️ Eine Taste drücken → Nährwerte anzeigen
☑️ Vor dem Kauf kann Kunde Informationen abrufen
Ihr braucht also:
showProductInfo(Product p)
Gibt aus:
Portion
kcal
Zucker
Fett
etc.
Alle Daten stehen in PDF Tabelle auf Seite 2.

⭐ Schritt 6 — Was ihr dafür im Code verändern müsst
🔧 Muss ersetzt werden:
Drink Klasse → ersetzen durch Product
🔧 Muss neu erstellt werden:
Product Klasse
loadSnackProducts()
loadMixedProducts()
showProductInfo()
🔧 Muss angepasst werden:
Menüausgabe → zeigt jetzt Produkte
Auswahl → funktioniert weiterhin
PaymentSystem → bleibt unverändert 😎
Ausgabe / Bestandverwaltung → bleibt ähnlich