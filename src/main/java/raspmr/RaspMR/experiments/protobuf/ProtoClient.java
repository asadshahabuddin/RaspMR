package raspmr.RaspMR.experiments.protobuf;


import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.googlecode.protobuf.pro.duplex.client.DuplexTcpClientPipelineFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class ProtoClient {

    private DuplexTcpClientPipelineFactory clientFactory;
    private Bootstrap bootstrap;
    private HashMap<String,RpcClientChannel> channelMap;

    public ProtoClient(){
        clientFactory = new DuplexTcpClientPipelineFactory();
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.handler(clientFactory);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1048576);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1048576);
        channelMap = new HashMap<>();
    }

    private RpcClientChannel getConnection(String ip, int port){
        String key = ip+":"+port;
        if(!channelMap.containsKey(key)){
            try {
                RpcClientChannel channel = clientFactory.peerWith(
                        new InetSocketAddress(InetAddress.getByName(ip),port),
                        bootstrap);
                channelMap.put(key,channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return channelMap.get(key);
    }




    public void send(String ip, int port, String message){

        RpcClientChannel channel = null;
        try {
            channel = getConnection(ip,port);
            TransferDataProtos.TransferService.BlockingInterface transferService = TransferDataProtos.TransferService.newBlockingStub(channel);
            RpcController controller = channel.newRpcController();
            TransferDataProtos.TransferData request = TransferDataProtos.TransferData.newBuilder().setData(message).build();
            TransferDataProtos.TransferResponse response = transferService.ping(controller, request);

        } catch (ServiceException e) {
            e.printStackTrace();
        } finally{
            if(null != channel)
                channel.close();

        }

    }

}


