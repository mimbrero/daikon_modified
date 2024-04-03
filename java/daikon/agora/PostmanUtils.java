package daikon.agora;

/**
 * @author Juan C. Alonso
 */
public class PostmanUtils {


    // TODO: THIS METHOD MUST BE IDENTICAL TO THE ONE IN DAIKON, every modification performed here must be performed in Daikon too!!!
    // TODO: Document properly (with multiple input/output example)
    // TODO: Consider moving to a different class
    // TODO: Program points that are nested arrays? (e.g., GitHub)
    // TODO: Replace special characters (e.g., kebab case is not allowed in JS)
    // Returns the variable name in the format used in the Postman assertion
    public static String getPostmanVariableName(String originalVariableName) {

        // TODO: Test this if clause
        if(!originalVariableName.startsWith("input.") &&
                !originalVariableName.startsWith("return.") &&
                !originalVariableName.startsWith("size(input.") &&
                !originalVariableName.startsWith("size(return.")
        ) {
            throw new RuntimeException("Unexpected variable name");
        }

        String postmanVariableName = originalVariableName;

        // If the variable contains shift
        String shiftSuffix = "";
        if (postmanVariableName.matches(".* [+]{1}[0-9]{1,}$")) {            // increase
            int plusIndex = postmanVariableName.lastIndexOf(" +");
            shiftSuffix = "_plus_" + postmanVariableName.substring(plusIndex+2);
            postmanVariableName = postmanVariableName.substring(0, plusIndex);

        } else if (postmanVariableName.matches(".* [-]{1}[0-9]{1,}$")) {    // decrease
            int minusIndex = postmanVariableName.lastIndexOf(" -");
            shiftSuffix = "_minus_" + postmanVariableName.substring(minusIndex+2);
            postmanVariableName = postmanVariableName.substring(0, minusIndex);
        }

        // If the variable is the size of an array
        if(postmanVariableName.startsWith("size(")) {

            // Remove "size(" (at the start) and ")" (at the end) characters
            postmanVariableName = postmanVariableName.substring("size(".length(), postmanVariableName.length() - 1);

            // Add suffix
            postmanVariableName = postmanVariableName + "_size";
        }

        // Remove array special characters and add a suffix indicating that the variable is an array
        if (postmanVariableName.contains("[]") || postmanVariableName.contains("[..]")) {

            // Remove characters
            postmanVariableName = postmanVariableName.replace("[]", "");
            postmanVariableName = postmanVariableName.replace("[..]", "");

            // Add suffix
            postmanVariableName += "_array";
        }

        // Add shift suffix
        postmanVariableName += shiftSuffix;

        // Replace variable hierarchy separator with snake_case
        postmanVariableName = postmanVariableName.replace(".", "_");


        return postmanVariableName;

    }

}
