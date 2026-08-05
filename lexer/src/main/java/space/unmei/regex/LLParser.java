package space.unmei.regex;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import java.util.function.Supplier;


public abstract class LLParser<T, U extends LLToken<V>, V extends Enum<V>>{
    // This is a LL(1) parser
    private Map<Pair<GramSymbol<V>, V>, List<GramProd<T, V>>> predParseTable = new HashMap<>();
    private List<GramProd<T, V>> gramProds  = new ArrayList<>();
    private List<GramSymbol<V>> nonTermSyms = new ArrayList<>();
    private List<GramSymbol<V>> termSyms = new ArrayList<>();

    private Map<String, GramSymbol<V>> valueToSym = new HashMap<>();

    protected int nthRun = 0;
    protected int currTokIdx = 0; // Only written by this.advance(...) and this.runParse(...)

    protected List<U> tokens;
    protected boolean parserSetup = false;
    protected boolean isParserOk;


    public LLParser(){}

    private void addGramProd(GramProd<T, V> prod){
        this.gramProds.add(prod);
    }


    private void addValueToSym(String val, GramSymbol<V> sym){
        this.valueToSym.put(val, sym);
    }

    protected GramSymbol<V> getValueToSym(String val){
        return this.valueToSym.getOrDefault(val, null);
    }


    private void addTermSym(GramSymbol<V> term){
        this.termSyms.add(term);
    }

    private void addNonTermSym(GramSymbol<V> nonTerm){
        this.nonTermSyms.add(nonTerm);
    }

    protected Supplier<T> getProdSupp(String nonTermSym){

        List<GramProd<T, V>> prods = this.predParseTable.getOrDefault(
            new Pair<>(this.valueToSym.get(nonTermSym),
            this.tokens.get(this.currTokIdx).getType()), new ArrayList<>());

        if(prods.size() == 0){
            // LL1 parse error: TODO

            System.out.println("First Set of Regex:");

            for(GramSymbol<V> sym: this.valueToSym.get("Regex").getFirstSet()){
                System.out.println(sym.getSymbolType().name());
            }

            throw new IllegalArgumentException("no prod found for: nonTerm- " + nonTermSym + " and nonTermType: " + this.tokens.get(this.currTokIdx).getType().name());
        }
        return prods.get(0).getSupp();
    }

    protected void printFirstAndFollow(){
        for(GramSymbol<V> sym : this.nonTermSyms){
            System.out.println("-------new-entry---------");
            System.out.println("-------------------------");
            System.out.println(sym.getValue() + "->");
            System.out.println("null: " + (sym.getIsNullable() ? "yes" : "no"));

            StringBuilder sb = new StringBuilder();
            // first and follow set should always contain terminal symbols
            for(GramSymbol<V> s: sym.getFirstSet()){
                sb.append(" ");
                sb.append(s.getSymbolType().name());
            }
            System.out.println("FIRST: " + sb.toString());

            sb = new StringBuilder();
            for(GramSymbol<V> s: sym.getFollowSet()){
                sb.append(" ");
                sb.append(s.getSymbolType().name());
            }
            System.out.println("FOLLOW: " + sb.toString());

            System.out.println("-------end-entry---------");
            System.out.println("-------------------------");
            System.out.println("                         ");
            System.out.println("                         ");
        }
    }

    private void makeFixedSets(){
        // first add term symbols in the first set of term symbols.
        // The algo runs until a fixed point is acheived where,
        // First map and follow map are same. That will require overriding equals on
        // GramSymbol object and jvm will take care of the rest.
        for(GramSymbol<V> sym: this.termSyms){
            sym.getFirstSet().add(sym);
        }
        for(GramProd<T, V> prod: this.gramProds){
            // this prod is essentially an outer loop
            if(!prod.getLhs().getIsNullable()){
                // if we think that it's not nullable then we check
                boolean thisProdLhsNullable = true;
                for(GramSymbol<V> sym: prod.getRhs()){
                    if(!sym.getIsNullable()){
                        thisProdLhsNullable = false;
                        break;
                    }
                }
                prod.getLhs().setIsNullable(thisProdLhsNullable);
            }
            // calc FIRST of prod.getRhs()
            boolean nullPrefix = true;
            for(GramSymbol<V> sym: prod.getRhs()){
                if(!nullPrefix){break;}
                for(GramSymbol<V> firstSym : sym.getFirstSet()){
                    prod.getLhs().getFirstSet().add(firstSym);
                }
                nullPrefix = sym.getIsNullable();
            }

            // calc FOLLOW of syms on the Rhs from left to right
            boolean nullSuffix = true;
            int idxMax = prod.getRhs().size();
            for(int idx = idxMax-1; idx >= 0; --idx){
                if(!nullSuffix){break;}
                GramSymbol<V> currSym = prod.getRhs().get(idx);
                for(GramSymbol<V> symInLhs : prod.getLhs().getFollowSet()){
                    currSym.getFollowSet().add(symInLhs);
                }
                nullSuffix = currSym.getIsNullable();
            }

            // calc FOLLOW(s) of the symbols on rhs by i,j iteration
            for(int idx1=idxMax-1; idx1 >= 0; idx1--){
                nullSuffix = true;
                for(int idx2=idx1-1; idx2 >= 0; idx2--){
                    if(!nullSuffix){break;}
                    for(GramSymbol<V> sym_ : prod.getRhs().get(idx1).getFirstSet()){
                        prod.getRhs().get(idx2).addFollow(sym_);
                    }
                    nullSuffix = prod.getRhs().get(idx2).getIsNullable();
                }
            }

        }
    }


    private Triplet<Map<String, List<String>>,
           Map<String, List<String>>,
           List<String>> extrFirstAndFollow(){
        // extract the Map<String, List<V>> for first and follow and
        // List<String> for nullability
        Map<String, List<String>> firsts = new HashMap<>();
        Map<String, List<String>> follows = new HashMap<>();
        List<String> nulls = new ArrayList<>();
        for(GramSymbol<V> sym : this.nonTermSyms){

            List<String> currFirsts = firsts.getOrDefault(sym.getValue(), new ArrayList<>());
            for(GramSymbol<V> sym_ : sym.getFirstSet()){
                currFirsts.add(sym_.getSymbolType().name());
            }
            firsts.put(sym.getValue(), currFirsts);


            List<String> currFollows = follows.getOrDefault(sym.getValue(), new ArrayList<>());
            for(GramSymbol<V> sym_ : sym.getFollowSet()){
                currFollows.add(sym_.getSymbolType().name());
            }
            follows.put(sym.getValue(), currFollows);


            if(sym.getIsNullable()){
                nulls.add(sym.getValue());
            }
        }

        return new Triplet<>(firsts, follows, nulls);
    }

    private void makeFirstAndFollow(){
        while(true){
            Triplet<Map<String, List<String>>,
                       Map<String, List<String>>,
                       List<String>> trip1 = this.extrFirstAndFollow();

            this.makeFixedSets();

            Triplet<Map<String, List<String>>,
                       Map<String, List<String>>,
                       List<String>> trip2 = this.extrFirstAndFollow();

            if(trip1.equals(trip2)){break;}
        }
    }

    public void printPredParseTable(){
        for(Map.Entry<Pair<GramSymbol<V>, V>, List<GramProd<T, V>>> p: this.predParseTable.entrySet()){
            Pair<GramSymbol<V>, V> pk = p.getKey();
            List<GramProd<T, V>> pv = p.getValue();
            System.out.println("------------------------");
            System.out.println("------------------------");
            System.out.println("( " + pk.first().toString() + ", " + pk.second().name()+ ")");
            System.out.println("Prods: ");
            for(GramProd<T, V> prod: pv){
                System.out.println(prod.toString());
            }
        }
    }

    private boolean makePredParseTable(){
        boolean finRes = true;
        for(GramProd<T, V> prod : this.gramProds){
            GramSymbol<V> symR = prod.getLhs();
            boolean nullPrefix = true;
            for(GramSymbol<V> sym : prod.getRhs()){
                if(!nullPrefix){break;}
                for(GramSymbol<V> sym_ : sym.getFirstSet()){
                    Pair<GramSymbol<V>, V> k = new Pair<>(symR, sym_.getSymbolType());
                    List<GramProd<T, V>> currProds = this.predParseTable.getOrDefault(k, new ArrayList<>());
                    currProds.add(prod);
                    if(currProds.size() > 1){
                        System.out.println("was here more than 1 entry in parse table!");
                        finRes = false;
                    }
                    //if(sym.getValue().equals("Regex")){
                    //    System.out.println("Adding for Regex and " + sym_.getSymbolType().name());
                    //}
                    //System.out.println();
                    this.predParseTable.put(k, currProds);
                }
                nullPrefix = sym.getIsNullable() && sym.getIsNonTerm();
                if(!sym.getIsNonTerm()){
                    nullPrefix = false;
                }
            }
            if(nullPrefix){
                // the whole prod is nullable so the prod can be in FOLLOW of lhs
                for(GramSymbol<V> sym: prod.getLhs().getFollowSet()){
                    Pair<GramSymbol<V>, V> k = new Pair<>(prod.getLhs(), sym.getSymbolType());
                    List<GramProd<T, V>> currProds = this.predParseTable.getOrDefault(k, new ArrayList<>());
                    currProds.add(prod);
                    if(currProds.size() > 1){
                        finRes = false;
                    }
                    this.predParseTable.put(k, currProds);
                }
            }
        }
        return finRes;
    }


    protected void advance(U tok){
        if(tok.equals(tokens.get(this.currTokIdx))){
            // found what tok as expected
            this.currTokIdx+=1;
        }else{
            throw new IllegalArgumentException("Unexpected Token: " + tokens.get(this.currTokIdx).getText() + " ");
        }
    }

    // each grammer has its own logic for its action.
    // subclass should implement this to populate this.symToFunc.
    // call the setup_(..) from this function
    protected abstract boolean setup();


    public boolean setup_(String[] nonTermSyms, List<V> termSyms, List<Pair<List<String>, Supplier<T>>> prodStrs){

        for(String s: nonTermSyms){
            GramSymbol<V> gramSym = new GramSymbol<>(true, s);
            gramSym.setIsNullable(false);
            this.addNonTermSym(gramSym);
            this.addValueToSym(s, gramSym);
        }

        for(V s: termSyms){
            GramSymbol<V> gramSym = new GramSymbol<>(false, null);
            gramSym.setSymbolType(s);
            gramSym.setIsNullable(false);
            this.addTermSym(gramSym);
            this.addValueToSym(s.name(), gramSym);
        }

        for(Pair<List<String>, Supplier<T>> prodPair: prodStrs){
            List<String> prod = prodPair.first();
            Supplier<T> supp = prodPair.second();
            GramSymbol<V> lhs = this.getValueToSym(prod.get(0));
            List<GramSymbol<V>> rhs = new ArrayList<>();
            for(int i=1; i< prod.size(); i++){
                rhs.add(this.valueToSym.get(prod.get(i)));
            }
            this.addGramProd(new GramProd<T, V>(lhs, rhs, supp));
        }
        this.makeFirstAndFollow();
        boolean isLL1 = this.makePredParseTable();
        return isLL1;
    }

    public T runParse(List<U> tokens){
        // the Start symbol is reserved for start symbol
        if(!this.parserSetup || !this.isParserOk){
            throw new IllegalArgumentException("Parser has not been setup (or is invalid) before parsing!");
        }
        this.tokens = tokens;
        this.currTokIdx = 0;
        T finRes = this.getProdSupp("Start").get();
        this.nthRun +=1;
        return finRes;
    }
}
