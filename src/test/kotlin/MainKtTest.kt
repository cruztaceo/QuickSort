import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainKtTest {

    @Test
    fun quicksort() {
        val A = mutableListOf(3, 2, 5, 6, 4, 1)
        val result = quicksort(A, 0, A.lastIndex);
        val expected = listOf(1, 2, 3, 4, 5, 6)
        println(result.toString())
        assertArrayEquals(expected.toIntArray(), result.toIntArray())
    }

    @Test
    fun fisherYatesPermutation() {
        val A = mutableListOf(1, 2, 3, 4, 5, 6)
        val result = fisherYatesPermutation(A);
        val expected = listOf(1, 2, 3, 4, 5, 6)
        println(result.toString())
        assertFalse(A.toIntArray().contentEquals(expected.toIntArray()))
    }
}