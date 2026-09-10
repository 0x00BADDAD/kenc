package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.exps.lvalue.AstLvalueExp;
import space.unmei.ast.stmts.AstAssignOpType;

public class AstAssignStmt extends AstStmt{

    private AstLvalueExp lvalue;
    private AstExp val;
    private AstAssignOpType opType;

    public AstAssignStmt(Pos pos, AstLvalueExp lvalue, AstExp val, AstAssignOpType opTy){
        this.pos = pos;
        this.lvalue = lvalue;
        this.val = val;
        this.ty = ty;
        this.opType = opTy;
    }


}
