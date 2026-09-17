package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstTypeDec extends AstNode{

    private Symbol tyName;
    private AstType tyVal;

    public AstTypeDec(Pos pos, Symbol tyName, AstType tyVal) {
        super(pos);
        this.tyName = tyName;
        this.tyVal = tyVal;
    }
}

