package bTree;

public class BTreeTest
{
	public static void main(String[] args) throws Exception
	{
		BTree bTree = new BTree();
		
		int[] data = {53, 5, 700, 11, 13, 17, 9, 23, 9, 31, 347, 40, 41, -43, 47};
		
		for (int i : data)
		{
			bTree.insert(i);
		}
		if (bTree.exist(53)) System.out.println("Yes1");
		for (int i : data)
		{
			bTree.delete(i);
		}
	    /*for(int i =0;i<6;i++){
            bTree.delete(data[i]);
        }
        bTree.delete(19);*/
		if (bTree.exist(17)) System.out.println("Yes2");
		System.out.println(bTree.getRoot().keys.size());
		System.out.println(bTree.getRoot().childNodes.size());
		for (int i : bTree.getRoot().keys)
		{
			System.out.println(i);
		}

        /*for (int i : bTree.getRoot().childNodes.get(2).keys) {
            System.out.println(i);
        }*/
        /*for (int i : bTree.getRoot().childNodes.get(0).keys) {
            System.out.println(i);
        }*/
	}
}
