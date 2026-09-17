package space.unmei.ast.stmts;

import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

import space.unmei.ast.Pos;

public class AstAssignStmt extends AstStmt{

    private AstAssign assign;

    public AstAssignStmt(Pos pos, AstAssign assign){
        super(pos);
        this.assign = assign;
    }


}
