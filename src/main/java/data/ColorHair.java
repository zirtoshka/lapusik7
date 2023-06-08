package data;




public enum ColorHair{
    RED("RED"),
    BLUE("BLUE"),
    YELLOW("YELLOW"),
    DEFAULT_COLOR("default color");
    private final String color;

    ColorHair(String color) {
        this.color = color;
    }

    public static String getList() {
        StringBuilder types = new StringBuilder();
        for (ColorHair type : values()) {
            if (!type.equals(DEFAULT_COLOR)) {
                types.append(type.name()+" - "+type.ordinal()).append("; ");
            }
        }
        return types.substring(0, types.length() - 2);
    }
}
