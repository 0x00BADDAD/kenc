package space.unmei.ast.exps;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstRecordExp extends AstExp{

    private Symbol recordName;
    private List<Pair<Symbol, AstExp>> fieldInitList;

    public AstRecordExp(Pos pos, AstType ty, Symbol recordName, List<Pair<Symbol, AstExp>> fieldInitList){
        this.ty = ty;
        this.pos = pos;
        this.recordName = recordName;
        this.fieldInitList = fieldInitList;
    }
}
