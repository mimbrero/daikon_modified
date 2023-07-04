package daikon;

import daikon.inv.Invariant;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Juan C. Alonso
 */
public class RemoveSubStringRedundancies {


    public static List<Invariant> getSubStringInvariants(List<Invariant> invariants) {

        List<Invariant> subStringInvariants = new ArrayList<>();
        for(Invariant inv: invariants) {
            if(inv.getClass().getName().equals("daikon.inv.binary.twoString.StdString$SubString")) {
                subStringInvariants.add(inv);
            }
        }


        return subStringInvariants;
    }

    public static List<Invariant> getRedundantInvariants(List<Invariant> invariants) {
        List<Pair<String, String>> pairs = getPairsFromInvariants(invariants);

        Map<String, Set<String>> filteredMap = removeRedundanciesFromMap(pairs);


        // Print resulting map
//        for(String key: filteredMap.keySet()) {
//            System.out.println("Key: " + key);
//            for(String value: filteredMap.get(key)){
//                System.out.println("- " + value);
//            }
//        }
        ///////////////////////////////////////////////

        List<Pair<String,String>> filteredPairs = filteredMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(value -> new Pair<>(entry.getKey(), value)))
                .collect(Collectors.toList());

        // Print resulting pairs
//        for(Pair<String,String> filteredPair: filteredPairs){
//            System.out.println(filteredPair);
//        }
        ///////////////////////////////////////////////

        // Get Redundant invariants
        List<Invariant> redundantInvariants = new ArrayList<>();
        for(Invariant inv: invariants){
            Pair<String, String> invariantPair = getPairFromSingleInvariant(inv);

            if(!filteredPairs.contains(invariantPair)){
                redundantInvariants.add(inv);
            }
        }

        return redundantInvariants;
    }


    private static List<Pair<String, String>> getPairsFromInvariants(List<Invariant> invariants){
        List<Pair<String, String>> res = new ArrayList<>();

        for(Invariant inv: invariants){
            Pair<String, String> pair = getPairFromSingleInvariant(inv);
            res.add(pair);
        }

        return res;
    }

    private static Pair<String, String> getPairFromSingleInvariant(Invariant invariant) {

        List<String> variables = getVariablesSorted(
                Arrays.asList(invariant.varNames().replace("(", "").replace(")", "").split(",")),
                invariant.toString()
                );
        return new Pair<>(variables.get(0).trim(), variables.get(1).trim());

    }

    public static List<String> getVariablesSorted(List<String> variables, String invariant) {
        Collections.sort(variables, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int index1 = invariant.indexOf(s1.replace("[..]", "[]"));
                int index2 = invariant.indexOf(s2.replace("[..]", "[]"));
                if (index1 != -1 && index2 != -1) {
                    return Integer.compare(index1, index2);
                } else {
                    return s1.compareTo(s2);
                }
            }
        });
        return variables;
    }


    private static Map<String, Set<String>> removeRedundanciesFromMap(List<Pair<String,String>> input){

        Map<String, Set<String>> matrix = input.stream()
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toSet())));

//        for(String key: matrix.keySet()) {
//            System.out.println("# " + key + ":");
//            for(String value: matrix.get(key)) {
//                System.out.println(value);
//            }
//        }

        // Remove redundancies
        Set<String> keys = keySort(matrix);
        for(String key: keys) {
            Set<String> value = matrix.get(key);

            // Iterate over a copy of the values to avoid ConcurrentModificationException
            // TODO: ORDER?
            for(String valueInRow: new HashSet<>(value)) {
                if(matrix.containsKey(valueInRow)) {
                    value.removeAll(matrix.get(valueInRow));
                }
            }
            matrix.replace(key, value);
        }

        return matrix;
    }


    // Sorts the keys according to the size of the value
    private static Set<String> keySort(Map<String, Set<String>> map) {

        Comparator<String> valueComparator = (k1, k2) -> {
            Set<String> value1 = map.get(k1);
            Set<String> value2 = map.get(k2);
            if (value1 == null && value2 == null) {
                return 0;
            } else if (value1 == null) {
                return 1;
            } else if (value2 == null) {
                return -1;
            } else {
                return Integer.compare(value1.size(), value2.size());
            }
        };

        // SortedMap created using the comparator
        Map<String, Set<String>> sorted = new TreeMap<>(valueComparator.reversed().thenComparing(Comparator.naturalOrder()));
        sorted.putAll(map);

        return sorted.keySet();
    }


}
