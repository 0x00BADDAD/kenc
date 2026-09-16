package space.unmei.ast.stmts.fors;

import space.unmei.ast.stmts.AstVarDecInitStmt;
import space.unmei.ast.stmts.AstAssignStmt;

import space.unmei.ast.Pos;

public class AstForFirst extends AstNode{

    private AstVarDecInitStmt varDecInit;
    private AstAssignStmt assignStmt;

    public AstForFirst(Pos pos, AstVarDecInitStmt s1, AstAssignStmt s2){
        this.pos = pos;
        this.varDecInit = s1;
        this.assignStmt = s2;
    }
}
