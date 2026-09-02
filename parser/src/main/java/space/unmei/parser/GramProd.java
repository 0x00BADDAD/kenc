package space.unmei.parser;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import java.util.function.BiConsumer;

// U -> Token Type
// T -> type returned by the semantic reduce action
public class GramProd<T, U>{

    private GramSymbol<U> lhs;
    private List<GramSymbol<U>> rhs = new ArrayList<>();
    private BiConsumer<Deque<LR1State<T,U>>, Deque<Pair<T, GramSymbol<U>>> suppFunc;

    public GramProd(){}

    public GramProd(GramSymbol<U> lhs, List<GramSymbol<U>> rhs, BiConsumer<Deque<LR1State<T,U>>, Deque<Pair<T, GramSymbol<U>>> supp){
        this.lhs = lhs;
        this.rhs = rhs;
        this.suppFunc = supp;
    }

    public GramSymbol<U> getLhs(){
        return this.lhs;
    }

    public BiConsumer<Deque<LR1State<T,U>>, Deque<Pair<T, GramSymbol<U>>> getSupp(){
        return this.suppFunc;
    }

    public void setLhs(GramSymbol<U> lhs){
        this.lhs = lhs;
    }

    public List<GramSymbol<U>> getRhs(){
        return this.rhs;
    }

    public void addRhs(GramSymbol<U> rhsSym){
        this.rhs.add(rhsSym);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GramProd<?, ?> other)) {
            return false;
        }

        return Objects.equals(lhs, other.lhs)
                && Objects.equals(rhs, other.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.lhs.toString());
        sb.append(" -> ");
        for(GramSymbol<U> g: this.rhs){
            sb.append(g.toString() + " ");
        }
        return sb.toString();
    }

}

