/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruby.util;

import ruby.dao.CourseDAO;

/**
 *
 * @author Ruby
 */
public class Validation {
    
    public static boolean isEmpty(String str) {
        return str.isEmpty();
    }
    
    public static boolean isValidCode(String code) {
        return new CourseDAO().searchCourse(code) != null;
    }
    
    public static String isValidName(String name) {
        name = " " + name.trim();
        String result = "";
        for (int i = 1; i < name.length(); i++) {
            if (name.charAt(i) != ' ' && name.charAt(i - 1) == ' ') {
                result = result + Character.toUpperCase(name.charAt(i));
            } else if (name.charAt(i) != ' ') {
                result = result + Character.toLowerCase(name.charAt(i));
            } else if (name.charAt(i - 1) != ' '){
                result = result + " ";
            }
        }
        return result;
    }
    
    public static int isValidCredit(String str) throws Exception {
        int credit;
        try {
            credit = Integer.parseInt(str);
            if (credit < 1 || credit > 33) {
                throw new Exception("Credit must be a positive number and less than or equals to 33");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Credit must be a positive number");
        }
        return credit;
    }
    
}
