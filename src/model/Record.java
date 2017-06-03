package model;
import java.util.Random;
public class Record implements Comparable<Record>
{
	public int a;
	public String b;
	public Record()
	{
		Random random = new Random();
		a = random.nextInt();
		String base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 12; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		b = sb.toString();
	}
	
	public Record(byte[] bytes)
	{
		a = (bytes[0] & 0xFF) + ((bytes[1] & 0xFF) << 8) + ((bytes[2] & 0xFF) << 16) + ((bytes[3] & 0xFF) << 24);
		b = new String(bytes, 4, 12);
	}
	
	private byte[] inttoChar(int i)
	{
		byte[] chars = new byte[4];
		chars[0] = (byte) (i & 0xff);
		chars[1] = (byte) ((i >>> 8) & 0xff);
		chars[2] = (byte) ((i >>> 16) & 0xff);
		chars[3] = (byte) ((i >>> 24) & 0xff);
		return chars;
	}
	@Override
	public String toString()
	{
		return (new String(inttoChar(a)) + b);
	}
	
	@Override
	public int compareTo(Record o)
	{
		return this.a - o.a;
	}
}