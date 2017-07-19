package it.unipi.ing.mim.img.lucene;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.FeaturesStorage;
import it.unipi.ing.mim.img.lucene.Fields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;

public class LucImageIndexing {
	
	private Pivots pivots;
	
	private List<ImgDescriptor> idsDataset;
	private int topKIdx;
	
	private IndexWriter indexWriter;
		
	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException {
		LucImageIndexing luceneImgIdx = new LucImageIndexing(Parameters.PIVOTS_FILE, Parameters.STORAGE_FILE, Parameters.TOP_K_IDX);
		luceneImgIdx.openIndex(Parameters.LUCENE_PATH);
		luceneImgIdx.index();
		luceneImgIdx.closeIndex();
	}
	
	public LucImageIndexing(File pivotsFile, File datasetFile, int topKIdx) throws IOException, ClassNotFoundException {
		//load the dataset and the pivots
		this.pivots = new Pivots(pivotsFile);
		this.idsDataset = FeaturesStorage.load(datasetFile);
		this.topKIdx = topKIdx;
		
	}
	
	public void openIndex(String lucenePath) throws IOException {
		//initialize Lucene stuff
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		conf.setOpenMode(OpenMode.CREATE);
		indexWriter = new IndexWriter(index, conf);
	}
	
	public void closeIndex() throws IOException {
		//close Lucene writer
		indexWriter.close();
	}
	
	public void index() throws ClassNotFoundException, IOException {
		String st = null;
		Document doc;
		
		//index all dataset features into Lucene
		for(ImgDescriptor i: idsDataset){
			st = pivots.features2Text(i, topKIdx);
			System.out.println(i.getName());
			doc = createDoc(i, st);
			indexWriter.addDocument(doc);
		}
		//commit Lucene
		indexWriter.commit();
	}
	
	private Document createDoc(ImgDescriptor imgDes, String imgTXT) throws IOException{
		Document doc = null;
		doc = new Document();
		//Create Fields.IMG and Fields.ID and Fields.BINARY fields and add them in doc
		FieldType ft = new FieldType(StringField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS);
		Field f = new Field(Fields.ID, imgDes.getId(), ft);
		doc.add(f);
		
		ft = new FieldType(TextField.TYPE_NOT_STORED);
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		ft.setStoreTermVectors(true);
	    ft.setStoreTermVectorPositions(true);
	    ft.storeTermVectorOffsets();
		f = new Field(Fields.IMG, imgTXT, ft);
		doc.add(f);
		
		byte[] value = imgDes.toBytes();
		Field binary = new StoredField(Fields.BINARY, value);
		doc.add(binary);
		
	    return doc;
	}
}
