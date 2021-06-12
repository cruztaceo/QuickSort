import jetbrains.datalore.plot.PlotHtmlExport
import jetbrains.datalore.plot.PlotHtmlHelper.scriptUrl
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

    val p = plot(records)

    // Export to HTML.
    // Note: if all you need is to save HTML to a file than you can just use the 'ggsave()' function.
    val content = PlotHtmlExport.buildHtmlFromRawSpecs(p.toSpec(), scriptUrl("2.0.3"))
    openInBrowser(content)
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

fun plot(records: MutableList<Register>): Plot {
    val data = mapOf(
        "Size" to records.map { i -> List(i.times.size) { i.nth } }.flatten(),
        "Time" to records.flatMap { it.times }
    )

    var p = letsPlot(data) { x = "Size"; y = "Time" }
    p += geomPoint { color = "Time" } + statSmooth(method = "loess") { color = "Time" } + ggsize(900, 500)
    return p
}


fun openInBrowser(content: String) {
    val dir = File(System.getProperty("user.dir"), "lets-plot-images")
    dir.mkdir()
    val file = File(dir.canonicalPath, "my_plot.html")
    file.createNewFile()
    file.writeText(content)

    Desktop.getDesktop().browse(file.toURI())
}