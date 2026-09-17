package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.semant.Symbol;

public class AstArgTail extends AstNode{
    private boolean isEmpty;
    private AstArgTail tail;

    public AstArgTail(Pos pos, boolean isEmpty, AstArgTail tail){
        super(pos);
        this.isEmpty = isEmpty;
        this.tail= tail;
    }
}
