package space.unmei.lexer;



public class LexToken{

    private String name;
    private String content;

    public LexToken(String name, String con){
        this.name = name;
        this.content = con;
    }

    public void setName(String str){
        this.name = str;
    }

    public String getName(){
        return this.name;
    }

    public void setContent(String str){
        this.content = str;
    }

    public String getContent(){
        return this.content;
    }

}
