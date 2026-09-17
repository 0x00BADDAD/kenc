package space.unmei.ast.stmts.fors;

import space.unmei.ast.*;

import space.unmei.ast.Pos;

public class AstForThirds extends AstNode{

    private AstForThirdList list;

    public AstForThirds(Pos pos, AstForThirdList list){
        super(pos);
        this.list = list;
    }

    public boolean isEmpty(){
        return this.list == null;
    }
}

