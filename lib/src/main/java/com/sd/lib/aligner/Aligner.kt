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
        /**
         * 源相对于源容器的溢出信息
         */
        val sourceOverflow: Overflow
            get() = with(input) {
                val containerStart = containerX
                val containerEnd = containerStart + containerWidth
                val containerTop = containerY
                val containerBottom = containerTop + containerHeight

                val start = x
                val end = start + sourceWidth
                val top = y
                val bottom = top + sourceHeight

                Overflow(
                    start = containerStart - start,
                    end = end - containerEnd,
                    top = containerTop - top,
                    bottom = bottom - containerBottom,
                )
            }

        /**
         * 目标相对于源容器的溢出信息
         */
        val targetOverflow: Overflow
            get() = with(input) {
                val containerStart = containerX
                val containerEnd = containerStart + containerWidth
                val containerTop = containerY
                val containerBottom = containerTop + containerHeight

                val start = targetX
                val end = start + targetWidth
                val top = targetY
                val bottom = top + targetHeight

                Overflow(
                    start = containerStart - start,
                    end = end - containerEnd,
                    top = containerTop - top,
                    bottom = bottom - containerBottom,
                )
            }
    }

    /**
     * 4个方向距离源容器4条边的值。值大于0表示溢出，值小于0表示未溢出。
     */
    data class Overflow(
        val start: Int,
        val end: Int,
        val top: Int,
        val bottom: Int,
    ) {
        /**
         * 水平方向溢出的大小值
         */
        val horizontalOverflow: Int
            get() {
                var value = 0
                if (start > 0) value += start
                if (end > 0) value += end
                return value
            }

        /**
         * 竖直方向溢出的大小值
         */
        val verticalOverflow: Int
            get() {
                var value = 0
                if (top > 0) value += top
                if (bottom > 0) value += bottom
                return value
            }
    }
}