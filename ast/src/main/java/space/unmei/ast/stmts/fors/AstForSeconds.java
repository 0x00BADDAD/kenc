package space.unmei.ast.stmts.fors;


import space.unmei.ast.Pos;

public class AstForSeconds extends AstNode{

    private List<AstForSecond> flist;

    public AstForSeconds(Pos pos, List<AstForSecond> l){
        this.pos = pos;
        this.flist = l;
    }

    public boolean isEmpty(){

        boolean ans = this.flist.size() == 0;

        boolean ans_ = true;
        for(AstForSecond x: this.flist){
            ans_ &= x.isEmpty();
        }
        return (ans | ans_);

    }
}

