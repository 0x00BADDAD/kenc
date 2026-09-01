package space.unmei.ast.decls;


public class AstDecl extends AstNode{

    private AstFunDec fundec;
    private AstTypeDec typedec;
    private AstVarDec vardec;
    private AstVarDecInit vardecinit;


    public AstDecl(AstFunDec fundec) { this.fundec = fundec; }


    public AstDecl(AstTypeDec typedec) { this.typedec = typedec; }


    public AstDecl(AstVarDec vardec) { this.vardec = vardec; }


    public AstDecl(AstVarDecInit vardecinit) { this.vardecinit = vardecinit; }


}
