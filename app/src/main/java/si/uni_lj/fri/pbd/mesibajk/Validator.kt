package si.uni_lj.fri.pbd.mesibajk

object Validator {

    fun validateDate(h :String, d :String, m :String, min :String) :Boolean{
        if(!h.all { Character.isDigit(it) } || !d.all { Character.isDigit(it) } || !m.all { Character.isDigit(it) } || !min.all { Character.isDigit(it) }){
            return false
        }
        if(h == "" || d == "" || m == "" || min == ""){
            return false
        }
        val hn = h.toInt()
        val dn = d.toInt()
        val mn = m.toInt()
        val minn = min.toInt()
        if(hn < 0 || hn > 23 || mn < 1 || mn > 12 || dn < 0 || dn > 31 || minn < 0 || minn > 59){
            return false
        }
        if(mn in arrayOf(4, 6, 9, 11)){
            if(dn > 30){
                return false
            }
        }else if(mn == 2){
            if(dn < 0 || dn > 28){
                return false
            }
        }

        return true
    }

    fun validateName(name :String) :Boolean{
        return (name.length > 0)
    }
}