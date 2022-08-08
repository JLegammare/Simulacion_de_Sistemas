import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {

    public static List<Particle> parseParticles(String staticFilePath, String dynamicFilePath) throws IOException {

        File staticFile = new File(staticFilePath);
        File dynamicFile = new File(dynamicFilePath);

        Scanner staticScanner = new Scanner(new File(staticFilePath));
        Scanner dynamicScanner = new Scanner(new File(dynamicFilePath));

        int N = Integer.parseInt(staticScanner.next());
        int L = Integer.parseInt(staticScanner.next());
        CellIndexMethod.setL(L);
        CellIndexMethod.setN(N);
        ArrayList<Integer> times= new ArrayList<>();
        times.add(dynamicScanner.nextInt());
        List<Particle> particles = new ArrayList<>();

        for (int j = 0; staticScanner.hasNextLine() && dynamicScanner.hasNextLine(); j++) {
            double radius = staticScanner.nextDouble();
            double property = staticScanner.nextDouble();
            double posX = dynamicScanner.nextDouble();
            double posY = dynamicScanner.nextDouble();
            particles.add(new Particle(j,posX,posY,radius,property));
        }

        return particles;
    }

     public static  void generateOutput(Map<Particle,List<Particle>> neighbors, String path) throws IOException {

            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }

        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        neighbors.forEach((particle, particles) -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[ ");
                    sb.append(particle.getId());
                    for (Particle p: particles) {
                        sb.append(String.format(" %d",p.getId()));
                    }
                     sb.append(" ]");
                     pw.println(sb);

        });

        pw.close();
    }


}
