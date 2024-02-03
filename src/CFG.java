import java.util.*;

public class CFG {
    private final Set<Terminal> terminalSet;
    private final Set<Variable> variableSet;
    private final Variable startVariable;
    private final List<Rule> rules;

    public CFG(Set<Terminal> terminalSet, Set<Variable> variableSet, Variable startVariable) {
        terminalSet = new HashSet<>();
        variableSet = new HashSet<>();
        rules = new ArrayList<>();
        this.terminalSet = terminalSet;
        this.variableSet = variableSet;
        this.startVariable = startVariable;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("CFG {\n");
        out.append("Terminals: ");
        for (Terminal terminal : terminalSet) {
            out.append(terminal).append(" ");
        }
        out.append("\nVariables: ");
        for (Variable variable : variableSet) {
            out.append(variable).append(" ");
        }
        out.append("\nStart Variable: ").append(startVariable);
        out.append("\nRules: {\n");
        for (Rule rule : rules) {
            out.append(rule).append("\n");
        }
        out.append("}\n");
        return out.toString();
    }

    public boolean addRule(Variable lhs, List<Symbol> rhs) {
        if (!variableSet.contains(lhs)) {
            throw new RuntimeException("Invalid Variable");
        }
        for (Symbol element : rhs) {
            if (element instanceof Variable variable && !variableSet.contains(
                    variable) || element instanceof Terminal terminal && !terminalSet.contains(terminal)) {
                throw new RuntimeException("Invalid Symbol");
            }
        }
        Rule rule = new Rule(lhs, rhs);
        if (rules.contains(rule)) {
            return false;
        }
        rules.add(rule);
        return true;
    }

    public abstract static class Symbol {
        private final String value;

        public Symbol(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Symbol symbol = (Symbol) o;
            return Objects.equals(value, symbol.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static class Terminal extends Symbol {
        public Terminal(String value) {
            super(value);
        }
    }

    public static class Variable extends Symbol {
        public Variable(String value) {
            super(value);
        }
    }

    public static class Rule {
        private final Variable lhs;
        private final List<Symbol> rhs;

        public Rule(Variable lhs, List<Symbol> rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public boolean applyRule(Stack<Symbol> stack) {
            for (int i = rhs.size() - 1; i >= 0; i--) {
                if (!rhs.get(i).equals(stack.pop())) {
                    // Don't apply and Push back the popped symbols (Restore to original state)
                    for (int j = i; j < rhs.size(); j++) {
                        stack.push(rhs.get(i));
                    }
                    return false;
                }
            }
            // Apply rule
            stack.push(lhs);
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Rule rule = (Rule) o;
            for (int i = 0; i < rhs.size(); i++) {
                if (!rhs.get(i).equals(rule.rhs.get(i))) {
                    return false;
                }
            }
            return Objects.equals(lhs, rule.lhs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lhs, rhs);
        }

        @Override
        public String toString() {
            StringBuilder rhs = new StringBuilder();
            for (Symbol rh : this.rhs) {
                rhs.append(rh);
            }
            return lhs + "->" + rhs;
        }
    }
}