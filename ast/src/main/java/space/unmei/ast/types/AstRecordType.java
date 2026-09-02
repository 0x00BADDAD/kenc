package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstRecordType{

    private Pos pos;
    private List<Pair<Symbol, AstType>> tyFields;

    public AstRecordType(Pos pos, List<Pair<Symbol, AstType>> tyFields){
        this.pos = pos;
        this.tyFields = tyFields;
    }
}


