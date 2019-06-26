package debug;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import client.search.SearchHelper;

class SearchHelperTest {

	@Test
	void testSearch() {
		String[] list= {"ycy","ssj","myk","lyl","gay","ssj is gay"};
		ArrayList<String> l=new ArrayList();
		for(String i:list) 
		{
			l.add(i);
		}
		SearchHelper sh=new SearchHelper();
		l=new ArrayList(sh.search(l, "gay"));
		assertEquals(l.get(0),"gay");
		assertEquals(l.get(1),"ssj is gay");
	}
	@Test
	void testfind1() 
	{
		SearchHelper sh=new SearchHelper();
		assertTrue(sh.find("gay","gay"));
	}
	@Test
	void testfind2() 
	{
		SearchHelper sh=new SearchHelper();
		assertTrue(sh.find("gay hhh","gay"));
	}
	@Test
	void testfind3() 
	{
		SearchHelper sh=new SearchHelper();
		assertFalse(sh.find("ycy","gay"));
	}
	@Test
	void testfind4() 
	{
		SearchHelper sh=new SearchHelper();
		try {
		assertTrue(sh.find("gay","gay hhhh"));
		}
		catch(Exception e)
		{
			System.out.println("数组越界");
		}
	}
}
