package com.wisebots.core.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Collections;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class Main {

	public static void main(String[] args) throws IOException, URISyntaxException {
		AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler("default", ""));
		ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
		ConnectionFactory cf = factoryBuilder.setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build();

		MemcachedClient memcachedClient = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress("ec2-107-20-50-182.compute-1.amazonaws.com", 11211)));
		memcachedClient.add("test", 1, "testData");
		memcachedClient.replace("test", 0, "testData2");
		System.out.println(memcachedClient.get("test"));
		memcachedClient.delete("test");
		System.out.println(memcachedClient.get("test"));
		memcachedClient.shutdown();
	}
}
