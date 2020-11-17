
import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
public class CaroGame extends javax.swing.JFrame {

    private final int POINT = 3;
    private final int EDGE = 50;
    private final int GAP = 5;
    private String turn;
    private int size;
    private Socket socket;
    private CellBoard[][] board;
    private int competitor;
    private int step = 0;
    
    /**
     * Creates new form CaroGame
     * @param socket
     * @param player1
     * @param player2
     * @param size
     * @param turn
     * @param competitor
     */
    public CaroGame(Socket socket, String player1, String player2, int size, String turn, int competitor) {
        initComponents();
        board = new CellBoard[size][size];
        this.turn = turn;
        this.size = size;
        this.socket = socket;
        this.competitor = competitor;
        txtChar.setText("Your Sign: " + turn);
        txtName.setText(player1 + " : " + player2);
        plBoard.setLayout(new java.awt.GridLayout(size, size, GAP, GAP));
        plBoard.setPreferredSize(new Dimension(size * EDGE + size * GAP - GAP, size * EDGE + size * GAP - GAP));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new CellBoard(i, j);
                board[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        cellBoardMouseCliked((CellBoard) evt.getSource());
                    }
                });
                plBoard.add(board[i][j]);
            }
        }
        int width = Math.max(plInfo.getPreferredSize().width, plBoard.getPreferredSize().width) + 50;
        int height = plInfo.getPreferredSize().height + plBoard.getPreferredSize().height + 75;
        this.setSize(width, height);   
    }
    
    public void setContent(int x, int y) {
        if (turn.endsWith("X")) 
            board[x][y].setContent("O");
        else 
            board[x][y].setContent("X");
        step++;
        this.changeTurn();
    }
    
    private void cellBoardMouseCliked(CellBoard cell) {
        if ((turn.equals("X") && step % 2 != 0 )|| (turn.equals("O") && step % 2 == 0)) {
            JOptionPane.showMessageDialog(rootPane, "Not your turn. Wait for the competitor!");
            return;
        }
        if (!cell.getContent().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "This cell has been chosen");
            return;
        }
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            cell.setContent(turn);
            os.writeBytes(Constant.SIGNAL_PLAY + Constant.REGEX);
            os.writeBytes(this.competitor + Constant.REGEX);
            os.writeBytes(cell.getRow() + Constant.REGEX + cell.getCol());
            os.write(13);
            os.write(10);
            os.flush();
            if (checkBoard(cell.getRow(), cell.getCol())) {
                this.endGame(true);
            } else if (++step == size * size) {
                this.endGame(false);
            }
            this.changeTurn();
        } catch (IOException ex) {
            this.dispose();
        }
    }
    
    private boolean checkBoard(int x, int y) {
        // main cross
        int count = 0;
        int t = 0;
        while (t < POINT && x - t >= 0 && y - t >= 0 && board[x - t][y - t].getContent().equals(turn)) {
            t++;
            count++;
        }
        t = 1;
        while (t < POINT && x + t < size && y + t < size && board[x + t][y + t].getContent().equals(turn)) {
            t++;
            count++;
        }    
        if (count == POINT) return true;
        // col
        count = 0;
        t = 0;
        while (t < POINT && x - t >= 0 && board[x - t][y].getContent().equals(turn)) {
            count++;
            t++;
        }
        t = 1;
        while (t < POINT && x + t < size && board[x + t][y].getContent().equals(turn)) {
            count++;
            t++;
        }
        if (count == POINT) return true;
        // row
        count = 0;
        t = 0;
        while (t < POINT && y - t >= 0 && board[x][y - t].getContent().equals(turn)) {
            count++;
            t++;
        }
        t = 1;
        while (t < POINT && y + t < size && board[x][y + t].getContent().equals(turn)) {
            count++;
            t++;
        }
        if (count == POINT) return true;
        // extra cross
        count = 0;
        t = 0;
        while (t < POINT && x - t >= 0 && y + t < size && board[x - t][y + t].getContent().equals(turn)) {
            count++;
            t++;
        }
        t = 1;
        while (t < POINT && x + t < size && y - t >= 0 && board[x + t][y - t].getContent().equals(turn)) {
            count++;
            t++;
        }
        if (count == POINT) return true;
        
        return false;
    }
    
    private void changeTurn() {
        if (step % 2 == 0) {
            txtTurn.setText("Turn: X");
        } else {
            txtTurn.setText("Turn: O");
        }
    }
    
    private void endGame(boolean flag) throws IOException {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {

        }
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        if (flag) os.writeBytes(Constant.SIGNAL_WIN + Constant.REGEX);
        else os.writeBytes(Constant.SIGNAL_TIE + Constant.REGEX);
        os.writeBytes(this.competitor + Constant.REGEX + size);
        os.write(13);
        os.write(10);
        os.flush();
        if (flag) JOptionPane.showMessageDialog(rootPane, "Congratulations, you won!");
        else JOptionPane.showMessageDialog(rootPane, "Tie!");
        this.dispose();         
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plInfo = new javax.swing.JPanel();
        txtName = new javax.swing.JLabel();
        txtChar = new javax.swing.JLabel();
        txtTurn = new javax.swing.JLabel();
        plBoard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Caro Game");
        setLocationByPlatform(true);
        getContentPane().setLayout(new java.awt.FlowLayout());

        plInfo.setLayout(new java.awt.GridLayout(1, 0, 30, 10));

        txtName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtName.setText("Ruby : Nguyen");
        plInfo.add(txtName);

        txtChar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtChar.setText("Your Sign: X");
        plInfo.add(txtChar);

        txtTurn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTurn.setText("Turn: X");
        plInfo.add(txtTurn);

        getContentPane().add(plInfo);

        plBoard.setLayout(new java.awt.GridLayout(2, 2, 5, 5));
        getContentPane().add(plBoard);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel plBoard;
    private javax.swing.JPanel plInfo;
    private javax.swing.JLabel txtChar;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtTurn;
    // End of variables declaration//GEN-END:variables
}
