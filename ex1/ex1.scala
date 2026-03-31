import scala.util.Try

// trip_id, pickup_time, distance_km, fare_amount

val rawTrips = List(
  "101, 2026-03-10T10:30, 12.5, 18.40",
  "102, 2026-03-10T11:05, 8.2, 13.20",
  "103, 2026-03-10T11:20, 3.1, 6.50",
  "104, 2026-03-10T11:45, bad, 10.00",
  "105, 2026-03-10T12:10, 15.7, 24.90",
  "106, 2026-03-10T12:40, 2.5, 5.40",
  "107, 2026-03-10T13:05, 7.8, 12.60",
  "108, 2026-03-10T13:20, 11.3, 17.80",
  "109, 2026-03-10T13:50, 5.2, notanumber",
  "110, 2026-03-10T14:10, 18.9, 29.30",
  "111, 2026-03-10T14:25, 6.4, 11.00",
  "112, 2026-03-10T14:50, 9.1, 14.80",
  "113, 2026-03-10T15:15, 1.8, 4.20",
  "114, 2026-03-10T15:40, 13.6, 21.50",
  "115, 2026-03-10T16:00, 4.7, 8.90",
  "116, 2026-03-10T16:20, 10.2, 16.10",
  "117, 2026-03-10T16:45, 14.5, 23.70",
  "118, 2026-03-10T17:05, 0.9, 3.50",
  "119, 2026-03-10T17:30, 7.0, 12.10",
  "120, 2026-03-10T17:50, 16.4, 26.80"
)


// 1
case class RawTrip(data: String)

case class Trip(
  id: Int,
  pickupTime: String,
  distanceKm: Double,
  fare: Double
)

case class TripReport(
  id: Int,
  distanceKm: Double,
  finalFare: Double,
  label: String
)

// 2
def parseTrip(raw: RawTrip): Option[Trip] =
  val fields = raw.data.split(",").map(_.trim)
  for
    id <- Try(fields(0).toInt).toOption
    distance <- Try(fields(2).toDouble).toOption
    fare <- Try(fields(3).toDouble).toOption
  yield Trip(id, fields(1), distance, fare)


// 3
def distanceAbove(min: Double)(t: Trip): Boolean = t.distanceKm >= min
def fareAbove(min: Double)(t: Trip): Boolean = t.fare >= min


// 4
val applyFuelSurcharge: Trip => Trip = t => t.copy(fare = t.fare * 1.10)
val applyCityTax: Trip => Trip = t => t.copy(fare = t.fare * 0.95)


// 5
def labelByDistance(threshold: Double, longLabel: String, shortLabel: String)(t: Trip): String = 
if (t.distanceKm >= threshold) longLabel else shortLabel

val labelTrip = labelByDistance(10, "LongTrip", "ShortTrip")


// 6
val toReport: Trip => TripReport = t =>
  TripReport(
    id = t.id,
    distanceKm = t.distanceKm,
    finalFare = t.fare,
    label = labelTrip(t)
  )


//7
val enrichTrip: Trip => Trip = applyFuelSurcharge andThen applyCityTax
val processTrip: Trip => TripReport = enrichTrip andThen toReport


// 8
val reports: List[TripReport] =
  rawTrips
    .map(s => RawTrip(s))
    .flatMap(parseTrip)
    .filter(distanceAbove(5))
    .filter(fareAbove(10))
    .map(processTrip)


// 9
@main def run(): Unit =
  println("TripID | Distance | FinalFare | Label")
  for (r <- reports){
    println(f"${r.id} | ${r.distanceKm} | ${r.finalFare}%.2f | ${r.label}")
  }