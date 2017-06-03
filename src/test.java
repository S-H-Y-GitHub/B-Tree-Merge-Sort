import model.Record;

import java.util.Arrays;
public class test
{
	public static void main(String[] args)
	{
		Record r1 = new Record();
		String s = r1.toString();
		byte[] bytes = s.getBytes();
		Record r2 = new Record(bytes);
		Arrays.asList(r1, r2);
	}
}
