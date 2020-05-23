package eu.mcone.gamble.api.utils;

import java.util.*;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class OrderUtils {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        LinkedHashMap<K, V> reverseSortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }

    public static <K, V> Map<K, V> sortByKeyDescending(Map<K, V> map) {
        Map<K, V> reversed = new TreeMap<K, V>(Collections.reverseOrder());
        reversed.putAll(map);
        return reversed;

    }

}
