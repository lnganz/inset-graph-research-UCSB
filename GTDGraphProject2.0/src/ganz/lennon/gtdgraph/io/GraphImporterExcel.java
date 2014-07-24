package ganz.lennon.gtdgraph.io;

import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.xssf.usermodel.*;
import org.jgrapht.*;

public class GraphImporterExcel {

	// List<PropertyVertex> addedAttributes = new ArrayList<PropertyVertex>(50);
	// Map<String, Object> addedVertices = new HashMap<String, Object>();
	Map<String, PropertyVertex> addedCorps = new HashMap<String, PropertyVertex>(1000);
	Map<String, PropertyVertex> addedGroups = new HashMap<String, PropertyVertex>(1000);
	Map<String, PropertyVertex> addedTargets = new HashMap<String, PropertyVertex>(1000);
	Map<Object, HashSet<PropertyVertex>> indexedByCountryCode = new HashMap<Object, HashSet<PropertyVertex>>(100000);
	Map<String, HashSet<PropertyVertex>> indexedByCorpName = new HashMap<String, HashSet<PropertyVertex>>(1000);
	static int numVerticesAdded = 0;

	
	public boolean importFromExcel(String filename, Graph<PropertyVertex, PropertyEdge> graph) {

		try {
			FileInputStream file = new FileInputStream(new File(filename));

			XSSFWorkbook wb = new XSSFWorkbook(file);

			Sheet sheet = wb.getSheetAt(0);

			int rowStart = sheet.getFirstRowNum() + 1;
			int rowEnd = sheet.getLastRowNum();
			int lastColumn;
			int added = 0;
			int cn;

			Cell cell;

			PropertyVertex v1, v2 = null, v3 = null,
					vg1, vg2, vg3;	//Temporary vertices
			PropertyEdge e;	//Temporary edge
			String curGroup, curTarget, curCorp, tempStr;//Temporary strings
			boolean corpAdded, unknown;
			int tempNum;
			ArrayList<PropertyVertex> tempAL;
			HashSet<PropertyVertex> tempSet;
			
			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) { // while there are more rows
				
				Row r = sheet.getRow(rowNum); // start of next row

				lastColumn = r.getLastCellNum();

				Iterator<Cell> cellIterator = r.cellIterator();
				while (cellIterator.hasNext()) { // while there are more columns

					vg1 = null;
					vg2 = null; 
					vg3 = null;
					corpAdded = false;
					cn = 0;
					cell = r.getCell(cn); // go to next column

					v1 = new PropertyVertex((long) cell.getNumericCellValue()); // new vertex with event ID
					v1.addLabel("INCIDENT");
					cell = r.getCell(++cn);

					graph.addVertex(v1); // Add new vertex to graph

					if (cell != null)
						v1.addProperty("YEAR", (int) cell.getNumericCellValue()); // Year
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("MONTH", (int) cell.getNumericCellValue()); // Month
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("DAY", (int) cell.getNumericCellValue()); // Day
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Approximate Date

					if (cell != null)
						v1.addProperty("EXTENDED_INCIDENT", (cell.getNumericCellValue() == 1)); // Extended Incident?
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Resolution

					if (cell != null){
						tempNum = (int) cell.getNumericCellValue();
						v1.addProperty("COUNTRY_CODE", tempNum); // Country Code
						if (indexedByCountryCode.containsKey(tempNum)){//If the country code is already indexed
							indexedByCountryCode.get(tempNum).add(v1);//add this vertex to that code's arraylist
						}
						else{
							tempSet = new HashSet<PropertyVertex>(10);
							tempSet.add(v1);
							indexedByCountryCode.put((int)tempNum, tempSet);
						}
							
					}
					cell = r.getCell(++cn);

					// if (cell != null)
					// v1.addProperty("COUNTRY_NAME", cell.getStringCellValue()); // Country Name
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("REGION", (int) cell.getNumericCellValue()); // Region Code
					cell = r.getCell(++cn);

					// if (cell != null)
					// v1.addProperty("REGION_NAME", cell.getStringCellValue()); // Region Name
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROVIDENCE/STATE", cell.getStringCellValue()); // Providence/State Name
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CITY", cell.getStringCellValue()); // City Name
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("LATITUDE", cell.getStringCellValue()); // Latitude
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("LONGITUDE", cell.getStringCellValue()); // Longitude
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("SPECIFICITY", (int) cell.getNumericCellValue()); // Specificity
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("VICINITY", (int) cell.getNumericCellValue()); // Vicinity
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("LOCATION", cell.getStringCellValue()); // Location
					cell = r.getCell(++cn);

					// if (cell != null)
					// v1.addProperty("SUMMARY", cell.getStringCellValue()); // String summary of incident
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CRITERIA_1", (int) cell.getNumericCellValue()); // Political, Economic, Religious...
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CRITERIA_2", (int) cell.getNumericCellValue()); // Intention to coerce, intimidate...
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CRITERIA_3", (int) cell.getNumericCellValue()); // Outstide International Humanitarian Law
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("DOUBT_TERRORISM_PROPER", (int) cell.getNumericCellValue()); // Uncertain whether an act of
																					// terrorism?
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Alternative Designation

					cell = r.getCell(++cn); // Alternative Text

					if (cell != null)
						v1.addProperty("MULTIPLE_INCIDENT", (int) cell.getNumericCellValue()); // Part of Multiple Incident
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("SUCCESS", (int) cell.getNumericCellValue()); // Successful attack?
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("SUICIDE", (int) cell.getNumericCellValue()); // Suicide attack?
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Attacktype1 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("ATTACK_TYPE_1", cell.getStringCellValue()); // Attacktype1 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Attacktype2 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("ATTACK_TYPE_2", cell.getStringCellValue()); // Attacktype2 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Attacktype3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("ATTACK_TYPE_3", cell.getStringCellValue()); // Attacktype3 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Targettype1 code

					if (cell != null)
						v1.addProperty("TARGET_TYPE_1", cell.getStringCellValue()); // Targettype1 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Targetsubtype1 code

					if (cell != null)
						v1.addProperty("TARGET_SUBTYPE_1", cell.getStringCellValue()); // Targetsubtype1 string
					cell = r.getCell(++cn);

					if (cell != null) {
						curCorp = cell.getStringCellValue();
						if (!curCorp.equals("")){
						if (indexedByCorpName.containsKey(curCorp)){//If the corp name is already indexed
							indexedByCorpName.get(curCorp).add(v1);//add this vertex to that corp's set
						}
						else{
							tempSet = new HashSet<PropertyVertex>(10);
							tempSet.add(v1);
							indexedByCorpName.put(curCorp, tempSet);
						}
						unknown = curCorp.equals("Unknown");
						if (!unknown && addedCorps.containsKey(curCorp)) {
							v2 = addedCorps.get(curCorp);
						} else {
							v2 = new PropertyVertex(++numVerticesAdded);
							graph.addVertex(v2);
							if (!unknown)
								addedCorps.put(curCorp, v2);
							v2.addProperty("NAME", curCorp);
							v2.addLabel("CORPORATION");
						}
						v3 = v2;
						e = graph.addEdge(v1, v2);
						e.addLabel("TARGET_CORPORATION");
						corpAdded = true;
//						System.out.println("CORP: " + curCorp);
						}else
							v3 = null;
					}
					// v1.addProperty("CORP_1", cell.getStringCellValue()); // Corporation1
					cell = r.getCell(++cn);

					if (cell != null) {
						curTarget = cell.getStringCellValue();
						unknown = curTarget.equals("Unknown");
						if (!unknown && addedTargets.containsKey(curTarget)) {
							v2 = addedTargets.get(curTarget);
						} else {
							v2 = new PropertyVertex(++numVerticesAdded);
							graph.addVertex(v2);
							if (!unknown)
								addedTargets.put(curTarget, v2);
							v2.addProperty("NAME", curTarget);
							v2.addLabel("TARGET");
						}
						e = graph.addEdge(v1, v2);
						e.addLabel("TARGET");
						if (v3 != null){
						e = graph.addEdge(v3, v2);
						e.addLabel("SUBTARGET_OF");
						}
//						System.out.println("Target: " + curTarget);

					}
					// v2 = addPropertyEdgeAndVertex(g, v1, "TARGET", cell.getStringCellValue(), "TARGETED");
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Nationality code

					if ((cell != null) && (v2 != null) && (!v2.hasProperty("TARGET_NATIONALITY")))
						if (!cell.getStringCellValue().equals("."))
						v2.addProperty("TARGET_NATIONALITY", cell.getStringCellValue());
					// v1.addProperty("NATIONALITY_1", cell.getStringCellValue()); // Nationality1 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // TargetType2 code
					
					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_TYPE_2", cell.getStringCellValue()); // Targettype2 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Targetsubtype2 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_SUBTYPE_2", cell.getStringCellValue()); // Targetsubtype2 string
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CORPORATION_2", cell.getStringCellValue()); // Corporation2
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("TARGET_2", cell.getStringCellValue()); // Target2 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Nationality code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_2_NATIONALITY", cell.getStringCellValue()); // Nationality2 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Targettype3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_TYPE_3", cell.getStringCellValue()); // Targettype3 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Targetsubtype3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_SUBTYPE_3", cell.getStringCellValue()); // Targetsubtype3 string
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("CORPORATION_3", cell.getStringCellValue()); // Corporation3
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("TARGET_3", cell.getStringCellValue()); // Target3 string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // Nationality3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("TARGET_3_NATIONALITY", cell.getStringCellValue()); // Nationality3 string
					cell = r.getCell(++cn);

					curGroup = null;
					if (cell != null) { // PERPETRATOR Group NAME
						curGroup = cell.getStringCellValue();
						if (!curGroup.equals("")){
							if (addedGroups.containsKey(curGroup) && !(curGroup.equals("Unknown"))) {
								v2 = addedGroups.get(curGroup);
							} else {
								v2 = new PropertyVertex(++numVerticesAdded);
								graph.addVertex(v2);
								addedGroups.put(curGroup, v2);
								v2.addProperty("NAME", curGroup);
								v2.addLabel("TGROUP");
							}
							e = graph.addEdge(v1, v2);
							e.addLabel("PERPETRATED_BY"); //Edge relating incident to group
							e = graph.addEdge(v2, v1);
							e.addLabel("PERPETRATED");	//Edge relating group to incident
							vg1 = v2;
//							System.out.println("Group: " + curGroup);
						}
					}

					cell = r.getCell(++cn);

					if ((cell != null) && (vg1 != null) && (!vg1.hasProperty("GROUP_SUBNAME"))) // PERPETRATOR Group
						if (!cell.getStringCellValue().equals(""))								// subname
						v2.addProperty("GROUP_SUBNAME", cell.getStringCellValue());
					cell = r.getCell(++cn);
					
					curGroup = null;
					if (cell != null) { // PERPETRATOR Group NAME 2222222
						curGroup = cell.getStringCellValue();
						if (!curGroup.equals("")){
							if (addedGroups.containsKey(curGroup) && !(curGroup.equals("Unknown"))) {
								v2 = addedGroups.get(curGroup);
							} else {
								v2 = new PropertyVertex(++numVerticesAdded);
								graph.addVertex(v2);
								addedGroups.put(curGroup, v2);
								v2.addProperty("NAME", curGroup);
								v2.addLabel("SUBGROUP");
							}
							e = graph.addEdge(v1, v2);
							e.addLabel("PERPETRATED_BY"); //Edge relating incident to group
							e = graph.addEdge(v2, v1);
							e.addLabel("PERPETRATED");	//Edge relating group to incident
							e = graph.addEdge(v2, vg1);
							e.addLabel("COLAB_WITH");
							e = graph.addEdge(vg1, v2);
							e.addLabel("COLAB_WITH");
							vg2 = v2;
	//						System.out.println("Group: " + curGroup);
						}
					}
					cell = r.getCell(++cn);

					if ((cell != null) && (vg2 != null) && (!vg2.hasProperty("GROUP_SUBNAME"))) // PERPETRATOR Group
						// subname
						if (!cell.getStringCellValue().equals(""))
						v2.addProperty("GROUP_SUBNAME", cell.getStringCellValue());
					cell = r.getCell(++cn); // gsubname2

					curGroup = null;
					if (cell != null) { // PERPETRATOR Group NAME 3333333
						curGroup = cell.getStringCellValue();
						if (!curGroup.equals("")){
						if (addedGroups.containsKey(curGroup) && !(curGroup.equals("Unknown"))) {
							v2 = addedGroups.get(curGroup);
						} else {
							v2 = new PropertyVertex(++numVerticesAdded);
							graph.addVertex(v2);
							addedGroups.put(curGroup, v2);
							v2.addProperty("NAME", curGroup);
							v2.addLabel("TGROUP");
						}
						e = graph.addEdge(v1, v2);
						e.addLabel("PERPETRATED_BY"); //Edge relating incident to group
						e = graph.addEdge(v2, v1);
						e.addLabel("PERPETRATED");	//Edge relating group to incident
						e = graph.addEdge(v2, vg1);
						e.addLabel("COLAB_WITH");
						e = graph.addEdge(vg1, v2);
						e.addLabel("COLAB_WITH");
						e = graph.addEdge(v2, vg2);
						e.addLabel("COLAB_WITH");
						e = graph.addEdge(vg2, v2);
						e.addLabel("COLAB_WITH");
						vg3 = v2;
//						System.out.println("Group: " + curGroup);
					}
					}
					cell = r.getCell(++cn);

					if ((cell != null) && (vg3 != null) && (!vg3.hasProperty("GROUP_SUBNAME"))) // PERPETRATOR Group
						// subname
						if (!cell.getStringCellValue().equals(""))
						v2.addProperty("GROUP_SUBNAME", cell.getStringCellValue());
					cell = r.getCell(++cn); // gsubname3

					if (cell != null)
						v1.addProperty("MOTIVE", cell.getStringCellValue()); // String description of motive
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("GROUP_UNCERTAINTY", cell.getNumericCellValue()); // Uncertainty of group's
																							// involvment
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // g uncertainty 2

					cell = r.getCell(++cn); // g uncertainty 3

					if (cell != null)
						v1.addProperty("#_PERPETRATORS", (int) cell.getNumericCellValue()); // # perpetrators
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_PERPETRATORS_CAPTURED", (int) cell.getNumericCellValue()); // # perps captured
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("GROUP_1_CLAIM_RESPONSIBILITY", cell.getNumericCellValue());
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // claimmode code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("GROUP_1_CLAIM_MODE", cell.getStringCellValue()); // claimmode string
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // claim2
					cell = r.getCell(++cn); // claimmode2 code
					cell = r.getCell(++cn); // claimmode2 string
					cell = r.getCell(++cn); // claimmode3
					cell = r.getCell(++cn); // claimmode3 code
					cell = r.getCell(++cn); // claimmode3 string
					cell = r.getCell(++cn); // competing claims?

					cell = r.getCell(++cn); // weapon type 1 code

					// if (cell != null)
					// v2 = addPropertyEdgeAndVertex(g, v1, "WEAPON_TYPE", cell.getStringCellValue(), "WEAPON_USED");
					// cell = r.getCell(++cn);

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_TYPE_1", cell.getStringCellValue());
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // weapon subtype code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_SUBTYPE_1", cell.getStringCellValue());
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // weapon type 2 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_TYPE_2", cell.getStringCellValue()); // weapon type 2
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // subtype 2 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_SUBTYPE_2", cell.getStringCellValue()); // weapon subtype 2
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // weapon type 3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_TYPE_3", cell.getStringCellValue()); // weapon type 3
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // subtype 3 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_SUBTYPE_3", cell.getStringCellValue()); // weapon subtype 3
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // weapon type 4 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_TYPE_4", cell.getStringCellValue()); // weapon type 4
					cell = r.getCell(++cn);

					cell = r.getCell(++cn); // subtype 4 code

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("WEAPON_SUBTYPE_4", cell.getStringCellValue()); // weapon subtype 4
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("WEAPON_DETAIL", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_KILLED", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_KILLED_US", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_KILLED_PERPETRATORS", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_WOUNDED", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_WOUNDED_US", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_WOUNDED_PERPETRATORS", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROPERTY_DAMAGE", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROPERTY_EXTENT", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROPERTY_EXTENT_SUMMARY", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROPERTY_VALUE", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("PROPERTY_COMMENT", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("HOSTAGE/KIDNAPPING", cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_HOSTAGES/KIDNAPPED", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_HOSTAGES/KIDNAPPED_US", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_HOURS", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_DAYS", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("DIVERT_COUNTRY", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RESOLUTION_COUNTRY", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM_AMOUNT", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM_AMOUNT_US", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM_PAID", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM_PAID_US", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("RANSOM_NOTE", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("HOSTKID_OUTCOME", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						if (!cell.getStringCellValue().equals("."))
						v1.addProperty("HOSTKID_OUTCOME_TXT", cell.getStringCellValue());
					cell = r.getCell(++cn);

					if (cell != null)
						v1.addProperty("#_HOSTKID_RELEASED", (int) cell.getNumericCellValue());
					cell = r.getCell(++cn);	

					break;

				}
				
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Map<Object, HashSet<PropertyVertex>> getIndexCountryCode(){
		return indexedByCountryCode;
	}
	
	public Map<String, HashSet<PropertyVertex>> getIndexCorpName(){
		return indexedByCorpName;
	}
}
