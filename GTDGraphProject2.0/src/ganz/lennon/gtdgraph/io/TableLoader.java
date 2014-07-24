package ganz.lennon.gtdgraph.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TableLoader {

	private String COUNTRY_CODE_FILENAME = "GTDCountryCodes.txt";
	private String WEAPON_CODE_FILENAME = "GTDWeaponSubtypeCodes.txt";
	
	String countries[] = new String[1010];
	String weapon_subtypes[] = new String[30];
	
	public String[] loadCountryCodes(){
		return loadCodes(COUNTRY_CODE_FILENAME, countries);
	}
	
	public String[] loadWeaponSubtypes(){
		return loadCodes(WEAPON_CODE_FILENAME, weapon_subtypes);
	}
	
	//Load strings from a formated file into an array
	public String[] loadCodes(String filename, String[] values){
		try {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;
		String temp[];
			while ((line = reader.readLine()) != null) {
			    temp = line.split("=");
			    values[Integer.parseInt(temp[0])] = temp[1].trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	return values;
	}
}
