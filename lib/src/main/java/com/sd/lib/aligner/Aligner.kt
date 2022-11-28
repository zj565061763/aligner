package com.sd.lib.aligner

/**
 * 对齐接口
 */
interface Aligner {
    /**
     * 回调对象
     */
    var callback: Callback?

    /**
     * 要对齐的位置，默认右上角对齐
     */
    var position: Position

    /**
     * 源位置信息
     */
    var sourceLayoutInfo: SourceLayoutInfo?

    /**
     * 目标位置信息
     */
    var targetLayoutInfo: LayoutInfo?


    /**
     * 触发一次对齐，并返回是否更新成功
     */
    fun update(): Boolean

    enum class Position {
        /** 与target左上角对齐 */
        TopLeft,
        /** 与target顶部中间对齐 */
        TopCenter,
        /** 与target右上角对齐 */
        TopRight,

        /** 与target左边中间对齐 */
        LeftCenter,
        /** 中间对齐 */
        Center,
        /** 与target右边中间对齐 */
        RightCenter,

        /** 与target左下角对齐 */
        BottomLeft,
        /** 与target底部中间对齐 */
        BottomCenter,
        /** 与target右下角对齐 */
        BottomRight,

        /** 与target左边对齐 */
        Left,
        /** 与target顶部对齐 */
        Top,
        /** 与target右边对齐 */
        Right,
        /** 与target底部对齐 */
        Bottom
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
        }
    }

    /**
     * 源信息
     */
    interface SourceLayoutInfo : LayoutInfo {
        /** 源的父容器信息 */
        val parentLayoutInfo: LayoutInfo?
    }

    abstract class Callback {
        /**
         * 在更新之前触发
         * @param source 源
         * @param target 目标
         * @return true-可以更新，false-不要更新
         */
        open fun canUpdate(source: SourceLayoutInfo, target: LayoutInfo): Boolean {
            return true
        }

        /**
         * 根据指定[Position]对齐[target]后触发。
         *
         * @param x      [source]相对于父容器的[x]值，如果为null，表示该方向不需要处理
         * @param y      [source]相对于父容器的[y]值，如果为null，表示该方向不需要处理
         * @param source 源
         * @param target 目标
         */
        abstract fun onUpdate(x: Int?, y: Int?, source: SourceLayoutInfo, target: LayoutInfo)
    }
}