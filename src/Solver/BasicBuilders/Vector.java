package Solver.BasicBuilders;


public class Vector {
    public MyPoint start, end;

    public Vector(double x, double y, double z) {
        this.start = new MyPoint(0,0,0);
        this.end = new MyPoint(x,y,z);
    }

    public Vector(MyPoint p1, MyPoint p2) {
        start = new MyPoint(p1.x,p1.y,p1.z);
        end = new MyPoint(p2.x,p2.y,p2.z);
    }

    public double getXComponent(){
        return this.end.x - this.start.x;
    }

    public double getYComponent(){
        return this.end.y - this.start.y;
    }

    public double getZComponent(){
        return this.end.z - this.start.z;
    }

    public static double magnitude(Vector v) {
        return Math.sqrt(Math.pow(v.getXComponent(), 2) + Math.pow(v.getYComponent(), 2) + Math.pow(v.getZComponent(), 2));
    }

    public static Vector scale(Vector v, double scaleFactor) {
        return new Vector(v.start, new MyPoint(scaleFactor*v.end.x,scaleFactor*v.end.y,scaleFactor*v.end.z));
    }

    public void translate(Vector v) {
        double xTranslation = v.getXComponent();
        double yTranslation = v.getYComponent();
        double zTranslation = v.getZComponent();
        this.start.x += xTranslation;
        this.end.x += xTranslation;
        this.start.y += yTranslation;
        this.end.y += yTranslation;
        this.start.z += zTranslation;
        this.end.z += zTranslation;
        return;
    }

    public static Vector add(Vector v1, Vector v2, double scale) {
        MyPoint start = new MyPoint(v1.start.x + scale*v2.getXComponent(),v1.start.y + scale*v2.getYComponent(),v1.start.z + scale*v2.getZComponent());
        MyPoint end = new MyPoint(v1.end.x + scale*v2.getXComponent(),v1.end.y + scale*v2.getYComponent(),v1.end.z + scale*v2.getZComponent());
        return new Vector(start,end);
    }

    public static double dotProduct(Vector v1, Vector v2){
        return (v1.getXComponent()*v2.getXComponent() + v1.getYComponent()*v2.getYComponent() + v1.getZComponent()*v2.getZComponent());
    }

    public static Vector crossProduct(Vector v1, Vector v2){
        return new Vector(v1.getYComponent()*v2.getZComponent()-v1.getZComponent()*v2.getYComponent(),
                v1.getZComponent()*v2.getXComponent()-v1.getXComponent()*v2.getZComponent(),
                v1.getXComponent()*v2.getYComponent()-v1.getYComponent()*v2.getXComponent());
    }

    public static Vector normalize(Vector v){
        double magnitude = magnitude(v);
        return new Vector(v.getXComponent()/magnitude, v.getYComponent()/magnitude, v.getZComponent()/magnitude);
    }

    public static Vector findPerpendicular(Vector v, MyPoint vP, MyPoint p) {
        // vP - point on the vector line
        double t = calcT(v, vP, p);
        double newVX = (p.x - vP.x) + v.getXComponent() * t;
        double newVY = (p.y - vP.y) + v.getYComponent() * t;
        double newVZ = (p.z - vP.z) + v.getZComponent() * t;
        Vector perp = add(new Vector(newVX,newVY,newVZ), v, -t);
        return perp;
    }

    public static double calcT(Vector v, MyPoint vP, MyPoint p){
        return -(v.getXComponent()*(p.x - vP.x) + v.getYComponent()*(p.y - vP.y) + v.getZComponent()*(p.z - vP.z))/(dotProduct(v,v));
    }
}
