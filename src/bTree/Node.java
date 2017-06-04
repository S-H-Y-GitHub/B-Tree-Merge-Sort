package bTree;

import java.util.ArrayList;

public class Node
{
	public boolean isLeaf = false;
	public Node parent = null;
	public ArrayList<Integer> keys = new ArrayList<>();
	public ArrayList<Node> childNodes = new ArrayList<>();
}
