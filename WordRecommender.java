/**
 * @author Claire Jiasin Lua WordRecommender class contains methods to detect
 *         and recommend similar words.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class WordRecommender {
    // List of words in the dictionary.
    HashSet<String> dictionaryWords;

    /**
     * loadDictionary methods takes the inputted dictionary file and loads it
     * into the dictionaryWords ArrayList. It also returns the number of words
     * in the dictionary file.
     * 
     * @param fileName path to the dictionary file
     * @return int the number of words loaded
     * @throws FileNotFoundException
     */
    private int loadDictionary(String fileName) throws FileNotFoundException {
        int wordCount = 0;

        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            dictionaryWords.add(sc.nextLine());
            wordCount++;
        }
        sc.close();

        return wordCount;
    }

    /**
     * dictionaryWordsCount method returns the number of words in the loaded
     * dictionary.
     * 
     * @return int
     */
    public int dictionaryWordsCount() {
        return dictionaryWords.size();
    }

    /**
     * The WordRecommender constructor class takes in the dictionary's filename
     * and topN (number of desired suggestions).
     * 
     * @param fileName
     * @param max_suggestion_size
     * @return
     * @throws FileNotFoundException
     */
    public WordRecommender(String fileName) throws FileNotFoundException {
        dictionaryWords = new HashSet<String>();
        loadDictionary(fileName);
    }

    /**
     * isValidWord checks that a word is a valid word as long as it contains i
     * or a.
     * 
     * @param word
     * @return boolean
     */
    public boolean isValidWord(String word) {
        return dictionaryWords.contains(word) || word.equals("i")
                || word.equals("a");
    }

    /**
     * getSimilarityMetric computes two measures of similarity (leftSimilarity,
     * rightSimilarity), and returns the average.
     * 
     * Function 1 in assignment
     * 
     * @param word1
     * @param word2
     * @return double
     */
    public double getSimilarityMetric(String word1, String word2) {
        int commonLen = Math.min(word1.length(), word2.length());
        int leftSimilarity = 0;
        int rightSimilarity = 0;
        for (int i = 0; i < commonLen; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                leftSimilarity++;
            }
        }
        for (int i = 1; i <= commonLen; i++) {
            if (word1.charAt(word1.length() - i) == word2
                    .charAt(word2.length() - i)) {
                rightSimilarity++;
            }
        }
        return (leftSimilarity + rightSimilarity) / 2.0;
    }

    /**
     * getCommonPercent calculates the similarity of 2 given words by taking the
     * number of letters that are common across the two words, divided by the
     * total number of letters both words.
     * 
     * @param word1
     * @param word2
     * @return double
     */
    public double getCommonPercent(String word1, String word2) {
        Set<Character> chars1 = new HashSet<Character>();
        Set<Character> chars2 = new HashSet<Character>();

        Set<Character> union = new HashSet<Character>();
        Set<Character> intersect = new HashSet<Character>();

        for (int i = 0; i < word1.length(); i++) {
            chars1.add(word1.charAt(i));
        }
        for (int i = 0; i < word2.length(); i++) {
            chars2.add(word2.charAt(i));
        }
        union.addAll(chars1);
        union.addAll(chars2);

        intersect.addAll(chars1);
        intersect.retainAll(chars2);

        return intersect.size() / (double) union.size();
    }

    public int getCommon(String word1, String word2) {
        Set<Character> chars1 = new HashSet<Character>();
        Set<Character> chars2 = new HashSet<Character>();

        Set<Character> intersect = new HashSet<Character>();

        for (int i = 0; i < word1.length(); i++) {
            chars1.add(word1.charAt(i));
        }
        for (int i = 0; i < word2.length(); i++) {
            chars2.add(word2.charAt(i));
        }
        intersect.addAll(chars1);
        intersect.retainAll(chars2);

        return intersect.size();
    }

    /**
     * isValidWordLength checks that a candidate word's length is within +/- ​n​
     * characters .
     * 
     * @param word
     * @param candidateWord
     * @param maxLengthDiff
     * @return boolean
     */
    boolean isValidWordLength(String word, String candidateWord,
            int maxLengthDiff) {
        if (Math.abs(word.length() - candidateWord.length()) > maxLengthDiff) {
            return false;
        }
        return true;
    }

    /**
     * getWordScore optionally returns a WordScore object which has the
     * commonPercent between a candidate word, and an checked word.
     * 
     * @param word
     * @param candidateWord
     * @param maxLengthDiff
     * @param minCommonPercent
     * @return Optional<WordScore>
     */
    Optional<WordScore> getWordScore(String word, String candidateWord,
            int maxLengthDiff, double minCommonPercent) {
        if (!isValidWordLength(word, candidateWord, maxLengthDiff)) {
            return Optional.empty();
        }
        double commonPercent = getCommonPercent(word, candidateWord);
        if (commonPercent < minCommonPercent) {
            return Optional.empty();
        }
        return Optional.ofNullable(new WordScore(candidateWord,
                getSimilarityMetric(word, candidateWord)));
    }

    /**
     * getWordScore returns the WordScore of a candidate word with default
     * score.
     * 
     * @param word          word to be checked
     * @param candidateWord potential candidate for a suggested word
     * @param maxLengthDiff number of common letters between 2 words
     * @return Optional<WordScore>
     */
    Optional<WordScore> getWord(String word, String candidateWord,
            int maxLengthDiff) {
        if (!isValidWordLength(word, candidateWord, maxLengthDiff)) {
            return Optional.empty();
        }
        return Optional.ofNullable(new WordScore(candidateWord));
    }

    /**
     * getWordSuggestionsInternal returns the word suggestions for a misspelled
     * word.
     * 
     * @param word
     * @param maxLengthDiff
     * @param minCommonPercent
     * @param topN
     * @return WordScores
     */
    ArrayList<String> getWordSuggestionsInternal(String word, int maxLengthDiff,
            double minCommonPercent, int topN, boolean debug) {
        WordQueue wordQueue = new WordQueue(topN);
        for (String candidateWord : dictionaryWords) {
            Optional<WordScore> result = getWordScore(word, candidateWord,
                    maxLengthDiff, minCommonPercent);
            if (result.isPresent()) {
                wordQueue.add(result.get());
            }
        }
        return wordQueue.clearAndGetWords(debug);
    }

    /**
     * public method of function 2 in assignment. This calls
     * getWordSuggestionsInternal and returns suggestions in an ArrayList.
     * 
     * @param word
     * @param maxLengthDiff
     * @param minCommonPercent
     * @param topN
     * @return ArrayList<String>
     */
    public ArrayList<String> getWordSuggestions(String word, int maxLengthDiff,
            double minCommonPercent, int topN) {
        return getWordSuggestionsInternal(word, maxLengthDiff, minCommonPercent,
                topN, false);
    }

    /**
     * @param word
     * @param maxLengthDiff
     * @param minCommonPercent
     * @param topN
     * @return ArrayList<String>
     */
    public ArrayList<String> getWordSuggestionsDebug(String word,
            int maxLengthDiff, double minCommonPercent, int topN) {
        return getWordSuggestionsInternal(word, maxLengthDiff, minCommonPercent,
                topN, true);
    }

    /**
     * Given a word and a list of words, getWordsWithCommonLetters returns the
     * list of words in the dictionary that have at least (>=) n letters in
     * common.
     * 
     * Function 3 in assignment
     * 
     * @param word
     * @param listOfWords
     * @param minCommon   min number of common words
     * @return WordScores
     */
    public ArrayList<String> getWordsWithCommonLetters(String word,
            ArrayList<String> listOfWords, int minCommon) {
        WordQueue wordQueue = new WordQueue();
        for (String candidateWord : listOfWords) {
            if (getCommon(word, candidateWord) >= minCommon) {
                wordQueue.add(new WordScore(candidateWord));
            }
        }
        return wordQueue.clearAndGetWords(false);
    }

    /**
     * prettyPrint calls toShortString function in WordScores to return an
     * enumerated list of words.
     * 
     * Function 4 in assignment
     * 
     * @param list
     * @return String
     */
    public String prettyPrint(ArrayList<String> list) {
        return SpellSuggestion.prettyPrint(list);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            WordRecommender wordRecommender =
                    new WordRecommender("engDictionary.txt");
            System.out.println(wordRecommender.dictionaryWordsCount());

            System.out.println("ewook - Spell Suggestions: ");
            System.out.println(SpellSuggestion.prettyPrint(wordRecommender
                    .getWordSuggestionsDebug("ewook", 2, 0.7, 5)));
            System.out.println(SpellSuggestion.prettyPrint(
                    wordRecommender.getWordSuggestions("ewook", 2, 0.7, 5)));

            System.out.println("woke - Spell Suggestions: ");
            System.out.println(SpellSuggestion.prettyPrint(wordRecommender
                    .getWordSuggestionsDebug("woke", 2, 0.7, 5)));
            System.out.println(SpellSuggestion.prettyPrint(
                    wordRecommender.getWordSuggestions("woke", 2, 0.7, 5)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
