/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Stack;

/**
 *
 * @author Ruby
 */
public class Calculator {
    
    private final String CLEAR = "C";
    private Stack<String> result;
    private double extra;
    private String operand;
    
    public Calculator() {
        result = new Stack();
        result.add("0");
        operand = CLEAR;
    }
    
    private String standardized(String str) {
        if (str.endsWith(".")) str = str + "0";
        return str;
    }
    
    public void input(char c) {
        if (operand.isEmpty() && result.size() == 1) clear();
        if (!operand.isEmpty() && !operand.equals(CLEAR) && result.size() == 1) result.add("0");
        String input = result.pop() + c;
        while (input.startsWith("0")) input = input.substring(1);
        if (input.isEmpty() || input.startsWith(".")) input = "0" + input;
        result.add(input);
    }
    
    public void inputPoint() {
        String input = result.pop();
        if (!input.contains(".")) input += '.';
        result.add(input);
    }
    
    public void clear() {
        result.clear();
        result.add("0");
        operand = CLEAR;
    }
    
    public void clearExtra() {
        extra = 0.0;
    }
    
    public String getResult() {
        return result.peek();
    }
    
    public String getExtra() {
        return extra + "";
    }
    
    public void setOperator(String op) {
        calculate();
        operand = op;
    }
    
    public void calculate() {
        if (result.size() <= 1 || operand.equals(CLEAR)) { operand = ""; return ;
        }
        double number1 = Double.parseDouble(standardized(result.pop()));
        double number2 = Double.parseDouble(standardized(result.pop()));
        switch (operand) {
            case "+":
                number2 += number1;
                break;
            case "-":
                number2 -= number1;
                break;
            case "x":
                number2 *= number1;
                break;
            case "/":
                number2 /= number1;
                break;
        }
        String res = number2 + "";
        if (res.endsWith(".0")) res = res.substring(0, res.length() - 2);
        result.add(res);
        operand = "";
    }
    
    public void calculate(String op) {
        double number = Double.parseDouble(standardized(result.pop()));
        switch (op) {
            case "1/x":
                number = 1 / number;
                break;
            case "%":
                number = number / 100;
                break;
            case "sqrt":
                number = Math.sqrt(number);
                break;
            case "+/-":
                number = -number;
                break;
            case "M+":
                extra += number;
                break;
            case "M-":
                extra -= number;
                break;
        }
        String res = number + "";
        if (res.endsWith(".0")) res = res.substring(0, res.length() - 2);
        result.add(res);
        if (operand.equals(CLEAR)) operand = "";
    }
    
}
