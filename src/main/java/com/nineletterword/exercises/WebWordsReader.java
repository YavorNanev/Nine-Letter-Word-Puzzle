package com.nineletterword.exercises;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebWordsReader {

    private final WebClientService webClientService;

    private static Optional<Set<String>> allWordsMap;

    public void readWordsFromUrl(String url) {

        var allWordsFlux = webClientService.doGetFlux(url, String.class);

        allWordsMap = allWordsFlux
                .collect(Collectors.toSet())
                .blockOptional();

        //Adding 'I' and 'A' to the list as they are missing from the web list
        allWordsMap.ifPresent(WebWordsReader::addMissingOneLetterWordsToTheSet);
        allWordsFlux
                .filter(word -> word.length() == 9)
                .collectList()
                .blockOptional()
                .ifPresent(a -> a.forEach(WebWordsReader::processWord));
    }

    private static boolean checkNestedWordSet(Set<String> set, String originalWord) {

        //base case - if no valid word is found set will be empty
        if (set.isEmpty()) {
            return false;
        }
        //base case - if  valid word is found set will contain A or I
        if (set.contains("I") || set.contains("A")) {
            set.clear();
            System.out.println(originalWord);
            return true;
        }
        //Add the current set to array to loop over each valid word at the current level
        Arrays.stream(set.toArray()).forEach(validWordAtTheCurrentLevel -> {
            String wordToCheck = validWordAtTheCurrentLevel.toString();
            //Looping over the word and remove one letter from it each time
            for (int i = 0; i < wordToCheck.length(); i++) {

                //Building the new word where letter at index i  removed
                String newWordToCheckWithOneLatterRemoved = wordToCheck.substring(0, i) + wordToCheck.substring(i + 1);

                if (checkIsValidWord(newWordToCheckWithOneLatterRemoved)) {
                    // Adding next level valid words
                    set.add(newWordToCheckWithOneLatterRemoved);
                }
            }
            //removing upper level words as they are not needed any more
            set.remove(wordToCheck);
        });
        return checkNestedWordSet(set, originalWord);

    }

    private static Set<String> addMissingOneLetterWordsToTheSet(Set<String> set) {
        set.add("I");
        set.add("A");
        return set;
    }

    private static void processWord(String word) {
        HashSet<String> set = new HashSet<>();
        set.add(word);
        checkNestedWordSet(set, word);
    }

    private static boolean checkIsValidWord(String newWordToCheck) {
        return allWordsMap.get().contains(newWordToCheck);
    }

}