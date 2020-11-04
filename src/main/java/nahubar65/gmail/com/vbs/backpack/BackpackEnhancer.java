package nahubar65.gmail.com.vbs.backpack;

import nahubar65.gmail.com.backpacksystem.api.Backpack;

import java.util.HashMap;
import java.util.Map;

public interface BackpackEnhancer {

    default Map<Integer, Double> defaultPrices() {
        Map<Integer, Double> prices = new HashMap<>();
        prices.put(1, 1000.0);
        prices.put(2, 2000.0);
        prices.put(3, 3000.0);
        prices.put(4, 4000.0);
        prices.put(5, 5000.0);
        prices.put(6, 6000.0);
        return prices;
    }

    double getPrice(int level);

    boolean upgrade(Backpack backpack);

    void reload();
}