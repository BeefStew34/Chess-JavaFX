import java.net.*;
import javafx.scene.text.Text;
import javafx.application.Platform;
import java.io.BufferedInputStream;
import java.io.DataInputStream;

public class NetHost extends Thread{
	private Main parent;
	public NetHost(Main _parent){
		this.parent = _parent;
	}

	public void run(){
		int port = 4444;
		int connectionsCount = 0;
		try{
			Platform.runLater(() -> {
				parent.msgOverlay.setText("Waiting for Connection");
				parent.chessBoard.Reset();
				parent.chessBoard.movesEnabled = false;
			});
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();

			DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			byte b = in.readByte();
			if(b == 0x02)
				System.out.println("Sucessful Connection");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}		
}
