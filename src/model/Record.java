package model;
import java.util.Random;
public class Record
{
	public int a;
	public String b;
	public Record()
	{
		Random random = new Random();
		a = random.nextInt();
		b = getRandomString(12);
	}
	public String getRandomString(int length)
	{ //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	public char[] inttoChar(int i)
	{
		char[] chars = new char[4];
		chars[0] = (char) (i & 0xff);
		chars[1] = (char) ((i >>> 2) & 0xff);
		chars[2] = (char) ((i >>> 4) & 0xff);
		chars[3] = (char) ((i >>> 6) & 0xff);
		return chars;
	}
	@Override
	public String toString()
	{
		return (new String(inttoChar(a)) + b);
	}
}
