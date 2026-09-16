package space.unmei.ast.stmts.fors;


public class AstForThirdList extends AstNode{

    private AstForThird third;
    private AstForThirdList tail;

    public AstForThirdList(Pos pos, AstForThird  third, AstForThirdList list){
        this.pos = pos;
        this.third = third;
        this.tail = list;
    }

}
