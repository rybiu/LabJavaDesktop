/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Ruby
 */
public class Validation {
    
    public static boolean isDate(String date) {
        boolean check = false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date.trim(), formatter);
            check = true;
        } catch (DateTimeParseException e) {
        }
        return check;
    }
    
}
