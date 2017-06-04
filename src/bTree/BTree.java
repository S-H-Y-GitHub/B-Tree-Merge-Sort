package bTree;


import model.Record;

import java.util.ArrayList;
import java.util.HashMap;
public class BTree
{
	private int m = 4;    //B树的阶数，则关键字数范围[[m/2]-1,m-1]
	private Node root;
	private HashMap<Integer, String> records = new HashMap<>(1024000, 1);
	public BTree()
	{
		Leaf root = new Leaf();
		root.keys.add(0);
		root.nextLeaf = null;
		root.isLeaf = true;
		this.setRoot(root);
	}
	public BTree(ArrayList<Record> records) throws Exception
	{
		Leaf root = new Leaf();
		root.keys.add(0);
		root.nextLeaf = null;
		root.isLeaf = true;
		this.setRoot(root);
		for (Record record : records)
			this.insert(record);
	}
	public int getM()
	{
		return m;
	}
	public void setM(int m)
	{
		this.m = m;
	}
	public Node getRoot()
	{
		return root;
	}
	public void setRoot(Node root)
	{
		this.root = root;
	}
	public void setRoot(int m, Node root)
	{
		this.m = m;
		this.root = root;
	}
	public void insert(Record record) throws Exception
	{
		Leaf cur = getNode(record.a);
		int index = getKeyInNode(cur, record.a);
		assert cur != null;
		cur.keys.add(index, record.a);
		insertNode(cur);
		records.put(record.a, record.b);
	}
	public void delete(int key) throws Exception
	{
		if (!exist(key))
		{
			System.out.println("The Key " + key + " can not found.");
		}
		else
		{
			Node cur = getNode(key);
			//delete
			int index = cur.keys.indexOf(key);
			cur.keys.remove(index);
			records.remove(key);
			deleteNode(cur);
		}
	}
	//定位key所在的节点
	private Leaf getNode(int key) throws Exception
	{
		Node search = root;
		if (search == null)
			throw new Exception("未正常初始化");
		//定位key所在的叶子节点
		while (true)
		{
			if (search.isLeaf) break;
			int index = getKeyInNode(search, key);
			search = search.childNodes.get(index);
		}
		return (Leaf) search;
	}
	public String get(int key) throws Exception
	{
		Node n = getNode(key);
		return records.get(key);
	}
	//key是否存在
	public Boolean exist(int key) throws Exception
	{
		Node search = getNode(key);
		if (search == null) return false;
		for (int temp : search.keys)
		{
			if (temp == key) return true;
		}
		return false;
	}
	//返回key在节点中的index,如3在节点1,5,9的index为1
	private int getKeyInNode(Node search, int key) throws Exception
	{
		for (int i = 0; i < search.keys.size(); i++)
		{
			if (key < search.keys.get(i)) return i;
		}
		return search.keys.size();
	}
	//返回key在节点中的index，key必须存在
	private int getKeyLocation(Node search, int key) throws Exception
	{
		if (search == null) return -1;
		return search.keys.indexOf(key);
	}
	//返回节点在父节点中的index，节点必须存在
	private int getNodeLocation(Node search, Node node) throws Exception
	{
		if (search == null) return -1;
		return search.childNodes.indexOf(node);
	}
	
	//向一个节点执行插入操作
	private void insertNode(Node current) throws Exception
	{
		int max = getM() - 1;    //最大关键字数
		int min = ((int) Math.ceil(getM() / 2)) - 1;   //最小关键字数
		
		Leaf leaf = null;
		Node iNode = null;
		Node newINode = null;
		Integer key = null;
		//对当前节点进行调整
		if (current.keys.size() > max)
		{
			if (current.isLeaf)
			{
				leaf = new Leaf();
				leaf.isLeaf = true;
				leaf.parent = current.parent;
				leaf.nextLeaf = ((Leaf) current).nextLeaf;
				((Leaf) current).nextLeaf = leaf;
				
				for (int i = min + 1; i < current.keys.size(); i++)
				{
					leaf.keys.add(current.keys.get(i));
				}
				
				for (int i = current.keys.size() - 1; i > min; i--)
				{
					current.keys.remove(i);
				}
				key = leaf.keys.get(0);
				newINode = leaf;
			}
			else
			{
				iNode = new Node();
				iNode.isLeaf = false;
				iNode.parent = current.parent;
				
				for (int i = min + 1; i < current.keys.size(); i++)
				{
					iNode.keys.add(current.keys.get(i));
					iNode.childNodes.add(current.childNodes.get(i + 1));
					current.childNodes.get(i + 1).parent = iNode;
				}
				
				for (int i = current.keys.size() - 1; i > min; i--)
				{
					current.keys.remove(i);
					current.childNodes.remove(i + 1);
				}
				key = iNode.keys.get(0);
				iNode.keys.remove(0);
				newINode = iNode;
			}
			
			//对父节点进行调整
			Node curParent = current.parent;
			//当根进行分裂时
			if (curParent == null)
			{
				Node theRootNode = new Node();
				theRootNode.keys.add(key);
				theRootNode.childNodes.add(current);
				theRootNode.childNodes.add(newINode);
				current.parent = theRootNode;
				newINode.parent = theRootNode;
				root = theRootNode;
				return;
			}
			else
			{
				curParent.keys.add(getKeyInNode(curParent, key), key);
				curParent.childNodes.add(getKeyInNode(curParent, key), newINode);
				insertNode(curParent);
			}
		}
		else
		{
			//否则不需要调整
			return;
		}
	}
	//向一个节点执行删除操作
	private void deleteNode(Node current) throws Exception
	{
		int min = ((int) Math.ceil(getM() / 2)) - 1;   //最小关键字数
		
		if (current.keys.size() < min)
		{
			Node curParent = current.parent;
			int nodeIndex = getNodeLocation(curParent, current);
			Node leftSib = null;
			Node rightSib = null;
			if (nodeIndex > 0) leftSib = curParent.childNodes.get(nodeIndex - 1);
			if (nodeIndex < curParent.childNodes.size() - 1) rightSib = curParent.childNodes.get(nodeIndex + 1);
			if (leftSib != null && leftSib.keys.size() > min)
			{
				if (current.isLeaf)
				{
					current.keys.add(0, leftSib.keys.get(leftSib.keys.size() - 1));
					//leftSib.keys.remove(leftSib.keys.size() - 1);
					curParent.keys.set(nodeIndex - 1, current.keys.get(0));
				}
				else
				{
					current.keys.add(0, curParent.keys.get(nodeIndex - 1));
					curParent.keys.set(nodeIndex - 1, leftSib.keys.get(leftSib.keys.size() - 1));
					//leftSib.keys.remove(leftSib.keys.size() - 1);
					current.childNodes.add(0, leftSib.childNodes.get(leftSib.childNodes.size() - 1));
					leftSib.childNodes.get(leftSib.childNodes.size() - 1).parent = current;
					leftSib.childNodes.remove(leftSib.childNodes.size() - 1);
				}
				leftSib.keys.remove(leftSib.keys.size() - 1);
				return;
			}
			if (rightSib != null && rightSib.keys.size() > min)
			{
				if (current.isLeaf)
				{
					current.keys.add(rightSib.keys.get(0));
					//rightSib.keys.remove(0);
					curParent.keys.set(nodeIndex, rightSib.keys.get(1));
				}
				else
				{
					current.keys.add(curParent.keys.get(nodeIndex));
					curParent.keys.set(nodeIndex, rightSib.keys.get(0));
					//rightSib.keys.remove(0);
					current.childNodes.add(rightSib.childNodes.get(0));
					rightSib.childNodes.get(0).parent = current;
					rightSib.childNodes.remove(0);
				}
				rightSib.keys.remove(0);
				return;
			}
			if (leftSib == null || leftSib.keys.size() <= min || rightSib == null || rightSib.keys.size() <= min)
			{
				if (leftSib != null)
				{
					if (current.isLeaf)
					{
						for (int key : current.keys)
						{
							leftSib.keys.add(key);
						}
						((Leaf) leftSib).nextLeaf = ((Leaf) current).nextLeaf;
						if (curParent != null)
						{
							curParent.childNodes.remove(nodeIndex);
							curParent.keys.remove(nodeIndex - 1);
							current = null;
							//if(curParent == root && curParent.keys.size() <= 0) root = leftSib;
							//else deleteNode(curParent);
						}
					}
					else
					{
						if (curParent != null)
						{
							leftSib.keys.add(curParent.keys.get(nodeIndex - 1));
							for (int key : current.keys)
							{
								leftSib.keys.add(key);
							}
							for (Node node : current.childNodes)
							{
								leftSib.childNodes.add(node);
								node.parent = leftSib;
							}
							curParent.keys.remove(nodeIndex - 1);
							curParent.childNodes.remove(nodeIndex);
							current = null;
							//if(curParent == root && curParent.keys.size() <= 0) root = leftSib;
							//else deleteNode(curParent);
						}
					}
					if (curParent == root && curParent.keys.size() <= 0) root = leftSib;
					else deleteNode(curParent);
					return;
				}
				if (rightSib != null)
				{
					if (current.isLeaf)
					{
						current.keys.addAll(rightSib.keys);
						((Leaf) current).nextLeaf = ((Leaf) rightSib).nextLeaf;
						if (curParent != null)
						{
							curParent.childNodes.remove(nodeIndex + 1);
							curParent.keys.remove(nodeIndex);
							rightSib = null;
							//if(curParent == root && curParent.keys.size() <= 0) root = current;
							//else deleteNode(curParent);
						}
					}
					else
					{
						if (curParent != null)
						{
							current.keys.add(curParent.keys.get(nodeIndex));
							current.keys.addAll(rightSib.keys);
							for (Node node : rightSib.childNodes)
							{
								current.childNodes.add(node);
								node.parent = current;
							}
							curParent.childNodes.remove(nodeIndex + 1);
							curParent.keys.remove(nodeIndex);
							rightSib = null;
							//if(curParent == root && curParent.keys.size() <= 0) root = current;
							//else deleteNode(curParent);
						}
					}
					if (curParent == root && curParent.keys.size() <= 0) root = current;
					else deleteNode(curParent);
				}
			}
		}
		//否则无需调整
	}
}
