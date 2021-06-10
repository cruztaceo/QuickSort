import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val arraySizes = intArrayOf(10, 100, 200, 500, 1000, 2000, 5000, 10000)
//    val array10 = generateArrayNth(10)
//    val array100 = generateArrayNth(100)
//    val array200 = generateArrayNth(200)
//    val array500 = generateArrayNth(500)
//    val array1000 = generateArrayNth(1000)
//    val array2000 = generateArrayNth(2000)
//    val array5000 = generateArrayNth(5000)
//    val array10000 = generateArrayNth(10000)

    val timeRecords = mutableListOf<Long>()

    for (i in 0 until 50) {
        arraySizes.map { generateArrayNth(it) }.map { a ->
            {
                val result = fisherYatesPermutation(a.toMutableList())
                val time = measureTimeMillis {
                    quicksort(result, 0, result.lastIndex)
                }
                timeRecords.add(time)
            }
        }
    }
    println(timeRecords.toLongArray().contentToString())
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