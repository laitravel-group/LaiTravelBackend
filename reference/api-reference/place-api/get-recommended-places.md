---
description: Get up to 20 recommended travel spots given city name, start and end date.
---

# Get Recommended Places

#### [.](./ "mention")<-

## Description

The API can provide up to 20 famous travel spots in the given city. It can also consider whether or not the places are open between the start and end date. If a place is closed within the start and end date, the place will be excluded from the list.

## URL

```
GET /places
```

## Java Method

### `PlaceService.java`

```java
public Pair<String, List<Place>> placeSearch(String cityName, String startDateString, String endDateString);
```

{% hint style="info" %}
The first element in the pair represents the city id, the second element represents the list of places of famous travel spots.
{% endhint %}

## Request Query Parameters

<table><thead><tr><th width="128.33333333333331">Parameter</th><th width="106">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>city</td><td>String</td><td>true</td><td>The full city name, casing does not matter.</td></tr><tr><td>start_date</td><td>String</td><td>true</td><td>The selected start date. Must be in "yyyy-MM-dd" format.</td></tr><tr><td>end_date</td><td>String</td><td>true</td><td>The selected end date. Must be in "yyyy-MM-dd" format.</td></tr></tbody></table>

## Response Codes

<table data-full-width="false"><thead><tr><th>Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>The data is successfully retrieved.</td></tr><tr><td>400 Bad Request</td><td><p>Failed to get places data, can be caused by one of the reasons: </p><ol><li>The city of input does not exist, or the parameter is missing</li><li>The dates are invalid, in incorrect format or missing.</li></ol></td></tr><tr><td>500 Internal Server Error</td><td>Failed to get places data because the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="209">Field</th><th width="96.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>city_id</td><td>String</td><td>The id of the city. It uses the same place id from Google API.</td></tr><tr><td>places</td><td>Object[]</td><td>Contains a list of places that are recommended for travel, each element of the array is the detail information of a place.</td></tr><tr><td>   place_id</td><td>String</td><td>The id of a place. It uses the same place id from Google API.</td></tr><tr><td>   place_name</td><td>String</td><td>The name of the place.</td></tr><tr><td>   lat</td><td>Double</td><td>The latitude of the place.</td></tr><tr><td>   lng</td><td>Double</td><td>The longitude of the place.</td></tr><tr><td>   photo</td><td>String</td><td>A link to a single photo of the place.</td></tr><tr><td>   types</td><td>String[]</td><td>Contains a list of strings representing the type of the place. It uses the same type description as Google API. Some possible values can be: "tourist_attraction", "park", "point_of_interest", "establishment", "amusement_park", "landmark", "museum", "zoo", etc.</td></tr><tr><td>   formatted_address</td><td>String</td><td>The full address of a place, e.g. "531 Seventeen Mile Rocks Rd, Seventeen Mile Rocks QLD 4073, Australia".</td></tr><tr><td>   description</td><td>String</td><td>A short description of the place. </td></tr><tr><td>   rating</td><td>Float</td><td>A number ranged from 0 to 5.0 representing the tourist rating of the place. It uses the same number as Google API.</td></tr><tr><td>   opening_hours</td><td>Object[]</td><td>Contains a list of opening hours in a week. Each element represents opening hours of a weekday. The size of the array can be less than 7. If a weekday is not presented, it means the place is closed on that day.</td></tr><tr><td>      day_of_week</td><td>String</td><td>What day of this element represents, contains a weekday in capital letters, e.g. "MONDAY".</td></tr><tr><td>      open_time</td><td>String</td><td>The opening time of the place in this weekday, in "HH:mm" format, e.g. "08:00".</td></tr><tr><td>      close_time</td><td>String</td><td>The closing time of the place in this weekday, in "HH:mm" format, e.g. "18:00".</td></tr></tbody></table>

## Relevant Java Objects

### `Place.java`

```java
public record Place(
        @JsonProperty("place_id") String placeId,
        @JsonProperty("place_name") String placeName,
        @JsonProperty("city_id") String cityId,
        Double lat,
        Double lng,
        String photo,
        List<String> types,
        @JsonProperty("formatted_address") String formattedAddress,
        String description,
        Float rating,
        @JsonProperty("opening_hours") List<OpeningHours> openingHours
) {
        // Other Code
}
```

### `OpeningHours.java`

```java
public record OpeningHours(
        @JsonProperty("day_of_week") DayOfWeek dayOfWeek,
        @JsonProperty("open_time") String openTime,
        @JsonProperty("close_time") String closeTime
) {}
```
