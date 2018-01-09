package com.wisebots.core.cache;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.couchbase.client.CouchbaseClient;


public class MemJCache {


	public static void main(String[] args) {
	    List<URI> uris = new LinkedList<URI>();
	    uris.add(URI.create("http://ec2-23-20-2-172.compute-1.amazonaws.com:8091/pools"));

	    CouchbaseClient client = null;
	    try {
	      client = new CouchbaseClient(uris, "default", "");
	    }
	    catch (Exception e) {
	      System.err.println("Error connecting to Couchbase: " + e.getMessage());
	      System.exit(0);
	    }

	    Object getObject = client.get("teste");
	    System.out.println((String)getObject);

	    client.set("teste", 10, "eba");

	    getObject = client.get("teste");
	    System.out.println((String)getObject);

	  }
	}
