import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.*;
import java.net.InetAddress;
import javafx.scene.text.Text;

public class Main extends Application{
	public Text msgOverlay;
    public Board chessBoard;
    public volatile TextField ip;
	@Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        Group game = new Group();
        Group overlay = new Group();
        Group ui = new Group();
        Button reset = new Button("Reset");
        ip = new TextField("127.0.0.1");
        Button connect = new Button("Connect");
        Button host = new Button("Host Server");
        msgOverlay = new Text("");

        ui.getChildren().add(reset);
        ui.getChildren().add(ip);
        ui.getChildren().add(connect);
        ui.getChildren().add(host);

        this.chessBoard = new Board(8,8,7, game, overlay);

        reset.setOnAction(e -> chessBoard.Reset());
        root.setCenter(game);
        root.setLeft(ui);
        root.setTop(msgOverlay);
        
        Scene scene = new Scene(root,1000,1000);
        stage.setScene(scene);
        stage.show();

        msgOverlay.setTranslateX(400);
        msgOverlay.setTranslateY(30);
        int offset = 200;
        reset.setTranslateY(offset);
        offset += reset.getHeight();
        ip.setTranslateY(offset);
        offset += ip.getHeight();
        connect.setTranslateY(offset);
        offset += connect.getHeight();
        host.setTranslateY(offset);
	 

        NetHost nh = new NetHost(this);
        host.setOnAction(e -> {
            nh.start();
        });
        NetClient nc = new NetClient(this);
        connect.setOnAction(e -> {
            nc.start();
        });
    }
    public void UITimeout(boolean enabled){
        msgOverlay.setText(!enabled ? "Waiting for Connection" : "");
        chessBoard.Reset();
        chessBoard.movesEnabled = enabled;
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
