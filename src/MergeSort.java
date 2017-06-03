import model.Record;

import java.io.File;
import java.io.FileInputStream;
public class MergeSort
{
	public static void main(String[] args) throws Exception
	{
		//10条一块，共100000块
		//第一遍扫描，每次读取1000块（160K），分成100个子集合
		FileInputStream records = new FileInputStream(new File("records.txt"));
		while (true)
		{
			byte[] tempbytes = new byte[16];
			if (records.read(tempbytes) == -1)
				break;
			Record record = new Record(tempbytes);
			
		}
		//第二遍扫描，归并排序，每次读取100块（16K），1000次排序
		
	}
}
