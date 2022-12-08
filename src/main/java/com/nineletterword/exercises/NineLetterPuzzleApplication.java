package com.nineletterword.exercises;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NineLetterPuzzleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NineLetterPuzzleApplication.class, args);

        WebClientService webClientService = new WebClientService();
        WebWordsReader wr = new WebWordsReader(webClientService);
        wr.readWordsFromUrl("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
    }

}
