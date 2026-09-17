package space.unmei.ast.nodes;

import space.unmei.ast.*;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.exps.lvalue.AstLvalueExp;
import space.unmei.ast.stmts.AstAssignOpType;

public class AstAssign extends AstNode{

    private AstLvalueExp lvalue;
    private AstExp val;
    private AstAssignOpType opType;

    public AstAssign(Pos pos, AstLvalueExp lvalue, AstExp val, AstAssignOpType opTy){
        super(pos);
        this.lvalue = lvalue;
        this.val = val;
        this.opType = opTy;
    }


}

