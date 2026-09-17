package space.unmei.ast.stmts.fors;

import space.unmei.ast.stmts.AstVarDecInitStmt;
import space.unmei.ast.stmts.AstAssignStmt;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

import space.unmei.ast.Pos;

public class AstForFirst extends AstNode{

    private AstVarDecInit varDecInit;
    private AstAssign assign;

    public AstForFirst(Pos pos, AstVarDecInit s1, AstAssign s2){
        super(pos);
        this.varDecInit = s1;
        this.assign = s2;
    }
}
