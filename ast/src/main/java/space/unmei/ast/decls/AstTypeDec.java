package space.unmei.ast.decls;

import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstTypeDec extends AstDecl{

    private Symbol tyName;
    private AstType tyVal;

    public AstTypeDec(Symbol tyName, AstType tyVal) {
        this.tyName = tyName;
        this.tyVal = tyVal;
    }
}
