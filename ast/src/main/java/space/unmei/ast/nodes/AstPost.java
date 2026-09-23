package space.unmei.ast.nodes;

import space.unmei.ast.*;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.exps.lvalue.AstLvalueExp;
import space.unmei.ast.stmts.AstPostOpType;

public class AstPost extends AstNode{

    private AstLvalueExp lvalue;
    private AstPostOpType opType;

    public AstPost(Pos pos, AstLvalueExp lvalue, AstPostOpType opTy){
        super(pos);
        this.lvalue = lvalue;
        this.opType = opTy;
    }


}


