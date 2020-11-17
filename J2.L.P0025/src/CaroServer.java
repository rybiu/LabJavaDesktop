
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class CaroServer extends Thread {
     
    private static List<String> names;
    private static List<Socket> sockets;
    private Socket client;
    
    public CaroServer(Socket client) {
        this.client = client;
    }
    
    public static void initServer(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Init server success");
            names = new ArrayList<>();
            sockets = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Socket client = server.accept();
                            if (client != null) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                names.add(br.readLine());
                                sockets.add(client);
                                CaroServer.updateListName();
                                new CaroServer(client).start();
                            }
                        } catch (IOException ex) {
    
                        }
                    }
                }
            }).start();
        } catch (IOException ex) {
            
        }
    }
    
    private static void updateListName() {
        String strName = "";
        for (String name : names) {
            strName = strName + name + Constant.REGEX;
        }
        try {
            DataOutputStream os;
            for (int i = 0; i < names.size(); i++) {
                os = new DataOutputStream(sockets.get(i).getOutputStream());
                os.writeBytes(Constant.SIGNAL_LIST + Constant.REGEX);
                os.writeBytes(strName + i);
                os.write(13);
                os.write(10);
                os.flush();
            }
        } catch (IOException ex) {
            
        }
    }
    
    private void forward(String mes) throws IOException {
        String[] item = mes.split(Constant.REGEX);
        int receiver = Integer.parseInt(item[1]);
        DataOutputStream os = new DataOutputStream(sockets.get(receiver).getOutputStream());
        os.writeBytes(item[0] + Constant.REGEX);
        os.writeBytes(sockets.indexOf(client) + "");
        if (item.length > 2) os.writeBytes(Constant.REGEX + item[2]);
        if (item.length > 3) os.writeBytes(Constant.REGEX + item[3]);
        os.write(13);
        os.write(10);
        os.flush();
    }

    @Override
    public void run() {
        while (client != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String mes = br.readLine();
                System.out.println("::SERVER " + mes);
                if (!mes.isEmpty()) this.forward(mes);
            } catch (IOException ex) {
                int index = sockets.indexOf(client);
                names.remove(index);
                sockets.remove(index);
                CaroServer.updateListName();
                break;
            }
        }
    }
 
}
