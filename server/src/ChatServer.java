import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import comunityConnection.TCPConnection;
import comunityConnection.TCPConnectionListener;

public class ChatServer implements TCPConnectionListener{

    public static void main(String[] args) throws Exception {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>(); 

    private ChatServer() {
        System.out.println("Server Runing...");
        try (ServerSocket serverSocket = new ServerSocket(8000)){
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: "+ e);
                }
            }
            
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client Connected: "+ tcpConnection);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client Disconnect: "+ tcpConnection);
    }

    @Override
    public synchronized void onExeption(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception:"+ e);
        
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
        
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) connections.get(i).sendString(value);
    }


}
