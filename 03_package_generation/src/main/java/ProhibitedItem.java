public class ProhibitedItem {
    private String typeOfProhibitedItem = "";
    private int inPackage = 1;
    private int inLayer = 1;

    public ProhibitedItem(String typeOfProhibitedItem, int inPackage, int inLayer) {
        this.typeOfProhibitedItem = typeOfProhibitedItem;
        this.inPackage = inPackage;
        this.inLayer = inLayer;
    }

    public String getTypeOfProhibitedItem() {
        return typeOfProhibitedItem;
    }

    public int getInPackage() {
        return inPackage;
    }

    public int getInLayer() {
        return inLayer;
    }
}
