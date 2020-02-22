/**
 * @author Claire Jiasin Lua SpellCheckerRunner is a runner class with methods
 *         to support user input choices for mispelled words.
 */
import java.io.IOException;
import java.util.Scanner;

public class SpellCheckerRunner {

    /**
     * printInstructions prints instructions for users on the options to handle
     * a misspelled oword.
     * 
     * @param numSuggestions
     */
    static void printInstructions(int numSuggestions) {
        if (numSuggestions == 0) {
            System.out.println(
                    "Press ‘a’ for accept as is, ‘t’ for type in manually.");
        } else {
            System.out.println(
                    "Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.");
        }
    }

    /**
     * getCorrectedWord is a method that prints out the user interface texts and
     * returns the corrected word based on user input.
     * 
     * @param spellsuggestion
     */
    static String getCorrectedWord(Scanner scanner,
            SpellSuggestion spellsuggestion) {
        System.out.print("The word '" + spellsuggestion.originalWord
                + "' is misspelled.");
        int numSuggestions = spellsuggestion.getSuggestions().size();
        if (numSuggestions == 0) {
            System.out.println(
                    "\nThere are 0 suggestions in our dictionary for this word.");
        } else {
            System.out.println(" The following suggestions are available:");
            System.out.println(spellsuggestion.toSuggestionsString());
        }

        printInstructions(numSuggestions);

        while (true) {
            String userChoice = scanner.nextLine();
            if (userChoice.equals("r") && numSuggestions > 0) {
                System.out.println(
                        "Your word will now be replaced with one of the suggestions."
                                + "\n"
                                + "Enter the number corresponding to the word that you want to use for replacement.");
                while (true) {
                    String input = scanner.nextLine();
                    int userChoiceSuggestion;
                    try {
                        userChoiceSuggestion = Integer.parseInt(input);
                    } catch (Exception e) {
                        userChoiceSuggestion = -1;
                    }
                    if (userChoiceSuggestion < 1
                            || userChoiceSuggestion > numSuggestions) {
                        System.out.println(
                                "Invalid number entered. Please enter the number corresponding to the word that you want to use for replacement.");
                        continue;
                    }
                    return spellsuggestion
                            .getSuggestion(userChoiceSuggestion - 1);
                }

            } else if (userChoice.equals("a")) {
                return spellsuggestion.getOriginalWord();
            } else if (userChoice.equals("t")) {
                String correctedWord = scanner.nextLine();
                return correctedWord;
            } else {
                System.out.println("Invalid input!");
                printInstructions(numSuggestions);
            }
        }

    }

    /**
     * Main function to run for spell checker.
     */
    public static void RunSpellChecker() {
        while (true) {
            try {
                WordRecommender wordRecommender =
                        new WordRecommender("engDictionary.txt");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter path to input file: ");
                String inputPath = scanner.nextLine();
                SpellChecker spellChecker =
                        new SpellChecker(wordRecommender, inputPath);

                SpellSuggestion suggestion = spellChecker.nextError();
                while (suggestion != null) {
                    String correctedWord =
                            getCorrectedWord(scanner, suggestion);
                    spellChecker.setCorrection(correctedWord);
                    suggestion = spellChecker.nextError();
                }
                scanner.close();
                spellChecker.close();
                return;

            } catch (IOException e) {
                System.out.println("File operation error: " + e.toString());
                // e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        RunSpellChecker();
    }
}
