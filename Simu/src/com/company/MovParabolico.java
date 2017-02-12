

/**
 * Created by Eduardo on 28/12/2016.
 */
package com.company;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/* Program to calculate the range of a projectile,
neglecting air resistance. */

public class MovParabolico {
    double g = 9.8;// SI units
    public double velocity, initialhigh, RadsAngle;
    public double maxhigh, maxdistance, flyTime;
    int NroPasos = 100; //para los pasos del time_
    public List<Double> TrajectoryTime;
    public List<Double> XPos;
    public List<Double> YPos;
    public List<Double> XVel;
    public List<Double> YVel;

    public MovParabolico(double velocity, double AngleInGrads, double initialhigh) {
        this.RadsAngle = AngleInGrads * Math.PI / 180;
        this.velocity = velocity;
        this.initialhigh = initialhigh;
        double aux = velocity * Math.sin(RadsAngle);
        this.flyTime = (aux + Math.sqrt( aux * aux +
                (2 * this.g * initialhigh))) / this.g ;
        //maximal high, time given t = Vo * sin(theta) / g
        double[] Pos1 = PositionXY(velocity * Math.sin(RadsAngle) / this.g);
        this.maxhigh = Pos1[1];
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("max high = " + twoDecPlaces.format(maxhigh)  + "m");
        //Maximal distance, time is given by fly time
        double[] Pos2 = PositionXY(flyTime);
        this.maxdistance = Pos2[0];
        this.Simulate();
        //System.out.println("maxima distancia = " + twoDecPlaces.format(maxdistance)  + "m");
        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        System.out.println("Fly time = " + twoDecPlaces.format(flyTime) + "s");
        System.out.println("Max Distance = " + twoDecPlaces.format(maxdistance) + "m");
        System.out.println("Max High = " + twoDecPlaces.format(maxhigh) + "m");
    }

    private double [] PositionXY(double time_) {
        double [] pos = new double [2];
        pos[0] = this.velocity * Math.cos(this.RadsAngle) * time_; //position x
        pos[1] = - this.g * time_ * time_ / 2 + (this.velocity * Math.sin(this.RadsAngle) *
                time_) + this.initialhigh; //position y
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("x = " + twoDecPlaces.format(pos[0]) + " y = " + twoDecPlaces.format(pos[1]));
        return pos;
    }

    private double [] Velocity(double time_) {
        double [] vel = new double [2];
        vel[0] = this.velocity * Math.cos(this.RadsAngle); //velocity en x
        vel[1] = - this.g * time_ + this.velocity * Math.sin(this.RadsAngle); //velocity en y
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("Vx = " + twoDecPlaces.format(vel[0]) + " Vy = " + twoDecPlaces.format(vel[1]));
        return vel;
    }

    public void Simulate() {
        List<Double> TrajectoryTime = new ArrayList<>();
        List<Double> XPos = new ArrayList<>();
        List<Double> YPos = new ArrayList<>();
        List<Double> XVel = new ArrayList<>();
        List<Double> YVel = new ArrayList<>();

        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        double PasoTiempo = this.flyTime / this.NroPasos;
        System.out.println("Delta t = " + twoDecPlaces.format(PasoTiempo) + "s");

        double[] tPos = new double[2];
        double[] tVel = new double[2];

        for (int i = 0; i <= NroPasos; i++) {
            TrajectoryTime.add((double) i * PasoTiempo);
            tPos = PositionXY(TrajectoryTime.get(i));
            XPos.add(tPos[0]);
            YPos.add(tPos[1]);
            //System.out.println("Pos: t = " + twoDecPlaces.format(TrajectoryTime.get(i)) +
            //        "s: (" + twoDecPlaces.format(XPos.get(i)) + " ; " +
            //        twoDecPlaces.format(YPos.get(i)) + ')');
            tVel = Velocity(TrajectoryTime.get(i));
            XVel.add(tVel[0]);
            YVel.add(tVel[1]);
            //System.out.println("Vel: t = " + twoDecPlaces.format(TrajectoryTime.get(i)) +
            //       "s: (" + twoDecPlaces.format(XVel.get(i)) + " ; " +
            //        twoDecPlaces.format(YVel.get(i)) + ')');
        }
        this.TrajectoryTime = TrajectoryTime;
        this.XPos = XPos;
        this.YPos = YPos;
        this.XVel = XVel;
        this.YVel = YVel;
        System.out.println("Simulation finished, values computed");

        /*
        for (int i = 0; i <= NroPasos; i++) {
            TrajectoryTime[i] = (double) i * PasoTiempo;
            tPos = PositionXY(TrajectoryTime[i]);
            XPos[i] = tPos[0];
            YPos[i] = tPos[1];
            System.out.println("Pos: t = " + twoDecPlaces.format(TrajectoryTime[i]) +
                    "s: (" + twoDecPlaces.format(XPos[i]) + " ; " + twoDecPlaces.format(YPos[i]) + ')');
            tVel = Velocity(TrajectoryTime[i]);
            XVel[i] = tVel[0];
            YVel[i] = tVel[1];
            System.out.println("Vel: t = " + twoDecPlaces.format(TrajectoryTime[i]) +
                    "s: (" + twoDecPlaces.format(XVel[i]) + " ; " + twoDecPlaces.format(YVel[i]) + ')');
        }
        */
    }

    public static void main(String[] arg) {

        //Experiment Parameters
        double Velocity = 9; // m / s
        double Angle = 30.95; // grades
        double HighY0 = 30; // m

        try {
            Velocity = Double.parseDouble(arg[0]);
            Angle = Double.parseDouble(arg[1]);
            HighY0 = Double.parseDouble(arg[2]);
        }
        catch (ArrayIndexOutOfBoundsException e) { System.out.println("*1*");} // ...no arg
        catch (NumberFormatException e) {System.out.println("*2*");} // ...or invalid arg

        MovParabolico TiroParabolico = new MovParabolico(Velocity, Angle, HighY0);
        double TiempoMax = TiroParabolico.flyTime;
        double xMax = TiroParabolico.maxdistance;
        double yMax = TiroParabolico.maxhigh;
        int NroPasos = TiroParabolico.NroPasos;

        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        System.out.println("Fly Time = " + twoDecPlaces.format(TiempoMax) + "s");
        System.out.println("Max Distance = " + twoDecPlaces.format(xMax) + "m");
        System.out.println("Max High = " + twoDecPlaces.format(yMax) + "m");



    }
}


