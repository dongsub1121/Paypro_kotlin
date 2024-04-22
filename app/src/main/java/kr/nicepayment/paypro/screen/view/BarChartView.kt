package kr.nicepayment.paypro.screen.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.text.DecimalFormat

class BarChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    // 막대 데이터 목록을 저장합니다.
    private var bars: List<BarData> = emptyList()

    // 데이터 단위를 저장하는 변수 (예: "만", "천", "백만" 등)
    private var dataUnit: String = ""

    // 숫자를 한국식 천 단위 구분 형식으로 포맷팅합니다.
    private val numberFormat = DecimalFormat("#,###")

    // 데이터 세트에서 최댓값을 계산합니다. 데이터가 없는 경우 기본값은 1입니다.
    private val maxValue by lazy { bars.maxOfOrNull { it.value } ?: 1f }

    // 데이터 세트에서 최솟값을 계산합니다. 데이터가 없는 경우 기본값은 0입니다.
    private val minValue by lazy { bars.minOfOrNull { it.value } ?: 0f }

    // 막대를 그리기 위한 Paint 객체입니다. 여기서는 일반 막대의 색상과 텍스트 속성을 설정합니다.
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY // 막대의 색상
        textSize = 40f // 막대 상단의 텍스트 크기
        textAlign = Paint.Align.CENTER // 텍스트 정렬
    }

    // 강조된 막대를 그리기 위한 Paint 객체입니다. 강조될 막대의 색상을 설정합니다.
    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE // 강조될 막대의 색상
    }

    // 외부에서 데이터를 설정할 수 있게 하는 함수입니다. 데이터 설정 후 뷰를 다시 그립니다.
    fun setData(data: List<BarData>) {
        bars = data
        invalidate() // 데이터 변경 후 뷰를 다시 그리도록 요청합니다.
    }

    fun setDataUnit(unit: String) {
        dataUnit = unit
        invalidate() // 단위 변경 후 뷰를 다시 그리도록 요청
    }

    // 뷰에 실제로 막대 그래프를 그리는 함수입니다.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 막대와 간격을 계산하기 위한 기본 단위 설정
        val spacing = width / (bars.size * (barWidthRatio + barSpacingRatio) + barSpacingRatio)
        val barWidth = spacing * barWidthRatio // 막대의 너비
        val cornerRadius = barWidth / 3 // 막대의 둥근 모서리 반경

        // 레이블과 막대 사이의 마진 및 막대 상단의 값과 막대 사이의 마진을 설정
        val labelMarginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
        val barTopMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

        // 막대의 최대 및 최소 높이를 계산
        val maxBarHeight = height * maxHeightPercentage - labelMarginBottom * 2 - barPaint.textSize - barTopMargin
        val minBarHeight = maxBarHeight * minHeightPercentage

        // 각 막대에 대해 반복하여 그림
        bars.forEachIndexed { index, bar ->
            val left = spacing * (index * (barWidthRatio + barSpacingRatio) + barSpacingRatio)
            val right = left + barWidth

            // 정규화된 막대 높이를 계산
            val normalizedValue = (bar.value - minValue) / (maxValue - minValue)
            val calculatedBarHeight = normalizedValue * (maxBarHeight - minBarHeight) + minBarHeight
            val top = height - calculatedBarHeight - labelMarginBottom * 2 - barTopMargin
            val bottom = height - labelMarginBottom * 2 - barPaint.textSize

            // 막대를 그립니다.
            val rect = RectF(left, top, right, bottom)
            val paint = if (index == bars.size - 1) highlightPaint else barPaint // 마지막 막대를 강조
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

            // 막대 상단에 값 표시
            val formattedValue = numberFormat.format(bar.value.toInt())
            canvas.drawText(formattedValue, left + barWidth / 2, top - barTopMargin, barPaint)

            // 레이블 위치 조정
            val labelY = height - labelMarginBottom // 레이블 Y 좌표
            canvas.drawText(bar.label, left + barWidth / 2, labelY, barPaint)
        }

        // 단위 표기를 그리는 로직
        if (dataUnit.isNotEmpty()) {
            // 단위 표기의 텍스트 사이즈 및 색상 설정
            barPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
            //barPaint.color = Color.BLACK

            // 오른쪽과 상단 마진을 15dp로 설정
            val marginRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
            val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)

            // 단위 표기의 위치 설정 (상단 오른쪽)
            // 오른쪽 마진을 고려하여 X 좌표 설정
            val unitTextX = width - marginRight
            // 상단 마진을 고려하여 Y 좌표 설정 (텍스트의 베이스라인을 기준으로 하기 때문에 텍스트 크기를 더해줌)
            val unitTextY = marginTop + barPaint.textSize

            // Paint.Align.RIGHT를 사용하여 텍스트가 오른쪽 정렬되도록 설정
            barPaint.textAlign = Paint.Align.RIGHT

            // 단위 표기 그리기
            canvas.drawText("단위: $dataUnit", unitTextX, unitTextY, barPaint)
        }
    }

    companion object {
        const val maxHeightPercentage = 0.8f // 막대의 최대 높이 비율
        const val minHeightPercentage = 0.2f // 막대의 최소 높이 비율
        const val barWidthRatio = 1.5f // 막대의 너비 비율
        const val barSpacingRatio = 0.5f // 막대 간의 간격 비율
    }
}

data class BarData(val value: Float, val label: String)

/*
막대그래프 뷰에는 벨류
아래에는 레이블
뷰와 레이블은 막대그래프의 가로 중앙에 항상 위치
뷰의 bottom 에서 레이블은 10dp만큼 마진
레이블과 막대그래프는 10dp만큼 마진
막대 그래프는 cornerRedius 10dp
막대 그래프는 입체감을 줘
* */
