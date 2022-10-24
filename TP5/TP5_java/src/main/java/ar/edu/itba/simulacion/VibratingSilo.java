package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Particle;
import ar.edu.itba.simulacion.utils.ParticleGenerator;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.util.List;

public class VibratingSilo {

    final static int W = 20;
    final static int L = 70;
    final static int D = 3;
    final static double w = 5;
    final static double A = 0.15;
    final static int NUMBER_OF_PARTICLES = 200;
    final static int kN = 250;
    final static int kr = 2 * kN;
    final static double DT = 1E-3;
    final static double FINAL_T = 1000.0;

    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";

    public static void main(String[] args) throws IOException {

        List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, L, W);
        ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE, RESULTS_DIRECTORY);
        rg.fillStaticFile(particles);

        //0.CALCULAR VELOCIDADES INICIALES Y FUERZAS (EXPRESIONES N.2 Y T.3 DE LA DIAPOSITIVA 15 DE LA TEORICA 5) PARA t=0;
        rg.addStateToDynamicFile(null,0);

        for (double t = DT; t <= FINAL_T; t += DT) {

            //1.EVOLUCION TEMPORAL CON BEEMAN -> DE LAS PARTICULAS Y LA VIBRACION DEL SILO
            //2.CONDICIONES DE CONTORNO: SI SE PASA L/10 POR DEBAJO DE LA SALIDA REINYECTARLAS POR ARRIBA
            if (t%(DT*50)==0) {
                rg.addStateToDynamicFile(null, t);
            }
        }


    }
}
