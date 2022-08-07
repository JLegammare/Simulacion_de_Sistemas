import java.util.ArrayList;
import java.util.List;

public class CellIndexMethod {
    private static final int N_PARTICLES = 1000;
    //N_ITERATIONS: cantidad de iteraciones en el tiempo
    private static final int N_ITERATIONS = 1;
//    R: radio de la particula
    private static final double R= 0.37;
//    L: largo de una celda
    private static final int L= 6;
//    M: Cantidad de celdas del board cuadrado (MxM)
    private static final int M= 30;
//    RC: Radio de interaccion
    private static final int RC = 5;

    public static void main(String[] args) {

        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < N_PARTICLES; i++)
            particles.add(new Particle(M,R));

        dynamicGeneration(particles);
        staticGeneration(particles);
        neighborDetection(particles);

    }

    private static void staticGeneration( List<Particle> particles ){

    }
    private static void dynamicGeneration(List<Particle> particles){

    }

    private static void neighborDetection(List<Particle> particles){

    }


}
