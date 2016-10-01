import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * This class contains the main function and will test the compressor
 *
 * @author Chris Szafranski
 * @version May 23, 2016
 */
public class TEST
{

    /**
     * path to file to be compressed or decompressed
     */
    public static String  path;
    public static boolean compress   = false;
    public static boolean decompress = false;


    // C:/Users/Chris/Desktop/CompressionTest.txt
    // ----------------------------------------------------------
    /**
     * This is the main method of the Compressor/decompressor.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException
    {

        // printWelcome();
        String in =
            "T-A D-B K-C F-D X-E V-F E-G A-H J-I Q-J P-K N-L L-M I-N Z-O O-P G-Q S-R W-S B-T C-U M-V H-W Y-X U-Y R-Z";
        String in2 = "Axno lx Zdjhti Ptizdj. Uzc'sx lu zinu azox."; // remember
// to account for the
        // System.out.println(getOutputPath());
        String l = decode(in, in2);
        System.out.println(l);
        return;
        
          BitInputStream stream = new BitInputStream(path); HuffModel model =
          new HuffModel(); model.initialize(stream); // model.showCounts();
          model.showCodings(); stream.close();
          System.out.println("---------------------------------------------");
          // model.printTree(model.tree.root());
          System.out.println("---------------------------------------------");
          BitInputStream stream2 = new BitInputStream(path);
         
        
          model.write(stream2, "C:/Users/Chris/Desktop/OUTPUTHUFF.huff", true);
          stream.close(); stream2.close(); //
          System.out.println(IHuffModel.PSEUDO_EOF); //
          System.out.println(IHuffModel.MAGIC_NUMBER); BitInputStream huff =
          new BitInputStream("C:/Users/Chris/Desktop/OUTPUTHUFF.huff");
          BitOutputStream unhuff = new
         BitOutputStream("C:/Users/Chris/Desktop/OUTPUTUNHUFF.txt"); //
          System.out.println(huff.read((24*4))); model.uncompress(huff,
          unhuff); huff.close(); unhuff.close();
        
    }


    // ----------------------------------------------------------
    /**
     * This will run the decoding process
     *
     * @param m
     * @param n
     * @return
     */
    public static String decode(String m, String n)
    {
        Map<String, String> map = buildKey(m);
        String solution = "";
        for (int i = 0; i < n.length(); i++)
        {
            char c = n.charAt(i);
            if (c == ' ')
            {
                solution += " ";
            }
            else if (Character.isLowerCase(c))
            {

                solution +=
                    map.get(Character.toString(c).toUpperCase()).toLowerCase();
            }
            else if (Character.isUpperCase(c))
            {
                solution += map.get(Character.toString(c).toUpperCase());
            }
            else
            {
                solution += Character.toString(c);
            }
        }
        return solution;
    }


    // ----------------------------------------------------------
    /**
     * 
     *
     * @param m
     * @return
     */
    public static Map<String, String> buildKey(String m)
    {
        String[] f = m.split("\\s");
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < f.length; i++)
        {
            String[] a = f[i].split("\\-");

            if (!map.containsKey(a[0]))
            {
                map.put(a[0], a[1]);
            }
        }

        return map;
    }


    public static void printArray(String[] f)
    {
        for (int j = 0; j < f.length; j++)
        {
            System.out.println(f[j]);
        }
    }


    // ----------------------------------------------------
    // ----------------------------------------------------
    // ----------------------------------------------------

    private static String getOutputPath()
    {
        String temp = "";
        String name = "";
        int lastSlash = 0;
        int lastDot = 0;
        for (int i = 0; i < path.length(); i++)
        {
            if (path.charAt(i) == '/')
            {
                lastSlash = i; // finding last instance of '/'
            }
            if (path.charAt(i) == '.')
            {
                lastDot = i; // finding last instance of '.'
            }
        }
        name = path.substring(lastSlash, lastDot); // parsing out original name
// of document
        temp = path.substring(0, lastSlash);
        return temp + name + ".huff";

    }


    // ----------------------------------------------------------
    /**
     * This method will encode the HuffTree into binary
     *
     * @param root
     *            of HuffTree
     * @return binary String
     */
    public static String encodeHuffTree(HuffBaseNode root)
    {

        if (root == null)
        {
            return null;
        }
        if (root.isLeaf())
        {
            return " "
                + (Integer.toBinaryString(((HuffLeafNode)root).element()))
                + " ";
        }
        else
        {
            String left = "0";
            String right = "0";
            if (((HuffInternalNode)root).left() != null)
            {
                left = "0";
            }
            if (((HuffInternalNode)root).right() != null)
            {
                right = "1";
            }
            return " "
                + (Integer.toBinaryString(((HuffInternalNode)root).weight()))
                + " " + left + " "
                + encodeHuffTree(((HuffInternalNode)root).left()) + " " + right
                + " " + encodeHuffTree(((HuffInternalNode)root).right()) + " ";

        }

    }


    private static void printWelcome()
    {

        System.out
            .println("***************C.S 2016 Commpressor****************");
        Scanner scan = new Scanner(System.in);
        System.out
            .println("Type 'c' for Compression or 'd' for decomression: ");
        String in = scan.next();
        if (in.equals("c") || in.equals("C"))
        {
            compress = true;
        }
        if (in.equals("d") || in.equals("D"))
        {
            decompress = true;
        }
        System.out.println("Enter path to file: ");
        path = scan.nextLine();

    }
}
