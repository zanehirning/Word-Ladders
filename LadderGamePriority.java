import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class LadderGamePriority extends LadderGame{
    ArrayList<ArrayList<String>> wordSize = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> copyDict = new ArrayList<ArrayList<String>>();
    public LadderGamePriority(String dictionaryFile) {
        readDictionary(dictionaryFile);
        for (int i = 0; i < wordSize.size(); i++) {
            copyDict.add(new ArrayList<String>());
            for (int j = 0; j < wordSize.get(i).size(); j++) {
                copyDict.get(i).add(wordSize.get(i).get(j));
            }
        }
    }

    public void play(String start, String end) {
        if (start.length() != end.length()) {
            System.out.printf("Seeking priority solution from %s -> %s \n\t Start and end words are not the same size\n", start, end);
            return;
        }
        if (!wordSize.get(start.length()).contains(start) || !wordSize.get(end.length()).contains(end)) {
            System.out.printf("Seeking priority solution from %s -> %s \n\t Start or end word is not in dictionary.\n", start, end);
            return;
        }
        boolean complete = false;
        int qCount = 1;
        AVLTree<WordInfoPriority> avlTree = new AVLTree<>();
        WordInfoPriority word = new WordInfoPriority(start, 1, lettersAway(start, end), start);
        avlTree.insert(word);
        ArrayList<String> oneAways = oneAway(word.getWord());
        for (int i=0; i<oneAways.size(); i++) {
            avlTree.insert(new WordInfoPriority(oneAways.get(i), word.getMoves() + 1, word.getMoves() + 1 + lettersAway(oneAways.get(i), end), word.getHistory() + " " +oneAways.get(i)));
        }
        oneAways.removeAll(oneAways);
        ArrayList<WordInfoPriority> prevWords = new ArrayList<>();

        while (!avlTree.isEmpty() && !complete) {
            WordInfoPriority leastWork = avlTree.deleteMin();
            oneAways = oneAway(leastWork.getWord());
            word = leastWork;
            loop: for (int i=0; i<oneAways.size(); i++) {
                WordInfoPriority nextStep = new WordInfoPriority(oneAways.get(i), word.getMoves() + 1, word.getMoves() + 1 + lettersAway(oneAways.get(i), end), word.getHistory() + " " +oneAways.get(i));
                for (WordInfoPriority used : prevWords) {
                    if (used.getWord().equals(nextStep.getWord())) {
                        if (used.getMoves() > nextStep.getMoves()) {
                            used = nextStep;
                        }
                        else {
                            continue loop;
                        }
                    }
                }
                avlTree.insert(nextStep);
                prevWords.add(nextStep);
                if (oneAways.get(i).equals(end)) {
                    System.out.printf("Seeking priority solution from %s -> %s \n\t [%s] total enqueues %d\n", start, end, leastWork.getHistory() + " " + end, qCount);
                    complete=true;
                }
                else {
                    qCount+=1;
                }
            }
            if (!complete && avlTree.isEmpty()) {
                System.out.printf("Seeking priority solution from %s -> %s \n\t No ladder was found\n", start, end);
            }
        }
    }


    public ArrayList<String> oneAway(String word) {
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
        return words;
    }

    public void listWords(int length, int howMany) {
        for (int i=0; i<howMany; i++) {
            System.out.println(wordSize.get(length).get(i));
        }
    }

    private int lettersAway(String first, String end) {
        int lettersAway=0;
        for (int i=0; i<first.length(); i++) {
            if (first.charAt(i) != end.charAt(i)) {
                lettersAway += 1;
            }
        }
        return lettersAway;
    }

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
