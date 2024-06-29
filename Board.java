import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Board{
    private Rectangle[][] _sections;
    private Piece[][] _pieces;
    private int _sizeX;
    private int _sizeY;
    private Group _parent;
    private Group _overlay;
    public Point size;
    public int scale;
    private boolean isWhiteTurn = true;
    public ArrayList<Point> points;
    private Piece _selected;

    public Group getParent(){
        return _parent;
    }

    public Board(int sizeX, int sizeY, int scale, Group parent, Group overlay){
        _sizeX = sizeX;
        _sizeY = sizeY;
        _overlay = overlay;
        size = new Point(_sizeX, _sizeY);
        _parent = parent;
        _sections = new Rectangle[sizeX][sizeY];
        this.scale = scale*10;
        _pieces = initializePieces(scale*10);
        
        /*for(int j = 0; j < 2; j++)
            for(int i = 0; i < 8; i++)
                _parent.getChildren().add(_pieces[i][j]);
        for(int j = 6; j < 8; j++)
            for(int i = 0; i < 8; i++)
                _parent.getChildren().add(_pieces[i][j]);*/
        
        for(int x = 0; x < sizeX; x++){
            for(int y = 0; y < sizeY; y++){
                final int finalX = x;
                final int finalY = y;
                _sections[x][y] = new Rectangle();
                _sections[x][y].setWidth(scale*10);
                _sections[x][y].setHeight(scale*10);
                _sections[x][y].setX(x*(scale*10));
                _sections[x][y].setY(y*(scale*10));

                _sections[x][y].setOnMouseClicked(e -> SquareClickProxy(finalX, finalY));
                if((x+y) % 2 == 0)
                    _sections[x][y].setFill(Color.BLACK);
                else
                    _sections[x][y].setFill(Color.CORNSILK);
            }
        }
        
    }
    private void SquareClickProxy(int x, int y){
        if(_pieces[x][y] == null){
            for(int i = 0; i < points.size(); i++){
                if(points.get(i).x == x && points.get(i).y == y && _selected != null)
                    MovePiece(points.get(i), _selected);
            }
        }
        else
            _pieces[x][y].Click();
    }
    private Piece[][] initializePieces(int scale){
        Piece[][] output = new Piece[_sizeX][_sizeY];

        for(int i = 0; i < 8; i++){
            Piece pawn = new Piece("P", false, new Point(i, 1), this);
            output[i][1] = pawn;
        }
        for(int i = 0; i < 8; i++){
            Piece pawn = new Piece("P", true, new Point(i, 6), this);
            output[i][6] = pawn;
        }
        //Rooks 
        output[0][0] = new Piece("R", false, new Point(0, 0), this);
        output[7][0] = new Piece("R", false, new Point(7, 0), this);
        output[0][7] = new Piece("R", true, new Point(0, 7), this);
        output[7][7] = new Piece("R", true, new Point(7, 7), this);
        //Knight
        output[1][0] = new Piece("K", false, new Point(1, 0), this);
        output[6][0] = new Piece("K", false, new Point(6, 0), this);
        output[1][7] = new Piece("K", true, new Point(1, 7), this);
        output[6][7] = new Piece("K", true, new Point(6, 7), this);
        //Bishop
        output[2][0] = new Piece("B", false, new Point(2, 0), this);
        output[5][0] = new Piece("B", false, new Point(5, 0), this);
        output[2][7] = new Piece("B", true, new Point(2, 7), this);
        output[5][7] = new Piece("B", true, new Point(5, 7), this);
        //King Queen
        output[3][0] = new Piece("Q", false, new Point(3, 0), this);
        output[4][0] = new Piece("O", false, new Point(4, 0), this);
        output[3][7] = new Piece("Q", true, new Point(3, 7), this);
        output[4][7] = new Piece("O", true, new Point(4, 7), this);

        return output;
    }
    public void render(Group ui){
        for(int x = 0; x < _sizeX; x++)
            for(int y = 0; y < _sizeY; y++)
                ui.getChildren().add(0,_sections[x][y]);
    }
    public void PieceClick(Piece subject){
        points = MoveMaker.moves(size, _pieces, subject);
        _selected = subject;

        if(subject.isWhite != isWhiteTurn)
            return;
        
        _overlay.getChildren().clear();
        if(points == null)
            return;
        for(int i = 0; i < points.size(); i++){
            Circle option = new Circle();
            //System.out.println(points.get(i).x*scale + " " + points.get(i).y*scale);
            option.setCenterX((points.get(i).x*scale)+(0.5*scale));
            option.setCenterY((points.get(i).y*scale)+(0.5*scale));
            option.setRadius(10);
            option.setFill(Color.GREEN);
            Point p = points.get(i);
            option.setOnMouseClicked(e -> MovePiece(p, subject));
            _overlay.getChildren().add(option);
        }
    }
    private void MovePiece(Point selected, Piece subject){
        if(_pieces[selected.x][selected.y] != null)
            _parent.getChildren().remove(_pieces[selected.x][selected.y]);
        isWhiteTurn = !isWhiteTurn;
        _pieces[selected.x][selected.y] = subject;
        _pieces[subject.position.x][subject.position.y] = null;
        subject.position = selected;
        subject.fresh = false;
        subject.setX((subject.position.x*scale)+(0.25*scale));
        subject.setY(((subject.position.y+1)*scale)-(0.25*scale));
        _overlay.getChildren().clear();
    }
}