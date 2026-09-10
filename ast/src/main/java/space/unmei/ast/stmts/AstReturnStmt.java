package space.unmei.ast.stmts;

import space.unmei.ast.Pos;

public class AstReturnStmt extends AstStmt{

    private AstExp retVal;

    public AstReturnStmt(Pos pos, AstExp v){
        this.pos = pos;
        this.retVal = v;
    }


}
