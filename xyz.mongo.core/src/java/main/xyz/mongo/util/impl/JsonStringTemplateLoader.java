
package xyz.mongo.util.impl;  
  
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.TemplateLoader;
  
 
public class JsonStringTemplateLoader implements TemplateLoader {  
    
    private static final Map<String,String> TEMP_MAP=new HashMap();
      
    
    private String toJson(String template){
    	StringBuilder sb=new StringBuilder();
    	int hasD=-1;
    	int hasK=-1;
    	for(int i=0,len=template.length();i<len;i++){
    		String now=template.substring(i, i+1);
    		if("'".equals(now)){
    			sb.append("\"");
    		}else if(hasD!=-1&&hasK==(hasD+1)&&"}".equals(now)){
    			sb.append("?json_string}");
    			hasD=-1;
    			hasK=-1;
    		}else{
    			sb.append(now);
    			if("$".equals(now)){
    				hasD=i;
    			}
    			if("{".equals(now)){
    				hasK=i;
    			}
    		}
    	}
    	String res=sb.toString();
    	return res;
    }
    
    public void addTemplate(String keyAndValue){
    	if(null==keyAndValue)throw new IllegalArgumentException("can't add a null template to json string template");
    	if(TEMP_MAP.containsKey(keyAndValue)){
    		return;
    	}
    	String realValue=toJson(keyAndValue);
    	TEMP_MAP.put(keyAndValue, realValue);
    }
      
    public void closeTemplateSource(Object templateSource) throws IOException {  
        ((StringReader) templateSource).close();  
    }  
  
    public Object findTemplateSource(String name) throws IOException { 
    	addTemplate(name);
        return new StringReader(TEMP_MAP.get(name));  
    }  
  
    public long getLastModified(Object templateSource) {  
        return 0;  
    }  
  
    public Reader getReader(Object templateSource, String encoding) {
       // return new StringReader((String)templateSource);
    	return (Reader)templateSource;
    }
  
}  
