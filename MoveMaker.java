import java.util.ArrayList;
enum CheckType{
    NONE,
    BLACKCHECK,
    WHITECHECK,
    BLACKMATE,
    WHITEMATE;
}

public final class MoveMaker{
    private MoveMaker(){

    }
    public static ArrayList<Point> moves(Point size, Piece[][] board, Piece subject){
        char type = subject.getText().charAt(0);
        //System.out.println("GetMoves");
        if(subject._parent.check){
            System.out.println("Moves Limited By Check");
        }

        switch(type){
            case 'R':
                return rookMoves(size, board, subject);
            case 'B':
                return bishopMoves(size, board, subject);
            case 'K':
                return knightMoves(size, board, subject);
            case 'P':
                return pawnMoves(size, board, subject);
            case 'O':
                return kingMoves(size, board, subject);
            case 'Q':
                return queenMoves(size, board, subject);
            default:
                return pawnMoves(size, board, subject);
        }
        //return null;
    }   
    private static ArrayList<Point> rookMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = new ArrayList<Point>();
        ArrayList<Point> output2 = new ArrayList<Point>();
        Point pos = subject.position;

        for(int x = 0; x < size.x; x++){
            if(board[x][pos.y] == null){
                output.add(new Point(x, pos.y));
                continue;
            }
            if(board[x][pos.y] == subject)
                continue;
            if(board[x][pos.y].isWhite == subject.isWhite && x < pos.x){
                output.clear();
                continue;
            }
            if(board[x][pos.y].isWhite == subject.isWhite && x != pos.x)
                break;

            if(board[x][pos.y].isWhite == !subject.isWhite && x < pos.x)
                output.clear();

            output.add(new Point(x, pos.y));

            if(board[x][pos.y].isWhite == !subject.isWhite && x > pos.x)
                break;
        }
        for(int y = 0; y < size.y; y++){
            if(board[pos.x][y] == null){
                output2.add(new Point(pos.x, y));
                continue;
            }
            if(board[pos.x][y] == subject)
                continue;
            if(board[pos.x][y].isWhite == subject.isWhite && y < pos.y){
                output2.clear();
                continue;
            }
            if(board[pos.x][y].isWhite == subject.isWhite && y != pos.y)
                break;

            if(board[pos.x][y].isWhite == !subject.isWhite && y < pos.y)
                output2.clear();

            output2.add(new Point(pos.x, y));

            if(board[pos.x][y].isWhite == !subject.isWhite && y > pos.y)
                break;
        }
        output.addAll(output2);
        return output;
    }
    private static ArrayList<Point> bishopMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = new ArrayList<Point>();
        Point pos = subject.position;

        for(int i = 1; pos.x+i < size.x && pos.y+i < size.y-1; i++){
            if(board[pos.x+i][pos.y+i] == null){
                output.add(new Point(pos.x+i, pos.y+i));
                continue;
            }
            if(board[pos.x+i][pos.y+i].isWhite == subject.isWhite)
                break;
            if(board[pos.x+i][pos.y+i].isWhite != subject.isWhite){
                output.add(new Point(pos.x+i, pos.y+i));
                break;
            }
        }
        for(int i = 1; pos.x-i > -1 && pos.y-i > -1; i++){
            if(board[pos.x-i][pos.y-i] == null){
                output.add(new Point(pos.x-i, pos.y-i));
                continue;
            }
            if(board[pos.x-i][pos.y-i].isWhite == subject.isWhite)
                break;
            if(board[pos.x-i][pos.y-i].isWhite != subject.isWhite){
                output.add(new Point(pos.x-i, pos.y-i));
                break;
            }
        }
        for(int i = 1; pos.x+i < size.x && pos.y-i > -1; i++){
            if(board[pos.x+i][pos.y-i] == null){
                output.add(new Point(pos.x+i, pos.y-i));
                continue;
            }
            if(board[pos.x+i][pos.y-i].isWhite == subject.isWhite)
                break;
            if(board[pos.x+i][pos.y-i].isWhite != subject.isWhite){
                output.add(new Point(pos.x+i, pos.y-i));
                break;
            }
        }
        for(int i = 1; pos.x-i > -1 && pos.y+i < size.y; i++){
            if(board[pos.x-i][pos.y+i] == null){
                output.add(new Point(pos.x-i, pos.y+i));
                continue;
            }
            if(board[pos.x-i][pos.y+i].isWhite == subject.isWhite)
                break;
            if(board[pos.x-i][pos.y+i].isWhite != subject.isWhite){
                output.add(new Point(pos.x-i, pos.y+i));
                break;
            }
        }
        return output;
    }
    private static boolean pointCheck(Point size, Point subject){
        return (subject.x >= 0 && subject.y >= 0) && (subject.x < size.x && subject.y < size.y);
    }
    private static ArrayList<Point> knightMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = new ArrayList<Point>();
        Point pos = subject.position;
        Point[] offsetCheck = new Point[]{
            new Point(pos.x+2, pos.y+1),
            new Point(pos.x-2, pos.y-1),
            new Point(pos.x-2, pos.y+1),
            new Point(pos.x+2, pos.y-1),
            new Point(pos.x+1, pos.y+2),
            new Point(pos.x+1, pos.y-2),
            new Point(pos.x-1, pos.y+2),
            new Point(pos.x-1, pos.y-2)
        };
        for(int i = 0; i < 8; i++){
            if(!pointCheck(size, offsetCheck[i]))
                continue;
            if(board[offsetCheck[i].x][offsetCheck[i].y] == null || board[offsetCheck[i].x][offsetCheck[i].y].isWhite != subject.isWhite)
                output.add(offsetCheck[i]);       
        }

        return output;
    }
    private static ArrayList<Point> pawnMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = new ArrayList<Point>();
        Point pos = subject.position;
        int direction = subject.isWhite ? -1 : 1;

        if(board[pos.x][pos.y+direction] == null)
            output.add(new Point(pos.x,pos.y+direction));
        if(board[pos.x][pos.y+(2*direction)] == null && subject.fresh)
            output.add(new Point(pos.x,pos.y+(2*direction)));

        if(pointCheck(size, new Point(pos.x-1, pos.y+direction)) && board[pos.x-1][pos.y+direction] != null && board[pos.x-1][pos.y+direction].isWhite != subject.isWhite)
            output.add(new Point(pos.x-1, pos.y+direction));
        if(pointCheck(size, new Point(pos.x+1, pos.y+direction)) && board[pos.x+1][pos.y+direction] != null && board[pos.x+1][pos.y+direction].isWhite != subject.isWhite)
            output.add(new Point(pos.x+1, pos.y+direction));
        return output;
    }
    private static ArrayList<Point> kingMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = new ArrayList<Point>();
        Point pos = subject.position;
        Point[] offsetCheck = new Point[]{
            new Point(pos.x-1, pos.y),
            new Point(pos.x-1, pos.y-1),
            new Point(pos.x, pos.y-1),
            new Point(pos.x-1, pos.y+1),
            new Point(pos.x+1, pos.y+1),
            new Point(pos.x+1, pos.y),
            new Point(pos.x+1, pos.y-1),
            new Point(pos.x, pos.y+1)
        };

        for(int i = 0; i < 8; i++){
            if(!pointCheck(size, offsetCheck[i]))
                continue;
            if(board[offsetCheck[i].x][offsetCheck[i].y] != null && board[offsetCheck[i].x][offsetCheck[i].y].isWhite == subject.isWhite)
                continue;
            output.add(offsetCheck[i]);
        }
        return output;
    }
    private static ArrayList<Point> queenMoves(Point size, Piece[][] board, Piece subject){
        ArrayList<Point> output = rookMoves(size, board, subject);
        output.addAll(bishopMoves(size,board,subject));
        output.addAll(kingMoves(size,board,subject));

        return output;
    }

    public static CheckType isCheck(Point size, Piece[][] board){
        Point bKingPos = null;
        Point wKingPos = null;
        for(int x = 0; x < size.x; x++){
            for(int y = 0; y < size.y; y++){
                if(board[x][y] == null || board[x][y].getText().charAt(0) != 'O')
                    continue;
                if(board[x][y].isWhite)
                    wKingPos = new Point(x,y);
                else
                    bKingPos = new Point(x,y);
                if(bKingPos != null && wKingPos != null)
                    break; 
            }
        }

        for(int x = 0; x < size.x; x++){
            for(int y = 0; y < size.y; y++){
                if(board[x][y] == null)
                    continue;
                ArrayList<Point> mvs = moves(size, board, board[x][y]);

                for(int i = 0; i < mvs.size(); i++){
                    if(mvs.get(i).x == bKingPos.x && mvs.get(i).y == bKingPos.y)
                        return CheckType.BLACKCHECK;
                    if(mvs.get(i).x == wKingPos.x && mvs.get(i).y == wKingPos.y)
                        return CheckType.WHITECHECK;
                }
            }
        }
        return CheckType.NONE;
    }
}