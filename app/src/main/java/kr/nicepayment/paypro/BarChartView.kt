package kr.nicepayment.paypro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.text.DecimalFormat
import kotlin.math.max

class BarChartView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    // 막대 데이터 목록을 저장하는 변수
    private var bars: List<BarData> = emptyList()

    // 숫자를 한국식 천 단위 구분 형식으로 포맷팅하는 객체
    private val numberFormat = DecimalFormat("#,###")

    // 데이터 세트에서 최댓값을 계산하는 지연 초기화 변수
    private val maxValue by lazy { bars.maxOfOrNull { it.value } ?: 1f }

    // 데이터 세트에서 최솟값을 계산하는 지연 초기화 변수
    private val minValue by lazy { bars.minOfOrNull { it.value } ?: 0f }

    // 막대를 그리기 위한 Paint 객체 (일반 막대)
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY // 막대 색상
        textSize = 40f // 텍스트 크기
        textAlign = Paint.Align.CENTER // 텍스트 정렬
    }

    // 강조된 막대를 그리기 위한 Paint 객체
    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE // 강조 막대 색상
    }

    // 막대 데이터를 설정하고 뷰를 갱신하는 함수
    fun setData(data: List<BarData>) {
        bars = data
        invalidate() // 뷰 다시 그리기 요청
    }

    // 실제로 막대 그래프를 그리는 함수
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // 막대와 간격을 계산하기 위한 기본 단위 설정
        val spacing = width / (bars.size * (Companion.barWidthRatio + Companion.barSpacingRatio) + Companion.barSpacingRatio)
        val barWidth = spacing * Companion.barWidthRatio // 막대 너비
        val cornerRadius = barWidth / 3 // 막대 둥근 모서리 반경

        // 레이블과 막대 사이의 마진 설정
        val labelMarginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)
        // 막대 상단 값과 막대 사이의 마진 설정
        val valueMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

        // 막대의 최대 및 최소 높이 계산
        val maxBarHeight = height * Companion.maxHeightPercentage - labelMarginBottom - barPaint.textSize // 최대 막대 높이
        val minBarHeight = maxBarHeight * Companion.minHeightPercentage // 최소 막대 높이


        // 각 막대를 순회하며 그리기
        bars.forEachIndexed { index, bar ->
            val left = spacing * (index * (Companion.barWidthRatio + Companion.barSpacingRatio) + Companion.barSpacingRatio)
            val right = left + barWidth

            // 정규화된 막대 높이 계산
            val normalizedValue = (bar.value - minValue) / (maxValue - minValue)
            val calculatedBarHeight = normalizedValue * (maxBarHeight - minBarHeight) + minBarHeight
            val top = height - calculatedBarHeight - labelMarginBottom - barPaint.textSize - valueMarginTop // 막대 상단 위치 설정
            val bottom = height - labelMarginBottom - barPaint.textSize // 막대 하단 위치

            val rect = RectF(left, top, right, bottom)
            val paint = if (index == bars.size - 1) highlightPaint else barPaint
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

            val formattedValue = numberFormat.format(bar.value.toInt())
            // 막대 상단에 값 표시, 마진 포함하여 위치 조정
            canvas.drawText(formattedValue, left + barWidth / 2, top - valueMarginTop, barPaint)

            // 레이블 위치 조정하여 바닥에 정렬
            val labelY = height - labelMarginBottom / 2 // 레이블 y 좌표 조정
            canvas.drawText(bar.label, left + barWidth / 2, labelY, barPaint)
        }
    }

    companion object {
        const val maxHeightPercentage = 0.8f // 막대 최대 높이 비율
        const val minHeightPercentage = 0.2f // 막대 최소 높이 비율
        const val labelHeightPercentage = 0.1f // 레이블 높이 비율
        const val barWidthRatio = 1.5f // 막대 너비 비율
        const val barSpacingRatio = 0.5f // 막대 간격 비율
    }
}

data class BarData(val value: Float, val label: String)
