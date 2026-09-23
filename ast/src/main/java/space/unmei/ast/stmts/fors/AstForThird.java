package space.unmei.ast.stmts.fors;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

import space.unmei.ast.stmts.AstAssignStmt;

import space.unmei.ast.Pos;

public class AstForThird extends AstNode{

    private AstAssign assign;
    private AstPost post;

    public AstForThird(Pos pos, AstAssign s, AstPost p){super(pos); this.assign = s; this.post = p;}


}

