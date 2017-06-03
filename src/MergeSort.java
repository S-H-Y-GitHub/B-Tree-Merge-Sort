import model.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
public class MergeSort
{
	public static void main(String[] args) throws Exception
	{
		//10条一块，共100000块
		//第一遍扫描，每次读取1000块（160K），分成100个子集合
		FileInputStream recordsfile = new FileInputStream(new File("records.txt"));
		File file1 = new File("temp1.txt");
		if (file1.exists())
			if (!file1.delete())
				throw new IOException("存在无法删除的temp1.txt文件或文件夹");
		if (!file1.createNewFile())
			throw new IOException("temp1.txt创建失败");
		FileOutputStream output1 = new FileOutputStream(file1);
		for (int i = 0; i < 100; i++)
		{
			//对一个子集合进行排序
			byte[] tempbytes = new byte[16];
			ArrayList<Record> records = new ArrayList<>();
			records.ensureCapacity(10240);
			for (int j = 0; j < 10000; j++)
			{
				if (recordsfile.read(tempbytes) == -1)
					throw new IOException("错误的EOF！");
				Record record = new Record(tempbytes);
				records.add(record);
			}
			Collections.sort(records);
			//写回这个子集合
			for (Record r : records)
			{
				output1.write(r.tobytes());
			}
		}
		output1.flush();
		output1.close();
		//第二遍扫描，归并排序，每次读取100块（16K），1000次排序
		
	}
}
