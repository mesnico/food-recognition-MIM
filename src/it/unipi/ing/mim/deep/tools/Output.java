package it.unipi.ing.mim.deep.tools;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Output {

	public static final int COLUMNS = 5;
	
	public static String generateHtmlResultsTable(List<ImgDescriptor> ids, String baseURI){
		String html = "<table align='center'>\n";

		for (int i = 0; i < ids.size(); i++) {
			System.out.println(i + " - " + (float) ids.get(i).getDist() + "\t" + ids.get(i).getId() );
			
			if (i % COLUMNS == 0) {
				if (i != 0)
					html += "</tr>\n";
				html += "<tr>\n";
			}
			html += "<td><img align='center' border='0' height='160' title='"+ ids.get(i).getName() + ", dist: "
			        + ids.get(i).getDist() + "' src='data/img_full/"  + ids.get(i).getName()+ "/" + ids.get(i).getId() + "'></td>\n";
		}
		if (ids.size() != 0)
			html += "</tr>\n";

		html += "</table>";
		
		return html;
	}
}
