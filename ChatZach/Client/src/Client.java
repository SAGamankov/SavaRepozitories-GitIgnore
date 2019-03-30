import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Client{

   private final Scanner scn=new Scanner(System.in);
   private final String ip;
   private final int PORT;
    {
        System.out.println("Send Servers Ip");
        ip=scn.nextLine();
        System.out.println("Send servers port");
        PORT= scn.nextInt();
    }

    private BufferedWriter writer;
    private BufferedReader reader;
    private  Socket socket;
    private Thread thread;

    public Client() throws IOException {
       socket=new Socket(ip,PORT);
       writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
       reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
       thread=new Thread(()->{
           while (!thread.isInterrupted()){
               try {
                   checkMessage(Client.this.reader.readLine());
               } catch (IOException e) {
                   closeAll();
                   e.printStackTrace();
               }
           }
       });
       thread.start();
       while (!thread.isInterrupted()) senMsg(scn.nextLine());
    }
     void checkMessage(String mess){
        if(mess!=null & !mess.equals("")) System.out.println(mess);
      }

      void closeAll(){
          try {
              reader.close();
              writer.close();
              socket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
          thread.interrupt();
      }

      void senMsg(String msg){
          try {
              writer.write(msg);
              writer.newLine();
              writer.flush();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}