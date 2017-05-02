package vehicle;

public class VehicleScore 
{
	private String name;
	private int vScore;
	private double suppScore;
	private double total;
	
	public VehicleScore(String car, int score1, double score2)
	{
		name = car;
		vScore = score1;
		suppScore = score2;
		total = score1 + score2;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getVScore()
	{
		return vScore;
	}
	
	public double getSuppScore()
	{
		return suppScore;
	}
	
	public double getTotalScore()
	{
		return total;
	}
	
	public String toString()
	{
		return name + " - " + vScore + " - " + suppScore + " - " + total;
	}
}
