package si.uni_lj.fri.pbd.mesibajk

import org.junit.Assert.*

//import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest {

    @Test
    fun validateDate() {
        var hour = "30"
        var day = "40"
        var month = "0"
        var min = "-1"

        val rez1 = Validator.validateDate(hour, day, month, min)

        hour = "a"
        day = "1"
        month = "1"
        min = "c"

        val rez2 = Validator.validateDate(hour, day, month, min)

        hour = "12"
        day = "30"
        month = "2"
        min = "5"

        val rez3 = Validator.validateDate(hour, day, month, min)

        hour = "10"
        day = "4"
        month = "7"
        min = "12"

        val rez4 = Validator.validateDate(hour, day, month, min)

        val finalRez = (!(rez1 || rez2 || rez3) && rez4)

        assertEquals(finalRez, true)
    }

    @Test
    fun validateName() {

        var name = ""
        val rez1 = Validator.validateName(name)

        name = "Niko Sok"
        val rez2 = Validator.validateName(name)

        val finalRez = !rez1 && rez2

        assertEquals(finalRez, true)
    }
}