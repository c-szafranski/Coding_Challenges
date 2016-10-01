import java.util.HashMap;
import java.util.Map;

// -------------------------------------------------------------------------
/**
 * This class was implemented for the Microsoft Lehigh Coding Challenge on Sep
 * 14 2016
 *
 * @author Chris Szafranski
 * @version Sep 14, 2016
 */
public class Main_Decoder
{

    // ----------------------------------------------------------
    /**
     * Main class
     * 
     * @param args
     *            (String Secret keys, String message to decode)
     */
    public static void main(String[] args)
    {
        String in = args[0];
        // Sample in:
// "T-A D-B K-C F-D X-E V-F E-G A-H J-I Q-J P-K N-L L-M I-N Z-O O-P G-Q S-R W-S B-T C-U M-V H-W Y-X U-Y R-Z";
        String in2 = args[1];
        // Sample in: "Axno lx Zdjhti Ptizdj. Uzc'sx lu zinu azox.";
        String l = decode(in, in2);
        System.out.println(l);
        return;

    }


    // ----------------------------------------------------------
    /**
     * Handles second input string utilizing hashtable of keys
     *
     * @param m
     * @param n
     * @return Solution
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
     * Builds Hashmap Key value pair
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
}
