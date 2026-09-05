package space.unmei.ast.types;


public class AstTypeFields extends AstNode{

    private List<Pair<Symbol, AstType>> tyFields;

    public AstTypeFields(Pos pos, List<Pair<Symbol, AstType>> tyFields){
        this.pos = pos;
        this.tyFields = tyFields;
    }

    public void addTyField(Symbol sym, AstType tyVal){
        this.pos = new Pos(sym.getToken());
        this.tyFields.add(new Pair<>(sym, tyVal));
    }


}
