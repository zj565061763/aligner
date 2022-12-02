package com.sd.lib.aligner

/**
 * 对齐接口
 */
interface Aligner {
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

        /** 底部开始对齐 */
        BottomStart,
        /** 底部中间对齐 */
        BottomCenter,
        /** 底部结束对齐 */
        BottomEnd,

        /** 开始顶部对齐 */
        StartTop,
        /** 开始中间对齐 */
        StartCenter,
        /** 开始底部对齐 */
        StartBottom,

        /** 开始顶部对齐 */
        EndTop,
        /** 开始中间对齐 */
        EndCenter,
        /** 开始底部对齐 */
        EndBottom,

        /** 中间对齐 */
        Center,
    }

    data class Input(
        val position: Position,

        // 目标坐标
        val targetX: Int,
        val targetY: Int,

        // 源坐标
        val sourceX: Int,
        val sourceY: Int,

        // 源容器坐标
        val containerX: Int,
        val containerY: Int,

        // 目标大小
        val targetWidth: Int,
        val targetHeight: Int,

        // 源大小
        val sourceWidth: Int,
        val sourceHeight: Int,

        // 源容器大小
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
     * 4条边距离源容器4条边的值。值大于0表示溢出，值小于0表示未溢出。
     */
    data class Overflow(
        val start: Int,
        val end: Int,
        val top: Int,
        val bottom: Int,
    )
}