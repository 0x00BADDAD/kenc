package space.unmei.ast.nodes;


public class AstFunArgs extends AstNode{

    private boolean isEmpty;
    private AstExp exp;
    private AstArgTail tail;

    public AstFunArgs(Pos pos, boolean isEmpty, AstExp exp, AstArgTail tail){
        this.pos = pos;
        this.isEmpty = isEmpty;
        this.exp = exp;
        this.tail = tail;
    }



}
