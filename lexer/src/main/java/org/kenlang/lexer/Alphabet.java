package org.kenlang.lexer;

import java.util.Map;
import java.util.HashMap;


public class Alphabets{
    
    private final Map<int, String> alphabets = new HashMap<>();

    public Alphabets(){
        for(int i = 32; i < 128; i++){
            if(i < 127){
                alphabets.put(i, String.valueOf((char) i));
            }else{
                alphabets.put(i, "epsilon");
            }
        }
    }

    public String getAlphabet(int asciiCode){
        return this.alphabets.get(asciiCode);
    }






}
