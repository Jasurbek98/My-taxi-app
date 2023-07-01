package uz.jsoft.mytaxiapp.data.model

data class Point(
    val latitude: Double,
    val longitude: Double,
) {

    override fun toString(): String {
        return String.format("$latitude,$longitude")
    }

}
