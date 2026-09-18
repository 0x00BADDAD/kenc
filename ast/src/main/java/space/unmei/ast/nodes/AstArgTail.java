package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.ast.exps.*;
import space.unmei.semant.Symbol;

public class AstArgTail extends AstNode{
    private boolean isEmpty;
    private AstExp exp;
    private AstArgTail tail;

    public AstArgTail(Pos pos, boolean isEmpty, AstExp exp, AstArgTail tail){
        super(pos);
        this.exp = exp;
        this.isEmpty = isEmpty;
        this.tail= tail;
    }
}
