package data;




public enum Country{
    VATICAN("VATICAN"),
    THAILAND("THAILAND"),
    NORTH_KOREA("NORTH_KOREA"),
    DEFAULT_COUNTRY("default country");
    private final String country;

    Country(String country) {
        this.country = country;
    }

    public static String getList() {
        StringBuilder types = new StringBuilder();
        for (Country type : values()) {
            if (!type.equals(DEFAULT_COUNTRY)) {
                types.append(type.name()+" - "+type.ordinal()).append("; ");
            }
        }
        return types.substring(0, types.length() - 2);
    }
}
