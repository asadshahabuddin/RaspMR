/**
 * Author : Rahul Madhavan
 * File   : Reducer.java
 * Email  : 
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/* Import list */
import java.util.Map;
import java.util.List;

public interface Reducer<KEY_IN,VALUE_IN,KEY_OUT,VALUE_OUT>
{
    void setup();

    Map<KEY_OUT,List<VALUE_OUT>> reduce(KEY_IN key,List<VALUE_IN> value);

    void cleanup();
}