package com.sd.lib.aligner

/**
 * 对齐接口
 */
interface Aligner {
    /**
     * 对齐的位置
     */
    val position: Position

    /**
     * 设置要对齐的位置
     */
    fun setPosition(position: Position)

    /**
     * 触发一次对齐，并返回结果
     */
    fun align(input: Input): Result

    enum class Position {
        /** 顶部开始对齐 */
        TopStart,
        /** 顶部中间对齐 */
        TopCenter,
        /** 顶部结束对齐 */
        TopEnd,

        /** 中间开始对齐 */
        CenterStart,
        /** 中间对齐 */
        Center,
        /** 中间结束对齐 */
        CenterEnd,

        /** 底部开始对齐 */
        BottomStart,
        /** 底部中间对齐 */
        BottomCenter,
        /** 底部结束对齐 */
        BottomEnd,
    }

    data class Input(
        // 目标
        val targetX: Int,
        val targetY: Int,

        // 源
        val sourceX: Int,
        val sourceY: Int,

        // 源容器
        val containerX: Int,
        val containerY: Int,

        val targetWidth: Int,
        val targetHeight: Int,
        val sourceWidth: Int,
        val sourceHeight: Int,
        val containerWidth: Int,
        val containerHeight: Int,
    )

    data class Result(
        /** 输入参数 */
        val input: Input,

        /** 源相对于源容器的x值 */
        val x: Int,

        /** 源相对于源容器的y值 */
        val y: Int,
    ) {
        val overflow: Overflow
            get() = with(input) {
                val containerStart = containerX
                val containerEnd = containerStart + containerWidth
                val containerTop = containerY
                val containerBottom = containerTop + containerHeight

                val newStart = x
                val start = containerStart - newStart

                val newEnd = newStart + sourceWidth
                val end = newEnd - containerEnd

                val newTop = y
                val top = containerTop - newTop

                val newBottom = newTop + sourceHeight
                val bottom = newBottom - containerBottom

                Overflow(
                    start = start,
                    end = end,
                    top = top,
                    bottom = bottom,
                )
            }
    }

    /**
     * 源的4个边溢出源容器的信息。
     * 值大于0表示该边溢出了，值小于0表示该边未溢出。
     */
    data class Overflow(
        val start: Int,
        val end: Int,
        val top: Int,
        val bottom: Int,
    )
}