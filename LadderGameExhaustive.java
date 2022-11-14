import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

public class LadderGameExhaustive extends LadderGame{
    ArrayList<ArrayList<String>> wordSize = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> copyDict = new ArrayList<ArrayList<String>>();
    public LadderGameExhaustive(String dictionaryFile) {
        readDictionary(dictionaryFile);
        for (int i=0; i<wordSize.size(); i++) {
            copyDict.add(new ArrayList<String>());
            for (int j = 0; j < wordSize.get(i).size(); j++) {
                copyDict.get(i).add(wordSize.get(i).get(j));
            }
        }
    }

    public void play(String start, String end) {
        if (start.length() != end.length()) {
            System.out.printf("Seeking exhaustive solution from %s -> %s \n\t Start and end words are not the same size\n", start, end);
            return;
        }
        if (!wordSize.get(start.length()).contains(start) || !wordSize.get(end.length()).contains(end)) {
            System.out.printf("Seeking exhaustive solution from %s -> %s \n\t Start or end word is not in dictionary.\n", start, end);
            return;
        }

        boolean complete = false;
        Queue q = new Queue();

        q.enqueue(new WordInfo(start, 0, start));
        int qCount = 1;

        copyDict = new ArrayList<ArrayList<String>>();
        for (int i=0; i<wordSize.size(); i++) {
            copyDict.add(new ArrayList<String>());
            for (int j = 0; j < wordSize.get(i).size(); j++) {
                copyDict.get(i).add(wordSize.get(i).get(j));
            }
        }
        copyDict.get(start.length()).remove(start);

        while (!q.isEmpty() && !complete) {
            WordInfo word = (WordInfo) q.dequeue();
            ArrayList<String> oneAways = oneAway(word.getWord(), true);
            for (int i=0; i<oneAways.size(); i++)
                q.enqueue(new WordInfo(oneAways.get(i), word.getMoves()+1, word.getHistory() + " " + oneAways.get(i)));
                qCount += 1;
            if (Objects.equals(word.getWord(), end)) {
                complete = true;
                System.out.printf("Seeking exhaustive solution from %s -> %s \n\t [%s] total enqueues %d\n", start, end, word.getHistory(), qCount);
            }
            if (!complete && q.isEmpty()) {
                System.out.printf("Seeking exhaustive solution from %s -> %s \n\t No ladder was found\n", start, end);
            }
        }
    }

    public ArrayList<String> oneAway(String word, boolean withRemoval) {
        ArrayList<String> words = new ArrayList<>();
        for (int i=0; i < copyDict.get(word.length()).size(); i++) {
            int correctLetter = 0;
            for (int j=0; j<word.length(); j++) {
                if (word.charAt(j) == copyDict.get(word.length()).get(i).charAt(j))
                    correctLetter++;
            }
            if (correctLetter == word.length()-1) {
                words.add(copyDict.get(word.length()).get(i));
            }
        }
        if (withRemoval) {
            for (int k=0; k<words.size(); k++) {
                copyDict.get(word.length()).remove(words.get(k));
            }
        }
        return words;
    }

    public void listWords(int length, int howMany) {
        for (int i=0; i<howMany; i++) {
            System.out.println(wordSize.get(length).get(i));
        }
    }

    /*
        Reads a list of words from a file, putting all words of the same length into the same array.
     */
    private void readDictionary(String dictionaryFile) {
        File file = new File(dictionaryFile);
        ArrayList<String> allWords = new ArrayList<>();
        //
        // Track the longest word, because that tells us how big to make the array.
        int longestWord = 0;
        try (Scanner input = new Scanner(file)) {
            //
            // Start by reading all the words into memory.
            //ArrayList<String> allWords = new ArrayList<String>();
            while (input.hasNextLine()) {
                String word = input.nextLine().toLowerCase();
                allWords.add(word);
                longestWord = Math.max(longestWord, word.length());
            }

            wordSize = new ArrayList<>();
            for (int i=0; i <= longestWord; i++) {
                ArrayList<String> words = new ArrayList<>();
                wordSize.add(words);
                for (int j = 0; j < allWords.size(); j++) {
                    if (allWords.get(j).length() == i) {
                        words.add(allWords.get(j));
                    }
                }
            }
        }
        catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the dictionary: " + ex);
        }
    }
}