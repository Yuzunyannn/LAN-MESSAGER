package user;

import java.util.Iterator;
import java.util.LinkedList;

public class SearchHelper {
	public SearchHelper()
	{
	}
	
	public LinkedList<?> search(Iterable<?> list,String text)
	{
		Iterator<?> it=list.iterator();
		LinkedList result=new LinkedList();
		while(it.hasNext())
		{
			Object obj=it.next();
			if(obj instanceof String)
			{
				if(find((String)obj,text))
				{
					result.add(obj);
				}
			}
			else if(obj instanceof User)
			{
				if(find(((User)obj).getUserName(),text))
				{
					result.add(obj);
				}
			}
			else
			{
				System.out.print("search helper error");
			}
		}
		return result;
	}
	
	
	public boolean find(String str, String text){
		if(str.length()>=text.length())
			return str.indexOf(text)!=-1;
		else 
			return false;
	}
	
}
