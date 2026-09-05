package space.unmei.ast.stmts.fors;

import space.unmei.ast.Pos;

public class AstForThirds extends AstNode{

    private boolean List<AstForThird> flist;

    public AstForThirds(Pos pos, List<AstForThird> flist){
        this.pos = pos;
        this.flist = flist;
    }

    public boolean isEmpty(){

        boolean ans = this.flist.size() == 0;

        boolean ans_ = true;
        for(AstForThird x: this.flist){
            ans_ &= x.isEmpty();
        }
        return (ans | ans_);
    }
}

