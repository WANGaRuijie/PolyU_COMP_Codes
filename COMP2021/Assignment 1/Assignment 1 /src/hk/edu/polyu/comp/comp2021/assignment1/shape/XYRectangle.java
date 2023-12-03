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
        topLeft = new Point(min(p1.getX(), p2.getX()), max(p1.getY(), p2.getY()));
        bottomRight = new Point(max(p1.getX(), p2.getX()), min(p1.getY(), p2.getY()));
    }

    public String toString(){
        return "<" + topLeft.toString() + "," + bottomRight.toString() + ">";
    }

    public int area(){
        int width = bottomRight.getX() - topLeft.getX();
        int height = topLeft.getY() - bottomRight.getY();
        return width * height;
    }

    public void rotateClockwise(){

        int width = bottomRight.getX() - topLeft.getX();
        int height = topLeft.getY() - bottomRight.getY();


        int topLeftXNew = topLeft.getX() - height;
        int topLeftYNew = topLeft.getY();

        int bottomRightXNew = topLeft.getX();
        int bottomRightYNew = topLeft.getY() - width;

        topLeft = new Point(topLeftXNew, topLeftYNew);
        bottomRight = new Point(bottomRightXNew, bottomRightYNew);

    }

    public void move(int deltaX, int deltaY){

        int topLeftXNew = topLeft.getX() + deltaX;
        int topLeftYNew = topLeft.getY() + deltaY;

        int bottomRightXNew = bottomRight.getX() + deltaX;
        int bottomRightYNew = bottomRight.getY() + deltaY;

        topLeft = new Point(topLeftXNew, topLeftYNew);
        bottomRight = new Point(bottomRightXNew, bottomRightYNew);

    }

    public boolean contains(Point p){
        return p.getX() >= topLeft.getX() && p.getX() <= bottomRight.getX() && p.getY() <= topLeft.getY() && p.getY() >= bottomRight.getY();
    }

    public boolean contains(XYRectangle r) {
        return contains(r.topLeft) && contains(r.bottomRight);
    }
    public boolean overlapsWith(XYRectangle r){
        return r.contains(this.topLeft) || r.contains(this.bottomRight) || contains(r.topLeft) || contains(r.bottomRight);
    }

    public int min(int a, int b) {
        return a > b ? b : a;
    }

    public int max(int a, int b) {
        return a > b ? a : b;
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

