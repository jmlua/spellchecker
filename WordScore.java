/**
 * @author Claire Jiasin Lua WordScore class implements methods related to a
 *         suggested word and it's score; compared to another word's.
 */

class WordScore implements Comparable<WordScore> {
    String word;
    double score;

    /**
     * @param word
     * @param score
     * @return
     */
    public WordScore(String word, double score) {
        this.word = word;
        this.score = score;
    }

    /**
     * @param word
     * @return
     */
    public WordScore(String word) {
        this.word = word;
        this.score = 0;
    }

    /**
     * @return String
     */
    public String getWord() {
        return word;
    }

    /**
     * @return double
     */
    public double getScore() {
        return score;
    }

    /**
     * Compare 2 WordScore
     * @param b the other WordScore to compare
     * @return int -1 if a < b, 1 if b > a else 0
     */
    public int compareTo(WordScore b) {
        if (score < b.score) {
            return -1;
        }
        if (score > b.score) {
            return 1;
        }
        return word.compareTo(b.word) * -1;
    }

    /**
     * @return String
     */
    public String toString() {
        return  String.format("%s - %.2f", word, score);
    }
}
