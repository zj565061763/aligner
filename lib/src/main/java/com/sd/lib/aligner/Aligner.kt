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
     * 源信息
     */
    var sourceLayoutInfo: SourceLayoutInfo?

    /**
     * 目标信息
     */
    var targetLayoutInfo: LayoutInfo?

    /**
     * 触发一次对齐，并返回是否成功
     */
    fun update(): Boolean

    enum class Position {
        /** 左上角对齐 */
        TopLeft,
        /** 顶部中间对齐 */
        TopCenter,
        /** 右上角对齐 */
        TopRight,

        /** 左边中间对齐 */
        LeftCenter,
        /** 中间对齐 */
        Center,
        /** 右边中间对齐 */
        RightCenter,

        /** 左下角对齐 */
        BottomLeft,
        /** 底部中间对齐 */
        BottomCenter,
        /** 右下角对齐 */
        BottomRight,
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
         * [Aligner.update]后触发，返回是否可以更新
         *
         * @param source 源
         * @param target 目标
         * @return true-可以更新，false-不要更新
         */
        open fun canUpdate(source: SourceLayoutInfo, target: LayoutInfo): Boolean {
            return true
        }

        /**
         * 根据[Position]计算对齐[target]后触发。
         *
         * @param x      [source]相对于父容器的[x]值
         * @param y      [source]相对于父容器的[y]值
         * @param source 源
         * @param target 目标
         */
        abstract fun onUpdate(x: Int, y: Int, source: SourceLayoutInfo, target: LayoutInfo)
    }
}