package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.semant.Symbol;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

public class AstFuncallStmt extends AstStmt{

    private Symbol funName;
    private AstFunArgs args;

    public AstFuncallStmt(Pos pos, Symbol funName, AstFunArgs args){
        super(pos);
        this.funName = funName;
        this.args = args;
    }
}
