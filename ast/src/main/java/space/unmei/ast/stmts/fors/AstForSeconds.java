package space.unmei.ast.stmts.fors;

import space.unmei.ast.*;

import space.unmei.ast.exps.*;
import space.unmei.ast.Pos;

public class AstForSeconds extends AstNode{

    private AstExp secondExp;

    public AstForSeconds(Pos pos, AstExp secondExp){
        super(pos);
        this.secondExp = secondExp;
    }

    public boolean isEmpty(){
        return this.secondExp == null;
    }
}

