package space.unmei.ast;

import space.unmei.ast.decls.AstDecl;

public class AstProg extends AstNode{

    private List<AstDecl> decls;

    public AstProg(List<AstDecl> decls){
        this.decls = decls;
    }
}
