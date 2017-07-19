package it.unipi.ing.mim.deep;

import java.io.File;

public class Parameters {
	
	//DEEP parameters
	public static final String DEEP_PROTO = "data/caffe/train_val.prototxt";//PER DEBONIS: "data/caffe/debonis/deploy.prototxt"; //PER VGG : "data/caffe/VGG_ILSVRC_16_layers_deploy.prototxt";
	public static final String DEEP_MODEL = "data/caffe/bvlc_reference_caffenet.caffemodel"; // PER DEBONIS: "data/caffe/debonis/snapshot_iter_106620.caffemodel"; //PER VGG : "data/caffe/VGG_ILSVRC_16_layers.caffemodel";
	public static final String DEEP_MEAN_IMG = "data/caffe/meanImage.png"; // PER DEBONIS: "data/caffe/debonis/mean.png";
	
	public static final String DEEP_LAYER = "fc7"; //PER DEBONIS: "softmax"
	public static final int IMG_WIDTH = 227;
	public static final int IMG_HEIGHT = 227;
	
	//Image Source Folder
	public static final File SRC_FOLDER = new File("data/img_full");
	
	//Features Storage File
	public static final File STORAGE_FILE = new File("data/deep.seq.dat");//PER DEBONIS: new File("data/deep.seq.debonis.dat");  // PER VGG: ("data/deep.seq.vgg.dat");
	
	//k-Nearest Neighbors returned by Lucene
	public static final int K = 100;
	
	//Among the K images returned, the best 30 according to the real distance are returned.
	public static final int K_REORDER = 30;
	
	//Pivots File
	public static final File  PIVOTS_FILE = new File("out/deep.pivots.dat"); // PER DEBONIS: new File("out/deep.pivots.debonis.dat"); // PER VGG: ("out/deep.pivots.vgg.dat");
	
	//Number Of Pivots
	public static final int NUM_PIVOTS = 1000;

	//Top K pivots For Indexing
	public static final int TOP_K_IDX = 50;
	
	//Top K pivots For Searching
	public static final int TOP_K_QUERY = 44;
	
	//Lucene Index
	public static final String  LUCENE_PATH = "out/"  +"Lucene_Deep" ;   // PER VGG : "Lucene_Deep_vgg";  
	
	//HTML Output Parameters
	public static final  String BASE_URI = "file:///" + Parameters.SRC_FOLDER.getAbsolutePath() + "/";
	public static final File RESULTS_HTML = new File("out/deep.seq.html");
	public static final File RESULTS_HTML_LUCENE = new File("out/deep.lucene.html");
	public static final File RESULTS_HTML_REORDERED = new File("out/deep.reordered.html");

}
