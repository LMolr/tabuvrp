package tabuvrp.vrp;

import tabuvrp.tabustage.TabuIndex;
import junit.framework.TestCase;


public class NewTabuIndexTest extends TestCase {

    protected TabuIndex<String, String> instance;
    private final java.io.PrintStream out = System.out;
    
    public NewTabuIndexTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        out.print(">> TEST ");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        out.println("<< END TEST\n");
    }


    public void testStep() {
        out.println("step");

        final int MAXEXP = 5;
        final int STEPS = 15;
        instance = new TabuIndex<String, String>(MAXEXP);

        String[] tgts = new String[MAXEXP];
        String[] srcs = new String[MAXEXP];

        for (int exp = 0; exp < MAXEXP; ++exp) {
            srcs[exp] = exp + "_src";
            tgts[exp] = exp + "_tgt";
            instance.setTabu(srcs[exp], tgts[exp], exp);
        }

        for (int step = 0; step < STEPS; ++step) {
            out.print("step " + step + ":  ");
            for (int i = 0; i < MAXEXP; ++i) {
                if (i < step) {
                    assertFalse(instance.isTabu(srcs[i], tgts[i]));
                    out.print("!");
                }
                else {
                    assertTrue(instance.isTabu(srcs[i], tgts[i]));
                    out.print(" ");
                }
                out.print(i+ " ");
            }
            instance.step();
            out.println();
        }
    }


    public void testClearTarget() {
        out.println("clearTarget");

        final int MAXEXP = 5;
        final int STEPS = 10;
        instance = new TabuIndex(MAXEXP);

        String[] tgts = new String[MAXEXP];
        String[] srcs = new String[MAXEXP];

        for (int i = 0; i < MAXEXP; ++i) {
            srcs[i] = i + "_src";
            tgts[i] = i + "_tgt";
        }

        for (int i = 0; i < MAXEXP; ++i) {
            for (int j = 0; j < MAXEXP; ++j) {
                instance.setTabu(srcs[i], tgts[j], i);
            }
        }

        instance.clearTarget(tgts[0]);
        for (int step = 0; step < STEPS; ++step) {
            for (int i = 0; i < MAXEXP; ++i) {
                for (int j = 0; j < MAXEXP; ++j) {
                    if (i < step || j == 0) {
                        assertFalse(instance.isTabu(srcs[i], tgts[j]));
                    } else {
                        assertTrue(instance.isTabu(srcs[i], tgts[j]));
                    }
                }
            }
            instance.step();
        }
        

    }

}