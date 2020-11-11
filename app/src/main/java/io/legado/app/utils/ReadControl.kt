package io.legado.app.utils

import com.jayway.jsonpath.internal.function.numeric.Max
import io.legado.app.constant.AppConst
import java.text.SimpleDateFormat
import java.util.*

/**
 * 防沉迷控制，每天10章
 */
object ReadControl {
    private const val fileName = "read_control"
    private const val keyTotal = "_total"
    private const val keyLast = "_last"
    private const val keyLastDt = "_dt"

    fun saveMax(book: String?, captionIndex: Int) {
        book?:let { return }
        val nowDate = getCurrentDate()
        val lastDt = SharePrefUtil.getData(fileName, book + keyLastDt)
        val total = SharePrefUtil.getIntData(fileName, book + keyTotal)
        SharePrefUtil.saveData(fileName, book + keyTotal, maxOf(captionIndex,total))
        if (lastDt != nowDate) {
            SharePrefUtil.saveData(fileName, book + keyLast, maxOf(captionIndex,total))
            SharePrefUtil.saveData(fileName, book + keyLastDt, nowDate)
        }
    }

    /**
     * 第一天不限制
     */
    fun check(book: String?, captionIndex: Int): Boolean {
        book?:let { return false }
        val lastTotal = SharePrefUtil.getIntData(fileName, book + keyLast)
        return lastTotal == 0 || captionIndex - lastTotal <= AppConst.MAX_READ
    }


    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(Date(System.currentTimeMillis()-6*60*60*1000)) //早上六点刷新
    }


}