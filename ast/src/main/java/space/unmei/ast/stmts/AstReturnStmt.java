package space.unmei.ast.stmts;

import space.unmei.ast.exps.*;
import space.unmei.ast.Pos;
import space.unmei.ast.*;

public class AstReturnStmt extends AstStmt{

    private AstExp retVal;

    public AstReturnStmt(Pos pos, AstExp v){
        super(pos);
        this.retVal = v;
    }


}
