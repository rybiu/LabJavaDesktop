/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class Calculator {
    
    private double result;
    private String operator;
    private String input;
    
    public Calculator() {
        this.operator = "";
        this.input = "";
    }

    public double getResult() {
        return result;
    }

    public String getInput() {
        return input;
    }
    
    public void setOperator(String o) {
        if (this.operator.isEmpty() && !this.input.isEmpty()) {
            this.result = Double.parseDouble(this.input);
        }
        if (!this.input.isEmpty()) this.calculate();
        this.operator = o;
        this.input = "";
    }
    
    public void clear() {
        this.result = 0;
        this.operator = "";
        this.input = "";
    }
    
    public void inputNumber(char c) throws Exception {
        this.input = Long.parseLong(input + c) + "";
    }
    
    public void calculate() {
        if (this.operator.equals("+")) {
            this.result += Double.parseDouble(this.input);
        } else if (this.operator.equals("-")) {
            this.result -= Double.parseDouble(this.input);
        } else if (this.operator.equals("x")) {
            this.result *= Double.parseDouble(this.input);
        } else if (this.operator.equals("/")) {
            this.result /= Double.parseDouble(this.input);
        } 
        this.input = "";
        this.operator = "";
    }
    
}
