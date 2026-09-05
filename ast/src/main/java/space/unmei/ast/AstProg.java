package space.unmei.ast;

import space.unmei.ast.decls.AstDecl;

public class AstProg extends AstNode{

    private List<AstDecl> decls;

    public AstProg(Pos pos, List<AstDecl> decls){
        this.pos = pos;
        this.decls = decls;
    }

    public void addDecl(AstDecl decl){
        this.decls.add(decl);
    }
}
