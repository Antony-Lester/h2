import kotlin.math.*

fun main() {
    fun calculateLatAndLongFromLatLongBearingDistance(lat : Double, lon : Double, bea : Double, dis : Double) : List<Double>{
        val radius = 6378.1370 //Radius of the Earth
        val bearing = Math.toRadians(bea) //Bearing in degrees.
        val distance = 1.609344 * dis //#Distance in miles.
        val lat1 = Math.toRadians(lat)
        val lon1 = Math.toRadians(lon)
        val lat2 = Math.toDegrees(asin(sin(lat1) * cos(distance / radius) + cos(lat1) * sin(distance / radius) * cos(bearing)))
        val lon2 = Math.toDegrees(lon1 + atan2(sin(bearing) * sin(distance / radius) * cos(lat1), cos(distance / radius) - sin(lat1) * sin(lat2)))
        return listOf(lat2, lon2)}

    fun calculateGlobalGrid(distance: Double): MutableList<List<Double>> {
        var initalLine: MutableList<List<Double>> = mutableListOf(listOf(0.0,0.0))
        var currentResult : List<Double>
        var lastResult : List<Double>
        var result: MutableList<List<Double>> = mutableListOf()
        var lat : Double = 0.00
        var lon : Double = 0.00
        //calculate initial line
        while(true){
            currentResult = calculateLatAndLongFromLatLongBearingDistance(lat, lon, 240.00, distance)
            lat = round(currentResult[0]*1000000) / 1000000; lon = round(currentResult[1]*1000000)/ 1000000;
            if (-90.0 < lat && lat < 90.00 && -180.0 < lon && lon < 180.00) { initalLine.add(listOf(lat,lon))}
            else {break}
        }
        lat = 0.00 ; lon = 0.00
        while(true){
            currentResult = calculateLatAndLongFromLatLongBearingDistance(lat, lon, 60.00, distance)
            lat = round(currentResult[0]*1000000)/ 1000000; lon = round(currentResult[1]*1000000)/ 1000000;
            if (-90.0 < lat && lat < 90.00 && -180.0 < lon && lon < 180.00) { initalLine.add(listOf(lat,lon))}
            else {break}
        }

        //from initial line.. calculate lines
        for (point in initalLine){
            result.add(point); lastResult = point;
            lat = point[0]; lon = point[1]
            //up
            while(true){
                currentResult = calculateLatAndLongFromLatLongBearingDistance(lat, lon, 0.00, distance)
                lat = round(currentResult[0]*1000000) / 1000000; lon = round(currentResult[1]*1000000)/ 1000000;
                if (lat > lastResult[0] && lat < 90.0) {result.add(listOf(lat,lon)); lastResult = currentResult}
                else {break}
            }
            //down
            lat = point[0]; lon = point[1]
            while(true){
                currentResult = calculateLatAndLongFromLatLongBearingDistance(lat, lon, 180.00, distance)
                lat = round(currentResult[0]*1000000) / 1000000; lon = round(currentResult[1]*1000000)/ 1000000;
                if (lat < lastResult[0] && lat > -90.0) {result.add(listOf(lat,lon)); lastResult = currentResult}
                else {break}
            }
        }
        result = result.sortedBy { it[0] }.sortedBy { it[1] }.toMutableList()

        //Output...
        println(initalLine.count())
        println(result.count())
        println((result.count() / initalLine.count()))
        println(((result.count() / initalLine.count())/initalLine.count()))
        for(item in result){
            println(item.toString().replace("[", "").replace("]",""))
        }
        //from point in initial line calculate vertical line
        return result
    }
    calculateGlobalGrid(1500.00)

    //look at hash map array??
}
//d 1500 = 19 long, 9 per line
//https://mobisoftinfotech.com/tools/plot-multiple-points-on-map/