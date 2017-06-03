import model.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
public class MergeSort
{
	public static void main(String[] args) throws Exception
	{
		//10条一块，共100000块
		//第一遍扫描，每次读取1000块（160K），分成100个子集合
		FileInputStream recordsfile = new FileInputStream(new File("records.txt"));
		File file = new File("temp1.txt");
		if (file.exists())
			if (!file.delete())
				throw new IOException("存在无法删除的temp1.txt文件或文件夹");
		if (!file.createNewFile())
			throw new IOException("temp1.txt创建失败");
		FileOutputStream output = new FileOutputStream(file);
		for (int i = 0; i < 100; i++)
		{
			//对一个子集合进行排序
			byte[] tempbytes = new byte[16];
			ArrayList<Record> records = new ArrayList<>(10240);
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
				output.write(r.tobytes());
			}
		}
		output.flush();
		output.close();
		//第二遍扫描，归并排序，每次读取100块（16K），1000次排序
		RandomAccessFile sortedRecords = new RandomAccessFile("temp1.txt", "r");
		file = new File("sortedRecords.txt");
		if (file.exists())
			if (!file.delete())
				throw new IOException("存在无法删除的sortedRecords.txt文件或文件夹");
		if (!file.createNewFile())
			throw new IOException("sortedRecords.txt创建失败");
		output = new FileOutputStream(file);
		int[] diskIndex = new int[100];//每一个子集合读取到的磁盘块序列号
		Arrays.fill(diskIndex, 0);
		int[] memIndex = new int[100];//内存中每一块访问到第几号
		Arrays.fill(memIndex, 0);
		HashMap<Integer, byte[][]> buffer = new HashMap<>(128, 1);
		//初始化
		for (int i = 0; i < 100; i++)
		{
			buffer.put(i, new byte[10][16]);
			sortedRecords.seek(16 * 10 * 1000 * i);
			byte[] temp = new byte[160];
			sortedRecords.read(temp);
			for (int m = 0; m < 10; m++)
				System.arraycopy(temp, m * 16, buffer.get(i)[m], 0, 16);
		}
		while (buffer.size() > 0)
		{
			//找出最小值
			int mini = -1;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < 100; i++)
			{
				byte[][] bytes = buffer.get(i);
				if (bytes == null)
					continue;
				Record temp = new Record(bytes[memIndex[i]]);
				if (min > temp.a)
				{
					min = temp.a;
					mini = i;
				}
			}
			//写入最小值
			output.write(buffer.get(mini)[memIndex[mini]]);
			//判断是否需要重新读取
			memIndex[mini]++;
			if (memIndex[mini] >= 10)
			{
				diskIndex[mini]++;
				if (diskIndex[mini] >= 1000)
				{
					buffer.remove(mini);
					continue;
				}
				sortedRecords.seek(16 * 10 * 1000 * mini + 16 * 10 * diskIndex[mini]);
				memIndex[mini] = 0;
				byte[] temp = new byte[160];
				sortedRecords.read(temp);
				for (int m = 0; m < 10; m++)
					System.arraycopy(temp, m * 16, buffer.get(mini)[m], 0, 16);
			}
		}
	}
}
