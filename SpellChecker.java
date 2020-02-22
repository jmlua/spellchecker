/**
 * @author Claire Jiasin Lua SpellChecker class puts together all the
 *         classes for the SpellChecker project. It has instance variables which
 *         are the WordRecommender, words from user input file, Spell
 *         Suggestions and scanners.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
    WordRecommender wordRecommender;
    BufferedWriter output;
    SpellSuggestion spellSuggestion;
    Scanner lineScanner;
    Scanner wordScanner;

    /**
     * getOutputPath returns output (checked file) file name and extension.
     * 
     * @param inputPath
     * @return String
     */
    public String getOutputPath(String inputPath) {
        String fileName;
        String ext = "";
        if (inputPath.indexOf(".") > 0) {
            int dotIndex = inputPath.lastIndexOf(".");
            fileName = inputPath.substring(0, dotIndex);
            ext = inputPath.substring(dotIndex, inputPath.length());
        } else {
            fileName = inputPath;
        }
        return fileName + "_chk" + ext;
    }

    /**
     * SpellChecker constructor
     * @param wordRecommender
     * @param inputPath path to the input file to spell check
     * @return
     * @throws IOException
     */
    public SpellChecker(WordRecommender wordRecommender, String inputPath)
            throws IOException {
        this.wordRecommender = wordRecommender;

        File file = new File(inputPath);
        FileWriter fileWriter =
                new FileWriter(new File(getOutputPath(inputPath)));
        output = new BufferedWriter(fileWriter);
        lineScanner = new Scanner(file);
    }

    /**
     * Write a word to the file.
     * @param word the word to write to the file
     * @throws IOException
     */
    void writeWord(String word) throws IOException {
        if (word.isEmpty()) {
            if (!wordScanner.hasNext()) {
                output.append("\n");
            }
            return;
        }
        output.append(word);
        if (wordScanner.hasNext()) {
            output.append(" ");
        } else {
            output.append("\n");
        }
    }

    /**
     * nextWordError() gets the next word in the input file and return
     * SpellSuggestion if there is a mispelled word.
     * @return SpellSuggestion suggestion for the misspelled word
     * @throws IOException
     */
    SpellSuggestion nextWordError() throws IOException {
        if (wordScanner == null) {
            return null;
        }
        while (wordScanner.hasNext()) {
            String nextWord = wordScanner.next();
            if (wordRecommender.isValidWord(nextWord)) {
                writeWord(nextWord);
            } else {
                ArrayList<String> suggestions = wordRecommender
                        .getWordSuggestions(nextWord, 2, 0.7, 10);
                return new SpellSuggestion(nextWord, suggestions);
            }
        }
        wordScanner.close();
        return null;
    }

    /**
     * Check the input file for mispelled word and write non-mispelled word 
     * to file. Stops and returns a misspelled word if there is one.
     * Return null where there is no more error
     * @return SpellSuggestion
     * @throws IOException
     */
    public SpellSuggestion nextError() throws IOException {
        while (true) {
            SpellSuggestion spellSuggestion = nextWordError();
            if (spellSuggestion != null) {
                return spellSuggestion;
            }
            if (!lineScanner.hasNextLine()) {
                return null;
            }
            String nextLine = lineScanner.nextLine();
            wordScanner = new Scanner(nextLine);
        }
    }

    /**
     * Set the correct word for the mispelled word.
     * @param word replacement word for the mis-spelled word.
     * @throws IOException
     */
    public void setCorrection(String word) throws IOException {
        writeWord(word);
    }

    /**
     * Close and write the file. Must call this function after nextError
     * returns null.
     * @throws IOException
     */
    public void close() throws IOException {
        output.flush();
        output.close();
    }
}
