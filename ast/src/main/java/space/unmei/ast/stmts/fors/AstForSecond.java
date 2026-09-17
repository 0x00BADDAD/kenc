package space.unmei.ast.stmts.fors;

import space.unmei.ast.exps.*;
import space.unmei.ast.stmts.AstVarDecInitStmt;
import space.unmei.ast.stmts.AstAssignStmt;
import space.unmei.ast.*;

import space.unmei.ast.Pos;

public class AstForSecond extends AstNode{

    private AstExp exp;

    public AstForSecond(Pos pos, AstExp exp){super(pos); this.exp = exp;}

    public boolean isEmpty(){return this.exp == null;}


}
