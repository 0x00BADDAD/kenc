package space.unmei.lexer;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Alphabets{

    private final Map<Integer, String> alphabets = new HashMap<>();
    public static final List<String> ALL_ALPHAS = allAlphas();
    public Alphabets(){
        for(int i = 32; i < 128; i++){
            if(i < 127){
                alphabets.put(i, String.valueOf((char) i));
            }else{
                alphabets.put(i, "epsilon");
            }
        }
    }
    public static List<String> allAlphas(){
        List<String> alps = new ArrayList<>();
        for(int i = 32; i < 128; i++){
            if(i < 127){
                alps.add(String.valueOf((char) i));
            }else{
                alps.add("epsilon");
            }
        }
        alps.add("\n");
        alps.add("\t");
        return alps;
    }
    public String getAlphabet(int asciiCode){
        return this.alphabets.get(asciiCode);
    }

}
