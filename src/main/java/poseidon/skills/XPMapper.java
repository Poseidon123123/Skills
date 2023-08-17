package poseidon.skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import poseidon.skills.Commands.Debug;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.Players;

import java.util.*;
import java.util.function.DoubleBinaryOperator;


public class XPMapper {
    private static final HashMap<EntityType, Integer> mobKillXP = new HashMap<>();
    public static void addMob(EntityType mob, int x){
        mobKillXP.putIfAbsent(mob,x);
    }
    public static void removeMob(EntityType mob){
        mobKillXP.remove(mob);
    }
    public static int getMobXP(EntityType mob){
        return mobKillXP.getOrDefault(mob, 0);
    }
    public static HashMap<EntityType, Integer> getMobKillXP(){
        return mobKillXP;
    }

    public static void xpAdd(Players p, double xp, boolean beruf){
        if(xp <= 0){
            return;
        }
        if(beruf && !p.getBerufklasse().equals(Berufklasse.Unchosed)) {
            int i1 = p.getBerufLevel();
            int i2 = 0;
            int xp2 = (int) (xp + p.getBerufXP());
            ArrayList<Integer> needed = new ArrayList<>();
            int level = 0;
            while (i2 <= 100 && i1 < 101) {
                needed.add((int) evaluateMathExpression(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Funktions.XP")), i1));
                i1++;
                i2++;
            }
            for (int i : needed) {
                if (xp2 >= i) {
                    xp2 = xp2 - i;
                    level++;
                } else break;
            }
            p.setBerufLevel(p.getBerufLevel() + level);
            p.setBerufXP(xp2);
            p.getPlayer().sendMessage(ChatColor.GREEN + String.valueOf(xp) + " XP");
            Debug.berufBar(p.getPlayer());
            if (level >= 1) {
                p.getPlayer().sendMessage(ChatColor.GREEN + "Herzlichen Glückwunsch, du bist jetzt " + p.getBerufklasse().getDisplayName() +" Level: " + p.getBerufLevel());
            }
        }
        else {
            if(!p.getKampfklasse().equals(Kampfklassen.Unchosed)) {
                int i1 = p.getKampfLevel();
                int i2 = 0;
                int xp2 = (int) (xp + p.getKampfXP());
                ArrayList<Integer> needed = new ArrayList<>();
                int level = 0;
                while (i2 <= 100 && i1 < 101) {
                    needed.add((int) evaluateMathExpression(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Funktions.XP")), i1));
                    i1++;
                    i2++;
                }
                for (int i : needed) {
                    if (xp2 >= i) {
                        xp2 = xp2 - i;
                        level++;
                    } else break;
                }
                p.setKampfLevel(p.getKampfLevel() + level);
                p.setKampfXP(xp2);
                p.getPlayer().sendMessage(ChatColor.GREEN + String.valueOf(xp) + " XP");
                Debug.kampfBar(p.getPlayer());
                if (level >= 1) {
                    p.getPlayer().sendMessage(ChatColor.GREEN + "Herzlichen Glückwunsch, du bist jetzt " + p.getKampfklasse().getDisplayName() +" Level: " + p.getKampfLevel());
                }
            }
        }
    }
    public static double evaluateMathExpression(String expression, double x) {
        Map<String, DoubleBinaryOperator> operators = new HashMap<>();
        operators.put("+", Double::sum);
        operators.put("-", (a, b) -> a - b);
        operators.put("*", (a, b) -> a * b);
        operators.put("/", (a, b) -> a / b);
        operators.put("^", Math::pow); // Hochzeichen für Potenzierung

        String[] tokens = expression.split("(?=[-+*/^()])|(?<=[-+*/^()])");

        Deque<Double> values = new ArrayDeque<>();
        Deque<String> operatorsStack = new ArrayDeque<>();

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            char firstChar = token.charAt(0);
            if (Character.isDigit(firstChar) || firstChar == '.') {
                values.push(Double.parseDouble(token));
            } else if (token.equals("x")) {
                values.push(x);
            } else if (token.equals("(")) {
                operatorsStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorsStack.isEmpty() && !operatorsStack.peek().equals("(")) {
                    evaluateTopOperator(values, operatorsStack, operators);
                }
                operatorsStack.pop(); // Entferne die öffnende Klammer
            } else {
                while (!operatorsStack.isEmpty() && hasPrecedence(operatorsStack.peek(), token)) {
                    evaluateTopOperator(values, operatorsStack, operators);
                }
                operatorsStack.push(token);
            }
        }

        while (!operatorsStack.isEmpty()) {
            evaluateTopOperator(values, operatorsStack, operators);
        }

        return values.pop();
    }

    private static boolean hasPrecedence(String op1, String op2) {
        int precedence1 = getPrecedence(op1);
        int precedence2 = getPrecedence(op2);
        return precedence1 >= 0 && precedence1 >= precedence2;
    }

    private static int getPrecedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3; // Potenzierung hat die höchste Priorität
            default -> -1; // Wenn es kein Operator ist, geben wir -1 zurück
        };
    }

    private static void evaluateTopOperator(Deque<Double> values, Deque<String> operatorsStack,
                                            Map<String, DoubleBinaryOperator> operators) {
        String operator = operatorsStack.pop();
        Double b = values.pop();
        Double a = values.pop();
        DoubleBinaryOperator operation = operators.get(operator);
        values.push(operation.applyAsDouble(a, b));
    }
}
