package org.kenlang.lexer;



public class LexToken{

    private String string;
    private String content;

    public LexToken(String str, String con){
        this.string = str;
        this.content = con;
    }

    public void setString(String str){
        this.string = str;
    }

    public String getString(){
        return this.string;
    }

    public void setContent(String str){
        this.content = str;
    }

    public String getContent(){
        return this.content;
    }

}
