package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.semant.Symbol;

public class AstFuncallStmt extends AstStmt{

    private Symbol funName;
    private List<AstExp> args;

    public AstFuncallStmt(Pos pos, Symbol funName, List<AstExp> args){
        this.pos = pos;
        this.funName = funName;
        this.args = args;
    }
}
