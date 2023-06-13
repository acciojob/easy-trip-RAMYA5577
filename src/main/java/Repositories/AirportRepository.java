package Repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Repository
public class AirportRepository {

     private Map<String,Airport> airportMap;

     private Map<Integer,Flight> flightMap;
     private Map<Integer,Passenger> passengerMap;
     private Map<Integer,List<Passenger>> listOfPassengers;  //flightId,ListOfPassengers

     private Map<Integer,Flight> passengerFlightMap;  //passengerId,flight
    public AirportRepository() {
        this.airportMap = new HashMap<String,Airport>();
        this.flightMap=new HashMap<Integer,Flight>();
        this.passengerFlightMap=new HashMap<Integer,Flight>();
        this.passengerMap=new HashMap<Integer,Passenger>();
        this.listOfPassengers=new HashMap<Integer,List<Passenger>>();
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
            if (airportMap.get(s).getNoOfTerminals() == size && s1.compareTo(s)>0){
                s1=s;
            }
            if(airportMap.get(s).getNoOfTerminals()==size && s1.compareTo(s)<0){
                s1=s1;
            }
        }

        return s1;
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

        int count=0;   //no of people who was there in an airport on a particular date
        for(int id: passengerFlightMap.keySet()){
            if(passengerFlightMap.get(id).getFromCity().toString()==airportName && passengerFlightMap.get(id).getFlightDate()==date
                    || passengerFlightMap.get(id).getToCity().toString()==airportName && passengerFlightMap.get(id).getFlightDate()==date){
                count++;
            }
        }
        return count;
    }

    public int calculateFlightFare(Integer flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        int totalFare=0;
        for (Integer id:listOfPassengers.keySet()){
            if(id==flightId){
               totalFare=3000+listOfPassengers.get(id).size()*50;
            }
        }
        return totalFare;

    }

    public String bookATicket(Integer flightId,Integer passengerId){

        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"


        for (Integer id: listOfPassengers.keySet()){
            Flight flight=flightMap.get(id);
            int capacityOfFlight= flight.getMaxCapacity();
            int passengerId1 =passengerMap.get(id).getPassengerId();
            if (capacityOfFlight<listOfPassengers.get(id).size() || passengerId1==passengerId){
                return "FAILURE";
            }
            if(capacityOfFlight>listOfPassengers.get(id).size()){
                passengerFlightMap.put(passengerId1,flight);

                capacityOfFlight-=flight.getMaxCapacity();
            }
        }
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId,Integer passengerId){

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId
        Flight flight=flightMap.get(flightId);
         for(Integer id: passengerFlightMap.keySet()){
             if(id!=passengerId || !passengerFlightMap.containsValue(flight.getFlightId())){
                 return "FAILURE";
             }
             if(passengerFlightMap.containsValue(flightId)){
                 passengerFlightMap.remove(id,flight);
             }
         }
         return "SUCCESS";
    }



    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){

        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        int count=0;
        Passenger passenger=passengerMap.get(passengerId);

        for (Integer id:passengerFlightMap.keySet()) {
            Flight flight =passengerFlightMap.get(passengerId);
            for(Integer flightId:listOfPassengers.keySet()){
                if(flightId==flight.getFlightId()){
                    count++;
                }
            }
        }

        return count;
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


    public int calculateRevenueOfAFlight(Integer flightId) {

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight
        int revenue = 0;
        int noOfPassengers=0;

        Passenger passenger=new Passenger();
        List<Passenger> passengers = new ArrayList<>();

        for (Integer id : listOfPassengers.keySet()) {
            if (id == flightId) {
                passengers = listOfPassengers.get(id);
                passenger = passengers.get(passenger.getPassengerId());
            }
            noOfPassengers = passengers.size();
            if (cancelATicket(passenger.getPassengerId(), flightId) == "SUCCESS") {
                noOfPassengers--;
            }
        }
        revenue=3000+noOfPassengers*50;
                return revenue;
        }

    public String addPassenger(Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
          passengerMap.put(passenger.getPassengerId(),passenger);
          return "SUCCESS";
    }

}




