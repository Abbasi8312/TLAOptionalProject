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
        System.out.println(parser.parse(scanner.nextLine()));
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