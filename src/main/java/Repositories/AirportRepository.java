package Repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Repository
public class AirportRepository {

     private Map<String,Airport> airportMap;

     private Map<Integer,Flight> flightMap;


    public AirportRepository() {
        this.airportMap = new HashMap<String,Airport>();
        this.flightMap=new HashMap<Integer,Flight>();

    }

    public String addAirport(Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"
            airportMap.put(airport.getAirportName(),airport);
            return "SUCCESS";
    }

    public String getLargestAirportName() {

        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName
        Airport airport=new Airport();
        int size=0;
        String s1="";
        for( String s: airportMap.keySet()) {
            if (airportMap.get(s).getNoOfTerminals() > size) {
                size = airportMap.get(s).getNoOfTerminals();
                s1 = s;
            }
            if (airportMap.get(s).getNoOfTerminals() == size) {
                s1 = lexicography(s1, s, 0);
            }
        }

        return s1;
    }

    public String lexicography(String s1,String s2,int a) {
        if (s1.charAt(a) > s2.charAt(a))
            return s2;
        if (s1.charAt(a) < s2.charAt(a))
            return s1;
        else {
            a++;
            return lexicography(s1, s2, a);

        }
    }

        public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){

            //Find the duration by finding the shortest flight that connects these 2 cities directly
            //If there is no direct flight between 2 cities return -1.
           for (Integer id: flightMap.keySet()){
               if(flightMap.get(id).getFromCity()==fromCity && flightMap.get(id).getToCity()==toCity){
                   return flightMap.get(id).getDuration();
               }
           }
            return -1;
        }

    public int getNumberOfPeopleOn(Date date, String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
//        Flight flight=new Flight();
        int count=0;

        return 0;
    }

    public int calculateFlightFare(Integer flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        return 0;

    }

    public String bookATicket(Integer flightId,Integer passengerId){

        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"

        return null;
    }

    public String cancelATicket(Integer flightId,Integer passengerId){

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        return null;
    }


    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){

        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        return 0;
    }

    public String addFlight(Flight flight){

        //Return a "SUCCESS" message string after adding a flight.
        flightMap.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }


    public String getAirportNameFromFlightId(Integer flightId){

        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName
        for(int i: flightMap.keySet()){
            if (flightMap.containsKey(flightId)) {
                return flightMap.get(flightId).getFromCity().toString();
            }
        }
        return null;
    }


    public int calculateRevenueOfAFlight(Integer flightId){

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        return 0;
    }

    public String addPassenger(Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.

        return null;
    }

}




