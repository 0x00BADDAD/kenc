package space.unmei.parser;

import space.unmei.ast.*;
import space.unmei.lexer.LexToken;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;


// this class expects tokens to be of type LexToken
// T -> class of AST Node returned by gram prod reduction
// U -> LexToken class
public abstract class LR1Parser<T , U extends LexToken>{

    private List<U> tokSet; // set of unique tokens

    private List<U> tokens = new ArrayList<>(); // list of lexed tokens

    private Set<LR1State<T, U>> states = new HashSet<>();
    private LR1State<T, U> startState;

    private Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds = new HashMap<>();

    private List<GramProd<T, U>> gramProds  = new ArrayList<>();
    private List<GramSymbol<U>> nonTermSyms = new ArrayList<>();
    private List<GramSymbol<U>> termSyms = new ArrayList<>();

    private Map<String, GramSymbol<U>> valueToSym = new HashMap<>();

    // it expects GramProd(s) and GramSymbol(s) list(s)
    // it will generate the LR1 parse graph.
    public LR1Parser(){}

    public List<U> getTokens(){
        return this.tokens;
    }

    public void setTokens(List<U> tokens){
        // TODO: filter out the WHITESPACE tokens from the list of `tokens`
        this.tokens = tokens;
    }

    private LR1State<T, U> stateExists(LR1State<T, U> state){
        for (LR1State<T, U> x : this.states) {
            if (x.equals(state)) {
                return x;
            }
        }
        return null;
    }

    private void addGramProd(GramProd<T, U> prod){
        this.gramProds.add(prod);
    }


    private void addValueToSym(String val, GramSymbol<U> sym){
        this.valueToSym.put(val, sym);
    }

    protected GramSymbol<U> getValueToSym(String val){
        return this.valueToSym.getOrDefault(val, null);
    }


    private void addTermSym(GramSymbol<U> term){
        this.termSyms.add(term);
    }

    private void addNonTermSym(GramSymbol<U> nonTerm){
        this.nonTermSyms.add(nonTerm);
    }


    private void makeFixedSets(){
        // first add term symbols in the first set of term symbols.
        // The algo runs until a fixed point is acheived where,
        // First map and follow map are same. That will require overriding equals on
        // GramSymbol object and jvm will take care of the rest.
        for(GramSymbol<U> sym: this.termSyms){
            sym.getFirstSet().add(sym);
        }
        for(GramProd<T, U> prod: this.gramProds){
            // this prod is essentially an outer loop
            if(!prod.getLhs().getIsNullable()){
                // if we think that it's not nullable then we check
                boolean thisProdLhsNullable = true;
                for(GramSymbol<U> sym: prod.getRhs()){
                    if(!sym.getIsNullable()){
                        thisProdLhsNullable = false;
                        break;
                    }
                }
                prod.getLhs().setIsNullable(thisProdLhsNullable);
            }
            // calc FIRST of prod.getRhs()
            boolean nullPrefix = true;
            for(GramSymbol<U> sym: prod.getRhs()){
                if(!nullPrefix){break;}
                for(GramSymbol<U> firstSym : sym.getFirstSet()){
                    prod.getLhs().getFirstSet().add(firstSym);
                }
                nullPrefix = sym.getIsNullable();
            }

            // calc FOLLOW of syms on the Rhs from left to right
            boolean nullSuffix = true;
            int idxMax = prod.getRhs().size();
            for(int idx = idxMax-1; idx >= 0; --idx){
                if(!nullSuffix){break;}
                GramSymbol<U> currSym = prod.getRhs().get(idx);
                for(GramSymbol<U> symInLhs : prod.getLhs().getFollowSet()){
                    currSym.getFollowSet().add(symInLhs);
                }
                nullSuffix = currSym.getIsNullable();
            }

            // calc FOLLOW(s) of the symbols on rhs by i,j iteration
            for(int idx1=idxMax-1; idx1 >= 0; idx1--){
                nullSuffix = true;
                for(int idx2=idx1-1; idx2 >= 0; idx2--){
                    if(!nullSuffix){break;}
                    for(GramSymbol<U> sym_ : prod.getRhs().get(idx1).getFirstSet()){
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
                // extract the Map<String, List<U>> for first and follow and
                // List<String> for nullability
                Map<String, List<String>> firsts = new HashMap<>();
                Map<String, List<String>> follows = new HashMap<>();
                List<String> nulls = new ArrayList<>();
                for(GramSymbol<U> sym : this.nonTermSyms){

                    List<String> currFirsts = firsts.getOrDefault(sym.getValue(), new ArrayList<>());
                    for(GramSymbol<U> sym_ : sym.getFirstSet()){
                        currFirsts.add(sym_.getSymbolToken().getName());
                    }
                    firsts.put(sym.getValue(), currFirsts);


                    List<String> currFollows = follows.getOrDefault(sym.getValue(), new ArrayList<>());
                    for(GramSymbol<U> sym_ : sym.getFollowSet()){
                        currFollows.add(sym_.getSymbolToken().getName());
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

    private void makeStates(GramSymbol<U> extraEof){
        //System.out.println("Starting to make states!!");
        // Start -> .Prog $, ? (no lookahead)
        // have a running idx on a growing list of LR1State
        // for every state check if it has a reducible action for a lookahead.
        // then seperate out states for each of the symbols and the take their closures
        // for terms add as shift action and for non-term add as goto action
        // reuse the state incase the state was already produced before.

        // adding the starting state's item {Start -> . Prog $, ?}
        List<LR1State<T, U>> currStates = new ArrayList<>();
        GramProd<T, U> startProd = this.lhsToProds.get(this.valueToSym.get("Start")).get(0); // only one prod will have the "Start" symbol as lhs
        LR1item<T, U> startItem = new LR1item<>(startProd, 0, extraEof);
        LR1State<T, U> startState = new LR1State<>(new ArrayList<>(List.of(startItem)), this.lhsToProds);

        this.startState = startState;

        startState = LR1State.closure(startState);

        currStates.add(startState);
        this.states.add(startState);

        int idx = 0;
        while(idx < currStates.size()){

            Map<GramSymbol<U>, List<LR1item<T, U>>> symToItems = new HashMap<>();


            for(LR1item<T, U> it: currStates.get(idx).getItems()){

                if(it.getStackTopIdx() < it.getProd().getRhs().size()){
                    // shift/goto action
                    GramSymbol<U> transisym = it.getProd().getRhs().get(it.getStackTopIdx());
                    List<LR1item<T, U>> itemlist = symToItems.getOrDefault(transisym, new ArrayList<>());
                    itemlist.add(new LR1item<>(it.getProd(), it.getStackTopIdx()+1, it.getLookahead()));
                    symToItems.put(transisym, itemlist);

                }else{
                    // reducible item and so this state must have Reduce action for
                    // this lookahead
                    GramSymbol<U> lookaheadsym = it.getLookahead();


                    if(!lookaheadsym.getIsNonTerm() && lookaheadsym.getSymbolToken().getName().equals("EOF")){
                        currStates.get(idx).addAction(lookaheadsym, new Action.Accept());
                    }else{
                        currStates.get(idx).addAction(lookaheadsym, new Action.Reduce(it.getProd()));
                    }
                }
            }
            // for each of the List of the LR1items gathered. take closure of each of
            // these and see if it is a new LR1State, if it is then add it into this.states
            // and also into the currStates list. Finally put the action into the actions
            // of this LR1State
            for(Map.Entry<GramSymbol<U>, List<LR1item<T, U>>> ent: symToItems.entrySet()){
                LR1State<T,U> newState = new LR1State<>(ent.getValue(), this.lhsToProds);
                newState = LR1State.closure(newState);
                LR1State<T,U> existingState = this.stateExists(newState);
                if(existingState != null){
                    newState = existingState;
                }else{
                    // new state found
                    currStates.add(newState);
                    this.states.add(newState);
                }
                currStates.get(idx).addAction(ent.getKey(), new Action.Shift(newState));
            }
            idx += 1;
        }
        //System.out.println("Finished making states!!");
        //System.out.println("Number of states: " + String.valueOf(this.states.size()) );

    }

    protected void setup_(String[] nonTermSyms, List<U> termSyms, List<Pair<List<String>, ReduceAction<T, U>>> prodStrs, GramSymbol<U> extraEof){

        this.tokSet = termSyms;

        for(String s: nonTermSyms){
            GramSymbol<U> gramSym = new GramSymbol<>(true, s);
            gramSym.setIsNullable(false);
            this.addNonTermSym(gramSym);
            this.addValueToSym(s, gramSym);
        }

        for(U tok: termSyms){
            GramSymbol<U> gramSym = new GramSymbol<>(false, null);
            gramSym.setSymbolToken(tok);
            gramSym.setIsNullable(false);
            this.addTermSym(gramSym);
            this.addValueToSym(tok.getName(), gramSym);
        }

        for(Pair<List<String>, ReduceAction<T, U>> prodPair: prodStrs){
            List<String> prod = prodPair.first();
            ReduceAction<T, U> supp = prodPair.second();

            GramSymbol<U> lhs = this.valueToSym.get(prod.get(0));
            List<GramSymbol<U>> rhs = new ArrayList<>();
            for(int i=1; i< prod.size(); i++){
                rhs.add(this.valueToSym.get(prod.get(i)));
            }

            GramProd<T, U> newProd = new GramProd<T, U>(lhs, rhs, supp);

            this.addGramProd(newProd);

            // very critical!! make the this.lhsToProds field here.
            // i had initially forgotten to do this! disastrous!
            List<GramProd<T, U>> currProdsForLhs = this.lhsToProds.getOrDefault(this.valueToSym.get(prod.get(0)), new ArrayList<>());
            currProdsForLhs.add(newProd);
            this.lhsToProds.put(this.valueToSym.get(prod.get(0)), currProdsForLhs);
        }
        this.makeFirstAndFollow(); // Follow set is wasteful for LR1 parser but we do it
                                   // anyways since this method is legacy from lexer package
        this.makeStates(extraEof);
        // lousey design, if parser fails to form LR1 parse table it will throw an unchecked Excpetion.
        // TODO: to return boolean upon parser success
    }




    public Pair<T, List<ParseErr<U>>> parse(){
        T astFull = null;

        Integer windowSz = 6;
        Integer R = 4;
        boolean currAccepted = false;
        boolean oldAccepted = false;

        // this is the burke-fisher err rec.
        // the currStack(s) only execute action and change states but don't really
        // perform any kind of actions.
        Deque<LR1State<T,U>> oldStateStack = new ArrayDeque<>();
        // in the pair `Pair<T, GramSymbol<U>>` the first element is the
        // semantic value for that non term sym
        // and that value is only active in oldSymStack
        Deque<Pair<T, GramSymbol<U>>> oldSymStack = new ArrayDeque<>();
        oldStateStack.push(this.startState);

        Deque<LR1State<T,U>> currStateStack = new ArrayDeque<>();
        Deque<Pair<T, GramSymbol<U>>> currSymStack = new ArrayDeque<>();
        currStateStack.push(this.startState);

        // window is a LinkedList instead of a queue since we need to edit it
        LinkedList<Pair<T, GramSymbol<U>>> window = new LinkedList<>();

        Integer N = this.tokens.size();

        Integer oldIdx = 0; // the index of next lookahead for the old stack.
        Integer currIdx = 0; // the index of the next lookahead for the new stack.

        List<ParseErr<U>> errList = new ArrayList<>();

        while(!currAccepted){
            // consume a token by the currStack
            U tok = this.tokens.get(currIdx);
            System.out.println("currTok name: " + tok.getName() + " Content: " + tok.getContent());
            GramSymbol<U> gramSym = new GramSymbol<>(false, null);
            gramSym.setSymbolToken(tok);


            // look for the action for this gramSym in the currState
            LR1State<T,U> currState = currStateStack.peek();
            Action<T, U> action = currState.getAction(gramSym);

            //System.out.println("for sym: " + gramSym.toString());
            //System.out.printf("finding action for state: " + currState.toString() + "\n");

            if(action == null){
                System.out.println("!!! Action not found!");
                // error on currStack
                // we have to insert, sub, del every tok possible in bet
                // oldIdx and currIdx

                // we need to find the expected tok by either sub or insert
                U expectedTok = null;
                Integer tokIdx = null;
                Integer opCode = -1; // 0 for sub, 1 for insert, 2 for delete

                // add toks from tok list starting from currIdx upto R (or till the end if its smaller)
                for(int i = 1; i <= Math.min(R, this.tokens.size()-currIdx-1); i++){
                    U tok_ = this.tokens.get(currIdx+i);
                    GramSymbol<U> gramSym_ = new GramSymbol<>(false, null);
                    gramSym_.setSymbolToken(tok_);
                    window.addLast(new Pair<>(null, gramSym_));
                }

                // try substitution
outer_:
                for(U candTok: this.tokSet){
                    GramSymbol<U> candGramSym = new GramSymbol<>(false, null);
                    candGramSym.setSymbolToken(candTok);

                    ListIterator<Pair<T, GramSymbol<U>>> it = window.listIterator();

outer:
                    while(it.hasNext()){
                        GramSymbol<U> nowSym = it.next().second();
                        it.set(new Pair<>(null, candGramSym)); // edit the window

                        // now after subbing the gramSym with candGramSym
                        // we try to see if the cpy_oldSymStack can go beyond the
                        // currIdx upto 4 tokens.
                        Deque<LR1State<T,U>> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<Pair<T, GramSymbol<U>>> cpy_oldSymStack = new ArrayDeque<>(oldSymStack);
                        for(Pair<T, GramSymbol<U>> nextSym_: window){
                            GramSymbol<U> nextSym = nextSym_.second();

                            LR1State<T, U> st = cpy_oldStateStack.peek();

                            Action<T, U> ac = st.getAction(nextSym);

                            if(ac == null){
                                it.set(new Pair<>(null, nowSym)); // restore window
                                continue outer;
                            }
                            if (ac instanceof Action.Shift<?, ?> shAction) {
                                LR1State<T, U> state = (LR1State<T, U>) shAction.state();
                                // use state
                                cpy_oldStateStack.push(state);
                                cpy_oldSymStack.push(new Pair<>(null, nextSym));

                            } else if (ac instanceof Action.Reduce<?, ?> reAction) {
                                GramProd<T, U> prod = (GramProd<T, U>) reAction.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    cpy_oldSymStack.pop();
                                    cpy_oldStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.Shift<T, U> gotoAct = (Action.Shift<T, U>) cpy_oldStateStack.peek().getAction(prod.getLhs());
                                cpy_oldStateStack.push(gotoAct.state());
                                cpy_oldSymStack.push(new Pair<>(null, prod.getLhs()));

                            } else if (ac instanceof Action.Accept<?,?> acAction) {
                                // accept
                                expectedTok = candTok; // found a token!! exit and
                                opCode = 0;
                                tokIdx = it.previousIndex();
                                // no need to try insert and del
                                break outer;
                            }
                        }
                        it.set(new Pair<>(null, nowSym)); // restore window

                        // if we came here then we found a candTok
                        expectedTok = candTok; // found a token!! exit and
                        opCode = 0;
                        tokIdx = it.previousIndex();
                        break outer_;
                    }
                }

                // try insertion
                if(expectedTok == null){
outerinsert_:
                    for(U candTok: this.tokSet){
                        GramSymbol<U> candGramSym = new GramSymbol<>(false, null);
                        candGramSym.setSymbolToken(candTok);

                        ListIterator<Pair<T, GramSymbol<U>>> it = window.listIterator();

outerinsert:
                        while(it.hasNext()){
                            GramSymbol<U> nowSym = it.next().second();
                            Integer insertIdx = it.previousIndex();
                            it.add(new Pair<>(null, candGramSym)); // edit the window by inserting the candTok

                            // now after subbing the gramSym with candGramSym
                            // we try to see if the cpy_oldSymStack can go beyond the
                            // currIdx upto 4 tokens.
                            Deque<LR1State<T,U>> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                            Deque<Pair<T, GramSymbol<U>>> cpy_oldSymStack = new ArrayDeque<>(oldSymStack);
                            for(Pair<T, GramSymbol<U>> nextSym_: window){
                                GramSymbol<U> nextSym = nextSym_.second();

                                LR1State<T,U> st = cpy_oldStateStack.peek();
                                Action<T, U> ac = st.getAction(nextSym);

                                if(ac == null){
                                    it.next(); it.remove(); // restore window
                                    continue outerinsert;
                                }
                                if (ac instanceof Action.Shift<?,?> shAction) {
                                    LR1State<T,U> state = (LR1State<T, U>) shAction.state();
                                    // use state
                                    cpy_oldStateStack.push(state);
                                    cpy_oldSymStack.push(new Pair<>(null, nextSym));

                                } else if (ac instanceof Action.Reduce<?,?> reAction) {
                                    GramProd<T, U> prod = (GramProd<T, U>) reAction.prod();
                                    // use prod
                                    for(GramSymbol<U> rhsSym: prod.getRhs()){
                                        cpy_oldSymStack.pop();
                                        cpy_oldStateStack.pop();
                                    }
                                    // warning: here assuming gotoAct will always shift
                                    Action.Shift<T, U> gotoAct = (Action.Shift<T, U>) cpy_oldStateStack.peek().getAction(prod.getLhs());
                                    cpy_oldStateStack.push(gotoAct.state());
                                    cpy_oldSymStack.push(new Pair<>(null, prod.getLhs()));

                                } else if (ac instanceof Action.Accept<?,?> acAction) {
                                    // accept
                                    expectedTok = candTok; // found a token!! exit and
                                    opCode = 1;
                                    tokIdx = insertIdx;
                                    // no need to try insert and del
                                    break outerinsert;
                                }
                            }
                            it.next(); it.remove(); // restore the window by deleting the tok

                            // if we came here then we found a candTok
                            expectedTok = candTok; // found a token!! exit and
                            opCode = 1;
                            tokIdx = insertIdx;
                            break outerinsert_;
                        }
                    }
                }

                // try deletion
                if(expectedTok == null){
                        ListIterator<Pair<T, GramSymbol<U>>> it = window.listIterator();
outerdel:
                    while(it.hasNext()){
                        Pair<T, GramSymbol<U>> nowSym = it.next();
                        Integer delIdx = it.previousIndex();

                        it.remove(); // edit the window by removing a tok
                                     // we try to see if the cpy_oldSymStack can go beyond the
                                     // currIdx upto 4 tokens.
                        Deque<LR1State<T,U>> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<Pair<T, GramSymbol<U>>> cpy_oldSymStack = new ArrayDeque<>(oldSymStack);
                        for(Pair<T, GramSymbol<U>> nextSym_: window){
                            GramSymbol<U> nextSym = nextSym_.second();

                            LR1State<T,U> st = cpy_oldStateStack.peek();
                            Action<T, U> ac = st.getAction(nextSym);

                            if(ac == null){
                                it.add(nowSym); // restore window
                                continue outerdel;
                            }
                            if (ac instanceof Action.Shift<?,?> shAction) {
                                LR1State<T, U> state = (LR1State<T, U>) shAction.state();
                                // use state
                                cpy_oldStateStack.push(state);
                                cpy_oldSymStack.push(new Pair<>(null, nextSym));

                            } else if (ac instanceof Action.Reduce<?,?> reAction) {
                                GramProd<T, U> prod = (GramProd<T, U>) reAction.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    cpy_oldSymStack.pop();
                                    cpy_oldStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.Shift<T, U> gotoAct =(Action.Shift<T, U>) cpy_oldStateStack.peek().getAction(prod.getLhs());
                                cpy_oldStateStack.push(gotoAct.state());
                                cpy_oldSymStack.push(new Pair<>(null, prod.getLhs()));

                            } else if (ac instanceof Action.Accept<?,?> acAction) {
                                // accept
                                expectedTok = null; // no token in case of del
                                opCode = 2;
                                tokIdx = delIdx;
                                // no need to try insert and del
                                break outerdel;
                            }
                        }
                        it.add(nowSym); // restore the window by adding the deleted tok

                        // if we came here then we found a candTok
                        expectedTok = null; // found a token!! exit and
                        opCode = 2;
                        tokIdx = delIdx;
                        break outerdel;
                    }
                }

                // bringing back the size of window to its normal size
                for(int i = 0; i < Math.min(R, this.tokens.size()-currIdx-1); i++){
                    window.removeLast();
                }

                switch(opCode){
                    case 0: {
                        currIdx++;

                        GramSymbol<U> gramSym_ = new GramSymbol<>(false, null);
                        gramSym_.setSymbolToken(expectedTok);

                        window.set(tokIdx, new Pair<>(null, gramSym_));

                        currStateStack = new ArrayDeque<>(oldStateStack);
                        currSymStack = new ArrayDeque<>(oldSymStack);

                        for(Pair<T, GramSymbol<U>> nextSym_: window){
                            GramSymbol<U> nextSym = nextSym_.second();

                            LR1State<T,U> st = currStateStack.peek();
                            Action<T, U> ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift<?,?> shAction) {
                                LR1State<T,U> state = (LR1State<T, U>) shAction.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(new Pair<>(null, nextSym));

                            } else if (ac instanceof Action.Reduce<?,?> reAction) {
                                GramProd<T, U> prod = (GramProd<T, U>) reAction.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.Shift<T, U> gotoAct =(Action.Shift<T, U>) currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(new Pair<>(null, prod.getLhs()));

                            } else if (ac instanceof Action.Accept<?,?> acAction) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                tok.getColNo(),
                                expectedTok,
                                tok);
                        errList.add(errParse);
                        continue;
                    }
                    case 1: {
                        currIdx++;

                        GramSymbol<U> gramSym_ = new GramSymbol<>(false, null);
                        gramSym.setSymbolToken(expectedTok);

                        window.add(tokIdx, new Pair<>(null, gramSym_));

                        currStateStack = new ArrayDeque<>(oldStateStack);
                        currSymStack = new ArrayDeque<>(oldSymStack);

                        for(Pair<T, GramSymbol<U>> nextSym_: window){
                            GramSymbol<U> nextSym = nextSym_.second();

                            LR1State<T,U> st = currStateStack.peek();
                            Action<T, U> ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift<?,?> shAction) {
                                LR1State<T,U> state = (LR1State<T,U>) shAction.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(new Pair<>(null, nextSym));

                            } else if (ac instanceof Action.Reduce<?,?> reAction) {
                                GramProd<T, U> prod =(GramProd<T, U>) reAction.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.Shift<T, U> gotoAct =(Action.Shift<T, U>) currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(new Pair<>(null, prod.getLhs()));

                            } else if (ac instanceof Action.Accept<?,?> acAction) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                tok.getColNo(),
                                expectedTok,
                                tok);
                        errList.add(errParse);
                        continue;
                    }
                    case 2: {
                        currIdx++;

                        window.remove(tokIdx);

                        currStateStack = new ArrayDeque<>(oldStateStack);
                        currSymStack = new ArrayDeque<>(oldSymStack);

                        for(Pair<T, GramSymbol<U>> nextSym_: window){
                            GramSymbol<U> nextSym = nextSym_.second();

                            LR1State<T,U> st = currStateStack.peek();
                            Action ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift<?, ?> shAction) {
                                LR1State<T,U> state = (LR1State<T, U>)shAction.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(new Pair<>(null, nextSym));

                            } else if (ac instanceof Action.Reduce<?,?> reAction) {
                                GramProd<T, U> prod = (GramProd<T, U>) reAction.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.Shift<T, U> gotoAct =(Action.Shift<T, U>) currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(new Pair<>(null, prod.getLhs()));

                            } else if (ac instanceof Action.Accept<?, ?> acAction) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                tok.getColNo(),
                                expectedTok,
                                tok);
                        errList.add(errParse);
                        continue;
                    }
                }
            }

            // no error on this tok
            // update currStateStack and currSymStack
            if (action instanceof Action.Shift<?,?> shift) {
                LR1State<T,U> state = (LR1State<T,U>) shift.state();
                // use state
                currStateStack.push(state);
                currSymStack.push(new Pair<>(null, gramSym));
                currIdx++;

                // push it into the window
                window.addLast(new Pair<>(null, gramSym));

            } else if (action instanceof Action.Reduce<?,?> reduce) {
                GramProd<T, U> prod = (GramProd<T, U>) reduce.prod();
                // use prod
                for(GramSymbol<U> rhsSym: prod.getRhs()){
                    currSymStack.pop();
                    currStateStack.pop();
                }
                // warning: here assuming gotoAct will always shift
                Action.Shift<T, U> gotoAct =(Action.Shift<T, U>) currStateStack.peek().getAction(prod.getLhs());
                currStateStack.push(gotoAct.state());
                currSymStack.push(new Pair<>(null, prod.getLhs()));

            } else if (action instanceof Action.Accept<?,?> accept) {
                // accept
                currAccepted = true;
            }
            // now we need to take a token out of the window in case its size is > `windowSz`
            // and update the old statestack and symstack
            // emit the AstNode from here for each reduction
            if(window.size() > windowSz){
                System.out.println("never got into this loop");
                GramSymbol<U> gramSymFront = window.removeFirst().second();

                Action<T, U> action_ = oldStateStack.peek().getAction(gramSymFront);

                if (action_ instanceof Action.Shift<?, ?> shift_) {
                    LR1State<T,U> state = (LR1State<T, U>) shift_.state();
                    // use state
                    oldStateStack.push(state);
                    oldSymStack.push(new Pair<>(null, gramSymFront));

                } else if (action_ instanceof Action.Reduce<?, ?> reduce_) {

                    GramProd<T, U> prod = (GramProd<T, U>) reduce_.prod();
                    prod.getSupp().apply(prod, oldStateStack, oldSymStack);

                } else if (action_ instanceof Action.Accept<?, ?> accept_) {
                    // accept
                    // should not come here ever.
                    astFull = oldSymStack.peek().first(); // Prog. $
                    oldAccepted = true;
                }
            }
        }
        // curr has accepted. we need to consume the tokens in the window.
        if(currAccepted && astFull == null){
            System.out.println("was in the final window consumption loop");
            while(window.size() > 0){
                GramSymbol<U> gramSym = window.removeFirst().second();

                Action<T, U> action_ = oldStateStack.peek().getAction(gramSym);

                if (action_ instanceof Action.Shift<?,?> shift_) {
                    LR1State<T,U> state = (LR1State<T,U>) shift_.state();
                    // use state
                    oldStateStack.push(state);
                    oldSymStack.push(new Pair<>(null, gramSym));

                } else if (action_ instanceof Action.Reduce<?,?> reduce_) {
                    GramProd<T, U> prod = (GramProd<T, U>)reduce_.prod();

                    ReduceAction<T, U> reducFunc = prod.getSupp();

                    reducFunc.apply(prod, oldStateStack, oldSymStack);
                    window.addFirst(new Pair<>(null, gramSym)); // for shift we don't consume the token. so we add it back to window

                } else if (action_ instanceof Action.Accept<?,?> accept_) {
                    // accept
                    // should not come here ever.
                    astFull = oldSymStack.peek().first(); // Prog. $
                    oldAccepted = true;
                }
            }
        }


        return new Pair<>(astFull, errList);
    }

    public abstract void setup();


}
