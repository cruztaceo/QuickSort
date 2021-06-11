import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val arraySizes = intArrayOf(10, 100, 200, 500, 1000, 2000, 5000, 10000)
    val records = mutableListOf<Register>()

    val arrays = arraySizes.map { generateArrayNth(it) }

    arrays.map { a ->
        val timeRecords = mutableListOf<Long>()
        repeat(50) {
            timeRecords.add(permuteAndSortTime(a.toMutableList()))
        }
        records.add(Register(a.size, timeRecords))
    }
    records.map { println("Nth: ${it.nth}, times: ${it.times.toLongArray().contentToString()}") }
}

fun permuteAndSortTime(A: MutableList<Int>): Long {
    val permutation = fisherYatesPermutation(A)
    return measureTimeMillis {
        quicksort(permutation, 0, permutation.lastIndex)
    }
}

fun quicksort(A: MutableList<Int>, lo: Int, hi: Int): MutableList<Int> {
    if (lo < hi) {
        val p = randomizedPartition(A, lo, hi)
        quicksort(A, lo, p - 1)
        quicksort(A, p + 1, hi)
    }
    return A
}

fun randomizedPartition(A: MutableList<Int>, lo: Int, hi: Int): Int {
    val pivotIndex = Random.nextInt(lo, hi)
    A[pivotIndex] = A[hi].also { A[hi] = A[pivotIndex] }
    return partition(A, lo, hi)
}

fun partition(A: MutableList<Int>, lo: Int, hi: Int): Int {
    val pivot = A[hi]
    var i = lo
    for (j in lo..hi) {
        if (A[j] < pivot) {
            A[i] = A[j].also { A[j] = A[i] }
            i += 1
        }
    }
    A[i] = A[hi].also { A[hi] = A[i] }
    return i
}

fun fisherYatesPermutation(A: MutableList<Int>): MutableList<Int> {
    for (i in A.lastIndex downTo 1) {
        val j = Random.nextInt(0, i)
        A[j] = A[i].also { A[i] = A[j] }
    }
    return A
}

fun generateArrayNth(n: Int): IntArray {
    return IntArray(n) { i -> i + 1 }
}

class Register(val nth: Int, val times: MutableList<Long>)