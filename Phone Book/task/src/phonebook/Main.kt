package phonebook

import java.io.File
import java.lang.Math.floor
import java.lang.Math.sqrt
import java.util.concurrent.TimeUnit

fun main() {
    val linesFind = File("C:\\DeleteIt\\find.txt").readLines().toMutableList()
    val linesBookForQuickSort = File("C:\\DeleteIt\\directory.txt").readLines().toMutableList()
    val linesBookForLinearSearch = File("C:\\DeleteIt\\directory.txt").readLines().toMutableList()
    val linesBookForBubbleSort = File("C:\\DeleteIt\\directory.txt").readLines().toMutableList()
    val linesBookForHashing = File("C:\\DeleteIt\\directory.txt").readLines().toMutableList()
    var hashTableBook: MutableMap<String, String> = mutableMapOf<String, String>()

    println("Start searching (linear search)...")

    val linear = countLinear(linesBookForLinearSearch, linesFind, System.currentTimeMillis())
    val longLinearSearchTime : Long = linear.first
    val (minF, secF, msecF) = formatT(linear.first)

    println("Found ${linear.second} / ${linesFind.size} entries. Time taken: $minF min. $secF sec. $msecF ms.")

    println("\nStart searching (bubble sort + jump search)...")
    val startBubbleSortTime = System.currentTimeMillis()

    val longSortBubbleTime : Long = bubblesort(linesBookForBubbleSort, startBubbleSortTime, longLinearSearchTime)
    val (minS, secS, msecS) = formatT(longSortBubbleTime)

    // Searching - jump search
    val startJumpSearchTime = System.currentTimeMillis()
    val jumpsearchFound : Int = jumpSearch(linesBookForBubbleSort, linesFind)

    val  longJumpSearchTime = System.currentTimeMillis()-startJumpSearchTime
    val (minSe, secSe, msecSe) = formatT(longJumpSearchTime)
    println("Found $jumpsearchFound / ${linesFind.size} entries. Time taken: ${minS+minSe} min. ${secS+secSe} sec. ${msecS+msecSe} ms.")
    println("Sorting time: $minS min. $secS sec. $msecS ms.")
    println("Searching time: $minSe min. $secSe sec. $msecSe ms.")

    println("\nStart searching (quick sort + binary search)...")

    // Sorting - quick sort
    val quickSortingStartTime = System.currentTimeMillis()
    quicksort(linesBookForQuickSort,-1,-1)

    val longQuickSortTime = System.currentTimeMillis()-quickSortingStartTime
    val (minSq, secSq, msecSq) = formatT(longQuickSortTime)

    // Searching - binary search
    val searchingBinaryStartTime = System.currentTimeMillis()

    val searchingBinaryFound: Int =  binarySearchBook(linesBookForQuickSort, linesFind)
    val longSearchBinaryTime = System.currentTimeMillis()-searchingBinaryStartTime
    val (minSb, secSb, msecSb) = formatT(longSearchBinaryTime)
    println("Found $searchingBinaryFound / ${linesFind.size} entries. Time taken: ${minSq + minSb} min. ${secSq + secSb} sec. ${msecSq + msecSb} ms.")
    println("Sorting time: $minSq min. $secSq sec. $msecSq ms.")
    println("Searching time: $minSb min. $secSb sec. $msecSb ms.")

    println("\nStart searching (hash table)...")
    val hashCreateStartTime = System.currentTimeMillis()
    for(i in linesBookForHashing){
        hashTableBook.put(i.substringBefore(" "), i.substringAfter(" "))
    }
    val longHashCreateTime = System.currentTimeMillis()-hashCreateStartTime
    val (minCh, secCh, msecCh) = formatT(longHashCreateTime)

    val hashCountStartTime = System.currentTimeMillis()
    val foundInHash =  hashCount(hashTableBook, linesFind)
    val longHashCountTime = System.currentTimeMillis()-hashCountStartTime
    val (minCc, secCc, msecCc) = formatT(longHashCountTime)
    println("Found $foundInHash / ${linesFind.size} entries. Time taken: $minCc min. $secCc sec. $msecCc ms.")

    println("Creating time: $minCh min. $secCh sec. $msecCh ms.")

        val longHashSearchTime = longHashCountTime-longHashCreateTime
        val (minCs, secCs, msecCs) = formatT(longHashSearchTime)
        println("Searching time: $minCs min. $secCs sec. $msecCs ms.")
}
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun hashCount(hashTableBook: MutableMap<String, String>, linesFind: MutableList<String>): Int {
    var found = 0
    for(i in linesFind){
        if(hashTableBook.containsValue(i)) found++
    }
    return found+1
}
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun quicksort(listBook: MutableList<String>, start1: Int, end1: Int){
    var start = start1
    var end = end1

    if (start == -1) start = 0
    if (end == -1) end = listBook.size -1
    if (start < end) {
        val i = partition(listBook, start, end)
        quicksort(listBook, start, i)
        quicksort(listBook, i + 1, end)
    }
}
// --------------------------------------------------------------------
fun binarySearchBook(linesBook: MutableList<String>, linesFind: MutableList<String>): Int {

    var found = 0
    val low = 0
    val hight = linesBook.size - 1
    wl@ while(found<linesFind.size-1){
        for(i in linesFind){
            if  (binarySearchOneLine(i, linesBook, low, hight)) found++
        }
    }
    return found
}
// ------------------------------------------------------------
fun binarySearchOneLine(i: String, linesBook: MutableList<String>, ind1: Int, ind2: Int): Boolean {
    val half = (ind1 + ind2) / 2
    when {
        linesBook[half].contains(i) -> {
            return true
        }
        i < linesBook[half] -> if (ind1 == half || binarySearchOneLine(i, linesBook, ind1, half - 1)) return true
        else -> if (ind2 == half || binarySearchOneLine(i, linesBook, half + 1, ind2)) return true
    }
    return false
}
// +++++++++++++++++++++++++++++++++++++++++++++++++++
fun bubblesort(bookList: MutableList<String>, startBubbleSortTime: Long, longLinearSearchTime: Long): Long {
    var flag = 0
    wl@ while(true) {
        for (i in 0..bookList.size-2){
            if(bookList[i].substringAfter(" ") > bookList[i + 1].substringAfter(" ")) {
                val temp = bookList[i + 1]
                bookList[i+1] = bookList[i]
                bookList[i] = temp
                flag++
            }
        }
        if(flag!=0 && (System.currentTimeMillis() - startBubbleSortTime) > longLinearSearchTime * 10){
            return (System.currentTimeMillis()-startBubbleSortTime)
        }
        if(flag==0){
            return (System.currentTimeMillis()-startBubbleSortTime)
        }
        else{
            flag=0
            continue
        }
    }
}
// --------------------------------------------------------
fun partition(listBook: MutableList<String>, start1: Int, end1: Int): Int {
    var start = start1
    var end = end1
    while (start < end) {
        while (start < end) {
            if (listBook[start].substringAfter(" ") > listBook[end].substringAfter(" ")) {
                val swap = listBook[start]
                listBook[start] = listBook[end]
                listBook[end] = swap
                break
            }
            end -= 1
        }
        while (start < end) {
            if (listBook[start].substringAfter(" ") > listBook[end].substringAfter(" ")) {
                val swap = listBook[start]
                listBook[start] = listBook[end]
                listBook[end] = swap
                break
            }
            start += 1
        }
    }
    return start
}
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun jumpSearch(linesBook : MutableList<String>, sortedListFind: List<String>): Int {
    var index = 0
    val stepS = floor(sqrt(sortedListFind.size.toDouble())).toInt()
    var found = 0
    wl@ while (found<sortedListFind.size-1){
        for(i in linesBook){
            val name = i.substringAfter(" ")
            // find position where name_linesBook > name_sortedListFind
            f1@ for (j in sortedListFind.indices step stepS) {
                if (sortedListFind[j] > name){
                    index = j// NO NO NO YES => index==3
                    break@f1
                }
            }
            //   // discover from position down to previous position
            f2@ for (i in index downTo index - stepS) {
                if (sortedListFind[index - i]==name) {
                    found++
                    break@f2
                }
            }
            if(found==sortedListFind.size-1) break@wl
        }
    }
    return found+1
}
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun countLinear(linesBook: List<String>, linesFind: List<String>, startLinearTime: Long) :Pair<Long,Int> {
    var count=0
    for(i in linesBook){
        val name = i.substringAfter(' ')
        if(linesFind.contains(name)){
            count++
        }
    }
    return Pair(System.currentTimeMillis()-startLinearTime, count)
}
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun formatT(diff: Long):  IntArray {
    var d = diff
    val minutes = TimeUnit.MILLISECONDS.toMinutes(d)
    d -= minutes*60*1000
    val seconds = TimeUnit.MILLISECONDS.toSeconds(d)
    d -= seconds * 1000
    return intArrayOf(minutes.toInt(), seconds.toInt(), d.toInt())
}

