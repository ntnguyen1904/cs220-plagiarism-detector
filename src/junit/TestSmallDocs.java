package junit;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import plagdetect.IPlagiarismDetector;
import plagdetect.PlagiarismDetector;

public class TestSmallDocs
{
    private static final String SMALLDOCS = "docs/smalldocs";
    
    IPlagiarismDetector detector;
    
    private void makeDetector(int n) throws Exception {
        detector = new PlagiarismDetector(n);
        detector.readFilesInDirectory(new File(SMALLDOCS));
    }

    @Test
    public void testNumfiles() throws Exception {
        makeDetector(4);
        assertEquals(20, detector.getFilenames().size());
    }
    
    //@Test
    public void test() throws Exception {
        // this generates the code for the following test case;
        // students don't need to actually run this code
        makeDetector(4);
        for (String f1 : detector.getFilenames()){
            for (String f2 : detector.getFilenames()){
                if (f1.equals(f2)){
                    // skip files that are the same
                    continue;
                }
                int num12 = detector.getNumNGramsInCommon(f1, f2);
                int num21 = detector.getNumNGramsInCommon(f2, f1);
                System.out.printf("    assertEquals(%d, detector.getNumNGramsInCommon(\"%s\", \"%s\"));\n", num12, f1, f2);
                System.out.printf("    assertEquals(%d, detector.getNumNGramsInCommon(\"%s\", \"%s\"));\n", num21, f2, f1);
            }
        }
    }
    
    @Test
    public void testPairs3() throws Exception {
        makeDetector(3);
        Collection<String> pairs = detector.getSuspiciousPairs(10);
        for (String s : pairs) System.out.println(s);
        assertTrue(pairs.contains("2985.txt 2988.txt 10"));
        assertTrue(pairs.contains("2981.txt 2986.txt 16"));
        assertTrue(pairs.contains("2986.txt 2991.txt 11"));
        assertTrue(pairs.contains("2986.txt 2995.txt 11"));
        assertTrue(pairs.contains("2989.txt 2994.txt 10"));
        assertTrue(pairs.contains("2985.txt 2994.txt 13"));
        assertTrue(pairs.contains("2985.txt 2995.txt 14"));
        assertTrue(pairs.contains("2991.txt 2993.txt 11"));
        assertTrue(pairs.contains("2987.txt 2994.txt 10"));
    }

    
    @Test
    public void testPairs4() throws Exception {
        makeDetector(4);
        Collection<String> pairs = detector.getSuspiciousPairs(5);
        assertTrue(pairs.contains("2983.txt 2995.txt 5"));
        assertTrue(pairs.contains("2982.txt 2997.txt 5"));
        assertTrue(pairs.contains("2991.txt 2993.txt 7"));
        assertTrue(pairs.contains("2980.txt 2995.txt 6"));
        assertTrue(pairs.contains("2986.txt 2989.txt 5"));
        assertTrue(pairs.contains("2981.txt 2986.txt 7"));
        assertTrue(pairs.contains("2980.txt 2986.txt 5"));
        assertTrue(pairs.contains("2986.txt 2995.txt 5"));
    }
    
    @Test
    public void testPairs5() throws Exception {
        makeDetector(5);
        Collection<String> pairs = detector.getSuspiciousPairs(4);
        for (String s : pairs) System.out.println(s);
        assertTrue(pairs.contains("2980.txt 2995.txt 4"));
        assertTrue(pairs.contains("2982.txt 2997.txt 4"));
        assertTrue(pairs.contains("2986.txt 2989.txt 4"));
        assertTrue(pairs.contains("2991.txt 2993.txt 5"));    }
}
