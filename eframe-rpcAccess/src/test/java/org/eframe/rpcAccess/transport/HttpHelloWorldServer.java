package org.eframe.rpcAccess.transport;

import org.eframe.rpcAccess.encypt.algorithm.RSA;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class HttpHelloWorldServer {

    static final int PORT = 8080;

    public final static String responseContent = "响应内容";
    
    public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdellJkv25HQDsP0aeTMzu2363rN2JUF7AGMBx"
    		+ "21kfoA2/JR1gQlJqZ4WD38nJbwSKnNBS/WYCIdpPHak65ewGZBABqzFQjWaqbBe7uuGE7UGFMIW7"
    		+ "m+PmXA1NX+tKTgl7Ajr2Gz3lr8KzM1AsGMD3vjTd/4rvtHlffiulsFZAdwIDAQAB";

    public final static String caller = "yewuzhichibu";
    
    public final static RSA encoder = new RSA();
    
    
    public static void main(String[] args) throws Exception {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new HttpHelloWorldServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to http://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
