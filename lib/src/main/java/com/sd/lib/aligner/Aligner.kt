package com.sd.lib.aligner

/**
 * 对齐接口
 */
interface Aligner {

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
        // 目标大小
        val targetWidth: Int,
        val targetHeight: Int,

        // 源容器坐标
        val containerX: Int,
        val containerY: Int,
        // 源容器大小
        val containerWidth: Int,
        val containerHeight: Int,

        // 源大小
        val sourceWidth: Int,
        val sourceHeight: Int,
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
            get() = Overflow(
                start = -x,
                end = (x + input.sourceWidth) - input.containerWidth,
                top = -y,
                bottom = (y + input.sourceHeight) - input.containerHeight,
            )

        /**
         * 输入的参数相对于源容器的溢出信息
         */
        fun overflow(x: Int, y: Int, width: Int, height: Int): Overflow {
            return Companion.overflow(
                parentX = input.containerX,
                parentY = input.containerY,
                parentWidth = input.containerWidth,
                parentHeight = input.containerHeight,
                childX = x,
                childY = y,
                childWidth = width,
                childHeight = height,
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
    ) {
        /**
         * 4条边溢出容器的总值
         */
        fun totalOverflow(): Int {
            var size = 0
            if (top > 0) size += top
            if (bottom > 0) size += bottom
            if (start > 0) size += start
            if (end > 0) size += end
            return size
        }
    }

    companion object {
        @JvmStatic
        fun overflow(
            parentX: Int,
            parentY: Int,
            parentWidth: Int,
            parentHeight: Int,
            childX: Int,
            childY: Int,
            childWidth: Int,
            childHeight: Int,
        ): Overflow {
            val containerStart = parentX
            val containerEnd = containerStart + parentWidth
            val containerTop = parentY
            val containerBottom = containerTop + parentHeight

            val start = childX
            val end = start + childWidth
            val top = childY
            val bottom = top + childHeight

            val overflowStart = containerStart - start
            val overflowEnd = end - containerEnd
            val overflowTop = containerTop - top
            val overflowBottom = bottom - containerBottom

            return Overflow(
                start = overflowStart,
                end = overflowEnd,
                top = overflowTop,
                bottom = overflowBottom,
            )
        }
    }
}