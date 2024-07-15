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
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        Group game = new Group();
        Group overlay = new Group();
        Group ui = new Group();
        Button reset = new Button("Reset");
        TextField ip = new TextField("xxx.xxx.xxx.xxx:xxxx");
        Button connect = new Button("Connect");
        Button host = new Button("Host Server");
        Text msgOverlay = new Text("");

        ui.getChildren().add(reset);
        ui.getChildren().add(ip);
        ui.getChildren().add(connect);
        ui.getChildren().add(host);

        Board chessBoard = new Board(8,8,7, game, overlay);

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

        host.setOnAction(e -> {
            int port = 4444;
            int connectionsCount = 0;
            connect.setDisable(true);
            msgOverlay.setText("Waiting for Connection...");
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
            }
            catch(Exception except){
                connect.setDisable(false);
                System.out.println(except);
            }
        });
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}