package TravelPlanBuilder;

import java.util.*;
import java.util.Map;
import java.util.PriorityQueue;

class TravelPlanWithShortestPath {

    List<PlacePlan> finalPlans = new ArrayList<>();

    public void calculateShortestPath(Place start, double startTime) {

        PriorityQueue<Place> unsettledPlaces = new PriorityQueue<>();

        start.setCommuteTime(0.0);

        if(start.getOpenTime() > startTime){
            System.out.println("The place you chose is not open yet, please change start time or start location!");
            return;
        }
        // 将起始地点的旅行计划添加到队列中
        unsettledPlaces.add(start);
        double currentTime = startTime;

        // 处理队列中的每个旅行计划
        while (!unsettledPlaces .isEmpty()) {
            // 取出队列中的旅行计划
            Place currentPlace = unsettledPlaces.poll();
            currentPlace.setVisited(true);
            PlacePlan placePlan = new PlacePlan(currentPlace, currentTime + currentPlace.getCommuteTime(), currentTime + + currentPlace.getCommuteTime()+ currentPlace.getStayTime(), currentPlace.getStayTime());
            printTravelPlans(placePlan);

            finalPlans.add(placePlan);
            currentTime = currentTime + currentPlace.getStayTime() +currentPlace.getCommuteTime();

            // Iterate over the adjacent nodes of the current node
            for (Map.Entry<Place, Double> entry : currentPlace.getAdjacentPlaces().entrySet()) {
                Place adjacentPlace = entry.getKey();
                if (!adjacentPlace.getVisited()) {
                    adjacentPlace.setCommuteTime(entry.getValue());

                    // 计算到达和离开邻居节点的时间
                    double arrivalTime = placePlan.leaveTime + adjacentPlace.getCommuteTime();
                    double waitTime = adjacentPlace.getStayTime();
                    double leaveTime = arrivalTime + waitTime;

                    // 如果在开门时间之后到达并且在关门时间之前离开，且这个新的旅行计划更短，那么更新邻居的旅行计划
                    if (arrivalTime >= adjacentPlace.getOpenTime() && leaveTime <= adjacentPlace.getCloseTime()) {
                        if (!unsettledPlaces.contains(adjacentPlace)){
                            unsettledPlaces.add(adjacentPlace);
                        }
                    } else {
                        unsettledPlaces.remove(adjacentPlace);
                    }
                }
            }
        }

    }

    String formatTime(double time) {
        int hour = (int) time;
        int minute = (int)(time*60 % 60);
        return String.format("%02d:%02d", hour, minute);
    }

    void printTravelPlans(PlacePlan plan) {
        double commuteTime = plan.place.getCommuteTime() * 60;
        String arriveTime = formatTime(plan.arriveTime);
        String leaveTime = formatTime(plan.leaveTime);
        System.out.println("Commuting to " + plan.place.getPlace() + " takes " + (int)commuteTime + " minutes");
        System.out.println("Visiting " + plan.place.getPlace() + " from " + arriveTime + " to " + leaveTime);
    }

}
