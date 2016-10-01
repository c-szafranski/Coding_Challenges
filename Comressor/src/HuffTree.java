/** A Huffman coding tree */
public class HuffTree
    implements Comparable
{
    private HuffBaseNode root;


    /** Constructors */
    public HuffTree(char el, int wt)
    {
        root = new HuffLeafNode(el, wt);
    }


    public HuffTree(HuffBaseNode l, HuffBaseNode r, int wt)
    {
        root = new HuffInternalNode(l, r, wt);
    }

    public HuffTree(HuffBaseNode node)
    {
        root = node;
    }


    public HuffBaseNode root()
    {
        return root;
    }


    public int weight() // Weight of tree is weight of root
    {
        return root.weight();
    }


    public int compareTo(Object t)
    {
        HuffTree that = (HuffTree)t;
        if (root.weight() < that.weight())
            return -1;
        else if (root.weight() == that.weight())
            return 0;
        else
            return 1;
    }
}
