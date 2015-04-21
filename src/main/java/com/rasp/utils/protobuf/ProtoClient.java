/**
 * Author : Rahul Madhavan
 * File   : ProtoClient.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.protobuf;

/* Import list */
import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.googlecode.protobuf.pro.duplex.client.DuplexTcpClientPipelineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds the tcp client connections for different machines
 * using protocol buffer on the network
 *
 * reference : https://code.google.com/p/protobuf-rpc-pro/wiki/GettingStarted
 *
 */
public class ProtoClient {

    static final Logger LOG = LoggerFactory.getLogger(ProtoClient.class);

    private DuplexTcpClientPipelineFactory clientFactory;
    private Bootstrap bootstrap;
    private HashMap<String,RpcClientChannel> channelMap;


    public ProtoClient() {
        clientFactory = new DuplexTcpClientPipelineFactory();
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.handler(clientFactory);
        /**
         * set socket channel options
         */
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1048576);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1048576);
        channelMap = new HashMap<>();
    }

    /**
     *
     * @param ip
     * @param port
     * @return the rpc connection for the given string and port
     */
    public RpcClientChannel getConnection(String ip, int port) {
        String key = ip + ":" + port;
        if (!channelMap.containsKey(key)) {
            try {
                RpcClientChannel channel = clientFactory.peerWith(
                        new InetSocketAddress(InetAddress.getByName(ip), port),
                        bootstrap);
                channelMap.put(key, channel);
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        return channelMap.get(key);
    }

}
/* End of ProtoClient.java */


