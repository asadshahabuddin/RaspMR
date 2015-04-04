package com.rasp.fs.protobuf;

import com.google.protobuf.BlockingService;
import com.googlecode.protobuf.pro.duplex.PeerInfo;
import com.googlecode.protobuf.pro.duplex.execute.RpcServerCallExecutor;
import com.googlecode.protobuf.pro.duplex.execute.ThreadPoolCallExecutor;
import com.googlecode.protobuf.pro.duplex.server.DuplexTcpServerPipelineFactory;
import com.googlecode.protobuf.pro.duplex.util.RenamingThreadFactoryProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import raspmr.RaspMR.utils.autodiscovery.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class ProtoServer {

    public static void startServer(Service serviceConfig, BlockingService blockingService) throws UnknownHostException{
        InetSocketAddress inetSocketAddress = new InetSocketAddress(
                InetAddress.getByName(
                        serviceConfig.getIp()),
                        serviceConfig.getPort());

        PeerInfo serverInfo = new PeerInfo(inetSocketAddress);
        RpcServerCallExecutor executor = new ThreadPoolCallExecutor(3, 10);
        DuplexTcpServerPipelineFactory serverFactory = new DuplexTcpServerPipelineFactory(serverInfo);
        serverFactory.setRpcServerCallExecutor(executor);

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(new NioEventLoopGroup(0,new RenamingThreadFactoryProxy("boss", Executors.defaultThreadFactory())),
                new NioEventLoopGroup(0,new RenamingThreadFactoryProxy("worker", Executors.defaultThreadFactory()))
        );
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(serverFactory);
        bootstrap.localAddress(serverInfo.getPort());

        bootstrap.option(ChannelOption.SO_SNDBUF, 1048576);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1048576);
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 1048576);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 1048576);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        // we give the server a blocking and non blocking (pong capable) Ping Service
        //BlockingService bPingService = TransferDataProtos.TransferService.newReflectiveBlockingService(new TransferBlockingService());
        serverFactory.getRpcServiceRegistry().registerService(false, blockingService);

        try {
            bootstrap.bind(inetSocketAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
