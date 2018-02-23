package sample;
import java.io.File;
import java.util.*;

/***
 * This class takes all the spam words into consideration when counting words:
 * New words aren't counted, since you only care about words that exists in spam folders
 * */

public class WordCounterHam extends WordCounter{
    protected Set<String> spamWord;

    public WordCounterHam(Set<String> spamWord) {
        super();
        this.spamWord = spamWord;
        initializeEmptyWordCounts();
    }

    @Override
    protected void countWord(String word) {
        if(this.spamWord.contains(word)) {
            if (wordCounts.containsKey(word)) {
                int oldCount = wordCounts.get(word);
                wordCounts.put(word, oldCount + 1);
            }
//            else {
//                wordCounts.put(word, 1);
//            }
        }
    }

    private void initializeEmptyWordCounts(){
        Map<String,Integer> emptyWordCounts = new TreeMap<>();
        for(String word: this.spamWord){
            emptyWordCounts.put(word, 0);
            //System.out.println("SDFHSDF");
        }
        this.wordCounts = emptyWordCounts;
    }
}
