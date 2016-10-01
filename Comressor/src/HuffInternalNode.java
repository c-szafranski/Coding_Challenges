/** Huffman tree node: Internal class */
public class HuffInternalNode
    implements HuffBaseNode
{
    private int          weight;
    private HuffBaseNode left;
    private HuffBaseNode right;


    /** Constructor */
    public HuffInternalNode(HuffBaseNode l, HuffBaseNode r, int wt)
    {
        left = l;
        right = r;
        weight = wt;
    }


    /** @return The left child */
    public HuffBaseNode left()
    {
        return left;
    }


    /** @return The right child */
    public HuffBaseNode right()
    {
        return right;
    }


    /** @return The weight */
    public int weight()
    {
        return weight;
    }


    /** Return false */
    public boolean isLeaf()
    {
        return false;
    }


    @Override
    public int compareTo(Object o)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
