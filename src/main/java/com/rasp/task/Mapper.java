/**
 * Author : Asad Shahabuddin
 * File   : Mapper.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.task;

/* Import list */
import java.util.Map;
import java.util.List;

public class Mapper<K, V>
	implements com.rasp.interfaces.Mapper<K, V>
{
	@Override
	public void setup()
	{
		// TODO
	}

	@Override
	public Map<K, List<V>> map(Long key, String value)
	{
		// TODO
		return null;
	}

	@Override
	public void cleanup()
	{
		// TODO
	}
}
/* End of Mapper.java */