package hk.edu.polyu.comp.comp2021.assignment1.shape;

public class XYRectangle {
    private Point topLeft;
    private Point bottomRight;

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public XYRectangle(Point p1, Point p2){
        // Todo: complete the constructor
        int tlx = Math.min(p1.getX(), p2.getX());
        int tly = Math.max(p1.getY(), p2.getY());
        int brx = Math.max(p1.getX(), p2.getX());
        int bry = Math.min(p1.getY(), p2.getY());

        topLeft = new Point(tlx, tly);
        bottomRight = new Point(brx, bry);
    }

    public String toString(){
        // Todo: complete the method
        return "<" + getTopLeft() + "," + getBottomRight() + ">";
    }

    public int area(){
        // Todo: complete the method
        return (bottomRight.getX() - topLeft.getX()) * (topLeft.getY() - bottomRight.getY());
    }

    public void rotateClockwise(){
        // Todo: complete the method
        int tlx = topLeft.getX() - (topLeft.getY() - bottomRight.getY());
        int tly = topLeft.getY();
        int brx = topLeft.getX();
        int bry = topLeft.getY() - (bottomRight.getX() - topLeft.getX());
        topLeft.set(tlx, tly);
        bottomRight.set(brx, bry);
    }

    public void move(int deltaX, int deltaY){
        // Todo: complete the method
        topLeft.set(topLeft.getX() + deltaX, topLeft.getY() + deltaY);
        bottomRight.set(bottomRight.getX() + deltaX, bottomRight.getY() + deltaY);
    }

    public boolean contains(Point p){
        // Todo: complete the method
        return topLeft.getX() <= p.getX() && p.getX() <= bottomRight.getX()
                && topLeft.getY() >= p.getY() && p.getY() >= bottomRight.getY();
    }

    public boolean contains(XYRectangle r){
        // Todo: complete the method
        return contains(r.getTopLeft()) && contains(r.getBottomRight());
    }

    public boolean overlapsWith(XYRectangle r){
        // Todo: complete the method
        if(r.getBottomRight().getY() > getTopLeft().getY() || r.getTopLeft().getY() < getBottomRight().getY())
            return false;

        if(r.getBottomRight().getX() < getTopLeft().getX() || r.getTopLeft().getX() > getBottomRight().getX())
            return false;
        else
            return true;
    }
}

class Point{
    private int x;
    private int y;

    public Point(int x, int y) {
        set(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + getX() + "," + getY() + ")";
    }
}

