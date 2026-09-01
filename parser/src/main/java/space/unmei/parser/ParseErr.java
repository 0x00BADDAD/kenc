package space.unmei.parser;



public class ParseErr<U>{
    private Integer lineNo;
    private Integer colNo;
    private U expectedTok;
    private U errTok;

    public ParseErr(){}

    public ParseErr(Integer lineNo, Integer colNo, U expectedTok, U errTok){
        this.lineNo = lineNo;
        this.colNo = colNo;
        this.expectedTok = expectedTok;
        this.errTok = errTok;
    }

    public Integer getLineNo(){
        return this.lineNo;
    }

    public void setLineNo(Integer i){
        this.lineNo = i;
    }

    public Integer getColNo(){
        return this.colNo;
    }

    public void setColNo(Integer i){
        this.colNo = i;
    }

    public U getExpectedTok(){
        return this.expectedTok;
    }

    public void setExpectedTok(U i){
        this.expectedTok = i;
    }

    public U getErrTok(){
        return this.errTok;
    }

    public void setErrTok(U i){
        this.errTok = i;
    }
}
