package si.uni_lj.fri.pbd.mesibajk

data class BikeModel(val id :String, val name :String, val km :Int, var status :String) :Comparable<BikeModel>{

    override fun compareTo(other :BikeModel): Int {
        val id1 = id
        val id2 = other.id
        return id1.compareTo(id2)
    }
}