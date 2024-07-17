import java.net.*;
import javafx.scene.text.Text;
import javafx.application.Platform;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;

public class NetHost extends Thread{
	private Main parent;
	public NetHost(Main _parent){
		this.parent = _parent;
	}
	public void NetMakeMove(int x, int y, int x1, int y1){
		byte[] output = {(byte)x,(byte)y,(byte)x1,(byte)y1};
		parent.chessBoard.movesEnabled = false;
		try{
		    op.write(output);
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	private OutputStream op;
	public void run(){
		int port = 4444;
		int connectionsCount = 0;
		try{
			Platform.runLater(() -> parent.UITimeout(false));
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();

			DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			op = clientSocket.getOutputStream();

			byte b = in.readByte();
			if(b == 0x02)
				Platform.runLater(() -> parent.UITimeout(true));
			parent.chessBoard.nh = this;
			while(true){
                byte[] buffer = new byte[4];
                if(in.read(buffer,0,4) != 4)
                    continue;
                Platform.runLater(() -> parent.chessBoard.NetMovePiece(buffer));
            }
		}
		catch(Exception e){
			System.out.println(e);
		}
	}		
}
