package space.unmei.ast.decls;

import space.unmei.ast.Pos;

public class AstDecl extends AstNode{

    private Pos pos;
    private AstFunDec fundec;
    private AstTypeDec typedec;
    private AstVarDec vardec;
    private AstVarDecInit vardecinit;


    public AstDecl(Pos pos, AstFunDec fundec)         {this.pos = pos; this.fundec = fundec; }


    public AstDecl(Pos pos, AstTypeDec typedec)       {this.pos = pos; this.typedec = typedec; }


    public AstDecl(Pos pos, AstVarDec vardec)         {this.pos = pos; this.vardec = vardec; }


    public AstDecl(Pos pos, AstVarDecInit vardecinit) {this.pos = pos; this.vardecinit = vardecinit; }


}
