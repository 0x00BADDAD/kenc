package space.unmei.ast.stmts.fors;


public class AstForFirstList extends AstNode{

    private AstForFirst first;
    private AstForFirstList list;

    public AstForFirstList(Pos pos, AstForFirst first, AstForFirstList list){
        this.pos = pos;
        this.first = first;
        this.list = list;
    }
}
