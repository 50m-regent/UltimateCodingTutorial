interface Sizeable {
    abstract double getSize();
}

class Vector2D implements Sizeable {
    public static final Vector2D origin = new Vector2D(0, 0);

    public static double calculateDistance(final Vector2D vector1, final Vector2D vector2) {
        return new Vector2D(
            vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY()
        ).getSize();
    }

    private final double x, y;

    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(final int x, final int y) {
        this.x = (double) x;
        this.y = (double) y;
    }

    public double getX() {return this.x;}
    public double getY() {return this.y;}

    @Override
    public double getSize() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
}

enum ShapeType {
    TRIANGLE,
    RECTANGLE,
    SQUARE,
}

abstract class Shape {

    protected final Vector2D[] vertices;
    private ShapeType type;

    public Shape(final Vector2D[] vertices) {
        this.vertices = vertices;
    }

    public ShapeType setType(final ShapeType type) {return this.type = type;}
    public ShapeType getType() {return this.type;}
}

class Triangle extends Shape implements Sizeable {
    public Triangle(final Vector2D vertex1, final Vector2D vertex2, final Vector2D vertex3) {
        super(new Vector2D[]{vertex1, vertex2, vertex3});
        super.setType(ShapeType.TRIANGLE);
    }

    private Vector2D[] getSanitized() {
        return new Vector2D[]{
            Vector2D.origin,
            new Vector2D(
                super.vertices[1].getX() - super.vertices[0].getX(),
                super.vertices[1].getY() - super.vertices[0].getY()
            ),
            new Vector2D(
                super.vertices[2].getX() - super.vertices[0].getX(),
                super.vertices[2].getY() - super.vertices[0].getY()
            )
        };
    }

    @Override
    public double getSize() {
        final Vector2D[] sanitized = this.getSanitized();

        assert 0 == Vector2D.calculateDistance(Vector2D.origin, sanitized[0]);

        return Math.abs(
            sanitized[1].getX() * sanitized[2].getY() - sanitized[1].getY() * sanitized[2].getX()
        ) / 2.;
    }
}

class Quadrilateral extends Shape {
    public Quadrilateral(
        final Vector2D vertex1,
        final Vector2D vertex2,
        final Vector2D vertex3,
        final Vector2D vertex4
    ) {
        super(new Vector2D[]{vertex1, vertex2, vertex3, vertex4});
    }
}

class Rectangle extends Quadrilateral implements Sizeable {
    public Rectangle(final Vector2D vertex1, final Vector2D vertex2) {
        super(
            vertex1,
            new Vector2D(vertex1.getX(), vertex2.getY()),
            vertex2,
            new Vector2D(vertex2.getX(), vertex1.getY())
        );
        super.setType(ShapeType.RECTANGLE);
    }

    @Override
    public double getSize() {
        return
            Math.abs(this.vertices[0].getY() - this.vertices[1].getY()) *
            Math.abs(this.vertices[0].getX() - this.vertices[2].getX());
    }
}

class Square extends Rectangle implements Sizeable {
    public Square(final Vector2D vertex1, final double edgeLength) {
        super(
            vertex1,
            new Vector2D(vertex1.getX() + edgeLength, vertex1.getY() + edgeLength)
        );
        super.setType(ShapeType.SQUARE);
    }
}

public class Main {
    private static final Vector2D vector1 = new Vector2D(3, 4);

    public static void main(String[] args) {
        System.out.println("size of vector1 is " + Main.vector1.getSize());

        final Triangle shape1 = new Triangle(
            new Vector2D(1, 1),
            new Vector2D(1, 3),
            new Vector2D(4., 1.)
        );

        System.out.println(shape1.getType());
        System.out.println("size of shape1 is " + shape1.getSize());

        Quadrilateral shape2, shape3;
        shape2 = new Rectangle(new Vector2D(0, 0), new Vector2D(1, 2));
        shape3 = new Square(new Vector2D(0, 0), 3.);

        Quadrilateral[] shapes = new Quadrilateral[]{shape2, shape3};
        for (final Quadrilateral shape: shapes) {
            System.out.println(((Rectangle) shape).getType());
            if (shape instanceof Rectangle) {
                System.out.println("size of shape is " + ((Rectangle) shape).getSize());
            } else {
                System.out.println("size of shape is " + ((Square) shape).getSize());
            }
        }
    }
}