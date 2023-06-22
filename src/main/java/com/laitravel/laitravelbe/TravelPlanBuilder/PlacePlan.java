package TravelPlanBuilder;

public class PlacePlan {
    Place place;
    double arriveTime;
    double leaveTime;
    double waitTime;


    PlacePlan(Place node, double arriveTime, double leaveTime, double waitTime) {
        this.place = node;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.waitTime = waitTime;
    }
}
