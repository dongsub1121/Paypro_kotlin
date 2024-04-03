package kr.nicepayment.paypro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import kr.nicepayment.paypro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bt1.setOnClickListener {
            val intent = Intent(this, PaymentPayProActivity::class.java)
            startActivity(intent)
/*            // 프래그먼트 전환 시작
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // MyFragment 인스턴스 생성
            val myFragment = SampleFragment()

            // 컨테이너에 프래그먼트 추가, 교체 또는 제거
            fragmentTransaction.replace(R.id.container, myFragment)

            // 트랜잭션을 백 스택에 추가(선택적)
            // fragmentTransaction.addToBackStack(null)

            // 변경사항 적용
            fragmentTransaction.commit()*/
        }
    }
}