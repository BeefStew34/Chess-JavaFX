import java.net.*;
import javafx.scene.text.Text;
import javafx.application.Platform;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;

public class NetClient extends Thread{
    private Main parent;
	public NetClient(Main _parent){
		this.parent = _parent;
	}
    public OutputStream op;
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
	public void run(){
        int port = 4444;
        try{
            Platform.runLater(() -> parent.UITimeout(false));

            String ip = parent.ip.getText();
            Socket socket = new Socket(ip,port);
            op = socket.getOutputStream();
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            op.write(0x02);
            Platform.runLater(() -> parent.UITimeout(true));
            Platform.runLater(() -> {parent.chessBoard.movesEnabled = false;});
            System.out.println(ip);

            parent.chessBoard.nc = this;
            while(true){
                byte[] buffer = new byte[4];
                if(in.read(buffer,0,4) != 4)
                    continue;
                Platform.runLater(() -> parent.chessBoard.NetMovePiece(buffer));
            }
        }
        catch(Exception e){
            Platform.runLater(() -> parent.UITimeout(true));
            System.out.println(e);
        }
    }
}