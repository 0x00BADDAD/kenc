package space.unmei.ast.stmts.fors;

import space.unmei.ast.*;

public class AstForFirstList extends AstNode{

    private AstForFirst first;
    private AstForFirstList list;

    public AstForFirstList(Pos pos, AstForFirst first, AstForFirstList list){
        super(pos);
        this.first = first;
        this.list = list;
    }
}
