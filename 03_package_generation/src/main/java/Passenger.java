import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Passenger {

    private String name = null;
    private int countOfPackages = 0;
    private List<ProhibitedItem> listOfProhibitedItems = new ArrayList();

    public Passenger(String passengerLine) {
        String[] itemSplit = passengerLine.split(";");
        this.name = itemSplit[0];
        this.countOfPackages = Integer.parseInt(itemSplit[1]);
        if (!itemSplit[2].equals("-")) {
            String prohibitedItems = itemSplit[2].replace("[", "").replace("]","");
            String[] prohibitedItemSplit = prohibitedItems.split(";");
            for (int i = 0; i < prohibitedItemSplit.length; i++) {
                String[] parameterSplit = prohibitedItemSplit[i].split(",");
                ProhibitedItem prohibitedItem = new ProhibitedItem(parameterSplit[0],
                                                         Integer.parseInt(parameterSplit[1]),
                                                         Integer.parseInt(parameterSplit[2]));
                this.listOfProhibitedItems.add(prohibitedItem);
            }
        }
    }

    public int  toFile(int packageId) throws IOException, InterruptedException {
        char[] validCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', '!', '@', '#', '$', '^', '&', '*', '(', ')', '_', '-', '+', '='};

        char[][] plainContent = new char[5][10000];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 10000; i++) {
                plainContent[j][i] = validCharacters[(int)(System.currentTimeMillis()+i * 17) % validCharacters.length];
                Thread.sleep(0,0);
            }
        }

        for (int i = 0; i < this.countOfPackages; i++) {
            int prohibitedItemCounter = 0;
            String baggageName = "./src/main/resources/";
            if (packageId < 10) {
                baggageName = "00" + Integer.toString(packageId);
            } else if (packageId < 100) {
                baggageName = "0" + Integer.toString(packageId);
            } else {
                baggageName = Integer.toString(packageId);
            }
            baggageName += "_baggage.txt";

            FileWriter writer = new FileWriter(baggageName);

            for (int j = 0; j < 5; j++) {
                if (prohibitedItemCounter < listOfProhibitedItems.size()) {
                    if (listOfProhibitedItems.get(prohibitedItemCounter).getInPackage() == i + 1 &&
                        listOfProhibitedItems.get(prohibitedItemCounter).getInLayer() == j + 1) {
                        char[] modified = plainContent[j];
                        int itemLength = 0;
                        String item = "";
                        switch (listOfProhibitedItems.get(prohibitedItemCounter).getTypeOfProhibitedItem()) {
                            case "K":
                                item = "kn!fe";
                                itemLength = item.length();
                                break;

                            case "W":
                                item = "glock|7";
                                itemLength = item.length();
                                break;

                            case "E":
                                item = "exp|os!ve";
                                itemLength = item.length();
                                break;
                        }

                        int startIndex = (int) ((System.currentTimeMillis() * 17) % 10000) - itemLength;
                        for (int k = startIndex; k < startIndex + itemLength; k++) {
                            modified[k] = item.charAt(k - startIndex);
                        }
                        writer.write(modified);
                        prohibitedItemCounter++;
                    } else {
                        writer.write(plainContent[j]);
                    }
                }else {
                    writer.write(plainContent[j]);
                }
                
                writer.write('\n');
            }

            writer.close();
            packageId++;
        }

        return packageId;
    }
}
