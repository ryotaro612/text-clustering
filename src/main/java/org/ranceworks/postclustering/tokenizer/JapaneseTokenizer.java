package org.ranceworks.postclustering.tokenizer;

import com.atilika.kuromoji.ipadic.Token;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JapaneseTokenizer implements Tokenizer {
     @Override
     public List<String> tokenize(String text) throws IOException {
         com.atilika.kuromoji.ipadic.Tokenizer tokenizer =  new com.atilika.kuromoji.ipadic.Tokenizer();
         return tokenizer.tokenize(text).parallelStream().map(Token::getBaseForm).collect(Collectors.toList());
     }
}
