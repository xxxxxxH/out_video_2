package net.utils

class TimeUtils {
    fun a() {


    }

    fun times(times: Long): String {
        val time = times / 1000
        //时间戳小于60秒
        if (time < 60) {
            val ts: String = if (time < 10) {
                "0$time"
            } else {
                "$time"
            }
            return "00:00:$ts"
        } else if (time in 59..3599) {
            //时间戳在60秒和3600秒之间（没到小时）
            val miao = time % 60
            val miaos: String
            miaos = if (miao < 10) {
                "0$miao"
            } else {
                "$miao"
            }
            val min = time / 60
            val mins: String
            mins = if (min < 10) {
                "0$min"
            } else {
                "$min"
            }
            return "00:$mins:$miaos"
        } else if (time > 3599) {
            //时间戳大于3600秒（到小时了）
            val h = time / 3600
            val hs: String
            hs = if (h < 10) {
                "0$h"
            } else {
                "$h"
            }
            //取余
            val m = time % 3600
            if (m < 60) {
                val miaos: String = if (m < 10) {
                    "0$m"
                } else {
                    "$m"
                }
                return "$hs:00:$miaos"
            } else {
                val miao = m % 60
                val miaos: String
                miaos = if (miao < 10) {
                    "0$miao"
                } else {
                    "$miao"
                }
                val min = m / 60
                val mins: String
                mins = if (min < 10) {
                    "0$min"
                } else {
                    "$min"
                }
                return "$hs:$mins:$miaos"
            }
        }
        return ""

    }
}