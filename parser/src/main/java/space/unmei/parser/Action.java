package space.unmei.parser;

import space.unmei.lexer.LexToken;

public interface Action<T , U extends LexToken> {

    record Shift<T , U extends LexToken>(
            LR1State<T, U> state
    ) implements Action<T, U> {}

    record Reduce<T , U extends LexToken>(
            GramProd<T, U> prod
    ) implements Action<T, U> {}

    record Accept<T , U extends LexToken>()
            implements Action<T, U> {}
}
