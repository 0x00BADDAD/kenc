package space.unmei.ast.stmts.fors;

import space.unmei.ast.stmts.AstAssignStmt;

import space.unmei.ast.Pos;

public class AstForThird extends AstNode{

    private AstAssignStmt assignStmt;

    public AstForThird(Pos pos, AstAssignStmt s){this.pos = pos; this.assignStmt = s;}

    public boolean isEmpty(){return this.assignStmt == null;}


}

