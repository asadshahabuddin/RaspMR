package raspmr.RaspMR.experiments;


import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.PeerInfo;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.googlecode.protobuf.pro.duplex.client.DuplexTcpClientPipelineFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import raspmr.RaspMR.experiments.protobuf.TransferDataProtos;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class ProtoClient {

    public static void main(String[] args) {
        PeerInfo client = new PeerInfo("clientHostname", 1234);
        PeerInfo server = new PeerInfo("10.103.33.242", 9292);

        DuplexTcpClientPipelineFactory clientFactory = new DuplexTcpClientPipelineFactory();
        //clientFactory.setClientInfo(client);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.handler(clientFactory);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1048576);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1048576);

        RpcClientChannel channel = null;
        try {

            channel = clientFactory.peerWith(new InetSocketAddress(InetAddress.getByName("10.103.33.242"),9292),bootstrap);
            TransferDataProtos.TransferService.BlockingInterface transferService = TransferDataProtos.TransferService.newBlockingStub(channel);
            RpcController controller = channel.newRpcController();

            TransferDataProtos.TransferData request = TransferDataProtos.TransferData.newBuilder().setData("Yoooo!!!").build();
            TransferDataProtos.TransferResponse response = transferService.ping(controller, request);
            System.out.println(response.getStatus());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } finally{
            if(null != channel)
                channel.close();

        }

    }

}
