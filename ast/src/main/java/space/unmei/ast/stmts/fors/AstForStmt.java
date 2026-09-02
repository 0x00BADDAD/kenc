package space.unmei.ast.stmts.fors;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.stmts.fors.AstForFirsts;
import space.unmei.ast.stmts.fors.AstForSeconds;
import space.unmei.ast.stmts.fors.AstForThirds;

public class AstForStmt{

    private Pos pos;
    private AstForFrists firsts;
    private AstForSeconds seconds;
    private AstForThirds thirds;

    public AstForStmt(Pos pos, AstForFirsts firsts, AstForSeconds seconds, AstForThirds thirds){
        this.pos = pos;
        this.firsts = firsts;
        this.seconds = seconds;
        this.thirds = thirds;
    }


}

