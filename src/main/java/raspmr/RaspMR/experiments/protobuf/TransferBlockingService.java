package raspmr.RaspMR.experiments.protobuf;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import raspmr.RaspMR.experiments.protobuf.TransferDataProtos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


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
//        File file = new File("/Users/rahulmadhavan/Documents/developer/ms/parallel/projects/RaspMR/output.txt");
//
//        // if file doesnt exists, then create it
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(request.getData());
//            bw.close();
//
//            System.out.println("Done");
//        }catch (Exception e){}
        //System.out.print("******request data  : ");
        System.out.println(request.getData());
        return TransferDataProtos.TransferResponse.newBuilder().setStatus("awesome").build();

    }
}
