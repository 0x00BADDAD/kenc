package space.unmei.regex;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class GramSymbol<T extends Enum<T>>{

    private boolean isNonTerm;
    private T type = null; // non-null when the Symbol is terminal
    private String value; // null when symbol is terminal.

    private Set<GramSymbol<T>> firstSet = new HashSet<>();
    private Set<GramSymbol<T>> followSet = new HashSet<>();
    private boolean isNullable;

    public GramSymbol(boolean isNonTerm, String val){
        this.isNonTerm = isNonTerm;
        this.value = val;
    }

    public void setSymbolType(T ty){
        this.type = ty;
    }

    public T getSymbolType(){
       return this.type;
    }


    public boolean getIsNonTerm(){
        return this.isNonTerm;
    }

    public boolean getIsNullable(){
        return this.isNullable;
    }

    public void setIsNullable(boolean nullability){
        this.isNullable = nullability;
    }

    public void setIsNonTerm(boolean isNonTerm){
        this.isNonTerm = isNonTerm;
    }

    public void setValue(String val){
        this.value = val;
    }

    public String getValue(){
        return this.value;
    }

    public Set<GramSymbol<T>> getFirstSet(){
        return this.firstSet;
    }

    public Set<GramSymbol<T>> getFollowSet(){
        return this.followSet;
    }

    public void addFirst(GramSymbol<T> sym){
        this.firstSet.add(sym);
    }

    public void addFollow(GramSymbol<T> sym){
        this.followSet.add(sym);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof GramSymbol<?> other))
            return false;

        if (isNonTerm != other.isNonTerm)
            return false;

        if (isNonTerm) {
            return Objects.equals(value, other.value);
        }

        return Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        if (isNonTerm) {
            return Objects.hash(true, value);
        }

        return Objects.hash(false, type);
    }

    @Override
    public String toString(){
        if(this.isNonTerm){
            return "| " + this.value + " |";
        }
        return "| " + this.type.name() + " |";
    }
}
