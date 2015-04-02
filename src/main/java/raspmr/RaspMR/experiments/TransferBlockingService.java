package raspmr.RaspMR.experiments;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import raspmr.RaspMR.experiments.protobuf.TransferDataProtos;


/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class TransferBlockingService implements TransferDataProtos.TransferService.BlockingInterface {

    @Override
    public TransferDataProtos.TransferResponse ping(RpcController controller, TransferDataProtos.TransferData request) throws ServiceException {
        System.out.print("******request data  : ");
        System.out.println(request.getData());
        return TransferDataProtos.TransferResponse.newBuilder().setStatus("awesome").build();
    }
}
