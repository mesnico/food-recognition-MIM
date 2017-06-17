package it.unipi.ing.mim.img.lucene;

import it.unipi.ing.mim.deep.DNNExtractor;
import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.Output;
import it.unipi.ing.mim.img.lucene.Fields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class LucImageSearch {

	private IndexSearcher indexSearcher;
	
	private Pivots pivots;
	
	private int topKSearch;
	
	public static void main(String[] args) throws Exception {
		
		LucImageSearch imgSearch = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
		
		imgSearch.openIndex(Parameters.LUCENE_PATH);
		
		//Image Query File
		File imgQuery = new File(Parameters.SRC_FOLDER, "apple_pie/63651.jpg");
		
		DNNExtractor extractor = new DNNExtractor();
		
		float[] imgFeatures = extractor.extract(imgQuery, Parameters.DEEP_LAYER);
		
		ImgDescriptor query = new ImgDescriptor(imgFeatures, imgQuery.getName(), "");
				
		List<ImgDescriptor> resLucene = imgSearch.search(query, Parameters.K);
		Output.toHTML(resLucene, Parameters.BASE_URI, Parameters.RESULTS_HTML_LUCENE);
		
		//Uncomment for the optional step
		List<ImgDescriptor> resReordered = imgSearch.reorder(query, resLucene, Parameters.K_REORDER);
		Output.toHTML(resReordered, Parameters.BASE_URI, Parameters.RESULTS_HTML_REORDERED);
	}
	
public List<ImgDescriptor> recognizeImage(File imgQuery) throws Exception{		
		openIndex(Parameters.LUCENE_PATH);
		
		DNNExtractor extractor = new DNNExtractor();
		
		float[] imgFeatures = extractor.extract(imgQuery, Parameters.DEEP_LAYER);
		
		ImgDescriptor query = new ImgDescriptor(imgFeatures, imgQuery.getName(), "");
				
		List<ImgDescriptor> resLucene = search(query, Parameters.K);
		//Output.toHTML(resLucene, Parameters.BASE_URI, Parameters.RESULTS_HTML_LUCENE);
		
		//Uncomment for the optional step
		List<ImgDescriptor> resReordered = reorder(query, resLucene, Parameters.K_REORDER);
		//Output.toHTML(resReordered, Parameters.BASE_URI, Parameters.RESULTS_HTML_REORDERED);
		
		return resReordered;
	}
	
	//TODO
	public LucImageSearch(File pivotsFile, int topKSearch) throws ClassNotFoundException, IOException {
		//Initialize fields
		this.pivots = new Pivots(pivotsFile);
		this.topKSearch = topKSearch;
	}
	
	//TODO
	public void openIndex(String lucenePath) throws IOException {	
		//Initialize Lucene stuff
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		DirectoryReader ir = DirectoryReader.open(index);
		indexSearcher = new IndexSearcher(ir);
	}
	
	//TODO
	public List<ImgDescriptor> search(ImgDescriptor queryF, int k) throws ParseException, IOException, ClassNotFoundException{
		List<ImgDescriptor> res = null;
		res = new ArrayList<ImgDescriptor>();
		ImgDescriptor imgD;
		//convert queryF to text and perform Lucene search
		String query = pivots.features2Text(queryF, topKSearch);
		
		QueryParser p = new QueryParser(Fields.IMG, new WhitespaceAnalyzer());
		Query q = p.parse(query);
		
		TopDocs hits = indexSearcher.search(q, k);
		//LOOP to fill res
			//for each result create an ImgDescriptor object and set ID and call setDist to set the score
			for(int i = 0; i < hits.scoreDocs.length; i++){
				int doc = hits.scoreDocs[i].doc;
				//imgD = new ImgDescriptor(null, indexSearcher.doc(doc).get(Fields.ID));
				BytesRef binary = indexSearcher.doc(doc).getBinaryValue(Fields.BINARY);
				imgD = ImgDescriptor.fromBytes(binary.bytes);
				imgD.setDist(hits.scoreDocs[i].score);
				res.add(imgD);
			}
			//for the optional step get the ImgDescriptor objects stored in Lucene
		return res;
	}
	
	//TODO
	public List<ImgDescriptor> reorder(ImgDescriptor queryF, List<ImgDescriptor> res, int k) throws IOException, ClassNotFoundException {
		//Optional Step!!!
		//LOOP
		//for each result evaluate the distance with the query, call  setDist to set the distance, then sort the results
		for(int i = 0; i < res.size(); i++){
			double distance = queryF.distance(res.get(i));
			res.get(i).setDist(distance);
		}
		Collections.sort(res);
		res = res.subList(0, k > res.size() ? res.size() : k);
		return res;
	}
}
