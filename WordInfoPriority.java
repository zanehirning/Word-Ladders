public class WordInfoPriority implements Comparable<WordInfoPriority> {
    private int priority;
    private String word;
    private int moves;
    private String history;

    public WordInfoPriority(String word, int moves, int estimatedWork) {
        this.word = word;
        this.moves = moves;
        this.priority = estimatedWork;
    }

    public WordInfoPriority(String word, int moves, int estimatedWork, String history) {
        this.word = word;
        this.moves = moves;
        this.priority = estimatedWork;
        this.history = history;
    }
    public String getWord() {
        return this.word;
    }

    public int getMoves() {
        return this.moves;
    }

    public String getHistory() {
        return this.history;
    }

    public int getPriority() {return this.priority;}

    public String toString() {
        return String.format("Word %s Moves %d Priority %d: History[%s]",
                word, moves, priority, history);
    }

    @Override
    public int compareTo(WordInfoPriority o) {
        if (this.priority > o.priority) {
            return 1;
        }
        if (this.priority < o.priority) {
            return -1;
        }
        return 0;
    }
}
