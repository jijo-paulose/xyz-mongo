package xyz.mongo.ds.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionsResource;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonColsResource implements ICollectionsResource {
	private static final Log LOG = LogFactory.getLog(JsonColsResource.class);

	private static final String[] ext = { "json" };

	private String jsonDir;

	private boolean check = true;
	
	private String basetable="basetable";
    //运行时不改，hashmap比concurrent快，没有线程安全问题
	private Map<String,  DBObject> map = new HashMap();
	
	public DBObject methodQuery(String colName,String methodName){
		//Map<String,DBObject> dbQuerys=collectionsResource.getQuerys();
		DBObject tableQuerys=map.get(colName);
		//如果一个也没写，支持找到basetable.json
		if(null==tableQuerys){
			tableQuerys=map.get(basetable);
			if(LOG.isDebugEnabled()){
				LOG.debug("now ds for table "+basetable);
			}
		}else{
			if(LOG.isDebugEnabled()){
				LOG.debug("now ds for table "+colName);
			}
		}
		if(null==tableQuerys){
			throw new IllegalStateException("can't find tableQuerys for table :"+colName);
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("now ds for method "+methodName);
		}
		DBObject methodQuery=(DBObject)tableQuerys.get(methodName);
		if(null==methodQuery){
			tableQuerys=map.get(basetable);
			methodQuery=(DBObject)tableQuerys.get(methodName);
		}
		
		if(null==methodQuery){
			throw new IllegalStateException("can't find methodQuery for table :"+colName+" & method:"+methodName);
		}
		return methodQuery;
	}

	public void setJsonDir(String jsonDir) {
		this.jsonDir = jsonDir;
		String root=this.getClass().getResource("/").getPath()+"/"; //添加一个去掉绝对路径，反正是放在classpath下的
		File directory = new File(root+jsonDir);
		for (Iterator<File> it = FileUtils.iterateFiles(directory, ext, false); it
				.hasNext();) {
			File realFile = it.next();
			try {
				oneFile(realFile);
			} catch (Throwable e) {
				LOG.error("error while trans file:"
						+ realFile.getAbsolutePath() + " to json", e);
			}
		}
	}

	private void oneFile(File file) throws Exception {
		String fileName = file.getName();
		if (LOG.isDebugEnabled()) {
			LOG.debug("now trans file to json 4 " + fileName);
		}
		InputStream input = new FileInputStream(file);
		String text = IOUtils.toString(input, "UTF-8");
		DBObject myQuerys = (DBObject)JSON.parse(text);

		int dot = fileName.indexOf(".json");
		if (dot != -1) {
			fileName = fileName.substring(0, dot);
		}

		for (String method : myQuerys.keySet()) {
			DBObject query=(DBObject)myQuerys.get(method);
			if (check) {
				String comment = (String)query.get("comment");
				if (null == comment ) {
					throw new RuntimeException("comment 4 method " + method
							+ " in file " + fileName + ".json ");
				}
			}
		}
		// 最后，加到map里面
		map.put(fileName, myQuerys);
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public void setBasetable(String basetable) {
		this.basetable = basetable;
	}
}
