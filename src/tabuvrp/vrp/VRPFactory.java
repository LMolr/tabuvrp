package tabuvrp.vrp;

import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;


public class VRPFactory {

    public static VRP newVRPFromFile(String path) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(path));

        /* node count */
        int nodeCount = Integer.parseInt(skipComments(r));

        Integer [] demands = new Integer[nodeCount];
        double[] x = new double[nodeCount];
        double[] y = new double[nodeCount];

        /* supply */
        demands[0] = - Integer.valueOf(skipComments(r));

        int i;
        String line;
        String[] toks;
        /* node positions */
        for (i = 0; i < nodeCount; ++i) {
            line = skipComments(r);
            toks = line.split(" +");
            if (i + 1 != Integer.parseInt(toks[0])) {
                throw new Exception("wrong node index: " + toks[0]);
            }
            x[i] = Double.parseDouble(toks[1]);
            y[i] = Double.parseDouble(toks[2]);
        }

        for (i = 0; i < nodeCount; ++i) {
            line = skipComments(r);
            toks = line.split(" +");
            if (i + 1 != Integer.parseInt(toks[0])) {
                throw new Exception("wrong node index: " + toks[0]);
            }
            if (i == 0) {
                continue;
            }
            demands[i] = Integer.valueOf(toks[1]);
        }

        VRP vrp = new VRP(demands, x, y);

        return vrp;
    }

    protected static String skipComments(BufferedReader r) throws Exception {
        String s;
        do {
            s = r.readLine();
        } while (s != null && s.startsWith("//"));
        return s;
    }
}
