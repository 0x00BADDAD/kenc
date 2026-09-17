package space.unmei.ast.exps.record_exp;

import space.unmei.ast.types.*;
import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstRecordExp extends AstExp{

    private AstFieldInit fieldInitList;

    public AstRecordExp(Pos pos, AstType ty, AstFieldInit fieldInitList){
        super(pos, ty);
        this.fieldInitList = fieldInitList;
    }
}
