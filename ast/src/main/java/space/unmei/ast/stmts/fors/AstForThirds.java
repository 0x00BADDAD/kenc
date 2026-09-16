package space.unmei.ast.stmts.fors;

import space.unmei.ast.Pos;

public class AstForThirds extends AstNode{

    private AstForThirdList list;

    public AstForThirds(Pos pos, AstForThirdList list){
        this.pos = pos;
        this.list = list;
    }

    public boolean isEmpty(){
        return this.list == null;
    }
}

