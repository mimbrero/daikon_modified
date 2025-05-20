package daikon;

import daikon.inv.Invariant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

        String resourcePath = "/daikon/config/taxonomy.json";
        JSONObject jsonObject = readJsonObject(resourcePath);
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

    public static JSONObject readJsonObject(String resourcePath) {
        InputStream inputStream =
                SuppressInvariantsByCategory.class.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new RuntimeException("Cannot find resource in classpath: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String jsonContent = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            return new JSONObject(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Error reading resource from classpath: " + resourcePath);
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing JSON from resource: " + resourcePath);
        }
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
