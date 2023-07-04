package daikon;

import daikon.inv.Invariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static daikon.RemoveSubStringRedundancies.getVariablesSorted;

/**
 * @author Juan C. Alonso
 */
public class SuppressInputOnlyInvariants {

    public static List<String> getVariablesFromInvariant(Invariant invariant) {

        return getVariablesSorted(
                Arrays.asList(invariant.varNames()
                        .replace("(", "")
                        .replace(")", "")
                        .split(",")),
                invariant.toString()
        );

    }

    // Remove all invariants that Only contain input variables
    public static List<Invariant> getInputOnlyInvariants(List<Invariant> invariants) {
        List<Invariant> res = new ArrayList<>();

        for(Invariant invariant: invariants) {
            if(getVariablesFromInvariant(invariant).stream().allMatch(x -> x.startsWith("input."))) {
                res.add(invariant);
            }
        }

        return res;
    }
}
