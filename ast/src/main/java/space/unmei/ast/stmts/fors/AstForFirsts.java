package space.unmei.ast.stmts.fors;

import space.unmei.ast.stmts.AstVarDecInitStmt;
import space.unmei.ast.stmts.AstAssignStmt;

import space.unmei.ast.Pos;

public class AstForFirsts extends AstNode{

    private AstForFirstList flist;

    public AstForFirsts(Pos pos, AstForFirstList l){
        this.pos = pos;
        this.flist = l;
    }

    public boolean isEmpty(){
        return this.flist == null;
    }
}

