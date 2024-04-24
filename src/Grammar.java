import java.util.*;

public class Grammar {
    private final char S = 'S';
    private final List<Character> V_n = List.of('S', 'D', 'R');
    private final List<Character> V_t = List.of('a', 'b', 'c', 'd', 'f');
    private final Map<Character, List<String>> P = Map.of(
            'S', List.of("aS", "bD", "fR"),
            'D', List.of("cD", "dR", "d"),
            'R', List.of("bR", "f")
    );

    public String generateString() {
        return generateFromSymbol(S);
    }

    private String generateFromSymbol(char symbol) {
        Random random = new Random();
        List<String> production = P.get(symbol);
        String result = "";
        if (V_t.contains(symbol)) {
            return String.valueOf(symbol);
        } else {
            String chosenProduction = production.get(random.nextInt(production.size()));
            for (char s : chosenProduction.toCharArray()) {
                result += generateFromSymbol(s);
            }
        }
        return result;
    }

    public void generateWords() {
        for (int i = 0; i < 5; i++) {
            String w = generateString();
            System.out.println("Generated string: " + w);
        }
    }

    public FiniteAutomaton toFiniteAutomaton() {
        List<Character> Q = List.of('S', 'D', 'R', 'f');
        List<Character> Sigma = List.of('a', 'b', 'c', 'd', 'f');
        Map<Pair, Character> Delta = new HashMap<>();
        Delta.put(new Pair('S', 'a'), 'S');
        Delta.put(new Pair('S', 'b'), 'D');
        Delta.put(new Pair('S', 'f'), 'R');
        Delta.put(new Pair('D', 'c'), 'D');
        Delta.put(new Pair('D', 'd'), 'R');
        Delta.put(new Pair('R', 'b'), 'R');
        Delta.put(new Pair('R', 'f'), 'f');
        char q0 = 'S';
        List<Character> F = List.of('d', 'f');
        return new FiniteAutomaton(Q, Sigma, Delta, q0, F);
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.generateWords();
        System.out.println(grammar.toFiniteAutomaton());

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();
        System.out.println(finiteAutomaton.stringBelongToLanguage("abdf"));
    }
}

class FiniteAutomaton {
    private final List<Character> Q;
    private final List<Character> Sigma;
    private final Map<Pair, Character> Delta;
    private final char q0;
    private final List<Character> F;

    public FiniteAutomaton(List<Character> Q, List<Character> Sigma, Map<Pair, Character> Delta, char q0, List<Character> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.Delta = Delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(String w) {
        char currentState = q0;
        for (char letter : w.toCharArray()) {
            if (Delta.containsKey(new Pair(currentState, letter))) {
                currentState = Delta.get(new Pair(currentState, letter));
            } else {
                return false;
            }
        }
        return F.contains(currentState);
    }

    @Override
    public String toString() {
        return "FiniteAutomaton{" +
                "Q=" + Q +
                ", Sigma=" + Sigma +
                ", Delta=" + Delta +
                ", q0=" + q0 +
                ", F=" + F +
                '}';
    }
}

class Pair {
    private final char first;
    private final char second;

    public Pair(char first, char second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return first == pair.first && second == pair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}