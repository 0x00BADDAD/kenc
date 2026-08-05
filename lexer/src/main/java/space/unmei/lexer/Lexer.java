package space.unmei.lexer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.io.Reader;
import java.io.IOException;

public class Lexer {
    private final Reader reader;

    public Lexer(Reader reader) {
        this.reader = reader;
    }

    public List<LexToken> lexComplete(Dfa dfa) throws IOException, LexerException {
        int lastNewLine=0;
        int lastFinalState = -1;
        int lastFinalPos = -1;
        int newLinesAfterStartIdx = 0;
        int newLinesUptoStartIdx = 0;
        int lastStartIdx = 0;

        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            // get the complete prog in memory
            char ch = (char) c;
            sb.append(ch);
       }

       System.out.println("str to lex: " + sb.toString());

       List<LexToken> tokens = new ArrayList<>();

       int totLen = sb.length();

       while(true){
           if(totLen <= lastStartIdx){break;}
           int currIdx = lastStartIdx;

           int currState = dfa.getStart();
           // check if the start state is final
           if(dfa.getFinalSetComp().containsKey(currState)){
               lastFinalPos = currIdx;
               lastFinalState = currState;
           }

           while(true){
                int tar = dfa.getDfaEdge(currState, String.valueOf(sb.charAt(currIdx)));
                if(sb.charAt(currIdx) == '\n'){
                    newLinesAfterStartIdx+=1;
                    lastNewLine = currIdx;
                }
                if(tar != -1){
                    // found an edge
                    currState = tar;
                    // check if this state is final state
                    // if it is then update lastFinalPos and lastFinalState
                    if(dfa.getFinalSetComp().containsKey(tar)){
                        lastFinalPos =  currIdx;
                        lastFinalState = tar;
                    }
                    if(currIdx == totLen -1){
                        // reached the end of the string
                        // just return the stored final
                        if(lastFinalState != -1){
                            // we had found a final state. so update the startIdx to it
                            // and add a LexToken
                            String tokName = dfa.getFinalSetComp().get(lastFinalState);
                            String tokContent = sb.substring(lastStartIdx, lastFinalPos+1);
                            LexToken tok = new LexToken(tokName, tokContent);
                            tokens.add(tok);
                            lastStartIdx = lastFinalPos + 1;
                            lastFinalState = -1;
                        }else{
                            // error
                            throw new LexerException(
                             "Unexpected character " + "'"+sb.charAt(currIdx)+"'",
                             newLinesUptoStartIdx + newLinesAfterStartIdx,
                             currIdx-lastNewLine
                            );
                        }
                        break;
                    }
                    currIdx+=1;
                }else{
                    // dead end. we update the lastStartIdx to lastFinalPos.
                    // lastFinalPos to -1 and lastFinalState to -1
                    // if we had any final state found then we put a token for it
                    // in the list
                    if(lastFinalState == -1){
                        // we have run into error
                       throw new LexerException(
                        "Unexpected character " + "'"+sb.charAt(currIdx)+"'",
                        newLinesUptoStartIdx + newLinesAfterStartIdx,
                        currIdx-lastNewLine
                       );
                    }else{
                        // we had found a final state. so update the startIdx to it
                        // and add a LexToken
                        String tokName = dfa.getFinalSetComp().get(lastFinalState);
                        String tokContent = sb.substring(lastStartIdx, lastFinalPos+1);
                        LexToken tok = new LexToken(tokName, tokContent);
                        tokens.add(tok);

                        lastStartIdx = lastFinalPos + 1;
                        lastFinalPos = -1;
                        lastFinalState = -1;
                        newLinesUptoStartIdx += newLinesAfterStartIdx;
                        newLinesAfterStartIdx = 0;
                        break;
                    }
                }
           }
       }
       tokens.add(new LexToken("EOF", ""));
       return tokens;
    }
}



