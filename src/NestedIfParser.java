import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NestedIfParser {
    public static void main(String[] args) {
        CFG cfg = constructGrammar();
        System.out.println(cfg);
        System.out.println("--------------------------------------------------");
        Parser parser = new Parser(cfg);
        Scanner scanner = new Scanner(System.in);
        List<CFG.Rule> parsed = parser.parse(scanner.nextLine());
        displayOutput(parsed);
    }

    private static void displayOutput(List<CFG.Rule> parsed) {
        if (parsed == null) {
            System.out.println("This input can't be parsed");
            return;
        }
        List<CFG.Variable> variables = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        for (CFG.Rule rule : parsed) {
            output.append(rule.lhs());
            for (int j = variables.size() - 1; j >= 0; j--) {
                if (variables.get(j) == null) {
                    continue;
                }
                if (rule.lhs().equals(variables.get(j))) {
                    output.append("[").append(j).append("]");
                    variables.set(j, null);
                    break;
                }
            }
            output.append("->");
            for (int j = 0; j < rule.rhs().size(); j++) {
                output.append(rule.rhs().get(j));
                if (rule.rhs().get(j) instanceof CFG.Variable variable) {
                    output.append("[").append(variables.size()).append("]");
                    variables.add(variable);
                }
            }
            output.append("\n");
        }
        System.out.println(output);
    }

    private static CFG constructGrammar() {
        String[] variables = new String[]{"X", "Y", "Z", "I", "N", "O"};

        List<String> terminals = new ArrayList<>(List.of(new String[]{"=", "<", ">", "!", "(", ")", "{", "}", "_"}));
        for (char i = 'A'; i <= 'Z'; i++) {
            terminals.add(String.valueOf(i));
        }
        for (char i = 'a'; i <= 'z'; i++) {
            terminals.add(String.valueOf(i));
        }
        for (char i = '0'; i <= '9'; i++) {
            terminals.add(String.valueOf(i));
        }

        String startVariable = "X";

        CFG cfg = new CFG(terminals.toArray(new String[0]), variables, startVariable);

        cfg.addRule("X", "if(Y){Z}");
        cfg.addRule("Z", "X");
        cfg.addRule("Z", "I=I");
        cfg.addRule("Z", "I=N");
        cfg.addRule("Y", "IOI");
        cfg.addRule("Y", "ION");
        cfg.addRule("O", "==");
        cfg.addRule("O", ">=");
        cfg.addRule("O", "<=");
        cfg.addRule("O", "!=");
        cfg.addRule("O", ">");
        cfg.addRule("O", "<");
        cfg.addRule("I", "II");
        for (char i = 'A'; i <= 'Z'; i++) {
            cfg.addRule("I", List.of(new CFG.Symbol[]{new CFG.Terminal(String.valueOf(i))}));
        }
        for (char i = 'a'; i <= 'z'; i++) {
            cfg.addRule("I", List.of(new CFG.Symbol[]{new CFG.Terminal(String.valueOf(i))}));
        }
        cfg.addRule("N", "NN");
        for (char i = '0'; i <= '9'; i++) {
            cfg.addRule("N", List.of(new CFG.Symbol[]{new CFG.Terminal(String.valueOf(i))}));
        }
        return cfg;
    }
}