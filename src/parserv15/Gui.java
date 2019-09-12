package parserv15;

import javax.swing.*;
import java.awt.*;
import java.util.*;

enum Style {
    LINE, RECT, CIRC
}

abstract class Shape {

    protected Color c;
    protected int x;
    protected int y;

    public Shape(int x, int y, Color c) {
        this.x = x;
        this.c = c;
        this.y = y;
    }

    public abstract void paint(Graphics g);
}

class Rectangle extends Shape {

    private int w;
    private int h;
    private String node;

    public Rectangle(int w, int h, Color c, int x, int y, String node) {
        super(x, y, c);
        this.w = w;
        this.h = h;
        this.node = node;
    }

    public void paint(Graphics g) {
        g.setColor(c);
        g.drawRect(x, y, w, h);
        char[] cs = node.toCharArray();
        g.drawChars(cs, 0, cs.length, x+5, y+20);
    }
}

class Oval extends Shape {

    private int w;
    private int h;
    String node;
    
    public Oval(int w, int h, Color c, int x, int y, String node) {
        super(x, y, c);
        this.w = w;
        this.h = h;
        this.node = node;
    }

    public void paint(Graphics g) {
        g.setColor(c);
        g.drawOval(x, y, w, h);
        char[] cs = node.toCharArray();
        g.drawChars(cs, 0, cs.length, x+5, y+20);
    }
}

class Line extends Shape {

    private int x2;
    private int y2;

    public Line(int x2, int y2, Color c, int x, int y) {
        super(x, y, c);
        this.x2 = x2;
        this.y2 = y2;
    }

    public void paint(Graphics g) {
        g.setColor(c);
        g.drawLine(x, y, x2, y2);

    }
}

class paintframe extends JPanel {

    private ArrayList shapes = new ArrayList();

    public void addShape(Shape s) {
        if (s != null) {
            shapes.add(s);
        }
    }

    public void removeShape(Shape s) {
        if (s != null) {
            shapes.remove(s);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < shapes.size(); i++) {
            ((Shape) shapes.get(i)).paint(g);
        }
    }
}