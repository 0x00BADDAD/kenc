package space.unmei.ast.stmts.fors;

import space.unmei.ast.stmts.AstVarDecInitStmt;
import space.unmei.ast.stmts.AstAssignStmt;

import space.unmei.ast.Pos;

public class AstForFirsts extends AstNode{

    private List<AstForFirst> flist;

    public AstForFirsts(Pos pos, List<AstForFirst> l){
        this.pos = pos;
        this.flist = l;
    }

    public boolean isEmpty(){

        boolean ans = this.flist.size() == 0;

        boolean ans_ = true;
        for(AstForFirst x: this.flist){
            ans_ &= x.isEmpty();
        }
        return (ans | ans_);

    }
}

