package space.unmei.ast.stmts.fors;

import space.unmei.ast.*;

public class AstForThirdList extends AstNode{

    private AstForThird third;
    private AstForThirdList tail;

    public AstForThirdList(Pos pos, AstForThird  third, AstForThirdList list){
        super(pos);
        this.third = third;
        this.tail = list;
    }

}
