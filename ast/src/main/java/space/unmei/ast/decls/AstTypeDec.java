package space.unmei.ast.decls;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstTypeDec extends AstDecl{

    private Pos pos;
    private Symbol tyName;
    private AstType tyVal;

    public AstTypeDec(Pos pos, Symbol tyName, AstType tyVal) {
        this.pos = pos;
        this.tyName = tyName;
        this.tyVal = tyVal;
    }
}
