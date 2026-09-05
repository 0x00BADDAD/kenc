package space.unmei.semant;


public class Symbol{

    private String name;
    private LexToken tok;

    public Symbol(){

    }

    public Symbol(String name){
        this.name  = name;
        this.tok = null;
    }

    public Symbol(String name, LexToken tok){
        this.name  = name;
        this.tok = tok;
    }

    public LexToken getToken(){
        return this.tok;
    }
}
