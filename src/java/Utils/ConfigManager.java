/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private InputStream inputStream;
	
	public Properties getConfigFile(String fileName) throws IOException{
		Properties prop = new Properties();
		inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		if(inputStream != null){
			prop.load(inputStream);
			return prop;
		}
		else{
			throw new FileNotFoundException("Property file " + fileName + "not found");
		}
	}
}
