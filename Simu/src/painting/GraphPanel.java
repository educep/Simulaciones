package painting;

import com.company.MovParabolico;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rodrigo (obtained from stackoverflow)
 * ou can define something like
 * int graphWidth = getWidth() - 2 * padding - labelPadding; int graphHeight = getHeight() - 2 * padding - labelPadding;
 * to avoid calculating position every time.
 */
public class GraphPanel extends JPanel {

    private int width = 600;
    private int heigth = 400;
    private int padding = 50; //25
    private int labelPadding = 50; //25
    private Color lineColor = new Color(16, 13, 117, 187);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(216, 218, 206, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private int numberXDivisions = 10;
    private List<Double> TrajectoryTime; //gives time of trajectory over which are computed:
    private List<Double> scoresY; //gives position in Y
    private List<Double> scoresX; //gives position in X
    private List<Double> VelX; //gives velocity in X
    private List<Double> VelY; //gives velocity in Y
    private List<Double> Velocity; //gives velocity intensity
    private List<Double> Angle; //gives velocity direction

    public GraphPanel(MovParabolico ParabolicMotion) {
        //Here we retrieve all the components that are going to be shown on the graphic
        //Computing trajectory
        ParabolicMotion.Simulate();
        //Setting new time step
        ParabolicMotion.DeltaTime = 0.1;
        ParabolicMotion.Simulate();
        this.TrajectoryTime = ParabolicMotion.TrajectoryTime;
        this.scoresX = ParabolicMotion.XPos;
        this.scoresY = ParabolicMotion.YPos;
        this.VelX = ParabolicMotion.XVel;
        this.VelY = ParabolicMotion.YVel;
        this.Velocity = ParabolicMotion.ModVel;
        this.Angle = ParabolicMotion.AngleInGrads;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) /
                (scoresY.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) /
                (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scoresY.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scoresY.get(i)) * yScale + padding);
            //System.out.println("x = " + scoresX.get(i) + ";" + "y = " + scoresY.get(i));
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() -
                (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) /
                    numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scoresY.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) /
                        numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < numberXDivisions; i++) {
            if (scoresX.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding)
                        / numberXDivisions + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                //if ((i % ((int) ((scoresX.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding -
                            1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = ((int) ((getXMinScore() + (getXMaxScore() - getXMinScore()) * ((i * 1.0) /
                            numberXDivisions)) * 100)) / 100.0 + "";
                    //String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 +
                            metrics.getHeight() + 3);
                //}
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding,
                padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding,
                getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);

        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            //System.out.println("x = " + graphPoints.get(i).x + ";" + "y = " + graphPoints.get(i).y);
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

// *   @Override
// *   public Dimension getPreferredSize() {
// *       return new Dimension(width, heigth);
// *   }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : scoresY) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getXMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : scoresX) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : scoresY) {
            maxScore = Math.max(maxScore, score);
        }
        //maxScore = maxScore % 10 - 10; //take into account the padding ()
        //System.out.println("getMinScore = " + maxScore);
        return maxScore;
    }

    private double getXMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : scoresX) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(List<Double> scores) {
        this.scoresY = scores;
        invalidate();
        this.repaint();
    }

    public List<Double> getScores() {
        return scoresY;
    }

    private static void createAndShowGui() {
        List<Double> scores = new ArrayList<>();
        //Parabolic Motion drawn by default:
        MovParabolico ParabolicMotion = new MovParabolico(25, 45, 10, 0.025);


        GraphPanel mainPanel = new GraphPanel(ParabolicMotion);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Parabolic Motion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}