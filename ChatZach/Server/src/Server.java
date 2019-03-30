import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    static {
        Scanner sc=new Scanner(System.in);
        System.out.println("Server PORT :");
        PORT=sc.nextInt();
    }

    static final int PORT;
    static Scanner sc;

    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private Thread thread;
    private Thread messageListener;
    private volatile static boolean serverRun=true;

    private volatile static ArrayList<ClientIn>clients=new ArrayList<>();

    private Server() {   //Cервер занимается обработкой только последнего сообщения,причем сообщения
        // последнего клиента доходят до остальных клиентов,но от 1 клиента до клиента н-1 сообщения не приходят
       while (serverRun){
           try(ServerSocket serverSocket=new ServerSocket(PORT)) {
               socket=serverSocket.accept();
              messageListener = new Thread(()-> {
                       ClientIn clientIn=null;
                       try {
                           writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                           reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                           clientIn=new ClientIn(socket, writer, reader,messageListener);
                           addNewConnection(clientIn);
                           while (!Server.this.messageListener.isInterrupted()){
                               String msg=Server.this.reader.readLine();
                               System.out.println(msg);
                               checkMessage(Server.this.socket,msg);
                           }
                       } catch (IOException e) {
                           closeConnection(clientIn);
                           e.printStackTrace();
                       }
                   });
              messageListener.start();
           }catch(IOException e){}

       }

    }
   synchronized void checkMessage(Socket socket,String message){
        if (message!=null & !message.equals(""))sendToAllConnections(socket.getInetAddress()+
                 ":"+socket.getPort()+"<:>"+message);
    }

    synchronized void addNewConnection(ClientIn clientIn){
        clients.add(clientIn);
    }

    synchronized void closeConnection(ClientIn clientIn){
        try {
            clientIn.getWriter().close();
            clientIn.getReader().close();
            clientIn.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientIn.getThread().interrupt();
        clients.remove(clientIn);
    }

    void sendToAllConnections(String message){
        int len=clients.size();
        for (int i = 0; i < len ; i++) {
            try {
                clients.get(i).getWriter().write(message);
                clients.get(i).getWriter().newLine();
                clients.get(i).getWriter().flush(); } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}