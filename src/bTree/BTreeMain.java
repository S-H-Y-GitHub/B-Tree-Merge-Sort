package bTree;
import model.Record;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class BTreeMain
{
	public static void main(String[] args) throws Exception
	{
		//初始化
		System.out.println("正在初始化...");
		FileInputStream recordsfile = new FileInputStream("records.txt");
		ArrayList<Record> records = new ArrayList<>(1024000);
		byte[] tempbytes = new byte[16];
		for (int j = 0; j < 1000000; j++)
		{
			if (recordsfile.read(tempbytes) == -1)
				throw new IOException("错误的EOF！");
			Record record = new Record(tempbytes);
			records.add(record);
		}
		BTree bTree = new BTree(records);
		Scanner scan = new Scanner(System.in);
		int choice = 0;
		//菜单驱动
		while (true)
		{
			System.out.print("请选择要进行的操作：\n1、查找\n2、插入\n3、删除\n>");
			if (scan.hasNextInt())
				choice = scan.nextInt();
			switch (choice)
			{
				case 1:
					System.out.print("请输入Key：");
					int key = 0;
					if (scan.hasNextInt())
						key = scan.nextInt();
					System.out.println(bTree.get(key));
					break;
				case 2:
					System.out.print("请输入Key：");
					key = 0;
					if (scan.hasNextInt())
						key = scan.nextInt();
					scan.nextLine();
					System.out.print("请输入Value：");
					String value = null;
					//if (scan.hasNextLine())
					value = scan.nextLine();
					Record record = new Record(key, value);
					bTree.insert(record);
					System.out.println(bTree.get(key));
					break;
				case 3:
					System.out.print("请输入Key：");
					key = 0;
					if (scan.hasNextInt())
						key = scan.nextInt();
					bTree.delete(key);
					System.out.println(bTree.get(key));
					break;
				default:
					System.out.println("无效的输入");
			}
		}
	}
}
