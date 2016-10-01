/** Huffman tree node implementation: Base class */
public interface HuffBaseNode extends Comparable
{
    public boolean isLeaf();


    public int weight();
}
