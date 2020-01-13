package com.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public class Dumper {
	String rootURL;
	String format;
	String languages_f;
	String output_dir;
	
	public static void main(String[] args) throws Exception {
		Dumper d = new Dumper();
		d.rootURL = args[0];//"http://downloads.dbpedia.org/2016-10/core-i18n/";
		d.languages_f = args[1];// "/home/renzo/dbpedia-downloads/langs.txt";
		d.format = args[2];//"ttl.bz2";
		d.output_dir = args[3];//"/home/renzo/dbpedia-downloads";
		
		d.pipeline();
	}
	
	
	public void pipeline() throws Exception {	
		List<String> langs = getLangs(languages_f);
		
		for(String lang : langs) {
			String lang_dir = output_dir + "/" + lang;
			if(!new File(lang_dir).exists())
				new File(lang_dir).mkdir();
			
			List<String> urls = getURLS(rootURL + lang);
			for (String url : urls) {
				String file_name = url.substring(url.lastIndexOf("/") + 1, url.indexOf("." + format)) + "." + format;
				String compress_f = lang_dir + "/" + file_name;
				
				System.out.println("downloading " + lang + "---> " + url);
				download(url, compress_f);
				
				System.out.println("appending...");
				unzip(compress_f, output_dir + "/dbpedia-" + lang + ".nt");
			}
			new File(lang_dir).delete();
		}
		System.out.println("done");
	}
	
	
	// legge il file con le lingue da considerare e li mette in lista
	public List<String> getLangs(String file) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) 
			if(!line.contains("#") && !line.equals("")) 
				list.add(line.substring(0, line.indexOf("/")));
		br.close();
		return list;
	}
	
	
	//data una lingua estrae i links dei pezzi della pagina dbpedia corrispondente
	public List<String> getURLS(String url) throws IOException{
        URLConnection urlConnection = new URL(url).openConnection();
        new URL(url).openConnection().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        List<String> urls = new ArrayList<String>();
        
       
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
		String inputLine;

		urls = new ArrayList<String>();
		while ((inputLine = bufferedReader.readLine()) != null) {
			if (inputLine.contains(format)) {
				String link = inputLine.substring(inputLine.indexOf("href=\"") + 6, inputLine.indexOf("\">"));
				urls.add(url + "/" + link);
			}
		}
        
        return urls;
    } 

	
	//scarica file 
	public void download(String FILE_URL, String FILE_NAME) throws MalformedURLException, IOException {
		BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
		Files.copy(in, Paths.get(FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
	}
	
	
	//decomprime file e lo appende al dataset
	public void unzip(String input, String output) throws Exception {
	    FileInputStream in = new FileInputStream(input);
	    FileOutputStream out = new FileOutputStream(output, true);
	    BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
	    final byte[] buffer = new byte[10000000];
	    int n = 0;
	    
	    while (-1 != (n = bzIn.read(buffer))) 
	    	out.write(buffer, 0, n);
	    out.close();
	    bzIn.close();
	    new File(input).delete();
	}


}
