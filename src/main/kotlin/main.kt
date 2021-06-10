import kotlin.random.Random

fun main() {
    println("Hello World!")
    val array = mutableListOf(3, 2, 5, 4, 6, 1)
    val result = quicksort(array, 0, array.lastIndex)
    println(result.toString())
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
    var i = lo;
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
    for (i in A.lastIndex downTo 1){
        val j = Random.nextInt(0, i)
        A[j] = A[i].also { A[i] = A[j] }
    }
    return A
}