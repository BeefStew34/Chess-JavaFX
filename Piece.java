import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

public class Piece extends Text{
    public boolean isWhite;
    public Point position;
    public boolean fresh = true;
    private int scale;
    public Board _parent;

    public Piece(String letter, Boolean isWhite, Point position, Board parent){
        this.setText(letter);
        Color pk = isWhite ? Color.WHITE : Color.BLACK;
        Color kp = !isWhite ? Color.WHITE : Color.BLACK;
        
        this.scale = parent.scale;

        this.setX((position.x*scale)+(0.25*scale));
        this.setY(((position.y+1)*scale)-(0.25*scale));
        this.setOnMouseClicked(e -> Click());

        //this.setStrokeWidth(1);
        //this.setStroke(kp);
        this.setFill(pk);

        this.isWhite = isWhite;
        this.position = position;
        
        this._parent = parent;
        
        this.setFont(new Font(50));
        _parent.getParent().getChildren().add(this);
        
    }
    public void Click(){
        _parent.PieceClick(this);
    }
}
