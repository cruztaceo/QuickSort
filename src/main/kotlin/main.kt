import jetbrains.datalore.plot.PlotHtmlExport
import jetbrains.datalore.plot.PlotHtmlHelper.scriptUrl
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomPoint
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.intern.toSpec
import jetbrains.letsPlot.letsPlot
import jetbrains.letsPlot.stat.statSmooth
import java.awt.Desktop
import java.io.File
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    //Array of sizes
    val sizes = intArrayOf(10, 100, 200, 500, 1000, 2000, 5000, 10000)
    val records = mutableListOf<Record>()

    // Create arrays for each size in sizes array
    val arrays = sizes.map { generateArrayNth(it) }

    // Per each array repeat process 50 times and take time from quicksorting
    arrays.map { a ->
        val timeRecords = mutableListOf<Long>()
        repeat(50) {
            timeRecords.add(permuteAndSortTime(a.toMutableList()))
        }
        // add total of time records in records array
        records.add(Record(a.size, timeRecords))
    }
    //print results
    records.map { println("Array Size: ${it.size}, times: ${it.times.toLongArray().contentToString()}") }

    //plot records
    val p = plot(records)

    // Export to HTML.
    // Note: if all you need is to save HTML to a file than you can just use the 'ggsave()' function.
    val content = PlotHtmlExport.buildHtmlFromRawSpecs(p.toSpec(), scriptUrl("2.0.4"))
    ggsave(p, "myPlot.svg")
    openInBrowser(content)
}

/**
 *
 * Permute and Sort
 *
 * @param A MutableList of Int
 * @return Time of sorting using quicksort
 */
fun permuteAndSortTime(A: MutableList<Int>): Long {
    val permutation = fisherYatesPermutation(A)
    return measureTimeMillis {
        quicksort(permutation, 0, permutation.lastIndex)
    }
}

/**
 * QuickSort method
 *
 * @param A Array to sort
 * @param lo Lowest Index
 * @param hi Highest Index
 * @return Sorted array
 */
fun quicksort(A: MutableList<Int>, lo: Int, hi: Int): MutableList<Int> {
    if (lo < hi) {
        val p = randomizedPartition(A, lo, hi)
        quicksort(A, lo, p - 1)
        quicksort(A, p + 1, hi)
    }
    return A
}

/**
 * Method that generates a random partition
 *
 * @param A Array to sort
 * @param lo Lowest Index
 * @param hi Highest Index
 * @return ordered partition
 */
fun randomizedPartition(A: MutableList<Int>, lo: Int, hi: Int): Int {
    val pivotIndex = Random.nextInt(lo, hi)
    A[pivotIndex] = A[hi].also { A[hi] = A[pivotIndex] }
    return partition(A, lo, hi)
}

/**
 * Method to sort partition
 *
 * @param A Array to sort
 * @param lo Lowest Index
 * @param hi Highest Index
 * @return Sorted Partition
 */
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

/**
 * Fisher-Yates Permutation
 *
 * @param A Array to permute
 * @return Permuted array
 */
fun fisherYatesPermutation(A: MutableList<Int>): MutableList<Int> {
    for (i in A.lastIndex downTo 1) {
        val j = Random.nextInt(0, i)
        A[j] = A[i].also { A[i] = A[j] }
    }
    return A
}

/**
 * Generate array of n size
 *
 * @param n size
 * @return Array of n size from 1 -> n
 */
fun generateArrayNth(n: Int): IntArray {
    return IntArray(n) { i -> i + 1 }
}

/**
 * Record Class
 */
class Record(val size: Int, val times: MutableList<Long>)

/**
 * Plot Record Array
 *
 * @param records Records Array to Plot
 * @return Plot
 */
fun plot(records: MutableList<Record>): Plot {
    val data = mapOf(
        "Size" to records.map { i -> List(i.times.size) { i.size } }.flatten(),
        "Time" to records.flatMap { it.times }
    )

    var p = letsPlot(data) { x = "Size"; y = "Time" }
    p += geomPoint { color = "Time" } + statSmooth(method = "loess") { color = "Time" } + ggsize(1500, 700)
    return p
}

/**
 * Method to open plot in browser
 *
 * @param content Content to open in browser and save in my_plot.html file
 */
fun openInBrowser(content: String) {
    val dir = File(System.getProperty("user.dir"), "lets-plot-images")
    dir.mkdir()
    val file = File(dir.canonicalPath, "my_plot.html")
    file.createNewFile()
    file.writeText(content)

    Desktop.getDesktop().browse(file.toURI())
}