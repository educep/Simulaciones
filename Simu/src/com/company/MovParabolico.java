package com.company;

/**
 * Created by Eduardo on 28/12/2016.
 */

/**
 * Created by Eduardo on 27/12/2016.
 */


import java.text.DecimalFormat;

/* Program to calculate the range of a projectile,
neglecting air resistance. */

class MovParabolico {
    double g = 9.8;// SI units
    double velocidad, alturainicial, anguloEnRads;
    double maxaltitud, maxdistance, tiempoDeVuelo;

    MovParabolico(double velocidad, double anguloEnGrads, double alturainicial) {
        this.anguloEnRads = anguloEnGrads * Math.PI / 180;
        this.velocidad = velocidad;
        this.alturainicial = alturainicial;
        double aux = velocidad * Math.sin(anguloEnRads);
        this.tiempoDeVuelo = (aux + Math.sqrt( aux * aux +
                (2 * this.g * alturainicial))) / this.g ;
        //Altitud maximal, el tiempo es dado por t = Vo * sin(theta) / g
        double[] Pos1 = Posicion(velocidad * Math.sin(anguloEnRads) / this.g);
        this.maxaltitud = Pos1[1];
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("maxima altitud = " + twoDecPlaces.format(maxaltitud)  + "m");
        //Distancia maximal, el tiempo es dado por el tiempo de vuelo
        double[] Pos2 = Posicion(tiempoDeVuelo);
        this.maxdistance = Pos2[0];
        //System.out.println("maxima distancia = " + twoDecPlaces.format(maxdistance)  + "m");
    }

    private double [] Posicion(double tiempo) {
        double [] pos = new double [2];
        pos[0] = this.velocidad * Math.cos(this.anguloEnRads) * tiempo; //posicion en x
        pos[1] = - this.g * tiempo * tiempo / 2 + (this.velocidad * Math.sin(this.anguloEnRads) *
                tiempo) + this.alturainicial; //posicion en y
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("x = " + twoDecPlaces.format(pos[0]) + " y = " + twoDecPlaces.format(pos[1]));
        return pos;
    }

    private double [] Velocidad(double tiempo) {
        double [] vel = new double [2];
        vel[0] = this.velocidad * Math.cos(this.anguloEnRads); //velocidad en x
        vel[1] = - this.g * tiempo + this.velocidad * Math.sin(this.anguloEnRads); //velocidad en y
        //DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        //System.out.println("Vx = " + twoDecPlaces.format(vel[0]) + " Vy = " + twoDecPlaces.format(vel[1]));
        return vel;
    }

    public static void main(String[] arg) {

        //Parámetros de experimento
        double Velocidad = 9; //en metros / segundo
        double Angulo = 30.95; //en grados
        double Altura = 30; //en metros
        int NroPasos = 100; //para los pasos del tiempo

        try {
            Velocidad = Double.parseDouble(arg[0]);
            Angulo = Double.parseDouble(arg[1]);
            Altura = Double.parseDouble(arg[2]);
        }
        catch (ArrayIndexOutOfBoundsException e) { System.out.println("*1*");} // ...no arg
        catch (NumberFormatException e) {System.out.println("*2*");} // ...or invalid arg

        MovParabolico TiroParabolico = new MovParabolico(Velocidad, Angulo, Altura);
        double TiempoMax = TiroParabolico.tiempoDeVuelo;
        double xMax = TiroParabolico.maxdistance;
        double yMax = TiroParabolico.maxaltitud;

        DecimalFormat twoDecPlaces = new DecimalFormat("0.00");
        System.out.println("Tiempo de vuelo = " + twoDecPlaces.format(TiempoMax) + "s");
        System.out.println("Distancia máxima = " + twoDecPlaces.format(xMax) + "m");
        System.out.println("Altura máxima = " + twoDecPlaces.format(yMax) + "m");


        double PasoTiempo = TiempoMax / NroPasos;
        System.out.println("Delta t = " + twoDecPlaces.format(PasoTiempo) + "s");

        double [] Tiempos = new double[NroPasos + 1];
        double [] XPos = new double[NroPasos + 1];
        double [] YPos = new double[NroPasos + 1];
        double [] XVel = new double[NroPasos + 1];
        double [] YVel = new double[NroPasos + 1];

        double [] tPos = new double[2];
        double [] tVel = new double[2];

        for(int i = 0; i <= NroPasos ; i++ ){
            Tiempos[i] = PasoTiempo * i;
            tPos = TiroParabolico.Posicion(Tiempos[i]);
            XPos[i] = tPos[0];
            YPos[i] = tPos[1];
            System.out.println("Pos: t = "+ twoDecPlaces.format(Tiempos[i]) +
                    "s: ("+ twoDecPlaces.format(XPos[i]) + " ; " + twoDecPlaces.format(YPos[i])+')'  );
            tVel = TiroParabolico.Velocidad(Tiempos[i]);
            XVel[i] = tVel[0];
            YVel[i] = tVel[1];
            System.out.println("Vel: t = "+ twoDecPlaces.format(Tiempos[i]) +
                    "s: ("+ twoDecPlaces.format(XVel[i]) + " ; " + twoDecPlaces.format(YVel[i])+')'  );


        }
    }
}

