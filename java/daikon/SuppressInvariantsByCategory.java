package daikon;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import daikon.inv.Invariant;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static daikon.Daikon.*;

/**
 * @author Juan C. Alonso
 */
public class SuppressInvariantsByCategory {

    public static List<Invariant> getSuppressedCategoriesInvariants(List<Invariant> invariants) {
        List<String> invariantsToSuppress = new ArrayList<>();

        // Read invariants taxonomy
//        String filePath = "daikon/config/taxonomy.json";
//        String filePath = "utils/taxonomy.json";
        String filePath = "C:\\Users\\jcav\\Documents\\GitHub\\daikon_modified\\out\\artifacts\\daikon_modified_jar\\utils\\taxonomy.json";
        JSONObject jsonObject = readJsonObject(filePath);
        if(suppress_arithmetic_comparisons) {
            invariantsToSuppress.addAll(getInvariantsFromCategory(jsonObject, "arithmetic_comparisons"));
        }

        if(suppress_string_comparisons) {
            invariantsToSuppress.addAll(getInvariantsFromCategory(jsonObject, "string_comparisons"));
        }

        if(suppress_specific_formats) {
            invariantsToSuppress.addAll(getInvariantsFromCategory(jsonObject, "specific_formats"));
        }

        if(suppress_specific_values) {
            invariantsToSuppress.addAll(getInvariantsFromCategory(jsonObject, "specific_values"));
        }

        if(suppress_array_properties) {
            invariantsToSuppress.addAll(getInvariantsFromCategory(jsonObject, "array_properties"));
        }


        if(invariantsToSuppress.isEmpty()) {
            return new ArrayList<>();
        }

        return invariants.stream()
                .filter(x -> invariantsToSuppress.contains(x.getClass().getName()))
                .collect(Collectors.toList());
    }

    public static JSONObject readJsonObject(String filePath) {

        JSONObject jsonObject = null;

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON content
            jsonObject = new JSONObject(jsonContent);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    private static List<String> getInvariantsFromCategory(JSONObject jsonObject, String category) {
        JSONArray jsonArray = (JSONArray) jsonObject.get(category);

        List<String> res = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                String element = jsonArray.getString(i);
                res.add(element);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return res;
    }


}
