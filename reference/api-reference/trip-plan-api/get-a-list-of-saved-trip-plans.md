---
description: Get a list of user saved trip plans, without the details of the plans.
---

# Get a List of Saved Trip Plans

[.](./ "mention")<-

## Description

When logged in, you can get a list of all saved trip plans by the logged in user, with minimum details. Only the destination city, start and end date can be retrieved using this API.&#x20;

## Authorization

Require authorization token in the header.

## URL

```
GET /trip-plans
```

## Java Method

### `TripPlanService.java`

```java
public List<TripPlan> getTripPlanList(String ownerId);
```

## Response Codes

<table><thead><tr><th width="238">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully get a list of saved trip plans.</td></tr><tr><td>401 Unauthorized</td><td>Failed to get trip plans because the authorization token is either non-existent or invalid.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to get trip plans because the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="181">Field</th><th width="98.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>trip_plans</td><td>Object[]</td><td>Contains a list of trip plan objects.</td></tr><tr><td>   trip_id</td><td>String</td><td>The unique string of id of the trip plan. It can be used to get the details of the trip plan in<a data-mention href="get-details-of-a-trip-plan.md">get-details-of-a-trip-plan.md</a>.</td></tr><tr><td>   city_id</td><td>String</td><td>The id of the destination city. It uses the same format as place_id, which is from Google API</td></tr><tr><td>   destination_city</td><td>String</td><td>The name of the destination city.</td></tr><tr><td>   start_date</td><td>String</td><td>The start date of the trip plan, in "yyyy-MM-dd" format.</td></tr><tr><td>   end_date</td><td>String</td><td>The end date of the trip plan, in "yyyy-MM-dd" format.</td></tr></tbody></table>

## Relevant Java Objects

### `TripPlan.java`

```java
public record TripPlan(
        @JsonProperty("trip_id") String tripId,
        @JsonProperty("city_id") String cityId,
        @JsonProperty("destination_city") String destinationCity,
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate,
        List<TripPlanDetailsPerDay> details /* This field will be intentionally set to null */
) {
        // Other code
}
```
