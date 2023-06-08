package data;




public enum Semester {
    FIRST("FIRST"),
    SECOND("SECOND"),
    FIFTH("FIFTH"),
    DEFAULT_SEMESTER("default semester");
    private final String semester;

    Semester(String semester) {
        this.semester = semester;
    }

    public static String getList() {
        StringBuilder types = new StringBuilder();
        for (Semester type : Semester.values()) {
            if (!type.equals(DEFAULT_SEMESTER) ) {
                types.append(type.name()+" - "+type.ordinal()).append("; ");
            }
        }
        return types.substring(0, types.length() - 2);
    }

}
