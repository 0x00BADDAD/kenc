package space.unmei.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;

public class RegexLexer {
    // this is all GPT
    private final String regex;
    private int idx;

    public RegexLexer(String regex) {
        this.regex = regex;
        this.idx = 0;
    }

    public List<RegexToken> lex() {

        List<RegexToken> tokens = new ArrayList<>();

        while (idx < regex.length()) {

            char ch = regex.charAt(idx);

            switch (ch) {

                case '(':
                    tokens.add(new RegexToken(
                            RegexTokenType.LPAREN,
                            "("));
                    idx++;
                    break;

                case ')':
                    tokens.add(new RegexToken(
                            RegexTokenType.RPAREN,
                            ")"));
                    idx++;
                    break;

                case '|':
                    tokens.add(new RegexToken(
                            RegexTokenType.OR,
                            "|"));
                    idx++;
                    break;

                case '*':
                    tokens.add(new RegexToken(
                            RegexTokenType.STAR,
                            "*"));
                    idx++;
                    break;

                case '+':
                    tokens.add(new RegexToken(
                            RegexTokenType.PLUS,
                            "+"));
                    idx++;
                    break;

                case '?':
                    tokens.add(new RegexToken(
                            RegexTokenType.QUESTION,
                            "?"));
                    idx++;
                    break;

                case '[':
                    tokens.add(readCharSet());
                    break;

                case '\\':
                    tokens.add(readEscape());
                    break;

                default:
                    tokens.add(new RegexToken(
                            RegexTokenType.CHAR,
                            String.valueOf(ch)));
                    idx++;
            }
        }


        return this.insertConcats(tokens);
    }

    public static List<RegexToken> insertConcats(List<RegexToken> tokens) {

        List<RegexToken> result = new ArrayList<>();

        for (int i = 0; i < tokens.size() - 1; i++) {

            RegexToken curr = tokens.get(i);
            RegexToken next = tokens.get(i + 1);

            result.add(curr);

            if (needsConcat(curr, next)) {
                result.add(
                    new RegexToken(
                        RegexTokenType.CONCAT,
                        "."
                    )
                );
            }
        }

        result.add(tokens.get(tokens.size() - 1));


        result.add(new RegexToken(
                RegexTokenType.EOF,
                "EOF"));

        return result;
    }

    private static boolean needsConcat(
            RegexToken left,
            RegexToken right) {

        boolean leftCanEnd =
                left.getType() == RegexTokenType.CHAR
             || left.getType() == RegexTokenType.CHARSET
             || left.getType() == RegexTokenType.RPAREN
             || left.getType() == RegexTokenType.STAR
             || left.getType() == RegexTokenType.PLUS
             || left.getType() == RegexTokenType.QUESTION;

        boolean rightCanBegin =
                right.getType() == RegexTokenType.CHAR
             || right.getType() == RegexTokenType.CHARSET
             || right.getType() == RegexTokenType.LPAREN;

        return leftCanEnd && rightCanBegin;
    }

    private RegexToken readEscape() {
        return new RegexToken(
                RegexTokenType.CHAR,
                String.valueOf(readEscapedChar()));
    }

    private char readEscapedChar() {

        idx++; // skip '\'

        if (idx >= regex.length()) {
            throw new IllegalArgumentException(
                    "Dangling escape at end of regex.");
        }

        char ch = regex.charAt(idx);
        idx++;

        return switch (ch) {
        case 'n' -> '\n';
        case 't' -> '\t';
        case 'r' -> '\r';
        case 'f' -> '\f';
        case '\\' -> '\\';
        case '[' -> '[';
        case ']' -> ']';
        case '(' -> '(';
        case ')' -> ')';
        case '*' -> '*';
        case '+' -> '+';
        case '?' -> '?';
        case '|' -> '|';
        case '-' -> '-';
        default -> ch;
        };
    }

    private RegexToken readCharSet() {

        idx++; // skip '['

        LinkedHashSet<Character> chars = new LinkedHashSet<>();

        while (true) {

            if (idx >= regex.length()) {
                throw new IllegalArgumentException(
                        "Missing closing ]");
            }

            if (regex.charAt(idx) == ']') {
                idx++;
                break;
            }

            char first;

            if (regex.charAt(idx) == '\\') {
                first = readEscapedChar();
            } else {
                first = regex.charAt(idx++);
            }

            if (idx < regex.length() - 1
                    && regex.charAt(idx) == '-'
                    && regex.charAt(idx + 1) != ']') {

                idx++; // skip '-'

                char last;

                if (regex.charAt(idx) == '\\') {
                    last = readEscapedChar();
                } else {
                    last = regex.charAt(idx++);
                }

                if (first > last) {
                    throw new IllegalArgumentException(
                            "Invalid character range: "
                            + first + "-" + last);
                }

                for (char c = first; c <= last; c++) {
                    chars.add(c);
                }

            } else {

                chars.add(first);
            }
        }

        StringBuilder sb = new StringBuilder();

        for (char c : chars) {
            sb.append(c);
        }

        return new RegexToken(
                RegexTokenType.CHARSET,
                sb.toString());
    }
}
