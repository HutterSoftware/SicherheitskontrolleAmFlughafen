package data;

public class ScanResult {
    public static String ITEM_TYPE_CLEAN = "CLEAN";
    public static String ITEM_TYPE_PROHIBITED_ITEM = "PROHIBITED";
    public static String PROHIBITED_ITEM_TYPE_KNIFE = "KNIFE";
    public static String PROHIBITED_ITEM_TYPE_WEAPON = "WEAPON";
    public static String PROHIBITED_ITEM_TYPE_EXPLOSIVE = "EXPLOSIVES";

    private String itemType;
    private String prohibitedItemType;
    private int[] position;

    public ScanResult(String resultType, String prohibitedItemType, int[] position) {
        this.itemType = resultType;
        this.prohibitedItemType = prohibitedItemType;
        this.position = position;
    }

    public String getItemType() {
        return itemType;
    }

    public String getProhibitedItemType() {
        return prohibitedItemType;
    }

    public int[] getPosition() {
        return position;
    }
}
