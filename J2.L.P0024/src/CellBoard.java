
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class CellBoard extends JLabel {
    
    private final int row;
    private final int col;
    private String content = "";
    
    public CellBoard(int row, int col) {
        super();
        this.setFont(new java.awt.Font("Tahoma", 0, 36)); 
        this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.setText("");
        this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.setText(content);
        this.content = content;
    }
    
}
