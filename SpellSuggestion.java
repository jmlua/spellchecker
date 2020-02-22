import java.util.ArrayList;

/**
 * @author Claire Jiasin Lua SpellSuggestion class handles methods related to
 *         the suggestions (i.e. suggested words) for a given word.
 */

public class SpellSuggestion {
    String originalWord;
    ArrayList<String> suggestions;

    /**
     * @param word
     * @param suggestions
     * @return
     */
    public SpellSuggestion(String word, ArrayList<String> suggestions) {
        this.originalWord = word;
        this.suggestions = suggestions;
    }

    /**
     * getOriginalWord returns the original word that is checked.
     * 
     * @return String
     */
    public String getOriginalWord() {
        return originalWord;
    }

    /**
     * getSuggestions returns an ArrayList of suggestions.
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getSuggestions() {
        return suggestions;
    }

    /**
     * prettyPrint takes the suggestions and returns a String which when printed
     * will have the list elements with a number in front of them.
     * 
     * @param list
     * @return String
     */
    public static String prettyPrint(ArrayList<String> list) {
        if (list.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(String.format("%2d. %s\n", i + 1, list.get(i)));
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    /**
     * @return String
     */
    public String toSuggestionsString() {
        return prettyPrint(suggestions);
    }

    /**
     * Handles error when user input a suggestion number that is out of bounds.
     * 
     * @param index
     * @return String
     * @throws IndexOutOfBoundsException
     */
    public String getSuggestion(int index) throws IndexOutOfBoundsException {
        return suggestions.get(index);
    }
}
