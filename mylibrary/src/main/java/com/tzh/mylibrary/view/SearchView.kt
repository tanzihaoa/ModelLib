package com.tzh.mylibrary.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.Toast
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.LayoutSearchViewBinding
import com.tzh.mylibrary.util.KeyBoardUtils
import com.tzh.mylibrary.util.bindingInflateLayout
import com.tzh.mylibrary.util.getColorInt
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.toDefault

/**
 *
 */
class SearchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var mSvCorners = 0
    private var mTextColor = 0
    private var mTextHintColor = 0
    private var mBackColor = 0
    private var mHintText : String = ""
    private var mSvTextSize = 0
    private var mSvImageWidth = 0
    private var mInputTextLength = 0
    private var mInputHintText : String = ""
    private var mNoInputHintText : String = ""
    var mListener : SvSearchListener? = null
    init {
        attrs?.run {
            initView(this)
        }
    }

    var binding: LayoutSearchViewBinding? = null

    private fun initView(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchView)

        //设置字体颜色
        mTextColor = attributes.getColor(R.styleable.SearchView_sv_text_color, getColorInt(R.color.color_333))
        //设置字体颜色
        mTextHintColor = attributes.getColor(R.styleable.SearchView_sv_hint_color, getColorInt(R.color.color_bbbbbb))
        //圆角
        mSvCorners = attributes.getDimensionPixelSize(R.styleable.SearchView_sv_corners, 99)
        //背景颜色
        mBackColor = attributes.getColor(R.styleable.SearchView_sv_back_color, getColorInt(R.color.color_f7f7f7))
        //提示文字
        mHintText = attributes.getString(R.styleable.SearchView_sv_hint).toDefault("请输入搜索内容")
        //圆角
        mInputTextLength = attributes.getInt(R.styleable.SearchView_sv_input_max_length, 50)
        //超过输入限制提示文字
        mInputHintText = attributes.getString(R.styleable.SearchView_sv_input_hint_text).toDefault("搜索字数超出"+mInputTextLength+"个，请重新输入")
        //没有输入文字时候的提示  不设置则默认不拦截
        mNoInputHintText = attributes.getString(R.styleable.SearchView_sv_no_input_hint_text).toDefault("")
        //字体大小
        mSvTextSize = attributes.getDimensionPixelSize(R.styleable.SearchView_sv_text_size, 14)
        //图片宽高
        mSvImageWidth = attributes.getDimensionPixelSize(R.styleable.SearchView_sv_image_width, 14)



        attributes.recycle()
        binding = bindingInflateLayout<LayoutSearchViewBinding>(R.layout.layout_search_view).also {
            it.layout.setShapeCorners(mSvCorners.toFloat())
            it.layout.setShapeBackgroundColor(mBackColor)
            it.etText.textSize = mSvTextSize.toFloat()
            it.etText.hint = mHintText
            it.etText.setHintTextColor(mTextHintColor)
            it.etText.setTextColor(mTextColor)
            it.etText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().isNotEmpty()){
                        it.ivClear.visibility = VISIBLE
                    }else{
                        it.ivClear.visibility = GONE
                        mListener?.clear()
                    }
                }
            })

            it.ivClear.setOnClickNoDouble {mIt->
                it.etText.setText("")
                mIt.visibility = GONE
            }

            it.etText.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if(mNoInputHintText.isNotEmpty() && it.etText.text.toString().isEmpty() && mHintText == it.etText.hint){
                        Toast.makeText(context,mNoInputHintText,Toast.LENGTH_LONG).show()
                    }else if(mInputTextLength>0 && it.etText.text.toString().length>mInputTextLength){
                        Toast.makeText(context,mInputHintText,Toast.LENGTH_LONG).show()
                    }else{
                        if( mListener?.search(if(TextUtils.isEmpty(it.etText.text.toString())) it.etText.text.toString() else it.etText.text.toString()).toDefault(true)){
                            KeyBoardUtils.closeKeyboard(it.etText,context)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
        }

    }

    fun setText(text: String) {
        binding?.etText?.setText(text)
        if(text.isNotEmpty()){
            binding?.ivClear?.visibility = VISIBLE
        }else{
            binding?.ivClear?.visibility = GONE
        }
    }

    fun setHintText(text: String) {
        binding?.etText?.postDelayed({
            binding?.etText?.hint = text
        },100)
    }

    fun getHintText(): String {
        return binding?.etText?.hint.toString().toDefault("")
    }

    fun getText(): String {
        return binding?.etText?.text.toString().toDefault("")
    }

    fun showKeyBord(){
        binding?.etText?.let {
            KeyBoardUtils.openKeybord(it,it.context)
        }
    }

    fun setSvSearchListener(listener : SvSearchListener){
        mListener = listener
    }

    /**
     * boolean ture为搜索关闭软键盘  false为不关闭
     */
    interface  SvSearchListener{
        fun search(text : String): Boolean

        fun clear()
    }
}