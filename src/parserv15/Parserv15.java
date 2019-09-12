package parserv15;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

class syntaxTree {

    private String root;
    private ArrayList<syntaxTree> children = new ArrayList();
    private boolean overwrite = false;

    public syntaxTree() {
    }

    public void makeStmtNode(String s) {
        this.root = s;
    }

    public void makeOpNode(String s) {
        this.root = s;
    }

    public void makeNode(String s) {
        this.root = s;
    }

    public void leftChild(syntaxTree s) {
        this.children.add(s);
    }

    public void rightChild(syntaxTree s) {
        this.children.add(s);
    }

    public void testChild(syntaxTree s) {
        this.children.add(s);
    }

    public void thenChild(syntaxTree s) {
        this.children.add(s);
    }

    public void elseChild(syntaxTree s) {
        this.children.add(s);
    }

    public void addChild(syntaxTree s) {
        this.children.add(s);
    }

    public void printTree() {
        if (root == null) {

        } else {
            System.out.println(this.root + "   ");
        }
        for (int i = 0; i < this.children.size(); i++) {
            if (this.children.get(i) != null) {
                this.children.get(i).printTree();
            }
        }

    }

    public void exportTreeAsTxtFile() {
        try {
            File f = new File("Exported Tree.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw, true);
            if (overwrite) {
                if (root == null) {

                } else {
                    pw.println(root);
                }
                for (int i = 0; i < this.children.size(); i++) {
                    if (this.children.get(i) != null) {
                        this.children.get(i).exportTreeAsTxtFile();
                    }
                }
                pw.close();
                fw.close();
            } else {

            }
        } catch (IOException ex) {
            Logger.getLogger(syntaxTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getroot() {
        return this.root;
    }

    public ArrayList<syntaxTree> getChildren() {
        return this.children;
    }

    public void setRoot(String s) {
        root = s;
    }
}

public class Parserv15 extends JFrame {

    private static ArrayList<MyScanner.tokenType> tts;
    private static ArrayList<String> ss;
    private static String currentToken;
    private static int index = 0;
    private static ArrayList<String> outputss = new ArrayList();
    private static int insideAnotherStmtSeq = 0;
    private static paintframe pnlDraw = new paintframe();
    private static BufferedReader stReader;
    private static int depth = 0;

    public static void main(String[] args) {
        Parserv15 parse = new Parserv15();
        parse.setVisible(true);
    }

    public Parserv15() {
        setTitle("Syntax Tree");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(d.width / 16, d.height / 16, 6 * d.width / 7, 6 * d.height / 7);
        Container c = this.getContentPane();
        pnlDraw.setBackground(Color.WHITE);
        c.add(this.pnlDraw);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();

    }

    public static void init() {
        MyScanner sc = new MyScanner();
        sc.scan();
        tts = sc.getTokenTypeArrayList();
        ss = sc.getStringArrayList();
        currentToken = ss.get(index);
        program();
        saveParserOutput();
        drawTree();
    }

    public static void drawTree() {
        try {
            File f = new File("Exported Tree.txt");
            FileReader fr = new FileReader(f);
            stReader = new BufferedReader(fr);
            String node;
            Shape s = new Rectangle(70, 30, Color.BLACK, 10, 10, "Empty");
            int xcoordinate = 70, ycoordinate = 70, x2 = 0;
            while (stReader.ready()) {
                node = stReader.readLine();
                if (node.startsWith("read")) {
                    s = new Rectangle(70, 30, Color.BLACK, xcoordinate, ycoordinate, node);
                    x2 = xcoordinate + 70;
                    xcoordinate += 130;
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                } else if (node.startsWith("if")) {
                    s = new Rectangle(70, 30, Color.BLACK, xcoordinate, ycoordinate, node);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    insideIf(xcoordinate, ycoordinate);
                    x2 = xcoordinate + 70;
                    xcoordinate += 130;
                } else if (node.startsWith("write")) {
                    s = new Rectangle(70, 30, Color.BLACK, xcoordinate, ycoordinate, node);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    insideWrite(xcoordinate, ycoordinate);
                    x2 = xcoordinate + 70;
                    xcoordinate += 130;
                } else if (node.startsWith("repeat")) {
                    s = new Rectangle(70, 30, Color.BLACK, xcoordinate, ycoordinate, node);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    insideRepeat(xcoordinate, ycoordinate);
                    x2 = xcoordinate + 70;
                    xcoordinate += 130;
                } else if (node.startsWith("assign")) {
                    s = new Rectangle(70, 30, Color.BLACK, xcoordinate, ycoordinate, node);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    insideAssign(xcoordinate, ycoordinate);
                    x2 = xcoordinate + 70;
                    xcoordinate += 130;
                }
                s = new Line(x2, ycoordinate + 15, Color.RED, xcoordinate, ycoordinate + 15);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
            }
            pnlDraw.removeShape(s);
            pnlDraw.repaint();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void insideIf(int x, int y) {
        int x2, y2;
        int xif = x, yif = y;
        try {
            String str = stReader.readLine();
            y += 70;
            x -= 70;
            Shape s = new Oval(70, 30, Color.BLACK, x, y, str);
            Shape s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
            pnlDraw.addShape(s2);
            pnlDraw.repaint();
            x2 = x + 35;
            y2 = y + 30;
            y += 70;
            x -= 70;
            pnlDraw.addShape(s);
            pnlDraw.repaint();
            for (int i = 0; i < 2; i++) {
                str = stReader.readLine();
                s = new Oval(70, 30, Color.BLACK, x, y, str);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
                s = new Line(x2, y2, Color.RED, x + 35, y);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
                x += 110;
            }
            x += 70;
            y -= 70;
            str = stReader.readLine();
            while (!str.equals("end")) {
                if (str.startsWith("if")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideIf(x, y);
                    x2 = x;
                    x += 130 + depth * 100;
                    s2 = new Line(x2 + 70, y + 15, Color.RED, x, y + 15);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                }
                if (str.startsWith("assign")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideAssign(x, y);
                    x2 = x;
                    x += 130 + depth * 100;
                    s2 = new Line(x2 + 70, y + 15, Color.RED, x, y + 15);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                }
                if (str.startsWith("write")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideWrite(x, y);
                    x2 = x;
                    x += 130 + depth * 100;
                    s2 = new Line(x2 + 70, y + 15, Color.RED, x, y + 15);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                }
                if (str.startsWith("repeat")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideRepeat(x, y);
                    x2 = x;
                    x += 130 + depth * 100;
                    s2 = new Line(x2 + 70, y + 15, Color.RED, x, y + 15);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                }
                if (str.startsWith("read")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xif + 35, yif + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    x2 = x;
                    x += 130 + depth * 100;
                    s2 = new Line(x2 + 70, y + 15, Color.RED, x, y + 15);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                }
                str = stReader.readLine();
            }
            pnlDraw.removeShape(s2);
            pnlDraw.repaint();
        } catch (IOException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insideAssign(int x, int y) {
        int x2 = x, y2 = y;
        int assignDepth = 0;
        Shape s, s2;
        try {
            String str = stReader.readLine();
            if (str.equals("<") || str.equals("=") || str.equals("+") || str.equals("-") || str.equals("*")
                    || str.equals("/")) {
                while (str.equals("<") || str.equals("=") || str.equals("+") || str.equals("-") || str.equals("*")
                        || str.equals("/")) {
                    y += 70;
                    x -= 70;
                    s = new Oval(70, 30, Color.BLACK, x, y, str);
                    s2 = new Line(x2 + 35, y2 + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    str = stReader.readLine();
                    assignDepth++;
                    x2 = x;
                    y2 = y;
                }
                x2 = x + 35;
                y2 = y + 30;
                y += 70;
                x -= 70;
                for (int i = 0; i < assignDepth; i++) {
                    s = new Oval(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s = new Line(x2, y2, Color.RED, x + 35, y);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    if(i == 0){
                    x += 110;
                    str = stReader.readLine();
                    s = new Oval(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s = new Line(x2, y2, Color.RED, x + 35, y);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    }
                    x2 += 70;
                    y2 -= 70;
                    y -= 70;
                    x += 70;
                    if(i != assignDepth - 1){
                    str = stReader.readLine();    
                    }
                }

            } else {
                x2 = x + 35;
                y2 = y + 30;
                y += 70;
                s = new Oval(70, 30, Color.BLACK, x, y, str);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
                s = new Line(x2, y2, Color.RED, x + 35, y);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
            }

        } catch (IOException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void insideWrite(int x, int y) {
        int x2, y2;
        try {
            String str = stReader.readLine();
            x2 = x + 35;
            y2 = y + 30;
            y += 70;
            Shape s = new Oval(70, 30, Color.BLACK, x, y, str);
            pnlDraw.addShape(s);
            pnlDraw.repaint();
            s = new Line(x2, y2, Color.RED, x + 35, y);
            pnlDraw.addShape(s);
            pnlDraw.repaint();
        } catch (IOException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void insideRepeat(int x, int y) {
        int x2, y2;
        int xrepeat = x, yrepeat = y;
        try {
            String str = stReader.readLine();
            Shape s, s2;
            y += 70;
            while (!str.equals("until")) {
                if (str.startsWith("if")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideIf(x, y);
                    x += 130;
                    y += 50;
                }
                if (str.startsWith("assign")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideAssign(x, y);
                    x += 130;
                    y += 50;
                }
                if (str.startsWith("write")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideWrite(x, y);
                    x += 130;
                    y += 50;
                }
                if (str.startsWith("repeat")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    insideRepeat(x, y);
                    x += 130;
                    y += 50;
                }
                if (str.startsWith("read")) {
                    s = new Rectangle(70, 30, Color.BLACK, x, y, str);
                    pnlDraw.addShape(s);
                    pnlDraw.repaint();
                    s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
                    pnlDraw.addShape(s2);
                    pnlDraw.repaint();
                    x += 130;
                    y += 50;
                }
                str = stReader.readLine();
                depth++;
            }

            str = stReader.readLine();
            x += 30;
            s = new Oval(70, 30, Color.BLACK, x, y, str);
            s2 = new Line(xrepeat + 35, yrepeat + 30, Color.RED, x + 35, y);
            x2 = x + 35;
            y2 = y + 30;
            y += 70;
            x -= 70;
            pnlDraw.addShape(s);
            pnlDraw.repaint();
            pnlDraw.addShape(s2);
            pnlDraw.repaint();
            for (int i = 0; i < 2; i++) {
                str = stReader.readLine();
                s = new Oval(70, 30, Color.BLACK, x, y, str);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
                s = new Line(x2, y2, Color.RED, x + 35, y);
                pnlDraw.addShape(s);
                pnlDraw.repaint();
                x += 110;
            }

        } catch (IOException ex) {
            Logger.getLogger(Parserv15.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void match(String expectedToken) {
        if (currentToken.equals(expectedToken) && index < ss.size() - 1) {
            outputss.add(currentToken);
            index++;
            currentToken = ss.get(index);
        } else if (currentToken.equals(expectedToken) && index == ss.size() - 1) {
            outputss.add(currentToken);
            index++;
            currentToken = "";
        } else if(ss.get(--index).equals("end")){
            index++;
        } else{
            error("Error: Expected " + expectedToken);
            index++;
        }
    }

    public static void error(String s) {
        outputss.add(s);
        //ei.add(errorIndex++, index); //we might use Integer.valueOf(index)
    }

    public static syntaxTree factor() {
        outputss.add("factor is found");
        syntaxTree temp = new syntaxTree();

        switch (tts.get(index)) {
            case LeftOvalBracket:
                match("(");
                exp();
                match(")");
                break;
            case Number:
                temp.makeNode(currentToken);
                match(currentToken);
                break;
            case Identifier:
                temp.makeNode(currentToken);
                match(currentToken);
                break;
            default:
                error("Expected left oval bracket/number/identifier");
                break;
        }
        return temp;
    }

    public static syntaxTree simpleExp() {
        outputss.add("Simple exp is found");
        syntaxTree temp;
        temp = term();

        while (currentToken.equals("+") || currentToken.equals("-")) {
            syntaxTree newtemp = new syntaxTree();
            newtemp.makeOpNode(currentToken);
            match(currentToken);
            newtemp.leftChild(temp);
            syntaxTree out = term();
            newtemp.rightChild(out);
            temp = newtemp;
        }
        return temp;
    }

    public static syntaxTree term() {
        outputss.add("term is found");
        syntaxTree temp;
        syntaxTree newtemp = new syntaxTree();
        temp = factor();

        while (currentToken.equals("*") || currentToken.equals("/")) {
            newtemp.makeOpNode(currentToken);
            match(currentToken);
            newtemp.leftChild(temp);
            syntaxTree out = factor();
            newtemp.rightChild(out);
            temp = newtemp;

        }
        return temp;
    }

    public static void addop() {

        switch (currentToken) {
            case "+":
                match("+");
                break;
            case "-":
                match("-");
                break;
            default:
                error("Expected +/-");
                break;
        }
    }

    public static void mulop() {
        switch (currentToken) {
            case "*":
                match("*");
                break;
            case "/":
                match("/");
                break;
            default:
                error("Expected * or /");
                break;
        }
    }

    public static void comaprisonop() {
        switch (currentToken) {
            case "<":
                match("<");
                break;
            case "=":
                match("=");
                break;
            default:
                error("Expected </=");
                break;
        }
    }

    public static syntaxTree exp() {

        outputss.add("exp is found");
        syntaxTree temp;
        syntaxTree newtemp = new syntaxTree();
        temp = simpleExp();
        while (currentToken.equals("<") || currentToken.equals("=")) {
            newtemp.makeOpNode(currentToken);
            match(currentToken);
            newtemp.leftChild(temp);
            syntaxTree out = simpleExp();
            newtemp.rightChild(out);
            temp = newtemp;
        }
        return temp;
    }

    public static void program() {
        outputss.add("program is found");
        syntaxTree output;
        output = stmt_sequence();
        output.printTree();
        output.exportTreeAsTxtFile();
    }

    public static syntaxTree stmt_sequence() {
        outputss.add("statement sequence is found");
        syntaxTree temp = new syntaxTree();
        temp.makeNode(null);
        syntaxTree out = statement();
        temp.addChild(out);
        if (insideAnotherStmtSeq == 0) {
            while (index < ss.size()) {
                match(";");
                syntaxTree out2 = statement();
                temp.addChild(out2);
            }
        }
        match(";");
        return temp;
    }

    public static syntaxTree statement() {
        outputss.add("statement is found");
        syntaxTree temp = new syntaxTree();
        if (index == ss.size()) {
            index--;
        }
        if (tts.get(index) == MyScanner.tokenType.Identifier) {
            temp = assign_stmt();
        } else if (tts.get(index) != MyScanner.tokenType.Undefined) {
            switch (currentToken) {
                case "if":
                    temp = if_stmt();
                    break;
                case "repeat":
                    temp = repeat_stmt();
                    break;
                case "read":
                    temp = read_stmt();
                    break;
                case "write":
                    temp = write_stmt();
                    break;
            }
        } else {
            error("Expected if/repeat/read/write");
        }
        return temp;
    }

    public static syntaxTree if_stmt() {
        outputss.add("if stmt is found");
        syntaxTree temp = new syntaxTree();
        match("if");
        temp.makeStmtNode("if");
        syntaxTree out = exp();
        temp.testChild(out);
        match("then");
        do {
            insideAnotherStmtSeq++;
            syntaxTree out2 = stmt_sequence();
            insideAnotherStmtSeq--;
            temp.thenChild(out2);
        } while (!(currentToken.equals("end") || currentToken.equals("else")));

        if (currentToken.equals("else")) {
            match("else");
            insideAnotherStmtSeq++;
            syntaxTree out3 = stmt_sequence();
            insideAnotherStmtSeq--;
            temp.elseChild(out3);
            syntaxTree st = new syntaxTree();
            st.setRoot("end");
            temp.addChild(st);
        } else {
            syntaxTree st = new syntaxTree();
            st.setRoot("end");
            temp.elseChild(st);
        }
        match("end");
        return temp;
    }

    public static syntaxTree repeat_stmt() {
        outputss.add("repeat statement is found");
        syntaxTree temp = new syntaxTree();
        match("repeat");
        temp.makeStmtNode("repeat");
        do {
            insideAnotherStmtSeq++;
            syntaxTree temp2 = stmt_sequence();
            temp.addChild(temp2);
            insideAnotherStmtSeq--;
        } while (!currentToken.equals("until"));
        match("until");
        syntaxTree st = new syntaxTree();
        st.setRoot("until");
        temp.addChild(st);
        temp.addChild(exp());
        return temp;
    }

    public static syntaxTree assign_stmt() {
        outputss.add("assignment statement is found");
        syntaxTree temp = new syntaxTree();
        temp.makeNode("assign " + currentToken);
        match(currentToken);
        match(":=");
        syntaxTree temp2 = exp();
        temp.addChild(temp2);
        return temp;
    }

    public static syntaxTree read_stmt() {
        outputss.add("read statement is found");
        syntaxTree temp = new syntaxTree();
        match("read");
        temp.makeNode("read " + currentToken);
        match(currentToken);
        return temp;
    }

    public static syntaxTree write_stmt() {
        outputss.add("write statement is found");
        syntaxTree temp = new syntaxTree();
        match("write");
        temp.makeNode("write");
        syntaxTree temp2 = exp();
        temp.addChild(temp2);
        return temp;
    }

    public static void saveParserOutput() {
        File f = new File("Parser Output.txt");
        try {
            FileWriter fw = new FileWriter(f, false);
            PrintWriter pw = new PrintWriter(fw, true);
            for (int i = 0; i < outputss.size(); i++) {
                pw.println(outputss.get(i));
            }
            pw.close();
            fw.close();
        } catch (IOException ex) {
            System.out.println("IO Exception");
        }
    }
}
