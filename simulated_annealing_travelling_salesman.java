import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TSP_SIMULATED_ANNEALING_1 {
	
	public static int distances [][];
    public static int subsetOfCities = 16;
    public static int shortestTourSA[];
    public static int shortestDistanceSA;
    
    public static void main(String [] args)
    {
        importFromFile();
        int inspections = 50000;
        double startTime = System.currentTimeMillis();
        SimulatedAnnealing(inspections);
        double stopTime = System.currentTimeMillis();
        System.out.print("Minimum distance Simulated Annealing tour path: ");
        System.out.println(ConvertToCities(shortestTourSA));
        System.out.println("Distance travelled: " + shortestDistanceSA);
        System.out.println("Simulated Annealing execution time (in seconds): " + ((stopTime - startTime)/1000.000000) + "\n");
            }
	   
    private static void importFromFile()
    {
	    distances = new int[subsetOfCities][subsetOfCities];
	    try
        { 	/* Input file path here */
            Scanner file = new Scanner(new File("C:\\Users\\Shankar Kumar\\Desktop\\citiesAndDistances.txt"));
            Scanner line = new Scanner(file.nextLine());
            for (int i = 0; i < subsetOfCities; ++i)
            {
                line = new Scanner(file.nextLine());
                line.next();
                for (int j = 0; j < subsetOfCities; ++j)
                {
                    distances[i][j] = line.nextInt();
                }
            }
            file.close();
            line.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
         }
    }
    
 /* Convert to string of city names */
 public static String ConvertToCities(int [] numbers)
    {
        String cities [] = new String[16];
        try
        {
            Scanner file = new Scanner(new File("C:\\Users\\Shankar Kumar\\Desktop\\citiesAndDistances.txt"));
            for (int i = 0; i < 16; i++)
            {
                cities[i] = file.next();
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
        }

        /* Map Cities */
        String mappedCities = cities[numbers[0]];
        for(int i = 1; i < numbers.length; i++)
        {
        	mappedCities = mappedCities + " to " + cities[numbers[i]];
        }
        return mappedCities;
    }
 
 /* Perform node exchange  */
 public static int [] nodeExchange(int [] start)
 {
     int tour [] = new int[subsetOfCities + 1];
     System.arraycopy(start, 0, tour, 0, tour.length);
     int pos1 = ThreadLocalRandom.current().nextInt(1, subsetOfCities);
     int pos2 = ThreadLocalRandom.current().nextInt(1, subsetOfCities);
     int city1 = tour[pos1];
     int city2 = tour[pos2];
     tour[pos1] = city2;
     tour[pos2] = city1;
     return tour;
 }

 	/* Create a default tour for the number of cities specified in subsetOfCities. Starts and ends with city 0 
     */
    public static int [] CreateDefaulttour()
    {
        int tour [] = new int[subsetOfCities + 1];
        for (int i = 0; i < subsetOfCities; i++)
        {
            tour[i] = i;
        }
        tour [subsetOfCities] = 0;
        return tour;
    }
    
    /* Calculate the distance travelled in a tour */
    public static int distanceTravelled(int [] tour)
    {
        int distance = 0;
        for(int i = 0; i < subsetOfCities; i++)
        {
            distance += distances[tour[i]][tour[i+1]];
        }
        return distance;
    }
    
    /* Improve default tour with Simulated Annealing algorithm */
    public static void SimulatedAnnealing(int inspections)
    {
    	double startTemperature = 1500;
        double temperature = startTemperature;
        int tour [] = CreateDefaulttour();
        shortestDistanceSA = distanceTravelled(tour);
        int copy [] = new int[tour.length];
        System.arraycopy(tour, 0, copy, 0, tour.length);
        shortestTourSA = copy;

        for(int i = 0; i < inspections; i++)
        {
            tour = nodeExchange(shortestTourSA);
                
            int lengthRoute = distanceTravelled(tour);
            int rand = ThreadLocalRandom.current().nextInt(0, 101);
            double difference = lengthRoute - shortestDistanceSA;
            double func = Math.exp(-difference/temperature) *100;
            
            if((lengthRoute < shortestDistanceSA) || rand <= func )
            {
                shortestDistanceSA = lengthRoute;
             }
            /* Select Cooling scheme here */
            
            /* Exponential Cooling */
            //temperature = startTemperature * (double)(java.lang.Math.pow(temperature,0.8));
            
            //geometric cooling
            //temperature = temperature * 0.95;

            /* Linear Cooling */
            temperature = temperature - (double)1500/inspections;
           
        }
            }
}
