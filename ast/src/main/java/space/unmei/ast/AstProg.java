package space.unmei.ast;


public class AstProg extends AstNode{

    private List<AstDecl> decls;

    public AstProg(List<AstDecl> decls){
        this.decls = decls;
    }

}
