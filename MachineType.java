enum MachineType {
    SNACKAUTOMAT("Snackautomat"),
    GEMISCHTER_AUTOMAT("Gemischter Automat");

    private final String displayName;

    MachineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}