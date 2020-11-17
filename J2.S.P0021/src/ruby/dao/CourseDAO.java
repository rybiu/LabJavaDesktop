/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruby.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ruby.util.MyConnection;
import ruby.dto.Course;

/**
 *
 * @author Ruby
 */
public class CourseDAO {
    
    private Connection conn;
    private PreparedStatement preStm;
    private ResultSet rs;
   
    private void closeConnection() {  
        try {
            if (rs != null) rs.close();
            if (preStm != null) preStm.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public boolean addCourse(Course course) {
        try {
            String sql = "INSERT INTO courses VALUES (?, ?, ?)";
            conn = MyConnection.getConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, course.getCode());
            preStm.setString(2, course.getName());
            preStm.setInt(3, course.getCredit());
            if (preStm.executeUpdate() > 0) {
                return true;
            } 
            this.closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    public Course searchCourse(String code) {
        Course course = null;
        try {
            String sql = "SELECT name, credit FROM courses WHERE code = ?";
            conn = MyConnection.getConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, code);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                int credit = rs.getInt("credit");
                course = new Course(code, name, credit);
            }
            this.closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return course;
    }
    
    public List<Course> getAll() {
        List<Course> courses = new ArrayList<>();
        try {
            String sql = "SELECT code, name, credit FROM courses ORDER BY credit";
            conn = MyConnection.getConnection();
            preStm = conn.prepareCall(sql);
            rs = preStm.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                int credit = rs.getInt("credit");
                courses.add(new Course(code, name, credit));
            }
            this.closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return courses;
    }
    
}
