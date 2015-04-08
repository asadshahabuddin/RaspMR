package com.rasp.experiments.protobuf;

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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class ProtoServer {

    public static void initializeServer(InetAddress inetAddress, int port){
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress,port);
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
        BlockingService bPingService = TransferDataProtos.TransferService.newReflectiveBlockingService(new TransferBlockingService());
        serverFactory.getRpcServiceRegistry().registerService(false, bPingService);

        try {
            bootstrap.bind(inetSocketAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
