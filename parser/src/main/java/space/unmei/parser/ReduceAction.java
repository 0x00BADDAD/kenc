package space.unmei.parser;

import java.util.*;
import java.lang.FunctionalInterface;
import space.unmei.lexer.*;

@FunctionalInterface
public interface ReduceAction<T, U extends LexToken> {
    void apply(
        GramProd<T, U> prod,
        Deque<LR1State<T, U>> stateStack,
        Deque<Pair<T, GramSymbol<U>>> symStack
    );
}
