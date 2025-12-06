```mermaid
classDiagram
    class DrinkMachine {
        - Map~MachineType, ProductCatalog~ machineCatalogs
        - Scanner scanner
        - PaymentSystem paymentSystem
        + DrinkMachine()
        + static main(String[] args) void
        - seedCatalogs() void
        - run() void
        - printlnHeader() void
        - showMenu(ProductCatalog catalog, MachineType machine) void
        - promptSelectionOrQuit(ProductCatalog catalog) Integer
        - promptMoneyUntilPriceOrQuit(Product product) BigDecimal
        - promptMachineSelection() MachineType
        - tryParsePositiveInt(String in) Optional~Integer~
        - tryParseMoney(String in) Optional~BigDecimal~
        - isQuit(String in) boolean
        - fmt(BigDecimal amount) String
        - readLine() String
        - nutritionSummary(ProductInfo info) String
        - printNutrition(Product product) void
        - static bd(String s) BigDecimal
        - static strip(BigDecimal value) String
    }

    enum MachineType {
        SNACKAUTOMAT
        GEMISCHTER_AUTOMAT
        + getDisplayName() String
    }
    
    class Product {
        - String name
        - BigDecimal price
        - int stock
        - ProductInfo info
        + Product(String name, BigDecimal price, int stock, BigDecimal portionSize, String portionUnit, int kcalPerPortion, BigDecimal sugarPerPortion, BigDecimal fatPerPortion, BigDecimal saturatedFatPerPortion, int caffeineMgPerPortion)
        + getName() String
        + getPrice() BigDecimal
        + getStock() int
        + getInfo() ProductInfo
        + isAvailable() boolean
        + dispenseOne() void
        + increaseStock(int amount) void
    }

    class Drink {
        - boolean cooled
        + Drink(String name, BigDecimal price, int stock, ProductInfo info, boolean cooled)
        + isCooled() boolean
    }

    class HotDrink {
        - int targetTemperature
        + HotDrink(String name, BigDecimal price, int stock, ProductInfo info, int targetTemperature)
        + getTargetTemperature() int
    }

    class Snack {
        - SnackType type
        - int weightGram
        
        + Snack(String name, BigDecimal price, int stock, ProductInfo info, SnackType type, int weightGram)
        + getType() SnackType
        + getWeightGram() int
    }

    class SnackType {
        <<enumeration>>
        SWEET
        SALTY
        HEALTHY
    }

    class ProductInfo {
        - String description
        - String ingredients
        - String manufacturer
        - BigDecimal portionSize
        - String portionUnit
        - int kcalPerPortion
        - BigDecimal sugarPerPortion
        - BigDecimal fatPerPortion
        - BigDecimal saturatedFatPerPortion
        - int caffeineMgPerPortion
        + ProductInfo(String description, String ingredients, String manufacturer, BigDecimal portionSize, String portionUnit, int kcalPerPortion, BigDecimal sugarPerPortion, BigDecimal fatPerPortion, BigDecimal saturatedFatPerPortion, int caffeineMgPerPortion)
        + getDescription() String
        + getIngredients() String
        + getManufacturer() String
        + getPortionSize() BigDecimal
        + getPortionUnit() String
        + getKcalPerPortion() int
        + getSugarPerPortion() BigDecimal
        + getFatPerPortion() BigDecimal
        + getSaturatedFatPerPortion() BigDecimal
        + getCaffeineMgPerPortion() int
        + toDisplayText() String
    }

    class ProductCatalog {
        - List~Product~ products
        + registerProduct(Product product) void
        - findByName(String name) Optional~Product~
        + allProducts() List~Product~
        + changeStock(String name, int delta) void
    }
    
    class PaymentSystem {
        - PaymentMethod method
        + PaymentSystem(PaymentMethod method)
        + boolean pay(BigDecimal amount)
        + void refund(BigDecimal amount)
        + void setPaymentMethod(PaymentMethod method)
    }

    class PaymentMethod {
        <<interface>>
        + pay(BigDecimal amount) boolean
        + refund(BigDecimal amount) void
    }

    class CashPayment {
        + CashPayment()
        + pay(BigDecimal amount) boolean
        + refund(BigDecimal amount) void
    }
    
    DrinkMachine "1" *-- "2" ProductCatalog : manages
    ProductCatalog "1" o-- "*" Product : contains
    Product "1" *-- "0..1" ProductInfo : includes
    Product <|-- Drink
    Drink <|-- HotDrink
    Product <|-- Snack
    Snack --> SnackType
    DrinkMachine ..> MachineType
    DrinkMachine --> PaymentSystem : uses
    PaymentSystem --> PaymentMethod : uses
    PaymentMethod <|.. CashPayment
```
