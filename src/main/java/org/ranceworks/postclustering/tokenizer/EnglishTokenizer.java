package org.ranceworks.postclustering.tokenizer;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EnglishTokenizer  implements  Tokenizer {

    @Override
    public List<String> tokenize(String text) throws IOException {
        final EnglishAnalyzer analyzer = new EnglishAnalyzer();
        final TokenStream ts = analyzer.tokenStream(this.getClass().getName(), text);
        final CharTermAttribute attr =  ts.addAttribute(CharTermAttribute.class);

        final List<String> tokens =  new ArrayList<>();
        ts.reset();
        while(ts.incrementToken()) {
            tokens.add(attr.toString());
        }
        ts.end();
        return tokens;
    }
}
