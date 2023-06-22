package TravelPlanBuilder;

import java.util.*;

public class Place implements Comparable<Place> {
    private final String place;
    private final double openTime;
    private final double closeTime;
    private double stayTime;

    private Double commuteTime = Double.MAX_VALUE;
    private Map<Place, Double> adjacentNodes = new HashMap<>();
    private boolean isVisited = false;

    public Place(String place, int openTime, int closeTime, int stayTime) {
        this.place = place;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.stayTime = stayTime;
    }

    public void addAdjacentPlace(Place node, double weight) {
        adjacentNodes.put(node, weight);
    }


    @Override
    public int compareTo(Place node) {
        return Double.compare(this.commuteTime, node.getCommuteTime());
    }

    public void setVisited(boolean isVisited){
        this.isVisited =isVisited;
    }

    public boolean getVisited(){
        return this.isVisited;
    }

    public String getPlace() {
        return place;
    }

    public Double getCommuteTime() {
        return commuteTime;
    }

    public void setCommuteTime(Double  distance) {
        this.commuteTime = distance;
    }

    public Map<Place, Double> getAdjacentPlaces() {
        return adjacentNodes;
    }

    public Double getOpenTime() {
        return openTime;
    }

    public Double getCloseTime() {
        return closeTime;
    }

    public Double getStayTime() {
        return stayTime;
    }


}
