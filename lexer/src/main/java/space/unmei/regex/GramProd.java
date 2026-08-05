package space.unmei.regex;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import java.util.function.Supplier;

public class GramProd<T, U extends Enum<U>>{

    private GramSymbol<U> lhs;
    private List<GramSymbol<U>> rhs = new ArrayList<>();
    private Supplier<T> suppFunc;

    public GramProd(){}

    public GramProd(GramSymbol<U> lhs, List<GramSymbol<U>> rhs, Supplier<T> supp){
        this.lhs = lhs;
        this.rhs = rhs;
        this.suppFunc = supp;
    }

    public GramSymbol<U> getLhs(){
        return this.lhs;
    }

    public Supplier<T> getSupp(){
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
