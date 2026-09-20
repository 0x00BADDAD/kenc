package space.unmei.lexer;

import java.util.Objects;


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

  @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof LexToken other)) {
            return false;
        }

        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return "Name: " + this.name + " Content: " + this.content;
    }
}
