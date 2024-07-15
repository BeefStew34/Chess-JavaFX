import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.control.Button;

public class Main extends Application{
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        Group game = new Group();
        Group overlay = new Group();
        Button reset = new Button("Reset");

        Board chessBoard = new Board(8,8,7, game, overlay);

        reset.setOnAction(e -> chessBoard.Reset());
        root.setCenter(game);
        root.setLeft(reset);
        
        Scene scene = new Scene(root,1000,1000);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}