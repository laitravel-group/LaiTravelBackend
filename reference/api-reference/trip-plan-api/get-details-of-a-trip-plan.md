---
description: Get the details of a trip plan, given a trip id.
---

# Get Details of a Trip Plan

[.](./ "mention")<-

## Description

When logged in, you can get a specific trip plan of the logged in user, given a trip id from [get-a-list-of-saved-trip-plans.md](get-a-list-of-saved-trip-plans.md "mention").

## Authorization

Require authorization token in the header.

## URL

```
GET /trip-plan-details
```

## Java Method

`TripPlanService.java`

```java
public List<TripPlanDetailsPerDay> getTripPlanDetails(int tripId);
```

## Request Query Parameters

<table><thead><tr><th width="136.33333333333331">Parameter</th><th width="106">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>trip_id</td><td>String</td><td>true</td><td>The id of the trip plan you want to get the details of, can be obtained from <a data-mention href="get-a-list-of-saved-trip-plans.md">get-a-list-of-saved-trip-plans.md</a>.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="238">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully retrieved the details of the trip plan.</td></tr><tr><td>401 Unauthorized</td><td>Failed to get details of the trip plan because the authorization token is either non-existent or invalid.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to get details of the trip plan because the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="181">Field</th><th width="98.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>details</td><td>Object[]</td><td>Contains a list of objects, each represents a detailed trip plan for one day. The dates in the plans may not be in succession. If a date does not exist, it means there is no plan for that date.</td></tr><tr><td>   date</td><td>String</td><td>The date of the trip plan, in "yyyy-MM-dd" format.</td></tr><tr><td>   start_location</td><td>Object</td><td>A place object representing the starting location of the day, for example can be the hotel the user stay will stay at.</td></tr><tr><td>      place_id</td><td>String</td><td>The id of the starting location. It uses the same place id from Google API. This may not exist because it can be an arbitrary location defined by the user.</td></tr><tr><td>      place_name</td><td>String</td><td>The name of the starting location. This may not exist because it can be an arbitrary location defined by the user.</td></tr><tr><td>      lat</td><td>Double</td><td>The latitude of the starting location</td></tr><tr><td>      lng</td><td>Double</td><td>The longitude of the starting location</td></tr><tr><td>   start_time</td><td>String</td><td>The time of departure from the starting location, in "HH:mm" format.</td></tr><tr><td>   end_time</td><td>String</td><td>The time when the last visit is finished, in "HH:mm" format.</td></tr><tr><td>   visits</td><td>Object[]</td><td>Contins a list of objects, each represents a place to visit on the day, as well as details of the visit such as the time the user want to spend at the place.</td></tr><tr><td>      place</td><td>Object</td><td>The place the user want to visit, containing essential information of the place. Other information not listed below may exist but it is not recommended to use here.</td></tr><tr><td>         place_id</td><td>String</td><td>The id of the place. It uses the same place id from Google API.</td></tr><tr><td>         place_name</td><td>String</td><td>The name of the place.</td></tr><tr><td>         lat</td><td>Double</td><td>The latitude of the place.</td></tr><tr><td>         lng</td><td>Double</td><td>The longitude of the place.</td></tr><tr><td>      travel_time</td><td>Integer</td><td>The approximate time in minutes to travel from previous place to this place. If this is the first visit in the list, this represents the time to travel from starting location to this place.</td></tr><tr><td>      start_time</td><td>String</td><td>The time of arrival at the place, in "HH:mm" format.</td></tr><tr><td>      end_time</td><td>String</td><td>The time of leaving the place, in "HH:mm" format.</td></tr><tr><td>      stay_duration</td><td>Integer</td><td>The time in minutes that the user wants to spend at the place. It equals to the time elapsed since arrival until leaving.</td></tr></tbody></table>

## Relevant Java Objects

### `TripPlanDetailsPerDay.java`

```java
public record TripPlanDetailsPerDay(
        String date,
        @JsonProperty("start_location") Place startLocation,
        @JsonProperty("start_time") String startTime,
        @JsonProperty("end_time") String endTime,
        List<PlaceVisitDetails> visits
) {}
```

### `PlaceVisitDetails.java`

```java
public class PlaceVisitDetails {
        public final Place place;
        @JsonProperty("travel_time")
        public Integer travelTime;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("stay_duration")
        public final Integer stayDuration;  // in minutes
        @JsonIgnore
        public boolean isVisited;
        
        // other code
}
```

{% hint style="warning" %}
The Place object can be found at [#place.java](../place-api/get-recommended-places.md#place.java "mention"), but the fields other than **placeId**, **placeName**, **lat** and **lng** may not contain any information and are not necessarily needed here.

The field **isVisited** should NOT be used.
{% endhint %}
