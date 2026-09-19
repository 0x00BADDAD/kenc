package space.unmei.ast;


public class AstStart extends AstNode{

    AstProg prog;

    public AstStart(Pos pos, AstProg prog){
        super(pos);
        this.prog = prog;
    }

}

