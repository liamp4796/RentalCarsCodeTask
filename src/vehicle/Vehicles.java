package vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Vehicles 
{

	public static void main(String[] args) 
	{
		JSONParser parser = new JSONParser();
		try 
		{
			// Read json from URL then get the list of all vehicles.
			Object obj = parser.parse(new InputStreamReader(new URL("http://www.rentalcars.com/js/vehicles.json").openStream()));
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject search = (JSONObject) jsonObj.get("Search");
			JSONArray vehicles = (JSONArray) search.get("VehicleList");
			
			System.out.println("-------------Prices-----------------");
			sortByPrice(vehicles);
			System.out.println("\n-------------Specifications-----------------------------------");
			specsBySIPP(vehicles);
			System.out.println("\n-------Best Supplier by Car Type--------------------------");
			bestSuppliers(vehicles);
			System.out.println("\n--------------Scores------------------------");
			carScores(vehicles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // main
	
	// Sort prices by ascending order and print 
	private static void sortByPrice(JSONArray array)
	{
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for (int i=0; i < array.size(); i++)
		{
			jsonList.add((JSONObject) array.get(i));
		}
		
		Collections.sort(jsonList, new Comparator<JSONObject>()
		{
			private static final String KEY_NAME = "price";
			
			public int compare(JSONObject a, JSONObject b)
			{
				double valA = 0;
				double valB = 0;
				
				try 
				{
					valA = (double) a.get(KEY_NAME);
					valB = (double) b.get(KEY_NAME);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (valA > valB)
					return 1;
				if(valA < valB)
					return -1;
				return 0;
			}
		});
		for (int i=0; i < jsonList.size(); i++)
		{
			System.out.println((i+1) + ". " + jsonList.get(i).get("name") + "-" + jsonList.get(i).get("price"));
		}
	} // sort by price ascending order
	
	// Print the specs of the car based on the SIPP
	private static void specsBySIPP(JSONArray array)
	{
		for (int i=0; i < array.size(); i++)
		{
			JSONObject vehicle = (JSONObject) array.get(i);
			System.out.print((i+1) + ". " + vehicle.get("name") + "-" + vehicle.get("sipp") + "-");
			String sipp = (String) vehicle.get("sipp");
			getSIPPDetails(sipp);
		}
	} // specsBySIPP
	
	// Get car type based on SIPP
	private static String getCarType(char s)
	{
		switch (s)
		{
			case 'M': return "Mini";
			case 'E': return "Economy";
			case 'C': return "Compact";
			case 'I': return "Intermediate";
			case 'S': return "Standard";
			case 'F': return "Full Size";
			case 'P': return "Premium";
			case 'L': return "Luxury";
			case 'X': return "Special";
		} // switch
		return "Car Type not Found";
	}
	
	// Print the details of the SIPP
	private static void getSIPPDetails(String sipp)
	{
		System.out.print(getCarType(sipp.charAt(0)) + "-");
		
		switch (sipp.charAt(1))
		{
			case 'B': System.out.print("2 Doors-");
				break;
			case 'C': System.out.print("4 Doors-");
				break;
			case 'D': System.out.print("5 Doors-");
				break;
			case 'W': System.out.print("Estate-");
				break;
			case 'T': System.out.print("Convertible-");
				break;
			case 'F': System.out.print("SUV-");
				break;
			case 'P': System.out.print("Pick Up-");
				break;
			case 'V': System.out.print("Passenger Van-");
				break;
		} // switch	
		
		if (sipp.charAt(2) == 'M')
			System.out.print("Manual-");
		else
			System.out.print("Automatic-");
		
		if (sipp.charAt(3) == 'N')
			System.out.print("Petrol-No A/C\n");
		else
			System.out.print("Petrol-A/C\n");
	} // getSIPPDetails
	
	private static void bestSuppliers(JSONArray array)
	{
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for (int i=0; i < array.size(); i++)
		{
			jsonList.add((JSONObject) array.get(i));
		}
		
		Collections.sort(jsonList, new Comparator<JSONObject>()
		{	
			public int compare(JSONObject a, JSONObject b)
			{
				String valA = new String();
				String valB = new String();
				double numA = 0;
				double numB = 0;
				try 
				{
					valA = (String) a.get("sipp");
					valB = (String) b.get("sipp");
					numA = ((Number) a.get("rating")).doubleValue();
					numB = ((Number) b.get("rating")).doubleValue();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				int sComp = valA.compareTo(valB);
				
				if (sComp != 0)
					return sComp;
				else
				{
					if (numA < numB)
						return 1;
					if(numA > numB)
						return -1;
					return 0;
				}
			}
		});
		
		char lastUsed = 'a';
		int count = 1;
		for (int i=0; i < jsonList.size(); i++)
		{
			String sipp = (String) jsonList.get(i).get("sipp");
			char s = sipp.charAt(0);
			if (s != lastUsed)
			{
				System.out.println(count + ". " + jsonList.get(i).get("name") + "-" + getCarType(sipp.charAt(0)) + "-"
									+ jsonList.get(i).get("supplier") + "-" + jsonList.get(i).get("rating"));
				lastUsed = s;
				count++;
			} // if
		} // for
	} // bestSuppliers
	
	private static void carScores(JSONArray array)
	{
		List<VehicleScore> scores = new ArrayList<VehicleScore>();
		
		for (int i=0; i < array.size(); i++)
		{
			int vScore = 0;
			JSONObject jsonObj = (JSONObject) array.get(i);
			String name = new String();
			String sipp = new String();
			double suppScore = 0;
			try
			{
				name = (String) jsonObj.get("name");
				sipp = (String) jsonObj.get("sipp");
				suppScore = ((Number) jsonObj.get("rating")).doubleValue();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if (sipp.charAt(2) == 'M')
				vScore++;
			else
				vScore += 5;
			
			if (sipp.charAt(3) == 'R')
				vScore += 2;
			
			scores.add(new VehicleScore(name, vScore, suppScore));
		}
		
		Collections.sort(scores, new Comparator<VehicleScore>()
		{
			public int compare(VehicleScore v1, VehicleScore v2)
			{
				if (v1.getTotalScore() < v2.getTotalScore())
					return 1;
				if (v1.getTotalScore() > v2.getTotalScore())
					return -1;
				return 0;
			}
		});
		
		for (int i=0; i < scores.size(); i++)
		{
			System.out.println((i+1) + ". " + scores.get(i).toString()); 
		}
	} // carScores
}