import java.util.*;

public class Parser {
    private final CFG cfg;
    private final List<CFG.Rule> rules;

    public Parser(CFG cfg) {
        this.cfg = cfg;
        rules = cfg.rules();
    }

    public List<CFG.Rule> parse(String input) {
        Stack<CFG.Symbol> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            queue.add(String.valueOf(input.charAt(i)));
        }
        return parse(queue, stack);
    }

    public List<CFG.Rule> parse(Queue<String> buffer, Stack<CFG.Symbol> stack) {
        Stack<CFG.Symbol> newStack = (Stack<CFG.Symbol>) stack.clone();
        Queue<String> newBuffer = new LinkedList<>(buffer);
        while (!buffer.isEmpty()) {
            String value = newBuffer.poll();
            if (value.equals(" ") || value.equals("\n")) {
                continue;
            }
            newStack.push(new CFG.Terminal(value));
            List<CFG.Rule> tmp = parse(newBuffer, newStack);
            if (tmp != null) {
                return tmp;
            }
            break;
        }
        for (CFG.Rule rule : rules) {
            newStack = (Stack<CFG.Symbol>) stack.clone();
            if (rule.applyRule(newStack)) {
                List<CFG.Rule> tmp = parse(new LinkedList<>(buffer), newStack);
                if (tmp != null) {
                    tmp.add(rule);
                    return tmp;
                }
            }
        }
        if (buffer.isEmpty()) {
            if (stack.size() == 1 && stack.peek().equals(new CFG.Variable(cfg.startVariable()))) {
                return new ArrayList<>();
            }
        }
        return null;
    }
}
