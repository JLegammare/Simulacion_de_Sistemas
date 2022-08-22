import java.io.*;
import java.util.List;

public class ResultsGenerator {

    private final File dynamicFile;
    private File vaTimeFile;
    private File staticFile;

    public ResultsGenerator(String dynamicFilePath,String vaFilePath, String staticFilePath) {

        File dymFile = new File(dynamicFilePath);
        if(dymFile.exists())
            dymFile.delete();
        this.dynamicFile= dymFile;

        File stcFile = new File(dynamicFilePath);
        if(stcFile.exists())
            stcFile.delete();
        this.staticFile= stcFile;

        File vaFile = new File(dynamicFilePath);
        if(vaFile.exists())
            vaFile.delete();
        this.vaTimeFile=vaFile;

    }

    public void fillStaticFile(List<Particle> particles,double l) throws IOException {

        FileWriter fw = new FileWriter(staticFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n%f\n",particles.size(),l));
        for (Particle p: particles) {
            sb.append(String.format("%f %f\n",p.getRadius(), p.getRadius()));
        }
        pw.println(sb);
    }

    public void addStateToDynamicFile( List<Particle> particles) throws IOException {

        FileWriter fw = new FileWriter(dynamicFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\na\n",particles.size()));
        for (Particle p: particles) {
            sb.append(String.format("%d %f %f %f %f %f\n",p.getID(), p.getX(),p.getY(), 0f,p.getVelocityModule(),p.getOmega()));
        }
        pw.print(sb);

        pw.close();
    }


}
