package TravelPlanBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanBuilder {

    List<Place> places = new ArrayList<>();
    List<Map<Place, Double>> maps = new ArrayList<>();

    public void addPlace(Place place, Map<Place, Double> neighborMap){
        places.add(place);
        maps.add(neighborMap);
    }

    public void graphBuilder (List<Place> places, List<Map<Place, Double>> maps){
        for (int i = 0; i < places.size(); i++){
            Place place= places.get(i);
            Map<Place, Double> neighborMap = maps.get(i);
            for(Map.Entry<Place, Double> entry: neighborMap.entrySet()){
                place.addAdjacentPlace(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setDefault(List<Place> places) {
        for (Place place: places){
            place.setVisited(false);
            place.setCommuteTime(Double.MAX_VALUE);
        }
    }

    public static void main(String[] args) {

        PlanBuilder plan = new PlanBuilder();

        // get information
        Place A = new Place("A", 8, 18, 4);
        Place B = new Place("B", 10,22, 4);
        Place C = new Place("C", 12, 17, 4);
        Place D = new Place("D", 14, 24, 2);

        // get information
        Map<Place, Double> mapA = new HashMap<>();
        mapA.put(B,0.3);
        mapA.put(C,0.4);
        mapA.put(D,0.7);

        Map<Place, Double> mapB = new HashMap<>();
        mapB.put(A,0.3);
        mapB.put(C,0.4);
        mapB.put(D,0.5);

        Map<Place, Double> mapC = new HashMap<>();
        mapC.put(A,0.4);
        mapC.put(B,0.4);
        mapC.put(D,0.8);

        Map<Place, Double> mapD = new HashMap<>();
        mapD.put(A,0.7);
        mapD.put(B,0.5);
        mapD.put(C,0.8);

        plan.addPlace(A, mapA);
        plan.addPlace(B, mapB);
        plan.addPlace(C, mapC);
        plan.addPlace(D, mapD);

        plan.graphBuilder(plan.places, plan.maps);

        TravelPlanWithShortestPath path1 = new TravelPlanWithShortestPath();
        System.out.println("shortest path:");
        path1.calculateShortestPath(A, 8.5);

        // reset to default to recalculate
        plan.setDefault(plan.places);

        TravelPlanWithMostPlaces path2 = new TravelPlanWithMostPlaces();
        System.out.println("most city path:");
        path2.calculateShortestPath(A, 8.5);

















    }
}