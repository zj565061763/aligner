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
     * 目标信息
     */
    val targetLayoutInfo: LayoutInfo

    /**
     * 源信息
     */
    val sourceLayoutInfo: LayoutInfo

    /**
     * 源容器的信息
     */
    val sourceContainerLayoutInfo: LayoutInfo

    /**
     * 设置回调对象
     */
    fun setCallback(callback: Callback?)

    /**
     * 设置要对齐的位置
     */
    fun setPosition(position: Position)

    /**
     * 设置目标信息
     */
    fun setTargetLayoutInfo(layoutInfo: LayoutInfo)

    /**
     * 设置源信息
     */
    fun setSourceLayoutInfo(layoutInfo: LayoutInfo)

    /**
     * 设置源容器信息
     */
    fun setSourceContainerLayoutInfo(layoutInfo: LayoutInfo)

    /**
     * 触发一次对齐，并返回结果
     */
    fun update(): Result?

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

    /**
     * 位置大小信息
     */
    interface LayoutInfo {
        /** 是否已经准备好 */
        val isReady: Boolean

        /** 宽度 */
        val width: Int

        /** 高度 */
        val height: Int

        /** 坐标 */
        val coordinate: IntArray

        companion object {
            val coordinateUnspecified = intArrayOf(Int.MAX_VALUE, Int.MIN_VALUE)
            val Unspecified = object : LayoutInfo {
                override val isReady: Boolean get() = false
                override val width: Int get() = 0
                override val height: Int get() = 0
                override val coordinate: IntArray get() = coordinateUnspecified
            }
        }
    }

    data class Result(
        /** 源相对于源容器的x值 */
        val x: Int,

        /** 源现对于源容器的y值 */
        val y: Int,
    )

    interface Callback {
        /**
         * 结果回调
         */
        fun onResult(result: Result)
    }
}