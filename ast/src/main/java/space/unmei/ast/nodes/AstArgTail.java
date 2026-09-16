package space.unmei.ast.nodes;

public class AstArgTail extends AstNode{
    private boolean isEmpty;
    private AstArgTail tail;

    public AstArgTail(Pos pos, boolean isEmpty, AstArgTail tail){
        this.pos = pos;
        this.isEmpty = isEmpty;
        this.tail= tail;
    }
}
