package utils;
import model.Record;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class GenerateRecords
{
	
	public static void main(String[] args) throws Exception
	{
		File file = new File("records.txt");
		if (file.exists())
			if (!file.delete())
				throw new IOException("存在无法删除的records.txt文件或文件夹");
		if (!file.createNewFile())
			throw new IOException("records.txt创建失败");
		FileOutputStream output = new FileOutputStream(file);
		for (int i = 0; i < 1000000; i++)
		{
			Record record = new Record();
			output.write(record.tobytes());
		}
		output.flush();
		output.close();
	}
}
