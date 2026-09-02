package space.unmei.ast.exps;

import space.unmei.semant.Symbol;
import space.unmei.ast.Pos;

public class AstFuncallExp extends AstExp{

    private Symbol funName;
    private List<AstExp> argVals;

    public AstFuncallExp(Pos pos, Symbol funName, List<AstExp> argVals){this.pos = pos; this.funName = funName; this.argVals = argVals;}

}


