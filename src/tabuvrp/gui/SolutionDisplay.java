package tabuvrp.gui;

import java.awt.*;
import tabuvrp.core.*;


public class SolutionDisplay extends Canvas {

    private Solution sol;
    private Graph graph;
    private final double minX, maxX, xCorr, mX;
    private final double minY, maxY, yCorr, mY;

    public SolutionDisplay() {
        this.graph = null;
        minX = maxX = xCorr = mX = 0;
        minY = maxY = yCorr = mY = 0;
        sol = null;
    }
    public SolutionDisplay(Graph graph) {
        super();
        this.graph = graph;
        minX = graph.getMinX();
        maxX = graph.getMaxX();
        xCorr = (maxX - minX) * 0.015;
        mX = minX - xCorr;
        minY = graph.getMinY();
        maxY = graph.getMaxY();
        yCorr = (maxY - minY) * 0.015;
        mY = minY - yCorr;
        sol = null;
    }

    @Override
    public void paint(Graphics graphics) {
        if (graph == null) return;
        
        Graphics2D g =(Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = getSize();
        double xFact = d.width  / (maxX - minX + xCorr * 2);
        double yFact = d.height / (maxY - minY + yCorr * 2);
        g.setColor(Color.BLUE);
        for (int i = 0; i < graph.getNodeCount(); ++i) {
            Node n = graph.getNode(i);
            g.fillOval((int) Math.round((n.getX() - mX) * xFact),
                       (int) Math.round((n.getY() - mY) * yFact),
                       10, 4);
            g.drawString(String.valueOf(i),
                         (int) Math.round((n.getX() - mX) * xFact),
                         (int) Math.round((n.getY() - mY) * yFact));
        }

        if (sol == null) {
            return;
        }
        Integer[][] solView = sol.getSolView();
        for (Integer[] route : solView) {
            for (int n = 1; n < route.length; ++n) {
                Node n1 = graph.getNode(route[n - 1]);
                Node n2 = graph.getNode(route[n]);
                g.setColor(Color.DARK_GRAY);
                g.drawLine((int) Math.round((n1.getX() - mX) * xFact),
                           (int) Math.round((n1.getY() - mY) * yFact),
                           (int) Math.round((n2.getX() - mX) * xFact),
                           (int) Math.round((n2.getY() - mY) * yFact));
                g.setColor(Color.GRAY);
                g.drawString(String.valueOf(Math.round(graph.getEdge(route[n - 1], route[n]).getCost())),
                             Math.round(((n2.getX() + n1.getX()) / 2 - mX) * xFact),
                             Math.round(((n2.getY() + n1.getY()) / 2 - mY) * yFact) );
            }
        }
    }
    
    public void setSolution(Solution solution) {
        sol = solution;
        this.repaint();
    }
}
