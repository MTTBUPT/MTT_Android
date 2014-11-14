package com.mtt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;


public class MapDatas {
	private String SDPATH;
	//private int i=0;
	File file;
	public String getSDPATH(){
		SDPATH=Environment.getExternalStorageDirectory()+"/";
		System.out.println("sdpath is "+SDPATH);
		return SDPATH;
	}
	public File creatFile(String fileName) throws IOException{
		File file=new File(getSDPATH()+fileName);
		file.createNewFile();
		return file;
	}
	
	public boolean ifFileExist(String fileName){
		File file=new File(getSDPATH()+fileName);
		return file.exists();
	}
	
	public void write2SD(File file,String data) throws IOException{
		OutputStream output=null;
		output=new FileOutputStream(file,true);
		byte buffer [] = new byte[4 * 1024];
		data = data + "\n";
		buffer=data.getBytes();
		output.write(buffer);
		System.out.println();
		output.flush();
	}
}
