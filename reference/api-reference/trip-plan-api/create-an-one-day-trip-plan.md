---
description: Get recommendation for place visit order in one day.
---

# Create an One-Day Trip Plan

[.](./ "mention")<-

## Description

Using this API, you can submit information about at least 1 place you want to visit and for how long, then the system can give you recommendation for a trip plan of the day, containing the order to visit the places, estimation of travel time between places, and when you will arrive and leave each place.

## URL

```
POST /trip-plan-build
```

## Java Method

### `PlaceService.java`

```java
public List<TripPlanDetailsPerDay> buildRecommendedTripPlanPerDay(TripPlanDetailsPerDay desiredPlan);
```

## Request Body

<table><thead><tr><th width="209">Field</th><th width="98.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>desired_plan</td><td>Object</td><td>Describes a one-day trip plan where the user decides what the starting location and time is, what places to visit and how long for each place.</td></tr><tr><td>   date</td><td>String</td><td>The date of the trip plan, must be in "yyyy-MM-dd" format.</td></tr><tr><td>   start_location</td><td>Object</td><td>A place object representing the starting location of the day, for example can be the hotel the user stay will stay at.</td></tr><tr><td>      place_id</td><td>String</td><td>The id of the starting location. If presented, it should use the same place id from Google API. <strong>This is optional.</strong></td></tr><tr><td>      place_name</td><td>String</td><td>The name of the starting location. <strong>This is optional.</strong></td></tr><tr><td>      lat</td><td>Double</td><td>The latitude of the starting location</td></tr><tr><td>      lng</td><td>Double</td><td>The longitude of the starting location</td></tr><tr><td>   start_time</td><td>String</td><td>The time of departure from the starting location, in "HH:mm" format. <strong>This is optional.</strong> If this field is missing, the system assumes start time to be 09:00.</td></tr><tr><td>   end_time</td><td></td><td>The time when the user wish to finish the last visit, in "HH:mm" format. <strong>This is optional.</strong> If this field is missing, the system assumes end time to be 18:00.</td></tr><tr><td>   visits</td><td>Object[]</td><td>Contins a list of objects, each represents a place the user wishes to visit on the day, as well as the time the user want to spend at the place.</td></tr><tr><td>      place</td><td>Object</td><td>The place object, can be an arbitrary location on the map thus only the latitude and longitude are required at minimum.</td></tr><tr><td>         place_id</td><td>String</td><td>The id of the place. It uses the same place id from Google API. <strong>This is optional.</strong></td></tr><tr><td>         place_name</td><td>String</td><td>The name of the place. <strong>This is optional.</strong></td></tr><tr><td>         lat</td><td>Double</td><td>The latitude of the place.</td></tr><tr><td>         lng</td><td>Double</td><td>The longitude of the place.</td></tr><tr><td>         opening_hours</td><td>Object[]</td><td>Contains a list of opening hours in a week. Each element represents opening hours of a weekday. The size of the array can be less than 7. If a weekday is not presented, it means the place is closed on that day. <strong>This is optional.</strong> If this is missing, the system assumes that the place opens everyday from 09:00 to 18:00.</td></tr><tr><td>            day_of_week</td><td>String</td><td>What day of this element represents, contains a weekday in capital letters, e.g. "MONDAY". <strong>This is optional.</strong></td></tr><tr><td>            open_time</td><td>String</td><td>The opening time of the place in this weekday, in "HH:mm" format, e.g. "08:00". <strong>This is optional.</strong></td></tr><tr><td>            close_time</td><td>String</td><td>The closing time of the place in this weekday, in "HH:mm" format, e.g. "18:00". <strong>This is optional.</strong></td></tr><tr><td>      stay_duration</td><td>Integer</td><td>The time in minutes that the user wishes to spend at the place. Must be greater than 0.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="238">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully generated a recommended one-day trip plan.</td></tr><tr><td>400 Bad Request</td><td>Failed to create a trip plan because the request body is incorrectly formatted or containes invalid data.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to create a trip plan because the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="181">Field</th><th width="98.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>proposed_plans</td><td>Object[]</td><td>Contains a list of plans, each represents a recommended detailed trip plan for one day. Each plan is created based on different algorithms. Until now only 1 plan is generated using the shortest path algorithm.</td></tr><tr><td>   date</td><td>String</td><td>The date of the trip plan, in "yyyy-MM-dd" format.</td></tr><tr><td>   start_location</td><td>Object</td><td>A place object representing the starting location of the day, for example can be the hotel the user stay will stay at.</td></tr><tr><td>      place_id</td><td>String</td><td>The id of the starting location. It uses the same place id from Google API. This maybe missing for an arbitrary location.</td></tr><tr><td>      place_name</td><td>String</td><td>The name of the starting location. This maybe missing for an arbitrary location.</td></tr><tr><td>      lat</td><td>Double</td><td>The latitude of the starting location</td></tr><tr><td>      lng</td><td>Double</td><td>The longitude of the starting location</td></tr><tr><td>   start_time</td><td>String</td><td>The time of departure from the starting location, in "HH:mm" format.</td></tr><tr><td>.   end_time</td><td>String</td><td>The time when the last visit is finished, in "HH:mm" format.</td></tr><tr><td>   visits</td><td>Object[]</td><td>Contins a list of objects, each represents a place to visit on the day, as well as details of the visit such as the time the user want to spend at the place.</td></tr><tr><td>      place</td><td>Object</td><td>The place the user want to visit, containing essential information of the place. Other information not listed below may exist but it is not recommended to use here.</td></tr><tr><td>         place_id</td><td>String</td><td>The id of the place. It uses the same place id from Google API. This maybe missing for an arbitrary location.</td></tr><tr><td>         place_name</td><td>String</td><td>The name of the place. This maybe missing for an arbitrary location.</td></tr><tr><td>         lat</td><td>Double</td><td>The latitude of the place.</td></tr><tr><td>         lng</td><td>Double</td><td>The longitude of the place.</td></tr><tr><td>      travel_time</td><td>Integer</td><td>The approximate time in minutes to travel from previous place to this place. If this is the first visit in the list, this represents the time to travel from starting location to this place.</td></tr><tr><td>      start_time</td><td>String</td><td>The time of arrival at the place, in "HH:mm" format.</td></tr><tr><td>      end_time</td><td>String</td><td>The time of leaving the place, in "HH:mm" format.</td></tr><tr><td>      stay_duration</td><td>Integer</td><td>The time in minutes that the user wants to spend at the place. It equals to the time elapsed since arrival until leaving.</td></tr></tbody></table>

## Relevant Java Objects

### `TripPlanDetailsPerDay.java`

{% hint style="info" %}
The code can be found at [#tripplandetailsperday.java](get-details-of-a-trip-plan.md#tripplandetailsperday.java "mention"). In the input **desiredPlan**, All **Place** object In the class only require **lat** and **lng** (latitude and longitude), **placeId** and **placeName** are optional, also all **PlaceVisitDetails** only require **place** and **stayDuration**, do NOT fill in other fields.

In the output however, all **PlaceVisitDetails** will have all field filled in.
{% endhint %}
