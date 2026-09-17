package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.semant.Symbol;
import space.unmei.ast.exps.*;

public class AstFunArgs extends AstNode{

    private boolean isEmpty;
    private AstExp exp;
    private AstArgTail tail;

    public AstFunArgs(Pos pos, boolean isEmpty, AstExp exp, AstArgTail tail){
        super(pos);
        this.isEmpty = isEmpty;
        this.exp = exp;
        this.tail = tail;
    }



}
