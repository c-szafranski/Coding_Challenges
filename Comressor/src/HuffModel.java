import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * This Class will handle creating the HuffTree to be used for encoding and
 * decoding
 *
 * @author Chris Szafranski
 * @version May 23, 2016
 */
public class HuffModel
    implements IHuffModel
{
    /**
     * the counter variable will count the different characters used in the
     * document
     */
    CharCounter     counter   = new CharCounter();
    /**
     * a HuffModel object will contain a HuffTree
     */
    public HuffTree tree;
    /**
     * to build the HuffTree used in this class we will need a minHeap data
     * structure
     */
    MinHeap         Hheap;
    /**
     * the encodings variable is an array of binary strings that will hold the
     * tree traversal to access that character in the tree
     */
    String[]        encodings = new String[256];
    /**
     * debugging variable to count how many times loop/recursive call is
     * executed
     */
    public int      turns     = 0;


    // ----------------------------------------------------------
    public void initialize(InputStream stream)
    {
        try
        {
            counter.countAll(stream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void showCounts()
    {
        for (int i = 0; i <= 127; i++)
        {
            if (counter.getCount(i) > 0)
                System.out.println((char)i + " count: " + counter.getCount(i));
        }
    }


    public void showCodings()
    {
        tree = buildTree(counter);
        this.findEncodings(tree.root(), "");
        // printTree(tree.root());
        for (int i = 0; i < 255; i++)
        {
            if (counter.getCount(i) > 0)
            {
                System.out.println("Encoding for " + (char)i + ": "
                    + encodings[i]);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @param node
     *            starting node
     * @param path
     *            encoding path
     */
    public void findEncodings(HuffBaseNode node, String path)
    {
        if (node.isLeaf())
        {
            encodings[((HuffLeafNode)node).element()] = path;
        }

        else
        {
            findEncodings(((HuffInternalNode)node).left(), path + '0');
            findEncodings(((HuffInternalNode)node).right(), path + '1');
        }
    }


    // ----------------------------------------------------------
    /**
     * Will build HuffTree non-recursive.
     *
     * @param countObj
     *            needed for building tree
     * @return HuffTree
     */
    public HuffTree buildTree(CharCounter countObj)
    {
        HuffBaseNode[] table1 = new HuffBaseNode[128];
        ArrayList<HuffTree> tempList = new ArrayList<HuffTree>();

        for (int i = 0; i <= 127; i++)
        {
            if (countObj.table[i] > 0)
            {
                tempList.add(new HuffTree((char)i, counter.table[i]));
                // table1[i] = new HuffLeafNode((char)i, counter.table[i]);
            }
        }

        HuffTree[] trees = new HuffTree[tempList.size()];
        for (int i = 0; i < tempList.size(); i++)
        {
            trees[i] = tempList.get(i);
        }

        Hheap = new MinHeap(trees, tempList.size(), 128);
        HuffTree tmp1, tmp2, tmp3 = null;

        while (Hheap.heapsize() > 1)
        { // While two items left
            tmp1 = (HuffTree)Hheap.removemin();
            tmp2 = (HuffTree)Hheap.removemin();
            tmp3 =
                new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight()
                    + tmp2.weight());
            Hheap.insert(tmp3); // Return new tree to heap
        }
        return tmp3; // Return the tree
    }


    public void write(InputStream stream, String file, boolean force)
    {
        // BitInputStream stream2 = new BitInputStream(stream);
        // initialize(stream2);
        // tree = buildTree(counter);
        // findEncodings(tree.root(), "");
        BitOutputStream output = new BitOutputStream(file);
        output.write(IHuffModel.BITS_PER_INT, IHuffModel.MAGIC_NUMBER);
        writeTree(tree.root(), output);
        output.write(1, 1);
        output.write(9, IHuffModel.PSEUDO_EOF);
        int inbits;
        try
        {
            while ((inbits =
                ((BitInputStream)stream).read(IHuffModel.BITS_PER_WORD)) != -1)
            {
                if (encodings[inbits] != null)
                {
                    char[] encodes = encodings[inbits].toCharArray();
                    for (int i = 0; i < encodes.length; i++)
                    {
                        if (encodes[i] == '0')
                            output.write(1, 0);
                        else
                            output.write(1, 1);
                    }
                }
            }
            output.write(9, IHuffModel.PSEUDO_EOF);
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

    }


    // ----------------------------------------------------------
    /**
     * This will write the HuffTree to a file
     *
     * @param node
     * @param output
     */
    public void writeTree(HuffBaseNode node, BitOutputStream output)
    {
        if (!node.isLeaf())
        {
            output.write(1, 0);
            writeTree(((HuffInternalNode)node).left(), output);
            writeTree(((HuffInternalNode)node).right(), output);
        }
        else
        {
            output.write(1, 1);
            output.write(9, ((HuffLeafNode)node).element());

        }

    }


    // public void setViewer(HuffViewer viewer)

    public void uncompress(InputStream input1, OutputStream output1)
    {
        BitInputStream input = (BitInputStream)input1;
        BitOutputStream output = (BitOutputStream)output1;
        try
        {
            int magic = input.read(BITS_PER_INT);

            if (magic != MAGIC_NUMBER)
            {
                throw new IOException("magic number not right");

            }

        }
        catch (Exception IO)
        {
            //
        }

        HuffTree cTree = new HuffTree(rebuild(input));
        // printTree(cTree.root()); //for debugging
        uncompressOutput(input1, output1, cTree, cTree.root());

    }


// ----------------------------------------------------------
    /**
     * This method will handle reading in the file in question
     *
     * @param input1
     * @param output1
     */
    public void uncompressOutput(
        InputStream input1,
        OutputStream output1,
        HuffTree cTree,
        HuffBaseNode pointer)
    {

        BitInputStream input = (BitInputStream)input1;
        BitOutputStream output = (BitOutputStream)output1;
        int bits = 0;
        try
        {
            bits = input.read(1);
            // System.out.println(bits);
            if (bits == -1)
            {
                System.err.println("should not happen! trouble reading bits1");
                return;
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // use the zero/one value of the bit read
        // to traverse Huffman coding tree
        // if a leaf is reached, decode the character and print
        // UNLESS
        // the character is pseudo-EOF, then decompression done

        if ((bits & 1) == 0)
        {
            // System.out.println("LEFT!");
            pointer = ((HuffInternalNode)pointer).left();
        }
        // if ((bits & 1) == 1)
        else
        {
            // System.out.println("RIGHT!");
            pointer = ((HuffInternalNode)pointer).right();
        }
        if (pointer.isLeaf())
        {
            if (((HuffLeafNode)pointer).element() == (char)IHuffModel.PSEUDO_EOF)
                return; // out of loop
            else
                // System.out.println("PRINT!");
                output.write(BITS_PER_WORD, ((HuffLeafNode)pointer).element());
            pointer = cTree.root();
            // uncompressOutput(input1, output1, cTree);
        }
        uncompressOutput(input1, output1, cTree, pointer);
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @param tree
     */
    public void printTree(HuffBaseNode root)
    {
        if (root == null)
        {
            return;
        }
        if (root.isLeaf())
        {
            System.out.println("Char: " + ((HuffLeafNode)root).element()
                + " Wieght: " + ((HuffLeafNode)root).weight());
        }
        else
        {
            System.out.println("Internal Node value: "
                + ((HuffInternalNode)root).weight());
            printTree(((HuffInternalNode)root).left());
            printTree(((HuffInternalNode)root).right());

        }
    }


    // ----------------------------------------------------------
    /**
     * Reads in tree from binary file and reconstructs the tree to be read by
     * decompresser
     * 
     * @param input1
     * @return root to HuffTree
     */
    HuffBaseNode rebuild(InputStream input1)
    {
        BitInputStream input = (BitInputStream)input1;
        HuffBaseNode temp = null;
        int bits;
        turns++;
        try
        {
            if ((bits = input.read(1)) == -1)
            {
                System.err
                    .println("should not happen! trouble reading bits2 Turns = "
                        + turns);
                return null;
            }

            else
            {
                if (bits == 0)
                {
                    temp =
                        new HuffInternalNode(rebuild(input), rebuild(input), 0);
                }
                else
                {
                    int charV = input.read(9);
                    if (charV == IHuffModel.PSEUDO_EOF)
                        return temp;
                    else
                        return new HuffLeafNode((char)charV, 0);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return temp;
    }

}
