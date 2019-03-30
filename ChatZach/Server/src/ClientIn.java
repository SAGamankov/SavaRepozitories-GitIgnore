import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class ClientIn {

    public ClientIn(Socket socket, BufferedWriter writer, BufferedReader reader,Thread thread){
        this.socket=socket;
        this.reader=reader;
        this.writer=writer;
        this.thread=thread;
    }

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Thread thread;

    @Override
    public String toString() {
        return this.socket.getInetAddress()+":"+this.socket.getPort();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Thread getThread() {
        return thread;
    }
}
