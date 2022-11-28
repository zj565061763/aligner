package com.sd.demo.aligner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.aligner.databinding.ActivityMainBinding
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.view.FViewAligner

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    private val _aligner by lazy {
        FViewAligner().apply {
            // 设置源View
            this.source = _binding.viewSource

            // 设置目标View
            this.target = _binding.viewTarget

            // 设置回调对象
            this.setCallback(object : Aligner.Callback {
                override fun onResult(result: Aligner.Result?) {
                    if (result != null) {
                        handleResult(result)
                    }
                }
            })
        }
    }

    private val _onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        _aligner.update()
        true
    }

    private fun handleResult(result: Aligner.Result) {
        val x = result.x
        val y = result.y

        logMsg { "(${x}, ${y})" }

        val view = _binding.viewSource
        view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
    }

    override fun onClick(v: View) {
        when (v) {
            _binding.btnStart -> {
                // 开始
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                    addOnPreDrawListener(_onPreDrawListener)
                }
            }
            _binding.btnStop -> {
                // 停止
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                }
            }

            _binding.btnTopStart -> _aligner.setPosition(Aligner.Position.TopStart)
            _binding.btnTopCenter -> _aligner.setPosition(Aligner.Position.TopCenter)
            _binding.btnTopEnd -> _aligner.setPosition(Aligner.Position.TopEnd)

            _binding.btnCenterStart -> _aligner.setPosition(Aligner.Position.CenterStart)
            _binding.btnCenter -> _aligner.setPosition(Aligner.Position.Center)
            _binding.btnCenterEnd -> _aligner.setPosition(Aligner.Position.CenterEnd)

            _binding.btnBottomStart -> _aligner.setPosition(Aligner.Position.BottomStart)
            _binding.btnBottomCenter -> _aligner.setPosition(Aligner.Position.BottomCenter)
            _binding.btnBottomEnd -> _aligner.setPosition(Aligner.Position.BottomEnd)
        }
    }
}

inline fun logMsg(block: () -> String) {
    Log.i("aligner-demo", block())
}