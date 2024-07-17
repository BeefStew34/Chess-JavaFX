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
    public boolean check;
    public boolean movesEnabled = true;

    public Group getParent(){
        return _parent;
    }
    public void Reset(){
        _overlay.getChildren().clear();
        _parent.getChildren().clear();
        _parent.getChildren().add(_overlay);
        size = new Point(_sizeX, _sizeY);
        _sections = new Rectangle[size.x][size.y];
        _selected = null;
        isWhiteTurn = true;
        check = false;
        points = null;
        
        for(int x = 0; x < size.x; x++){
            for(int y = 0; y < size.y; y++){
                final int finalX = x;
                final int finalY = y;
                _sections[x][y] = new Rectangle();
                _sections[x][y].setWidth(scale);
                _sections[x][y].setHeight(scale);
                _sections[x][y].setX(x*(scale));
                _sections[x][y].setY(y*(scale));

                _sections[x][y].setOnMouseClicked(e -> SquareClickProxy(finalX, finalY));
                _sections[x][y].setFill((x+y) % 2 == 0 ? Color.rgb(100,96,91) : Color.rgb(192,184,160));
                //_parent.getChildren().add(_sections[x][y]);
            }
        }
        render(_parent);
        _pieces = initializePieces(scale*10);
        _overlay.toFront();
    }
    public Board(int sizeX, int sizeY, int scale, Group parent, Group overlay){
        _sizeX = sizeX;
        _sizeY = sizeY;
        _overlay = overlay;
        this.scale = scale*10;
        _parent = parent;
        Reset();
    }
    private void SquareClickProxy(int x, int y){
        if(!movesEnabled || points == null)
            return;

        if(_pieces[x][y] == null || (_pieces[x][y] != null && _pieces[x][y].isWhite != _selected.isWhite)){
            for(int i = 0; i < points.size(); i++){
                if(points.get(i).x == x && points.get(i).y == y && _selected != null){
                    MovePiece(points.get(i), _selected);
                    return;
                }
            }
            return;
        }
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
        if(!movesEnabled)
            return;
        points = MoveMaker.moves(size, _pieces, subject);
        _selected = subject;

        if(subject.isWhite != isWhiteTurn)
            return;
        _overlay.getChildren().clear();
        if(points == null)
            return;
        for(int i = 0; i < points.size(); i++){
            _pieces[subject.position.x][subject.position.y] = null;
            Piece swap = _pieces[points.get(i).x][points.get(i).y];
            _pieces[points.get(i).x][points.get(i).y] = subject;
            CheckType ct = MoveMaker.isCheck(size, _pieces);
            _pieces[points.get(i).x][points.get(i).y] = swap;
            _pieces[subject.position.x][subject.position.y] = subject;

            if((ct == CheckType.WHITECHECK || ct == CheckType.WHITEMATE) && subject.isWhite)
                continue;
            if((ct == CheckType.BLACKCHECK || ct == CheckType.BLACKMATE) && !subject.isWhite)
                continue;

            Circle option = new Circle();
            option.setCenterX((points.get(i).x*scale)+(0.5*scale));
            option.setCenterY((points.get(i).y*scale)+(0.5*scale));
            option.setRadius(10);
            option.setFill(Color.GREEN);
            Point p = points.get(i);
            option.setOnMouseClicked(e -> MovePiece(p, subject));
            _overlay.getChildren().add(option);
        }
    }
    public void NetMovePiece(byte[] buffer){
        this.movesEnabled = true;
        System.out.println("Move Peice x:" + buffer[0] + ", y:" + buffer[1] + " To x:" + buffer[2] + ", y:" + buffer[3]);
        GenericMovePiece(new Point( buffer[2], buffer[3]), _pieces[buffer[0]][buffer[1]]);
    }
    public NetClient nc;
    public NetHost nh;
    private void GenericMovePiece(Point selected, Piece subject){
        if(_pieces[selected.x][selected.y] != null)
            _parent.getChildren().remove(_pieces[selected.x][selected.y]);
        isWhiteTurn = !isWhiteTurn;
        _pieces[selected.x][selected.y] = subject;
        _pieces[subject.position.x][subject.position.y] = null;
        subject.position = selected;
        subject.fresh = false;
        subject.setX((subject.position.x*scale)+(0.25*scale));
        subject.setY(((subject.position.y+1)*scale)-(0.25*scale));
    }
    private void MovePiece(Point selected, Piece subject){
        if(!movesEnabled)
            return;
        if(nc != null)
            nc.NetMakeMove(subject.position.x, subject.position.y, selected.x, selected.y);
        if(nh != null)
            nh.NetMakeMove(subject.position.x, subject.position.y, selected.x, selected.y);

        GenericMovePiece(selected,subject);
        _overlay.getChildren().clear();
    }
}
