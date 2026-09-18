package space.unmei.ast.exps.lvalue;

import space.unmei.ast.types.*;
import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstVarExp extends AstLvalueExp{

    private Symbol varName;

    public AstVarExp(Pos pos, AstType ty, Symbol varName){
        super(pos, ty);
        this.varName = varName;
    }
}
