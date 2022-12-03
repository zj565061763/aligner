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
        /** 顶部开始方向对齐 */
        TopStart,
        /** 顶部中间对齐 */
        TopCenter,
        /** 顶部结束方向对齐 */
        TopEnd,
        /** 顶部对齐，不计算x坐标，默认x坐标为0 */
        Top,

        /** 底部开始方向对齐 */
        BottomStart,
        /** 底部中间对齐 */
        BottomCenter,
        /** 底部结束方向对齐 */
        BottomEnd,
        /** 底部对齐，不计算x坐标，默认x坐标为0 */
        Bottom,

        /** 开始方向顶部对齐 */
        StartTop,
        /** 开始方向中间对齐 */
        StartCenter,
        /** 开始方向底部对齐 */
        StartBottom,
        /** 开始方向对齐，不计算y坐标，默认y坐标为0 */
        Start,

        /** 结束方向顶部对齐 */
        EndTop,
        /** 结束方向中间对齐 */
        EndCenter,
        /** 结束方向底部对齐 */
        EndBottom,
        /** 结束方向对齐，不计算y坐标，默认y坐标为0 */
        End,

        /** 中间对齐 */
        Center,
    }

    data class Input(
        val position: Position,

        // 目标坐标
        val targetX: Int,
        val targetY: Int,

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
            get() = overflow(
                x = this.x,
                y = this.y,
                width = input.sourceWidth,
                height = input.sourceHeight,
            )

        /**
         * 输入的参数相对于源容器的溢出信息
         */
        fun overflow(x: Int, y: Int, width: Int, height: Int): Overflow {
            return with(input) {
                val containerStart = containerX
                val containerEnd = containerStart + containerWidth
                val containerTop = containerY
                val containerBottom = containerTop + containerHeight

                val start = x
                val end = start + width
                val top = y
                val bottom = top + height

                val overflowStart = containerStart - start
                val overflowEnd = end - containerEnd
                val overflowTop = containerTop - top
                val overflowBottom = bottom - containerBottom

                Overflow(
                    start = overflowStart,
                    end = overflowEnd,
                    top = overflowTop,
                    bottom = overflowBottom,
                )
            }
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