package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.exps.lvalue.AstLvalue;

public class AstAssignStmt extends AstStmt{

    private AstLvalue lvalue;
    private AstExp val;

    public AstAssignStmt(Pos pos, AstLvalue lvalue, AstExp val){
        this.pos = pos;
        this.lvalue = lvalue;
        this.val = val;
    }


}
