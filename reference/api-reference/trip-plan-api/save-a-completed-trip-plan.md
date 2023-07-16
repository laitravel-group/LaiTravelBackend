---
description: Save a completed trip plan of multiple days.
---

# Save a Completed Trip Plan

[.](./ "mention")<-

## Description

When logged in, you can save a completed trip plan composed of one or more one-day trip plans. Each trip plan must be either from [create-an-one-day-trip-plan.md](create-an-one-day-trip-plan.md "mention") or [update-an-one-day-trip-plan.md](update-an-one-day-trip-plan.md "mention").

## Authorization

Require authorization token in the header.

## URL

```
POST /trip-plan-save
```

## Java Method

### `PlaceService.java`

```java
public void saveTripPlan(String ownerId, @NotNull TripPlan tripPlan);
```

{% hint style="danger" %}
This is subject to change due to incompleteness of the user system.
{% endhint %}

## Request Body

<table><thead><tr><th width="181">Field</th><th width="98.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>city_id</td><td>String</td><td>The id of the destination city. It must be a valid city id from Google API. Currently, it can only be obtained from <a data-mention href="../place-api/get-recommended-places.md">get-recommended-places.md</a>.</td></tr><tr><td>destination_city</td><td>String</td><td>The name of the destination city. It must be a valid city name. It can be obtained from <a data-mention href="../place-api/get-recommended-places.md">get-recommended-places.md</a>.</td></tr><tr><td>start_date</td><td>String</td><td>The start date of the trip plan, in "yyyy-MM-dd" format.</td></tr><tr><td>end_date</td><td>String</td><td>The end date of the trip plan, in "yyyy-MM-dd" format.</td></tr><tr><td>details</td><td>Object[]</td><td>Contains a list of objects, each represents a detailed trip plan for one day. Trip plans here will NOT be revalidated.</td></tr><tr><td>   date</td><td>String</td><td>The date of the trip plan, must be in "yyyy-MM-dd" format.</td></tr><tr><td>   start_location</td><td>Object</td><td>A place object representing the starting location of the day, for example can be the hotel the user stay will stay at.</td></tr><tr><td>      place_id</td><td>String</td><td>The id of the starting location. If presented, it should use the same place id from Google API. <strong>This is optional.</strong></td></tr><tr><td>      place_name</td><td>String</td><td>The name of the starting location. <strong>This is optional.</strong></td></tr><tr><td>      lat</td><td>Double</td><td>The latitude of the starting location</td></tr><tr><td>      lng</td><td>Double</td><td>The longitude of the starting location</td></tr><tr><td>   start_time</td><td>String</td><td>The time of departure from the starting location, in "HH:mm" format.</td></tr><tr><td>   end_time</td><td>String</td><td>The time when the last visit is finished, in "HH:mm" format.</td></tr><tr><td>   visits</td><td>Object[]</td><td>Contins a list of objects, each represents a place the user wishes to visit on the day, as well as the time the user want to spend at the place.</td></tr><tr><td>      place</td><td>Object</td><td>The place object, can be an arbitrary location on the map thus only the latitude and longitude are required at minimum.</td></tr><tr><td>         place_id</td><td>String</td><td>The id of the place. It uses the same place id from Google API. <strong>This is optional.</strong></td></tr><tr><td>         place_name</td><td>String</td><td>The name of the place. <strong>This is optional.</strong></td></tr><tr><td>         lat</td><td>Double</td><td>The latitude of the place.</td></tr><tr><td>         lng</td><td>Double</td><td>The longitude of the place.</td></tr><tr><td>      travel_time</td><td>Integer</td><td>The approximate time in minutes to travel from previous place to this place. If this is the first visit in the list, this represents the time to travel from starting location to this place.</td></tr><tr><td>      start_time</td><td>String</td><td>The time of arrival at the place, in "HH:mm" format.</td></tr><tr><td>      end_time</td><td>String</td><td>The time of leaving the place, in "HH:mm" format. Must be after start time. </td></tr><tr><td>      stay_duration</td><td>Integer</td><td>The time in minutes that the user wants to spend at the place. It should be equals to the time elapsed since arrival until leaving.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="238">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully saved the trip plan.</td></tr><tr><td>400 Bad Request</td><td>Failed to save the trip plan because the request body is incorrectly formatted.</td></tr><tr><td>401 Unauthorized</td><td>Failed to save the trip plan because the authorization token is either non-existent or invalid.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to save the trip plan because the server is experiencing some issues.</td></tr></tbody></table>

## Relevant Java Objects

### `TripPlan.java`

{% hint style="info" %}
The code can be found at [#tripplan.java](get-a-list-of-saved-trip-plans.md#tripplan.java "mention"), however, **details** must be filled in correctly according to [create-an-one-day-trip-plan.md](create-an-one-day-trip-plan.md "mention") or [update-an-one-day-trip-plan.md](update-an-one-day-trip-plan.md "mention")
{% endhint %}

