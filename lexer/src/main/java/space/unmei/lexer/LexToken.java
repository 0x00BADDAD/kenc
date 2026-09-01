package space.unmei.lexer;



public class LexToken{

    private String name;
    private String content;
    private Integer lineNo;
    private Integer colNo;

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

    public void setLineNo(Integer l){
        this.lineNo = l;
    }

    public Integer getLineNo(){
        return this.lineNo;
    }

    public void setColNo(Integer c){
        this.colNo = c;
    }

    public Integer getColNo(){
        return this.colNo;
    }
}
