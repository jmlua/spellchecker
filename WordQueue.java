/**
 * @author Claire Jiasin Lua
 * WordQueue class implements a priority queue that uses the similarity metric to return the top n suggestions.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class WordQueue {
    PriorityQueue<WordScore> wordScores = new PriorityQueue<WordScore>();;
    int size = -1; // default to unlimited size;
    
    /** 
     * WordQueue constructor 
     */
    public WordQueue() {
    }
    
    /** 
     * @param size
     * @return 
     */
    public WordQueue(int size) {
        this.size = size;
    }
    
    /** 
     * add wordScore to WordQueue
     * @param wordScore
     */
    public void add(WordScore wordScore) {
        wordScores.add(wordScore);
        if (size == -1) {
            return;
        }
        while (wordScores.size() > size) {
            wordScores.poll();
        }
    }

    /** 
     * Clear the queue and return the words.
     * @param debug return debug info if true
     * @return ArrayList<String> words in order
     */
    public ArrayList<String> clearAndGetWords(boolean debug) {
        ArrayList<String> result = new ArrayList<String>();
        while (!wordScores.isEmpty()) {
            WordScore wordScore = wordScores.poll();
            if (debug) {
                result.add(wordScore.toString());
            } else {
                result.add(wordScore.getWord());
            }
        }
        // Reverse so that best word is in front.
        Collections.reverse(result);
        return result;
    }
}
