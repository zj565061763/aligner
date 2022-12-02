package com.sd.demo.aligner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.aligner.databinding.ActivityMainBinding
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.FAligner

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val _aligner = FAligner()
    private var _position = Aligner.Position.Center

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    private val _onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        update()
        true
    }

    private fun update() {
        val target = _binding.viewTarget
        val source = _binding.viewSource
        val container = _binding.viewContainer

        val targetXY = IntArray(2)
        val sourceXY = IntArray(2)
        val containerXY = IntArray(2)

        target.getLocationOnScreen(targetXY)
        container.getLocationOnScreen(containerXY)

        val input = Aligner.Input(
            position = _position,

            targetX = targetXY[0],
            targetY = targetXY[1],

            containerX = containerXY[0],
            containerY = containerXY[1],

            targetWidth = target.width,
            targetHeight = target.height,

            sourceWidth = source.width,
            sourceHeight = source.height,

            containerWidth = container.width,
            containerHeight = container.height,
        )

        val result = _aligner.align(input)
        handleResult(result)
    }

    private fun handleResult(result: Aligner.Result?) {
        if (result == null) return

        val x = result.x
        val y = result.y

        logMsg { "(${x}, ${y})" }
        logMsg { "sourceOverflow: ${result.sourceOverflow}" }
        logMsg { "targetOverflow: ${result.targetOverflow}" }

        val view = _binding.viewSource
        view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
    }

    override fun onClick(v: View) {
        when (v) {
            _binding.btnStartListen -> {
                // 开始
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                    addOnPreDrawListener(_onPreDrawListener)
                }
                update()
            }
            _binding.btnStopListen -> {
                // 停止
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                }
            }


            _binding.btnTopStart -> {
                _position = Aligner.Position.TopStart
                update()
            }
            _binding.btnTopCenter -> {
                _position = Aligner.Position.TopCenter
                update()
            }
            _binding.btnTopEnd -> {
                _position = Aligner.Position.TopEnd
                update()
            }
            _binding.btnTop -> {
                _position = Aligner.Position.Top
                update()
            }


            _binding.btnBottomStart -> {
                _position = Aligner.Position.BottomStart
                update()
            }
            _binding.btnBottomCenter -> {
                _position = Aligner.Position.BottomCenter
                update()
            }
            _binding.btnBottomEnd -> {
                _position = Aligner.Position.BottomEnd
                update()
            }
            _binding.btnBottom -> {
                _position = Aligner.Position.Bottom
                update()
            }


            _binding.btnStartTop -> {
                _position = Aligner.Position.StartTop
                update()
            }
            _binding.btnStartCenter -> {
                _position = Aligner.Position.StartCenter
                update()
            }
            _binding.btnStartBottom -> {
                _position = Aligner.Position.StartBottom
                update()
            }
            _binding.btnStart -> {
                _position = Aligner.Position.Start
                update()
            }


            _binding.btnEndTop -> {
                _position = Aligner.Position.EndTop
                update()
            }
            _binding.btnEndCenter -> {
                _position = Aligner.Position.EndCenter
                update()
            }
            _binding.btnEndBottom -> {
                _position = Aligner.Position.EndBottom
                update()
            }
            _binding.btnEnd -> {
                _position = Aligner.Position.End
                update()
            }


            _binding.btnCenter -> {
                _position = Aligner.Position.Center
                update()
            }
        }
    }
}

inline fun logMsg(block: () -> String) {
    Log.i("aligner-demo", block())
}