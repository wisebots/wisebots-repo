package com.wisebots.core.cache;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneCache extends GameCache {

	public final static String CONSTRUCTOR_NAME = "lucene";

	private IndexWriter w;
	private IndexSearcher searcher;
	private Directory index ;
	
	public LuceneCache(String game, String botname) {
		try {
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);

			index = FSDirectory.open(new File("/wisebots/bots/" + CONSTRUCTOR_NAME + "/"  + game + "/" + botname));
			this.w = new IndexWriter(index, config);
			set(new int[0], 0);
			
			IndexReader reader = IndexReader.open(index);
		    this.searcher = new IndexSearcher(reader);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int size() {
		try {
			return index.listAll().length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public void clear() {
		try {
			index.clearLock("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void set(int[] key, Object obj) {
		String k = Arrays.toString(key);
		if(obj instanceof Double){
			Double v = (Double)obj;
			if(v.doubleValue() == 0){
				return;
			}
		}

		try {
			addDoc(w, k, "qv-" + obj.toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				w.commit();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Object get(String key){
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);

		String value = "";
		try{		    
		    QueryParser parser = new QueryParser(Version.LUCENE_35,  key, analyzer);
		    Query query = parser.parse("qv-");
		   
		    TopScoreDocCollector collector =  TopScoreDocCollector.create(1, true);
		    searcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs; 
		    if(hits == null || hits.length == 0){
		    	return "0.0";
		    }
			Document d = searcher.doc(hits[0].doc);
		    value = d.get(key);
		    if(value.length() >= 3){
		    	value = value.substring(3);
		    }
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		return value;
	}

	public void delete(String key) {
	
	}
	
	public HashMap<String, Object> getContent(){
		return new HashMap<String, Object>();
	}
	
	private void addDoc(IndexWriter w, String key, String value) throws IOException {
	    Document doc = new Document();
	    doc.add(new Field(key, value, Field.Store.YES, Field.Index.ANALYZED));
	    w.addDocument(doc);
	}

	@Override
	public Object getSpecified(Integer id, String method, int[] fstate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
