package space.unmei.ast.exps;

import space.unmei.ast.nodes.*;
import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.semant.Symbol;
import space.unmei.ast.Pos;

public class AstFuncallExp extends AstExp{

    private Symbol funName;
    private AstFunArgs argVals;

    public AstFuncallExp(Pos pos, AstType retTy, Symbol funName, AstFunArgs argVals){super(pos, retTy); this.funName = funName; this.argVals = argVals;}

}


