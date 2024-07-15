import java.net.*;
import javafx.scene.text.Text;

public class NetHost extends Thread{
	private Main parent;
	public NetHost(Main _parent){
		this.parent = _parent;
	}

	public void run(){
		int port = 4444;
		int connectionsCount = 0;
		try{
			parent.msgOverlay.setText("Wating for Connection");
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
		}
		catch(Exception e){
		}
	}		
}
