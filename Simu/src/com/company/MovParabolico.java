

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
    public double DeltaTime; //default time step, it will set the number of time steps
    public List<Double> TrajectoryTime;
    public List<Double> XPos;
    public List<Double> YPos;
    public List<Double> XVel;
    public List<Double> YVel;
    public List<Double> ModVel; //velocity's intensity on t
    public List<Double> AngleInGrads; //velocity direction on t

    public MovParabolico(double velocity, double AngleInGrads, double initialhigh, double deltatime) {
        this.RadsAngle = AngleInGrads * Math.PI / 180;
        this.velocity = velocity;
        this.initialhigh = initialhigh;
        double aux = velocity * Math.sin(RadsAngle);
        this.flyTime = (aux + Math.sqrt( aux * aux +
                (2 * this.g * initialhigh))) / this.g ;
        this.DeltaTime = deltatime;
        //maximal high, time given t = Vo * sin(theta) / g
        double[] Pos1 = PositionXY(velocity * Math.sin(RadsAngle) / this.g);
        this.maxhigh = Pos1[1];
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("max high = " + twoDecPlaces.format(maxhigh)  + "m");
        //Maximal distance, time is given by fly time
        double[] Pos2 = PositionXY(flyTime);
        this.maxdistance = Pos2[0];
        //this.Simulate();
        //System.out.println("maxima distancia = " + twoDecPlaces.format(maxdistance)  + "m");
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("Fly time = " + twoDecPlaces.format(flyTime) + "s");
        //System.out.println("Max Distance = " + twoDecPlaces.format(maxdistance) + "m");
        //System.out.println("Max High = " + twoDecPlaces.format(maxhigh) + "m");
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
        vel[0] = this.velocity * Math.cos(this.RadsAngle); //velocity on x
        vel[1] = - this.g * time_ + this.velocity * Math.sin(this.RadsAngle); //velocity on y
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("Vx = " + twoDecPlaces.format(vel[0]) + " Vy = " + twoDecPlaces.format(vel[1]));
        return vel;
    }

    private double AngleInGrads(double Vx, double Vy) {
        return Math.atan(Vy / Vx)  / Math.PI * 180;
    }

    public void Simulate() {
        List<Double> TrajectoryTime = new ArrayList<>();
        List<Double> XPos = new ArrayList<>();
        List<Double> YPos = new ArrayList<>();
        List<Double> XVel = new ArrayList<>();
        List<Double> YVel = new ArrayList<>();
        List<Double> ModVel = new ArrayList<>();
        List<Double> AngInGrads = new ArrayList<>();

        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        int TimeSteps = (int) (this.flyTime / this.DeltaTime);
        //double DeltaTime = this.flyTime / this.NroPasos;
        System.out.println("Delta t = " + twoDecPlaces.format(DeltaTime) + "s");

        double[] tPos = new double[2];
        double[] tVel = new double[2];

        for (int i = 0; i <= TimeSteps; i++) {
            TrajectoryTime.add((double) i * DeltaTime);
            tPos = PositionXY(TrajectoryTime.get(i));
            XPos.add(tPos[0]);
            YPos.add(tPos[1]);
            System.out.println("Pos: t = " + twoDecPlaces.format(TrajectoryTime.get(i)) +
                    "s: (" + twoDecPlaces.format(XPos.get(i)) + " ; " +
                    twoDecPlaces.format(YPos.get(i)) + ')');

            tVel = Velocity(TrajectoryTime.get(i));
            XVel.add(tVel[0]);
            YVel.add(tVel[1]);
            System.out.println("Vel: t = " + twoDecPlaces.format(TrajectoryTime.get(i)) +
                   "s: (" + twoDecPlaces.format(XVel.get(i)) + " ; " +
                    twoDecPlaces.format(YVel.get(i)) + ')');

            ModVel.add(Math.sqrt(tVel[0]*tVel[0] + tVel[1]*tVel[1]));
            AngInGrads.add(AngleInGrads(tVel[0],tVel[1]));
            System.out.println("(Vel,Direction): t = " + twoDecPlaces.format(TrajectoryTime.get(i)) +
                    "s: (" + twoDecPlaces.format(ModVel.get(i)) + " ; " +
                    twoDecPlaces.format(AngInGrads.get(i)) + ')');
        }
        this.TrajectoryTime = TrajectoryTime;
        this.XPos = XPos;
        this.YPos = YPos;
        this.XVel = XVel;
        this.YVel = YVel;
        this.ModVel = ModVel;
        this.AngleInGrads = AngInGrads;
        System.out.println("Number of time steps  " + twoDecPlaces.format(TimeSteps));
        System.out.println("Simulation finished, values computed");
    }

    public static void main(String[] arg) {

        //Experiment Parameters
        double Velocity = 9; // vector velocity intensity = sqrt(Vx^2 + Vy^2) m / s
        double Angle = 30; // grades
        double HighY0 = 30; // m
        double DeltaTime = 0.025; // s

        try {
            Velocity = Double.parseDouble(arg[0]);
            Angle = Double.parseDouble(arg[1]);
            HighY0 = Double.parseDouble(arg[2]);
        }
        catch (ArrayIndexOutOfBoundsException e) { System.out.println("*no args*");} // ...no arg
        catch (NumberFormatException e) {System.out.println("*invalid args*");} // ...or invalid arg

        MovParabolico ParabolicM = new MovParabolico(Velocity, Angle, HighY0,DeltaTime);
        ParabolicM.Simulate();
        double TiempoMax = ParabolicM.flyTime;
        double xMax = ParabolicM.maxdistance;
        double yMax = ParabolicM.maxhigh;
        int NbTimeSteps = (int) (ParabolicM.flyTime / ParabolicM.DeltaTime);

        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        System.out.println("Fly Time = " + twoDecPlaces.format(TiempoMax) + "s");
        System.out.println("Max Distance = " + twoDecPlaces.format(xMax) + "m");
        System.out.println("Max High = " + twoDecPlaces.format(yMax) + "m");
        System.out.println("Nb Time Steps = " + twoDecPlaces.format(NbTimeSteps) + "m");



    }
}


