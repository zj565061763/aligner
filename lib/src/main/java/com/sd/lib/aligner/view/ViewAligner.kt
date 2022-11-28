package com.sd.lib.aligner.view

import android.view.View
import com.sd.lib.aligner.Aligner

interface ViewAligner : Aligner {
    /**
     * 目标View
     */
    var target: View?

    /**
     * 源View
     */
    var source: View?
}