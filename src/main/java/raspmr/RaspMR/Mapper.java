/**
 * Author : Rahul Madhavan
 * File   : Mapper.java
 * Email  : 
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/* Import list */
import java.util.Map;
import java.util.List;

public interface Mapper<KEY_OUT,VALUE_OUT>
{
    void setup();

    Map<KEY_OUT,List<VALUE_OUT>> map(Long key,String value, Context context);

    void cleanup();
}