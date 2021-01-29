package com.example.schooassitant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.easeui.EaseConstant
import com.hyphenate.easeui.ui.EaseConversationListFragment
import kotlinx.android.synthetic.main.bottom_navigation.*


internal class TalkListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)
        val conversationListFragment = EaseConversationListFragment()
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.container, conversationListFragment).commit()
        conversationListFragment.setConversationListItemClickListener { conversation ->
            Log.e("TalkActivity", EaseConstant.EXTRA_USER_ID + " " + conversation.conversationId())
            startActivity(Intent(this@TalkListActivity, ChatActivity::class.java).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()))
        }
        btn_zudui.setOnClickListener{
            val intent= Intent(this,myTestzudui::class.java)
            startActivity(intent)
        }

        btn_person.setOnClickListener{
            val intent= Intent(this,myTestwode::class.java)
            startActivity(intent)
        }
        btn_post.setOnClickListener {
            val writePostsPopWindow = WritePostsPopWindow(this)
            writePostsPopWindow.showMoreWindow(it)
        }
        btn_main.setOnClickListener {
            val intent= Intent(this,myTest::class.java)
            startActivity(intent)
        }
    }


}
