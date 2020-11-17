
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JList;
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
public class ListenThread extends Thread {

    private String namePlayer;
    private final Socket socket;
    private final JList<String> list;
    private CaroGame game;
    private String turn = "O";

    public ListenThread(Socket socket, JList<String> list) {
        this.socket = socket;
        this.list = list;
    }
    
    private void updateListName(String mes) {
        String[] names = mes.split(Constant.REGEX);
        int position = Integer.parseInt(names[names.length - 1]);
        this.namePlayer = names[position + 1];
        names[position + 1] += " (me)";
        DefaultListModel<String> model = new DefaultListModel();
        for (int i = 1; i < names.length - 1; i++) {
            model.addElement(names[i]);
        }
        this.list.setModel(model);
    }
    
    private void joinRoom(String mes) throws IOException {
        // INVITE sender size
        String[] items = mes.split(Constant.REGEX);
        int sender = Integer.parseInt(items[1]);
        int size = Integer.parseInt(items[2]);
        String name = this.list.getModel().getElementAt(sender);
        int choice = JOptionPane.showConfirmDialog(list, "Do you want to play Caro (" + size + "x"+ size + ") with " + name + "?", "Invite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        if (choice == JOptionPane.OK_OPTION) {
            os.writeBytes(Constant.SIGNAL_ACCEPT + Constant.REGEX + sender + Constant.REGEX + size);
        } else {
            os.writeBytes(Constant.SIGNAL_REJECT + Constant.REGEX + sender);
        }
        os.write(13);
        os.write(10);
        os.flush();
    }
    
    private void acceptReq(String mes) throws IOException {
        // ACCEPT sender size
        String[] items = mes.split(Constant.REGEX);
        int sender = Integer.parseInt(items[1]);
        int size = Integer.parseInt(items[2]);
        turn = turn.equals("X") ? "O" : "X";
        game = new CaroGame(socket, namePlayer, list.getModel().getElementAt(sender), size, turn, sender);
        game.setVisible(true);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        String temp = turn.equals("X") ? "O" : "X";
        os.writeBytes(Constant.SIGNAL_CREATE + Constant.REGEX + sender + Constant.REGEX + size + Constant.REGEX + temp);
        os.write(13);
        os.write(10);
        os.flush();
    }
    
    private void makeRoom(String mes) throws IOException {
        // CREATE sender size [X, O]
        String[] items = mes.split(Constant.REGEX);
        int sender = Integer.parseInt(items[1]);
        int size = Integer.parseInt(items[2]);
        game = new CaroGame(socket, namePlayer, list.getModel().getElementAt(sender), size, items[3], sender);
        game.setVisible(true);
        turn = items[3];
    }
    
    private void clickCell(String mes) throws IOException {
        // PLAY sender i j
        String[] items = mes.split(Constant.REGEX);
        if (game.isVisible()) {
            game.setContent(Integer.parseInt(items[2]), Integer.parseInt(items[3]));
        } else {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeBytes(Constant.SIGNAL_EXIT + Constant.REGEX);
            os.writeBytes(items[1]);
            os.write(13);
            os.write(10);
            os.flush(); 
        }
    }
    
    private void playAgain(String mes) throws IOException {
        // [WIN, TIE] sender size
        int choice;
        if (mes.startsWith(Constant.SIGNAL_WIN))
            choice = JOptionPane.showConfirmDialog(game, "You losed. Do you want to play again?", "Play again", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        else 
            choice = JOptionPane.showConfirmDialog(game, "Tie. Do you want to play again?", "Play again", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        game.dispose();
        if (choice == JOptionPane.OK_OPTION) {
            String[] items = mes.split(Constant.REGEX);
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeBytes(Constant.SIGNAL_INVITE + Constant.REGEX);
            os.writeBytes(items[1] + Constant.REGEX + items[2]);
            os.write(13);
            os.write(10);
            os.flush();
        }
    }
    
    private void exitGame() {
        // EXIT sender
        JOptionPane.showMessageDialog(game, "Your competitor has been exited game!", "Message", JOptionPane.WARNING_MESSAGE);
        game.dispose();
    }
    
    @Override
    public void run() {
        while (socket != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mes = br.readLine();
                System.out.println("::client " + mes);
                if (mes.startsWith(Constant.SIGNAL_LIST)) this.updateListName(mes);
                else if (mes.startsWith(Constant.SIGNAL_INVITE)) this.joinRoom(mes);
                else if (mes.startsWith(Constant.SIGNAL_ACCEPT)) this.acceptReq(mes);
                else if (mes.startsWith(Constant.SIGNAL_CREATE)) this.makeRoom(mes);
                else if (mes.startsWith(Constant.SIGNAL_PLAY)) this.clickCell(mes);
                else if (mes.startsWith(Constant.SIGNAL_WIN)) this.playAgain(mes);
                else if (mes.startsWith(Constant.SIGNAL_TIE)) this.playAgain(mes);
                else if (mes.startsWith(Constant.SIGNAL_EXIT)) this.exitGame();
            } catch (IOException ex) {
                System.exit(0);
            }
        }
    }
    
}
