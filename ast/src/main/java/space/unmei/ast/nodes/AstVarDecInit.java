package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.Pos;

public class AstVarDecInit extends AstNode{

    private Symbol varName;
    private AstType tyVal;
    private AstExp expVal;

    public AstVarDecInit(Pos pos, Symbol varName, AstType tyVal, AstExp expVal) {
        super(pos);
        this.varName = varName;
        this.tyVal = tyVal;
        this.expVal = expVal;
    }
}

