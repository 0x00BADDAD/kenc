package space.unmei.ast.exps.record_exp;


public class AstFieldInit extends AstNode{
    private List<Pair<Symbol, AstExp>> fieldInitList = new ArrayList<>();

    public AstFieldInit(Pos pos, List<Pair<Symbol, AstExp>> l){
        this.pos = pos;
        this.fieldInitList = l;
    }

    public AstFieldInit(Pos pos){
        this.pos = pos;
    }

    public void addFieldInit(Symbol f, AstExp v){
        this.fieldInitList.add(new Pair(f, v));
    }

    public List<Pair<Symbol, AstExp>> getFieldInitList(){
        return this.fieldInitList;
    }
}
