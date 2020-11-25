package by.yura.quizapp.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import by.yura.quizapp.listener.OnFragmentListener
import by.yura.quizapp.R
import by.yura.quizapp.model.Answer
import by.yura.quizapp.model.Question
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_start.*
import org.json.JSONObject
import kotlin.collections.ArrayList

//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdView
//import com.google.android.gms.ads.MobileAds


class StartFragment : Fragment(), RewardedVideoAdListener {

    private lateinit var onFragmentListener: OnFragmentListener
    lateinit var rewardedVideoAdInstance: RewardedVideoAd
    lateinit var button2: Button

    fun setOnFragmentListener(onFragmentListener: OnFragmentListener) {
        this.onFragmentListener = onFragmentListener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_start, container, false)

        val button = view.findViewById<Button>(R.id.button)
        button2 = view.findViewById<Button>(R.id.button2)

        button.setOnClickListener {
            onFragmentListener.onClickBtn(button)
        }

        button2.setOnClickListener {
//            onFragmentListener.onClickBtn(button2)

            rewardedVideoAdInstance = MobileAds.getRewardedVideoAdInstance(context)
            rewardedVideoAdInstance.rewardedVideoAdListener = this
            rewardedVideoAdInstance.loadAd("ca-app-pub-3940256099942544/5224354917", AdRequest.Builder().build())
            rewardedVideoAdInstance.show()
        }

        MobileAds.initialize(context) {}
        val ads = view.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        ads.loadAd(adRequest)
////////////

        return view
    }

    override fun onRewardedVideoAdClosed() {
        Log.i("TAG", "onRewardedVideoAdClosed: ")
                    onFragmentListener.onClickBtn(button2)

    }

    override fun onRewardedVideoAdLeftApplication() {
        Log.i("TAG", "onRewardedVideoAdLeftApplication: ")
    }

    override fun onRewardedVideoAdLoaded() {
        Log.i("TAG", "onRewardedVideoAdLoaded: ")
        rewardedVideoAdInstance.show()
    }

    override fun onRewardedVideoAdOpened() {
        Log.i("TAG", "onRewardedVideoAdOpened: ")
    }

    override fun onRewardedVideoCompleted() {
        Log.i("TAG", "onRewardedVideoCompleted: ")
    }

    override fun onRewarded(p0: RewardItem?) {
        Log.i("TAG", "onRewarded: ")
    }

    override fun onRewardedVideoStarted() {
        Log.i("TAG", "onRewardedVideoStarted: ")
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.i("TAG", "onRewardedVideoAdFailedToLoad: ")
    }

}
