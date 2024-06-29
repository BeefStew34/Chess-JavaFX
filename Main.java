import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Group;

public class Main extends Application{
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        Group game = new Group();
        Group overlay = new Group();

        Board chessBoard = new Board(8,8,7, game, overlay);

        chessBoard.render(game);
        root.setCenter(game);
        game.getChildren().add(overlay);
        
        Scene scene = new Scene(root,1000,1000);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}