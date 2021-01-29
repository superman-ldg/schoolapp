package com.example.schooassitant

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity


import io.reactivex.Observable.timer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class WritePostsPopWindow: PopupWindow, View.OnClickListener
{
    private  lateinit var rootView: View
    private  lateinit var contentView :RelativeLayout
    private  var mContext:Activity

    constructor(mContext:Activity):super()
    {
        this.mContext=mContext
    }
    fun showMoreWindow(anchor: View)
    {
        val inflater =mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootView =inflater.inflate(R.layout.pop_menu,null)
        val height=mContext.windowManager.defaultDisplay.height
        val width=mContext.windowManager.defaultDisplay.width
        setContentView(rootView)
        this.width=width
        this.height=(height-ScreenUtils.getStatusHeight(mContext)).toInt()
        contentView=rootView.findViewById(R.id.conteview)
        val close=rootView.findViewById<LinearLayout>(R.id.ll_close)
        close.setBackgroundColor(0xffffffff.toInt())
        close.setOnClickListener(this)
        showAnimation(contentView)
        setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.blackbg))
        isOutsideTouchable=true
        isFocusable=true
        showAtLocation(anchor,Gravity.BOTTOM,0,0)


    }

    @SuppressLint("CheckResult")
    private fun showAnimation(contentView:ViewGroup)
    {
        var childCount:Int=contentView.childCount
        for (i in 0..childCount) {
            val view = contentView.getChildAt(i)
            if(view!=null){
                if(view.id==R.id.ll_close)
                    continue
                view.setOnClickListener(this)
                view.visibility=View.INVISIBLE
                timer(i * 50.toLong(), TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            view.visibility = View.VISIBLE
                            val fadeAnim: ValueAnimator = ObjectAnimator.ofFloat(view, "translationY", 600F, 0F)
                            //设置动画的时间
                            fadeAnim.duration = 300
                            fadeAnim.start()
                        }
            }
        }
    }

    override fun onClick(v: View?) {
            when(v?.id){
                R.id.senditem_window-> {
                    val intent= Intent(this.mContext,PostActivity::class.java)
                  mContext.startActivity(intent)

                }

                R.id.sendzudui_window-> {
                    val intent = Intent(this.mContext, PostzuduiActivity::class.java)
                    mContext.startActivity(intent)

                }
                R.id.ll_close->
                {
                    if(isShowing){
                        closeAnimation(contentView)
                    }
                }
            }
    }
    @SuppressLint("CheckResult")
    private fun closeAnimation(contentView: RelativeLayout)
    {
        for (i in 0..contentView.childCount){
            val view =contentView.getChildAt(i)
            if(view!=null){
                if(view.id==R.id.ll_close){
                    continue
                }
                view.setOnClickListener(this)
                view.visibility = View.INVISIBLE

                //延迟显示每个子视图
                io.reactivex.Observable.timer(((contentView.childCount - i - 1) * 30).toLong(), TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            view.visibility = View.VISIBLE
                            val fadeAnim: ValueAnimator = ObjectAnimator.ofFloat(view, "translationY", 0F, 600F)
                            fadeAnim.duration = 200

                            fadeAnim.run {
                                start()
                                addListener(object : Animator.AnimatorListener {
                                    override fun onAnimationRepeat(animation: Animator?) {
                                        //动画循环播放的时候
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        //动画结束的时候
                                        view.visibility = View.INVISIBLE
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {
                                        //动画被取消的时候
                                    }

                                    override fun onAnimationStart(animation: Animator?) {
                                        //动画开始的时候调用
                                    }

                                })
                            }

                        }
                //将个别的取出来 再延时显示 制造效果
                if (view.id == R.id.senditem_window) {
                    timer(((contentView.childCount - i) * 30 + 80).toLong(), TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dismiss()
                            })
                }
            }
        }
    }
            }
