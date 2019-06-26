package client.search;

import java.util.Iterator;
import java.util.LinkedList;

public class SearchHelper {
	public SearchHelper()
	{
	}
	public LinkedList<String> search(Iterable<String> list,String text)
	{
		Iterator<String> it=list.iterator();
		LinkedList<String> result=new LinkedList<String>();
		while(it.hasNext())
		{
			String str=it.next();
			if(find(str,text))
			{
				result.add(str);
			}
		}
		return result;
	}
	public boolean find(String str, String text){
		if(str.length()>=text.length())
			return str.indexOf(text)!=-1;
		else throw new ArrayIndexOutOfBoundsException();
	}
	
}
