
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class StockDAO {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    private void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        } catch (SQLException ex) {
            Logger.getLogger(StockDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Stock> getAll() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT id, name, address, date_available, note FROM stocks";
        try {
            con = MyConnection.getConnection();
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                LocalDate date = null;
                if (rs.getDate("date_available") != null) 
                    date = rs.getDate("date_available").toLocalDate().plusDays(2);
                String note = rs.getString("note");
                stocks.add(new Stock(id, name, address, date, note));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(StockDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeConnection();
        }
        return stocks;
    }
    
    public boolean updateAll(List<Stock> stocks) {
        boolean check = false;
        String sql = "UPDATE stocks SET "
                + "name = ?, address = ?, date_available = ?, note = ? "
                + "WHERE id = ?";
        try {
            con = MyConnection.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareCall(sql);
            for (Stock stock : stocks) {
                stm.setString(1, stock.getName());
                stm.setString(2, stock.getAddress());
                if (stock.getDateAvailable() != null) {
                    stm.setDate(3, Date.valueOf(stock.getDateAvailable()));
                } else {
                    stm.setNull(3, Types.NULL);
                }
                stm.setString(4, stock.getNote());
                stm.setInt(5, stock.getId());
                stm.executeUpdate();
            }
            con.commit();
            check = true;
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(StockDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return check;
    }
}
