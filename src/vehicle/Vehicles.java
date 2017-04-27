package vehicle;

import java.util.List;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.google.gson.*;

public class Vehicles 
{

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
		try 
		{
			Reader reader = new InputStreamReader(new URL("http://www.rentalcars.com/js/vehicles.json").openStream());
			Gson gson = new GsonBuilder().create();
			Search obj = gson.fromJson(reader, Search.class);
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class Search
	{
		private List<VehicleList> vehicles;
	}
	
	private class VehicleList
	{
		public String sipp;
		public String name;
		public double price;
		public String supplier;
		public double rating;
	}
}
